import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        JsonDeserializer<?> serializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(serializer);

        // Edge case: Class not found in _classNames set
        serializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(serializer);

        // Exception handling: rawType is null
        serializer = JdkDeserializers.find(null, UUID.class.getName());
        assertNull(serializer);

        // Exception handling: clsName is null
        serializer = JdkDeserializers.find(UUID.class, null);
        assertNull(serializer);
    }
}