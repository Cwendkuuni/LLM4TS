import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Typical use case
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);

        // Edge case: Class name not found in _classNames
        JsonDeserializer<?> deserializer2 = JdkDeserializers.find(UUID.class, "SomeRandomClass");
        assertNull(deserializer2);

        // Edge case: rawType is ByteBuffer
        JsonDeserializer<?> deserializer3 = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertTrue(deserializer3 instanceof ByteBufferDeserializer);

        // Error scenario: Class name is null
        JsonDeserializer<?> deserializer4 = JdkDeserializers.find(UUID.class, null);
        assertNull(deserializer4);
    }
}