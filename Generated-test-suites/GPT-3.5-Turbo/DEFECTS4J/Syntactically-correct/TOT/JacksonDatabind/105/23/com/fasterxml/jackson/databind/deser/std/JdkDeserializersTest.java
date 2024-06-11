import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidRawTypeAndValidClassName_returnsDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
    }
    
    @Test
    public void testFind_withInvalidRawTypeAndValidClassName_returnsNull() {
        Class<?> rawType = Boolean.class; // Invalid raw type
        String clsName = "java.util.UUID";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_withValidRawTypeAndInvalidClassName_returnsNull() {
        Class<?> rawType = UUID.class;
        String clsName = "com.example.CustomClass"; // Invalid class name
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_withInvalidRawTypeAndInvalidClassName_returnsNull() {
        Class<?> rawType = Boolean.class; // Invalid raw type
        String clsName = "com.example.CustomClass"; // Invalid class name
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_withValidRawTypeAndClassNotInHashSet_returnsNull() {
        Class<?> rawType = Integer.class; // Valid raw type, but not present in HashSet
        String clsName = "java.lang.Integer";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_withAtomicBooleanRawType_returnsAtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_withUUIDRawType_returnsUUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_withStackTraceElementRawType_returnsStackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_withByteBufferRawType_returnsByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
}