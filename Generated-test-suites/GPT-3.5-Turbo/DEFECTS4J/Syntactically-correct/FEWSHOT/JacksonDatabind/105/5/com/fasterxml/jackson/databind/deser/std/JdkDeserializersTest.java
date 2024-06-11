package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        assertNull(JdkDeserializers.find(Object.class, "java.lang.String"));
        assertNull(JdkDeserializers.find(Object.class, "java.lang.Integer"));

        assertTrue(JdkDeserializers.find(UUID.class, UUID.class.getName()) instanceof UUIDDeserializer);
        assertTrue(JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName()) instanceof AtomicBooleanDeserializer);
        assertTrue(JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName()) instanceof StackTraceElementDeserializer);
        assertTrue(JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName()) instanceof ByteBufferDeserializer);
    }
}