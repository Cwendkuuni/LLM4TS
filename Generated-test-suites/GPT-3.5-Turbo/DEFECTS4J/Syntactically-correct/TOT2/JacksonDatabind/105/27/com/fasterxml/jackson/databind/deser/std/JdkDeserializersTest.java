import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind() {
        // Typical use case
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        
        // Edge case: unknown class name
        clsName = "UnknownClass";
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
        
        // Edge case: different rawType
        rawType = AtomicBoolean.class;
        clsName = AtomicBoolean.class.getName();
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        
        // Error scenario: null input
        deserializer = JdkDeserializers.find(null, null);
        assertNull(deserializer);
    }
}