
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_NoDeserializerFound() {
        Class<?> rawType = ArrayList.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
    
    @Test
    public void testFind_AtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_StackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_ByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result);
        assertTrue(result instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_CustomType() {
        Class<?> rawType = HashSet.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
}
