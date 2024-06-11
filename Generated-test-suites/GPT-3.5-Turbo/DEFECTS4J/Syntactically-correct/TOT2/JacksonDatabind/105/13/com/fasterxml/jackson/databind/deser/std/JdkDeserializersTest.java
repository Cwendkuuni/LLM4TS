package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import org.junit.Test;

public class JdkDeserializersTest {

    // Test for find method with UUID class
    @Test
    public void testFindWithUUID() {
        Class<?> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }

    // Test for find method with StackTraceElement class
    @Test
    public void testFindWithStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }

    // Test for find method with AtomicBoolean class
    @Test
    public void testFindWithAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }

    // Test for find method with ByteBuffer class
    @Test
    public void testFindWithByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }

    // Test for find method with unsupported class
    @Test
    public void testFindWithUnsupportedClass() {
        Class<?> rawType = Date.class;
        String clsName = Date.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);

        assertNull(deserializer);
    }
}