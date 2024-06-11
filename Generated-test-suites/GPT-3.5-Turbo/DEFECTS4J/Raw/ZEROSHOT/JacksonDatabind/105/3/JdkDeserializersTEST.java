import org.junit.Test;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUIDDeserializers() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_StackTraceElementDeserializers() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_AtomicBooleanDeserializers() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ByteBufferDeserializers() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_UnhandledType_ReturnsNull() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer);
    }
}