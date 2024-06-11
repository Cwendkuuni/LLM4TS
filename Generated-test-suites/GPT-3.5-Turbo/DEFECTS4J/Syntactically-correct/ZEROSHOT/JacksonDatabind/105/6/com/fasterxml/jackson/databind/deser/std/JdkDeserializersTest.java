import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind() {
        // Test for UUID
        Class<?> rawTypeUUID = UUID.class;
        String clsNameUUID = "java.util.UUID";
        JsonDeserializer<?> resultUUID = JdkDeserializers.find(rawTypeUUID, clsNameUUID);
        assertNotNull(resultUUID);
        assertTrue(resultUUID instanceof UUIDDeserializer);
        
        // Test for StackTraceElement
        Class<?> rawTypeStackTraceElement = StackTraceElement.class;
        String clsNameStackTraceElement = "java.lang.StackTraceElement";
        JsonDeserializer<?> resultStackTraceElement = JdkDeserializers.find(rawTypeStackTraceElement, clsNameStackTraceElement);
        assertNotNull(resultStackTraceElement);
        assertTrue(resultStackTraceElement instanceof StackTraceElementDeserializer);
        
        // Test for AtomicBoolean
        Class<?> rawTypeAtomicBoolean = AtomicBoolean.class;
        String clsNameAtomicBoolean = "java.util.concurrent.atomic.AtomicBoolean";
        JsonDeserializer<?> resultAtomicBoolean = JdkDeserializers.find(rawTypeAtomicBoolean, clsNameAtomicBoolean);
        assertNotNull(resultAtomicBoolean);
        assertTrue(resultAtomicBoolean instanceof AtomicBooleanDeserializer);
        
        // Test for ByteBuffer
        Class<?> rawTypeByteBuffer = ByteBuffer.class;
        String clsNameByteBuffer = "java.nio.ByteBuffer";
        JsonDeserializer<?> resultByteBuffer = JdkDeserializers.find(rawTypeByteBuffer, clsNameByteBuffer);
        assertNotNull(resultByteBuffer);
        assertTrue(resultByteBuffer instanceof ByteBufferDeserializer);
    }
}