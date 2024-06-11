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

    private StdKeyDeserializer deserializer;

    @Before
    public void setup() {
        deserializer = new StdKeyDeserializer();
    }

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "testKey";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer.deserializeKey(key, context);
        
        assertEquals(key, result);
    }

    @Test
    public void testGetKeyClass() {
        Class<?> expected = Object.class;
        
        Class<?> result = deserializer.getKeyClass();
        
        assertEquals(expected, result);
    }

    @Test
    public void test_parse() {
        String key = "testKey";
        DeserializationContext context = new DeserializationContext(null, null, null);
        
        Object result = deserializer._parse(key, context);
        
        assertEquals(key, result);
    }

    @Test
    public void test_parseInt() {
        String key = "1";
        
        int result = deserializer._parseInt(key);
        
        assertEquals(1, result);
    }

    @Test
    public void test_parseLong() {
        String key = "1";
        
        long result = deserializer._parseLong(key);
        
        assertEquals(1L, result);
    }

    @Test
    public void test_parseDouble() {
        String key = "1.0";
        
        double result = deserializer._parseDouble(key);
        
        assertEquals(1.0, result, 0.00001);
    }
}