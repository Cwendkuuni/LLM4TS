
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class JdkDeserializersTest {
    
    @Test
    public void testFind_withUUIDClassAndUUIDClsName_shouldReturnUUIDDeserializer() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_withStackTraceElementClassAndStackTraceElementClsName_shouldReturnStackTraceElementDeserializer() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_withAtomicBooleanClassAndAtomicBooleanClsName_shouldReturnAtomicBooleanDeserializer() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_withByteBufferClassAndByteBufferClsName_shouldReturnByteBufferDeserializer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertTrue(result instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFind_withUnknownClass_shouldReturnNull() {
        Class<?> rawType = Object.class;
        String clsName = Object.class.getName();
        
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        assertNull(result);
    }
}
