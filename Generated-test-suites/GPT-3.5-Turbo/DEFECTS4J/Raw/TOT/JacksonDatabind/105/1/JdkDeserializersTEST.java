import org.junit.Test;
import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withUUIDClassAndValidClsName_shouldReturnUUIDDeserializer() {
        JsonDeserializer<?> result = JdkDeserializers.find(UUID.class, UUID.class.getName());
        
        assertTrue(result instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_withStackTraceElementClassAndValidClsName_shouldReturnStackTraceElementDeserializer() {
        JsonDeserializer<?> result = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        
        assertTrue(result instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_withAtomicBooleanClassAndValidClsName_shouldReturnAtomicBooleanDeserializer() {
        JsonDeserializer<?> result = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_withByteBufferClassAndValidClsName_shouldReturnByteBufferDeserializer() {
        JsonDeserializer<?> result = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        
        assertTrue(result instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_withPrimitiveTypeClassAndValidClsName_shouldReturnNull() {
        JsonDeserializer<?> result = JdkDeserializers.find(String.class, String.class.getName());
        
        assertNull(result);
    }
    
    @Test
    public void testFind_withInvalidClsName_shouldReturnNull() {
        JsonDeserializer<?> result = JdkDeserializers.find(UUID.class, "InvalidClassName");
        
        assertNull(result);
    }
    
}