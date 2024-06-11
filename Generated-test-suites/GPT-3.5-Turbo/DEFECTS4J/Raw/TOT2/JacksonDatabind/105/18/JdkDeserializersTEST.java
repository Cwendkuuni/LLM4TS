package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;

public class JdkDeserializersTest {

    @Test
    public void testFindUUIDDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);
    }

    @Test
    public void testFindStackTraceElementDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
    }

    @Test
    public void testFindAtomicBooleanDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
    }

    @Test
    public void testFindByteBufferDeserializer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
    }

    @Test
    public void testFindNonExistingClass() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer);
    }
}