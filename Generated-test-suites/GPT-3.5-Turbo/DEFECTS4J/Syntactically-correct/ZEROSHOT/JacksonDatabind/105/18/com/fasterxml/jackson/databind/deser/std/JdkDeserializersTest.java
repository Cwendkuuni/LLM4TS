package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        // Test UUID class
        JsonDeserializer<?> uuidDeserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(uuidDeserializer);
        assertTrue(uuidDeserializer instanceof UUIDDeserializer);

        // Test AtomicBoolean class
        JsonDeserializer<?> atomicBooleanDeserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(atomicBooleanDeserializer);
        assertTrue(atomicBooleanDeserializer instanceof AtomicBooleanDeserializer);

        // Test StackTraceElement class
        JsonDeserializer<?> stackTraceElementDeserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(stackTraceElementDeserializer);
        assertTrue(stackTraceElementDeserializer instanceof StackTraceElementDeserializer);

        // Test ByteBuffer class
        JsonDeserializer<?> byteBufferDeserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(byteBufferDeserializer);
        assertTrue(byteBufferDeserializer instanceof ByteBufferDeserializer);

        // Test unknown class
        JsonDeserializer<?> unknownDeserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(unknownDeserializer);
    }
}