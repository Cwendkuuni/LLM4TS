package com.example.test; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    

    @org.junit.Test
    public void testDeserializeKey_String() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        String key = "test";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals("test", deserializedKey);
    }

    @org.junit.Test
    public void testDeserializeKey_Int() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        String key = "123";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(123, deserializedKey);
    }

    @org.junit.Test
    public void testDeserializeKey_Boolean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        String key = "true";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue((Boolean) deserializedKey);

        key = "false";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertFalse((Boolean) deserializedKey);
    }

    @org.junit.Test
    public void testDeserializeKey_Enum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Enum enumType = Enum.class;
        Enum enumValue = Enum.valueOf(enumType, "VALUE");

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(enumType);
        String key = enumValue.name();
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(enumValue, deserializedKey);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeKey_Null() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        String key = null;
        keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        fail("Expected IOException, but no exception was thrown.");
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeKey_InvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        String key = "test";
        try {
            keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected IOException, but no exception was thrown.");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @org.junit.Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidValue_Boolean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        String key = "invalid";
        keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        fail("Expected JsonProcessingException, but no exception was thrown.");
    }

    @org.junit.Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidValue_Enum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Enum enumType = Enum.class;
        String invalidEnumValue = "InvalidValue";

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(enumType);
        keyDeserializer.deserializeKey(invalidEnumValue, mapper.getDeserializationContext());
        fail("Expected JsonProcessingException, but no exception was thrown.");
    }

    
}