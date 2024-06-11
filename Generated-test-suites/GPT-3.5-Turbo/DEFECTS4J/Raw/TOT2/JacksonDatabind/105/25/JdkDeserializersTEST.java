package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFindWithValidClass() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    @Test
    public void testFindWithInvalidClass() {
        Class<?> rawType = String.class;
        String clsName = String.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }

    @Test
    public void testFindWithNullClass() {
        Class<?> rawType = null;
        String clsName = "test";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }

    @Test
    public void testFindWithStringDeserializer() {
        Class<?> rawType = FromStringDeserializer.class;
        String clsName = FromStringDeserializer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializer);
    }

    @Test
    public void testFindWithAtomicBooleanClass() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFindWithStackTraceElementClass() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFindWithByteBufferClass() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    @Test
    public void testFindWithNullClassName() {
        Class<?> rawType = UUID.class;
        String clsName = null;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }
}