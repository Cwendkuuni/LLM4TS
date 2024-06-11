import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_StackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_AtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFind_NotFound() {
        Class<?> rawType = Integer.class;
        String clsName = "java.lang.Integer";
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }
}