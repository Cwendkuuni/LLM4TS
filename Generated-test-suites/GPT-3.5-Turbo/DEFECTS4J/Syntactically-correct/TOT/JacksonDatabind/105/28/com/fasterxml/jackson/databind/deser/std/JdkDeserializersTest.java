import org.junit.*;
import static org.junit.Assert.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;

public class JdkDeserializersTest {
    
    // Test cases for find() method
    
    @Test
    public void testFind_ClassNameIn_set_ReturnsDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> expected = new UUIDDeserializer();
        
        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
        
        assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testFind_ClassNameNotIn_set_ReturnsNull() {
        Class<?> rawType = Integer.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> expected = null;
        
        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
        
        assertEquals(expected, actual);
    }
        
    // Test cases for find() method with specific raw types
    
    @Test
    public void testFind_RawTypeIsUUID_ReturnsUUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> expected = new UUIDDeserializer();
        
        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
        
        assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testFind_RawTypeIsStackTraceElement_ReturnsStackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> expected = new StackTraceElementDeserializer();
        
        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
        
        assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testFind_RawTypeIsAtomicBoolean_ReturnsAtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> expected = new AtomicBooleanDeserializer();
        
        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
        
        assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testFind_RawTypeIsByteBuffer_ReturnsByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = rawType.getName();
        JsonDeserializer<?> expected = new ByteBufferDeserializer();
        
        JsonDeserializer<?> actual = JdkDeserializers.find(rawType, clsName);
        
        assertEquals(expected.getClass(), actual.getClass());
    }
    
}