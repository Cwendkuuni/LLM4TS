import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.UUID;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidPrimitiveType_ReturnsNull() {
        Class<?> rawType = int.class;
        String className = rawType.getName();
      
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
      
        assertNull(result);
    }
    
    @Test
    public void testFind_withUUIDClass_ReturnsUUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String className = rawType.getName();
      
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
      
        assertTrue(result instanceof UUIDDeserializer);
    }
  
    @Test
    public void testFind_withStackTraceElementClass_ReturnsStackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String className = rawType.getName();
      
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
      
        assertTrue(result instanceof StackTraceElementDeserializer);
    }
  
    @Test
    public void testFind_withAtomicBooleanClass_ReturnsAtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String className = rawType.getName();
      
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
      
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }
  
    @Test
    public void testFind_withByteBufferClass_ReturnsByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String className = rawType.getName();
      
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
      
        assertTrue(result instanceof ByteBufferDeserializer);
    }
  
    @Test
    public void testFind_withUnknownClass_ReturnsNull() {
        Class<?> rawType = WrongClass.class;
        String className = rawType.getName();
      
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);
      
        assertNull(result);
    }
}