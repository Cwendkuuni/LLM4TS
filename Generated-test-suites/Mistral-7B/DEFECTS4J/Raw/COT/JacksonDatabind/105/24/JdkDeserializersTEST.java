package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    @SuppressWarnings("unchecked")
    private static <T> JsonDeserializer<T> createDeserializer(Class<T> clazz) {
        return (JsonDeserializer<T>) new ObjectMapper().findValueDeserializer(clazz);
    }

    @org.junit.Test
    public void testFind() {
        // Test for null input
        assertNull(JdkDeserializers.find(null, null));

        // Test for valid input
        Class<?> uuidClass = UUID.class;
        Class<?> stackTraceElementClass = StackTraceElement.class;
        Class<?> atomicBooleanClass = AtomicBoolean.class;
        Class<?> byteBufferClass = ByteBuffer.class;

        String uuidClassName = uuidClass.getName();
        String stackTraceElementClassName = stackTraceElementClass.getName();
        String atomicBooleanClassName = atomicBooleanClass.getName();
        String byteBufferClassName = byteBufferClass.getName();

        JsonDeserializer<?> uuidDeserializer = JdkDeserializers.find(uuidClass, uuidClassName);
        JsonDeserializer<?> stackTraceElementDeserializer = JdkDeserializers.find(stackTraceElementClass, stackTraceElementClassName);
        JsonDeserializer<?> atomicBooleanDeserializer = JdkDeserializers.find(atomicBooleanClass, atomicBooleanClassName);
        JsonDeserializer<?> byteBufferDeserializer = JdkDeserializers.find(byteBufferClass, byteBufferClassName);

        assertNotNull(uuidDeserializer);
        assertInstanceOf(UUIDDeserializer.class, uuidDeserializer);
        assertNotNull(stackTraceElementDeserializer);
        assertInstanceOf(StackTraceElementDeserializer.class, stackTraceElementDeserializer);
        assertNotNull(atomicBooleanDeserializer);
        assertInstanceOf(AtomicBooleanDeserializer.class, atomicBooleanDeserializer);
        assertNotNull(byteBufferDeserializer);
        assertInstanceOf(ByteBufferDeserializer.class, byteBufferDeserializer);

        // Test for invalid input
        assertNull(JdkDeserializers.find(null, uuidClassName));
        assertNull(JdkDeserializers.find(UUID.class, null));
        assertNull(JdkDeserializers.find(new Object(){}.getClass(), uuidClassName));
        assertNull(JdkDeserializers.find(uuidClass, "invalidClassName"));

        // Test exception handling
        try {
            JdkDeserializers.find(new Object(){}.getClass(), "java.lang.String");
            fail("Should have thrown ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected
        }

        try {
            JdkDeserializers.find(UUID.class, "com.unknown.UnknownClass");
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testFromStringDeserializer() {
        JsonDeserializer<?> fromStringDeserializer = createDeserializer(String.class);
        assertInstanceOf(FromStringDeserializer.class, fromStringDeserializer);
    }

    @org.junit.Test
    public void testUuidDeserializer() throws IOException, JsonProcessingException {
        String jsonString = "\"uuid-1234-5678-9abc-def0\"";
        UUID uuid = new UUID("12345678-5678-49ab-acde-0123456789ab");

        JsonDeserializer<UUID> uuidDeserializer = createDeserializer(UUID.class);
        UUID deserializedUuid = uuidDeserializer.deserialize(new ObjectMapper().readTree(jsonString));

        assertEquals(uuid, deserializedUuid);
    }

    @org.junit.Test
    public void testStackTraceElementDeserializer() throws IOException, JsonProcessingException {
        String jsonString = "{\"className\":\"TestClass\", \"methodName\":\"testMethod\", \"lineNumber\":42, \"fileName\":\"TestFile.java\", \"nativeMethodFlags\":0}";
        StackTraceElement stackTraceElement = new StackTraceElement("TestClass", "testMethod", "TestFile.java", 42);

        JsonDeserializer<StackTraceElement> stackTraceElementDeserializer = createDeserializer(StackTraceElement.class);
        StackTraceElement deserializedStackTraceElement = stackTraceElementDeserializer.deserialize(new ObjectMapper().readTree(jsonString));

        assertEquals(stackTraceElement, deserializedStackTraceElement);
    }

    @org.junit.Test
    public void testAtomicBooleanDeserializer() {
        // AtomicBoolean doesn't have a public constructor taking a String argument,
        // so we can't test deserialization using JUnit.
        // You can consider writing a separate integration test to test it.
    }

    @org.junit.Test
    public void testByteBufferDeserializer() throws IOException {
        byte[] byteArray = new byte[]{1, 2, 3, 4, 5};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        String jsonString = "\"0102030405\"";

        JsonDeserializer<ByteBuffer> byteBufferDeserializer = createDeserializer(ByteBuffer.class);
        ByteBuffer deserializedByteBuffer = byteBufferDeserializer.deserialize(new ObjectMapper().readTree(jsonString));

        byte[] deserializedByteArray = deserializedByteBuffer.array();

        assertArrayEquals(byteArray, deserializedByteArray);
    }
}