import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFindWithUUID() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFindWithAtomicBoolean() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFindWithStackTraceElement() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFindWithByteBuffer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFindWithUnknownClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ArrayList.class, ArrayList.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindWithNullClassName() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindWithNullRawType() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(null, UUID.class.getName());
        assertNull(deserializer);
    }
}