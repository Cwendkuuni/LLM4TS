import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;
import java.lang.StackTraceElement;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Prepare
        Class<?> rawType = null;
        String clsName = null;
        
        // Test
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Verify
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_WithUUIDClass() {
        // Prepare
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        // Test
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Verify
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_WithStackTraceElementClass() {
        // Prepare
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        
        // Test
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Verify
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_WithAtomicBooleanClass() {
        // Prepare
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        
        // Test
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Verify
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_WithByteBufferClass() {
        // Prepare
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        
        // Test
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Verify
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_WithUnknownClass() {
        // Prepare
        Class<?> rawType = SomeUnknownClass.class;
        String clsName = SomeUnknownClass.class.getName();
        
        // Test
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Verify
        assertNull(deserializer);
    }
}