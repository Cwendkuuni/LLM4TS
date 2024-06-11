package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

public class JdkDeserializersTest {

    @Test
    public void testFind_UUID() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_AtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_StackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_ByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNotNull(deserializer);
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFind_OtherClass() {
        Class<?> rawType = HashSet.class;
        String clsName = HashSet.class.getName();
        
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        assertNull(deserializer);
    }
}