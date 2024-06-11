import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_UUIDDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, "java.util.UUID");
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_StackTraceElementDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement");
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_AtomicBooleanDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean");
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_ByteBufferDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer");
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_UnsupportedType() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ArrayList.class, "java.util.ArrayList");
        assertNull(deserializer);
    }
    
}