package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stringKeyDeserializer;
    private StdKeyDeserializer objectKeyDeserializer;

    @Before
    public void setUp() {
        stringKeyDeserializer = new StdKeyDeserializer.StringKD(String.class);
        objectKeyDeserializer = new StdKeyDeserializer.StringKD(Object.class);
    }

    @Test
    public void testStringKeyDeserializer_DeserializeKey_ValidString() throws IOException, JsonProcessingException {
        String key = "test";
        DeserializationContext context = null;

        Object result = stringKeyDeserializer.deserializeKey(key, context);

        assertEquals(key, result);
    }

    @Test
    public void testStringKeyDeserializer_DeserializeKey_NullString() throws IOException, JsonProcessingException {
        String key = null;
        DeserializationContext context = null;

        Object result = stringKeyDeserializer.deserializeKey(key, context);

        assertNull(result);
    }

    @Test
    public void testStringKeyDeserializer_DeserializeKey_ValidString_ObjectKeyType() throws IOException, JsonProcessingException {
        String key = "test";
        DeserializationContext context = null;

        Object result = objectKeyDeserializer.deserializeKey(key, context);

        assertEquals(key, result);
    }

    @Test
    public void testStringKeyDeserializer_DeserializeKey_NullString_ObjectKeyType() throws IOException, JsonProcessingException {
        String key = null;
        DeserializationContext context = null;

        Object result = objectKeyDeserializer.deserializeKey(key, context);

        assertNull(result);
    }
}