package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer;

        // Test for UUID
        deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertTrue(deserializer instanceof UUIDDeserializer);

        // Test for AtomicBoolean
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        // Test for StackTraceElement
        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        // Test for ByteBuffer
        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        // Test for unrecognized class name
        deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer);
    }

}