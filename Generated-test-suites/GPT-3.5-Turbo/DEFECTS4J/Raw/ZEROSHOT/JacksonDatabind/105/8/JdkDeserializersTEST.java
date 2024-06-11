import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
      
        // Test case for UUID
        Class<?> rawType1 = UUID.class;
        String clsName1 = rawType1.getName();
        JsonDeserializer<?> deserializer1 = JdkDeserializers.find(rawType1, clsName1);
        assertTrue(deserializer1 instanceof UUIDDeserializer);
        
        // Test case for AtomicBoolean
        Class<?> rawType2 = AtomicBoolean.class;
        String clsName2 = rawType2.getName();
        JsonDeserializer<?> deserializer2 = JdkDeserializers.find(rawType2, clsName2);
        assertTrue(deserializer2 instanceof AtomicBooleanDeserializer);
        
        // Test case for StackTraceElement
        Class<?> rawType3 = StackTraceElement.class;
        String clsName3 = rawType3.getName();
        JsonDeserializer<?> deserializer3 = JdkDeserializers.find(rawType3, clsName3);
        assertTrue(deserializer3 instanceof StackTraceElementDeserializer);
        
        // Test case for ByteBuffer
        Class<?> rawType4 = ByteBuffer.class;
        String clsName4 = rawType4.getName();
        JsonDeserializer<?> deserializer4 = JdkDeserializers.find(rawType4, clsName4);
        assertTrue(deserializer4 instanceof ByteBufferDeserializer);
        
        // Test case for unsupported class name
        Class<?> rawType5 = Date.class;
        String clsName5 = rawType5.getName();
        JsonDeserializer<?> deserializer5 = JdkDeserializers.find(rawType5, clsName5);
        assertNull(deserializer5);
    }
}