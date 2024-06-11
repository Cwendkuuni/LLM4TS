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
    
    private StdKeyDeserializer stdKeyDeserializer;
    
    @Before
    public void setup() {
        stdKeyDeserializer = new StdKeyDeserializer(1, Boolean.class);
    }
    
    @Test
    public void testDeserializeKey() throws IOException {
        String key = "true";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetKeyClass() {
        Class<?> expected = Boolean.class;
        Class<?> result = stdKeyDeserializer.getKeyClass();
        assertEquals(expected, result);
    }
    
    @Test
    public void test_parse() throws Exception {
        String key = "true";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer._parse(key, ctxt);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void test_parseInt() throws Exception {
        String key = "123";
        int expected = 123;
        int result = stdKeyDeserializer._parseInt(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void test_parseLong() throws Exception {
        String key = "123";
        long expected = 123;
        long result = stdKeyDeserializer._parseLong(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void test_parseDouble() throws Exception {
        String key = "12.34";
        double expected = 12.34;
        double result = stdKeyDeserializer._parseDouble(key);
        assertEquals(expected, result, 0.01);
    }
}