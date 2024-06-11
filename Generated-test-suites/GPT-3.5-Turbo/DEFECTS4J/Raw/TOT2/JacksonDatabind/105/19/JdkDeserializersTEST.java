import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_UUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_StackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_AtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_ByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
}