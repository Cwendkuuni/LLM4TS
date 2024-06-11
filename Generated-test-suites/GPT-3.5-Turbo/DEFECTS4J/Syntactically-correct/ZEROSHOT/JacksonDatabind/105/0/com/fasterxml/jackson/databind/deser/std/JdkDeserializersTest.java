import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {
    
    @Test
    public void testFind() {
        // Test 1
        Class<?> rawType1 = UUID.class;
        String clsName1 = "java.util.UUID";
        JsonDeserializer<?> result1 = JdkDeserializers.find(rawType1, clsName1);
        assertNotNull(result1);
        assertTrue(result1 instanceof UUIDDeserializer);
        
        // Test 2
        Class<?> rawType2 = StackTraceElement.class;
        String clsName2 = "java.lang.StackTraceElement";
        JsonDeserializer<?> result2 = JdkDeserializers.find(rawType2, clsName2);
        assertNotNull(result2);
        assertTrue(result2 instanceof StackTraceElementDeserializer);
        
        // Test 3
        Class<?> rawType3 = AtomicBoolean.class;
        String clsName3 = "java.util.concurrent.atomic.AtomicBoolean";
        JsonDeserializer<?> result3 = JdkDeserializers.find(rawType3, clsName3);
        assertNotNull(result3);
        assertTrue(result3 instanceof AtomicBooleanDeserializer);
        
        // Test 4
        Class<?> rawType4 = ByteBuffer.class;
        String clsName4 = "java.nio.ByteBuffer";
        JsonDeserializer<?> result4 = JdkDeserializers.find(rawType4, clsName4);
        assertNotNull(result4);
        assertTrue(result4 instanceof ByteBufferDeserializer);
        
        // Test with invalid class name
        Class<?> rawType5 = ByteBuffer.class;
        String clsName5 = "java.util.HashMap";
        JsonDeserializer<?> result5 = JdkDeserializers.find(rawType5, clsName5);
        assertNull(result5);
    }
    
    @Test
    public void testFindInvalidRawType() {
        Class<?> rawType = HashMap.class;
        String clsName = "java.util.HashMap";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
}