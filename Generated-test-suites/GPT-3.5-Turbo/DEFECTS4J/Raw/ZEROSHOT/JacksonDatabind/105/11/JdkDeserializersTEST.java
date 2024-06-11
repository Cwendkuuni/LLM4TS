import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withExistingClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_withNonExistingClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, String.class.getName());

        assertNull(deserializer);
    }

    @Test
    public void testFind_withAtomicBooleanClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_withStackTraceElementClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_withByteBufferClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_withUnknownClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, Object.class.getName());

        assertNull(deserializer);
    }
}