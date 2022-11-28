package example.micronaut;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.Map;

@Controller
public class HomeController {

    @Inject
    private ProductClient productClient;

    @Inject
    private StatefulRedisConnection<String, String> redisClient;

    public HomeController(ProductClient productClient, StatefulRedisConnection<String, String> redisClient) {
        this.productClient = productClient;
        this.redisClient = redisClient;
    }

    @Get
    public Map<String, Object> index() {
        String foo = redisClient.sync().get("foo");

        if (foo == null) {
            System.out.println("cache miss, setting foo to bar");
            redisClient.sync().set("foo", "bar");
        }

        productClient.sendProduct("Nike", "Blue Trainers!");
        System.out.println("Sent Product");
        return Collections.singletonMap("message", "Hello World");
    }
}
