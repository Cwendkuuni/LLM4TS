package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {
    
    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(Integer.class, "java.lang.Integer"));
        assertNull(JdkDeserializers.find(UUID.class, "java.util.UUID"));

        assertEquals(AtomicBooleanDeserializer.class, JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean").getClass());
        assertEquals(UUIDDeserializer.class, JdkDeserializers.find(UUID.class, "java.util.UUID").getClass());
        assertEquals(StackTraceElementDeserializer.class, JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement").getClass());
        assertEquals(ByteBufferDeserializer.class, JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer").getClass());
    }
}