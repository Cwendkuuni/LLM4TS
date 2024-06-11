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

    private StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(1, Boolean.class, null);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "true";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testDeserializeKeyWithInvalidKey() throws IOException {
        String key = "invalid";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);
    }

    @Test
    public void testGetKeyClass() {
        Class<?> keyClass = keyDeserializer.getKeyClass();
        assertEquals(Boolean.class, keyClass);
    }

    @Test
    public void testParse() throws Exception {
        String key = "true";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = keyDeserializer._parse(key, ctxt);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testParseWithInvalidKey() throws Exception {
        String key = "invalid";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = keyDeserializer._parse(key, ctxt);
        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithUnknownKeyType() throws Exception {
        String key = "unknown";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        keyDeserializer._parse(key, ctxt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntWithInvalidKey() throws Exception {
        String key = "invalid";
        keyDeserializer._parseInt(key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLongWithInvalidKey() throws Exception {
        String key = "invalid";
        keyDeserializer._parseLong(key);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDoubleWithInvalidKey() throws Exception {
        String key = "invalid";
        keyDeserializer._parseDouble(key);
    }
}