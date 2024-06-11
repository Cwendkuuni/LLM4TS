import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertNotNull(deserializer);
    }

    @Test(expected = NullPointerException.class)
    public void testFindWithNullClassName() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFindWithNullRawType() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(null, "java.util.UUID");
    }

    @Test
    public void testFindWithUnknownClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "com.example.UnknownClass");
        assertNull(deserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindWithInvalidClassName() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.InvalidClass");
    }

    @Test
    public void testFindWithRegisteredClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertNotNull(deserializer);
    }
}