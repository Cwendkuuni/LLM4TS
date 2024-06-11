import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {
    
    @Test
    public void testFind() {
        Class<?> rawType;
        String clsName;
        
        // Test case 1: rawType in _classNames
        rawType = UUID.class;
        clsName = rawType.getName();
        JsonDeserializer<?> result1 = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result1);
        assertEquals(UUIDDeserializer.class, result1.getClass());
        
        // Test case 2: rawType not in _classNames
        rawType = HashMap.class;
        clsName = rawType.getName();
        JsonDeserializer<?> result2 = JdkDeserializers.find(rawType, clsName);
        assertNull(result2);
        
        // Test case 3: rawType is StackTraceElement.class
        rawType = StackTraceElement.class;
        clsName = rawType.getName();
        JsonDeserializer<?> result3 = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result3);
        assertEquals(StackTraceElementDeserializer.class, result3.getClass());
        
        // Test case 4: rawType is AtomicBoolean.class
        rawType = AtomicBoolean.class;
        clsName = rawType.getName();
        JsonDeserializer<?> result4 = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result4);
        assertEquals(AtomicBooleanDeserializer.class, result4.getClass());
        
        // Test case 5: rawType is ByteBuffer.class
        rawType = ByteBuffer.class;
        clsName = rawType.getName();
        JsonDeserializer<?> result5 = JdkDeserializers.find(rawType, clsName);
        assertNotNull(result5);
        assertEquals(ByteBufferDeserializer.class, result5.getClass());
        
        // Test case 6: clsName not in _classNames
        rawType = ArrayList.class;
        clsName = rawType.getName();
        JsonDeserializer<?> result6 = JdkDeserializers.find(rawType, clsName);
        assertNull(result6);
    }
}