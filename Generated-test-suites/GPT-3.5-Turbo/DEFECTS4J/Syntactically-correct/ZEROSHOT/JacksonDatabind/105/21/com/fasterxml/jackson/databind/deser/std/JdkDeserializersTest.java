package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.*;

public class JdkDeserializersTest {

    @Test
    public void testFindForUUID() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFindForAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFindForStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFindForByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ByteBufferDeserializer);
    }
    
    @Test
    public void testFindForUnknownType() {
        Class<?> rawType = ArrayList.class;
        String clsName = ArrayList.class.getName();
        JsonDeserializer<?> result = JdkDeserializers.find(rawType, clsName);
        
        Assert.assertNull(result);
    }
}