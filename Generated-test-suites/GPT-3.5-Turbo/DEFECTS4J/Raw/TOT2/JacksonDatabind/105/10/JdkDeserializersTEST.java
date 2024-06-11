import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_UUID() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_StackTraceElement() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_AtomicBoolean() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_ByteBuffer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_NotFound() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ArrayList.class, "java.util.ArrayList");
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_NullClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(null, "java.lang.String");
        assertNull(deserializer);
    }
}