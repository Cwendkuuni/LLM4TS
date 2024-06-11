package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class JdkDeserializersTest {

    @org.junit.Test
    public void testFind() {
        // Test null arguments
        assertNull(JdkDeserializers.find(null, null));
        assertNull(JdkDeserializers.find(UUID.class, null));
        assertNull(JdkDeserializers.find(null, "UnknownClass"));

        // Test valid arguments
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);

        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);

        deserializer = JdkDeserializers.find(String[].class, String[].class.getName()); // Edge case: array class
        assertNotNull(deserializer);

        deserializer = JdkDeserializers.find(HashMap.class, HashMap.class.getName()); // Edge case: collection class
        assertNotNull(deserializer);

        deserializer = JdkDeserializers.find(List.class, List.class.getName()); // Edge case: collection interface
        assertNotNull(deserializer);

        deserializer = JdkDeserializers.find(Object.class, Object.class.getName()); // Edge case: Object class
        assertNotNull(deserializer);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFind_NullDeserializer() throws IOException {
        JsonDeserializer<?> deserializer = FromStringDeserializer.findDeserializer(null);
        JdkDeserializers.find(UUID.class, deserializer);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFind_InvalidClassName() {
        JdkDeserializers.find(UUID.class, "InvalidClassName");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFind_InvalidRawType() {
        JdkDeserializers.find(null, UUID.class.getName());
    }

    @org.junit.Test
    public void testFind_UnknownType() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(new TypeReference<List<Integer>>() {}.getType(), "UnknownClass");
        assertNull(deserializer);
    }
}