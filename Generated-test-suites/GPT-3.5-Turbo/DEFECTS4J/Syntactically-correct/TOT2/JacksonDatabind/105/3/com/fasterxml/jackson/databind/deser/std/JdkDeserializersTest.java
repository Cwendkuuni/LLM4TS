import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_StackTraceElement() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_AtomicBoolean() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ByteBuffer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_NotFound() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFind_EmptyString() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(Integer.class, "");
        assertNull(deserializer);
    }
}