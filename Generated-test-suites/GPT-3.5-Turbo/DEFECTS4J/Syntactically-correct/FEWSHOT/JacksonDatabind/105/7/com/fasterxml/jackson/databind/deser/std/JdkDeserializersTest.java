package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(String.class, "java.lang.String"));
        assertEquals(JdkDeserializers.find(UUID.class, "java.util.UUID").getClass(), UUIDDeserializer.class);
        assertEquals(JdkDeserializers.find(AtomicBoolean.class, "java.util.concurrent.atomic.AtomicBoolean").getClass(), AtomicBooleanDeserializer.class);
        assertEquals(JdkDeserializers.find(StackTraceElement.class, "java.lang.StackTraceElement").getClass(), StackTraceElementDeserializer.class);
        assertEquals(JdkDeserializers.find(ByteBuffer.class, "java.nio.ByteBuffer").getClass(), ByteBufferDeserializer.class);
    }
}