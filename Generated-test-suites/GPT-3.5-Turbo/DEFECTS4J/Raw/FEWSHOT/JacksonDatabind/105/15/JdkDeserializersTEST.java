package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(null, null));
        
        assertNull(JdkDeserializers.find(null, ""));
        
        assertNull(JdkDeserializers.find(Integer.class, null));
        
        assertNull(JdkDeserializers.find(Integer.class, ""));
        
        assertNull(JdkDeserializers.find(Integer.class, "Test"));
        
        assertNull(JdkDeserializers.find(Double.class, "Test"));
        
        assertNull(JdkDeserializers.find(UUID.class, ""));
        
        assertEquals(UUIDDeserializer.class, JdkDeserializers.find(UUID.class, UUID.class.getName()).getClass());
        
        assertNull(JdkDeserializers.find(StackTraceElement.class, ""));
        
        assertEquals(StackTraceElementDeserializer.class, JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()).getClass());
        
        assertNull(JdkDeserializers.find(AtomicBoolean.class, ""));
        
        assertEquals(AtomicBooleanDeserializer.class, JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()).getClass());
        
        assertNull(JdkDeserializers.find(ByteBuffer.class, ""));
        
        assertEquals(ByteBufferDeserializer.class, JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()).getClass());
    }
}