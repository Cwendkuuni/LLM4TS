package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        // Test UUID class
        assertEquals(new UUIDDeserializer().getClass(), JdkDeserializers.find(UUID.class, UUID.class.getName()).getClass());
        
        // Test AtomicBoolean class
        assertEquals(new AtomicBooleanDeserializer().getClass(), JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()).getClass());
        
        // Test StackTraceElement class
        assertEquals(new StackTraceElementDeserializer().getClass(), JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()).getClass());
        
        // Test ByteBuffer class
        assertEquals(new ByteBufferDeserializer().getClass(), JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()).getClass());
        
        // Test invalid class
        assertNull(JdkDeserializers.find(String.class, String.class.getName()));
    }
}