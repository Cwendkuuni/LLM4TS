package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {
    @Test
    public void testFind() {
        // Testing UUID class
        assertEquals(new UUIDDeserializer().getClass(), JdkDeserializers.find(UUID.class, UUID.class.getName()).getClass());

        // Testing StackTraceElement class
        assertEquals(new StackTraceElementDeserializer().getClass(), JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()).getClass());

        // Testing AtomicBoolean class
        assertEquals(new AtomicBooleanDeserializer().getClass(), JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()).getClass());

        // Testing ByteBuffer class
        assertEquals(new ByteBufferDeserializer().getClass(), JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()).getClass());

        // Testing classes not found in JdkDeserializers
        assertNull(JdkDeserializers.find(Object.class, Object.class.getName()));
        assertNull(JdkDeserializers.find(String.class, String.class.getName()));
        assertNull(JdkDeserializers.find(Integer.class, Integer.class.getName()));
    }
}