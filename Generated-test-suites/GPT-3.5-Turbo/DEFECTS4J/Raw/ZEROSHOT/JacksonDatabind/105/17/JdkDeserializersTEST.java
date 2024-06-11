import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertTrue(result instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_Null() {
        Class<?> rawType = Integer.class;
        String clsName = "java.lang.Integer";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
    
    @Test
    public void testFind_AtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_StackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertTrue(result instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_ByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertTrue(result instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_FromStringDeserializer() {
        Class<?> rawType = Integer.class;
        String clsName = "java.lang.Integer";
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        assertNull(result);
    }
}