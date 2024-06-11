import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);

        // Edge case: Unknown class name
        deserializer = JdkDeserializers.find(UUID.class, "InvalidClassName");
        assertNull(deserializer);

        // Edge case: Missing class name
        deserializer = JdkDeserializers.find(UUID.class, null);
        assertNull(deserializer);
        
        // Edge case: Missing rawType
        deserializer = JdkDeserializers.find(null, UUID.class.getName());
        assertNull(deserializer);

        // Error scenario: UnsupportedOperationException (AtomicBoolean class)
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
    }

}