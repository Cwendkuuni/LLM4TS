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

@JacksonStdImpl
public class StdKeyDeserializerTest {

    private static StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer();
    }

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext();
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(key, result);
    }

    @Test
    public void testGetKeyClass() {
        Class<?> keyClass = keyDeserializer.getKeyClass();
        assertEquals(Object.class, keyClass);
    }

    @Test
    public void testIntParse() throws Exception {
        String key = "100";
        int result = keyDeserializer._parseInt(key);
        assertEquals(100, result);
    }

    @Test
    public void testLongParse() throws Exception {
        String key = "1000";
        long result = keyDeserializer._parseLong(key);
        assertEquals(1000L, result);
    }

    @Test
    public void testDoubleParse() throws Exception {
        String key = "3.14159";
        double result = keyDeserializer._parseDouble(key);
        assertEquals(3.14159, result, 0.00001);
    }
}