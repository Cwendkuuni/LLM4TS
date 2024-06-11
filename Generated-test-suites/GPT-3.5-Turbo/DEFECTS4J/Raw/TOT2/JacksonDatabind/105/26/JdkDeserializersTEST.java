import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);

        // Edge cases
        deserializer = JdkDeserializers.find(UUID.class, "SomeOtherClass");
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(null, UUID.class.getName());
        assertNull(deserializer);

        deserializer = JdkDeserializers.find(null, null);
        assertNull(deserializer);
    }
}