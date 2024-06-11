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
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_atomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_stackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_byteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_unknownClass() {
        Class<?> rawType = Integer.class;
        String clsName = Integer.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertNull(result);
    }
}