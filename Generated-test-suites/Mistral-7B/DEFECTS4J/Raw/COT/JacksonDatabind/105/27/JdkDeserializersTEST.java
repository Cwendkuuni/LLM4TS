package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.deser.std.*; // Import all deserializers for completeness

/**
 * JUnit test file for JdkDeserializers class
 */
public class JdkDeserializersTest
{
    @org.junit.Test
    public void testFind_NullRawType()
    {
        assertNull(JdkDeserializers.find(null, "UUID.class"));
    }

    @org.junit.Test
    public void testFind_ValidClassName() throws JsonProcessingException, IOException
    {
        JsonDeserializer<UUID> deserializer = JdkDeserializers.find(UUID.class, "UUID.class");
        assertNotNull(deserializer);
        UUID result = mapper.readValue(json, UUID.class);
        assertEquals(deserializer, result.deserializer());
    }

    @org.junit.Test
    public void testFind_InvalidClassName()
    {
        assertNull(JdkDeserializers.find(Integer.class, "UUID.class"));
    }

    @org.junit.Test
    public void testFind_FromStringDeserializer() throws JsonProcessingException, IOException
    {
        JsonDeserializer<String> fromStringDeserializer = JdkDeserializers.find(String.class, FromStringDeserializer.class.getName());
        assertNotNull(fromStringDeserializer);
        assertEquals(FromStringDeserializer.class, fromStringDeserializer.getClass());
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testFind_InvalidReturnType()
    {
        JsonDeserializer<Integer> deserializer = JdkDeserializers.find(UUID.class, "Integer.class");
        fail("Expected ClassCastException, but got: " + deserializer);
    }

    @org.junit.Test(expected = ClassNotFoundException.class)
    public void testFind_UnknownClassName()
    {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(Object.class, "UnknownClass.class");
        fail("Expected ClassNotFoundException, but got: " + deserializer);
    }

    @org.junit.Test
    public void testFind_AtomicBoolean() throws JsonProcessingException, IOException
    {
        JsonDeserializer<AtomicBoolean> deserializer = JdkDeserializers.find(AtomicBoolean.class, "AtomicBoolean.class");
        assertNotNull(deserializer);
        AtomicBoolean result = mapper.readValue(json, AtomicBoolean.class);
        assertEquals(deserializer, result.deserializer());
    }

    @org.junit.Test
    public void testFind_StackTraceElement() throws JsonProcessingException, IOException
    {
        JsonDeserializer<StackTraceElement> deserializer = JdkDeserializers.find(StackTraceElement.class, "StackTraceElement.class");
        assertNotNull(deserializer);
        StackTraceElement result = mapper.readValue(json, StackTraceElement.class);
        assertEquals(deserializer, result.deserializer());
    }

    @org.junit.Test
    public void testFind_ByteBuffer() throws JsonProcessingException, IOException
    {
        JsonDeserializer<ByteBuffer> deserializer = JdkDeserializers.find(ByteBuffer.class, "ByteBuffer.class");
        assertNotNull(deserializer);
        ByteBuffer result = mapper.readValue(json, ByteBuffer.class);
        assertEquals(deserializer, result.deserializer());
    }

    private static ObjectMapper mapper = new ObjectMapper();
    private static String json = "[]"; // Empty JSON array for testing deserialization
}