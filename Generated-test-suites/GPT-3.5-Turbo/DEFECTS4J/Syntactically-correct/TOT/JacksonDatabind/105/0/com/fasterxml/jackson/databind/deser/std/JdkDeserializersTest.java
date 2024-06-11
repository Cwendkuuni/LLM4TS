import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_WithValidClassAndValidClsName_ShouldReturnDeserializerForUUID() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_WithValidClassAndValidClsName_ShouldReturnDeserializerForStackTraceElement() {
        // Arrange
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_WithValidClassAndValidClsName_ShouldReturnDeserializerForAtomicBoolean() {
        // Arrange
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_WithValidClassAndValidClsName_ShouldReturnDeserializerForByteBuffer() {
        // Arrange
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_WithInvalidClassAndValidClsName_ShouldReturnNull() {
        // Arrange
        Class<?> rawType = Integer.class;
        String clsName = Integer.class.getName();
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_WithValidClassAndInvalidClsName_ShouldReturnNull() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = "InvalidClassName";
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_WithInvalidClassAndInvalidClsName_ShouldReturnNull() {
        // Arrange
        Class<?> rawType = Integer.class;
        String clsName = "InvalidClassName";
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_WithValidClassAndNullClsName_ShouldReturnNull() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = null;
        
        // Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        assertNull(deserializer);
    }
    
}