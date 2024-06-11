package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_ReturnsNull_WhenClassnameNotInClassNamesSet() {
        Class<?> rawType = Object.class;
        String className = "java.lang.Object";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNull(result);
    }

    @Test
    public void testFind_ReturnsNull_WhenClassnameInClassNamesSetButNoDeserializerFound() {
        Class<?> rawType = LinkedList.class;
        String className = "java.util.LinkedList";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNull(result);
    }

    @Test
    public void testFind_ReturnsJsonDeserializer_WhenClassnameInClassNamesSet() {
        Class<?> rawType = UUID.class;
        String className = "java.util.UUID";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_ReturnsUUIDDeserializer_WhenRawTypeIsUUID() {
        Class<?> rawType = UUID.class;
        String className = "java.util.UUID";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNotNull(result);
        assertTrue(result instanceof UUIDDeserializer);
    }

    @Test
    public void testFind_ReturnsStackTraceElementDeserializer_WhenRawTypeIsStackTraceElement() {
        Class<?> rawType = StackTraceElement.class;
        String className = "java.lang.StackTraceElement";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNotNull(result);
        assertTrue(result instanceof StackTraceElementDeserializer);
    }

    @Test
    public void testFind_ReturnsAtomicBooleanDeserializer_WhenRawTypeIsAtomicBoolean() {
        Class<?> rawType = AtomicBoolean.class;
        String className = "java.util.concurrent.atomic.AtomicBoolean";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNotNull(result);
        assertTrue(result instanceof AtomicBooleanDeserializer);
    }

    @Test
    public void testFind_ReturnsByteBufferDeserializer_WhenRawTypeIsByteBuffer() {
        Class<?> rawType = ByteBuffer.class;
        String className = "java.nio.ByteBuffer";

        JsonDeserializer<?> result = JdkDeserializers.find(rawType, className);

        assertNotNull(result);
        assertTrue(result instanceof ByteBufferDeserializer);
    }
}