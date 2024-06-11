package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_withUUIDClassAndMatchingClassName_returnsUUIDDeserializer() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        Assert.assertTrue(result instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_withStackTraceElementClassAndMatchingClassName_returnsStackTraceElementDeserializer() {
        // Arrange
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        
        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        Assert.assertTrue(result instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_withAtomicBooleanClassAndMatchingClassName_returnsAtomicBooleanDeserializer() {
        // Arrange
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        
        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        Assert.assertTrue(result instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_withByteBufferClassAndMatchingClassName_returnsByteBufferDeserializer() {
        // Arrange
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        
        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        Assert.assertTrue(result instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_withUnknownClassAndMatchingClassName_returnsNull() {
        // Arrange
        Class<?> rawType = Integer.class; // Assuming Integer is not present in _classNames HashSet
        String clsName = Integer.class.getName();
        
        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testFind_withMatchingClassNameButNoMatchedDeserializer_returnsNull() {
        // Arrange
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        // Act
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        // Assert
        Assert.assertNull(result);
    }
}