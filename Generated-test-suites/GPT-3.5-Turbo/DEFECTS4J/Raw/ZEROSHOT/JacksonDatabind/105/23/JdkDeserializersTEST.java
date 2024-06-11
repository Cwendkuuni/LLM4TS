import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;
import java.lang.reflect.Constructor;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof UUIDDeserializer);
        
    }
    
    @Test
    public void testFindAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
        
    }
    
    @Test
    public void testFindStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
        
    }
    
    @Test
    public void testFindByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(deserializer instanceof ByteBufferDeserializer);
        
    }
    
    @Test
    public void testFindNull() {
        Class<?> rawType = String.class;
        String clsName = String.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
        
    }
}