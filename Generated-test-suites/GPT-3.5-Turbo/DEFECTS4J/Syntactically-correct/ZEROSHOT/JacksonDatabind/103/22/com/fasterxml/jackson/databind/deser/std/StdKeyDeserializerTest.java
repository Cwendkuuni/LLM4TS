package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@JacksonStdImpl
public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
    }

    @After
    public void tearDown() {
        keyDeserializer = null;
    }

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "10";
        DeserializationContext ctxt = null;
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        Assert.assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testGetKeyClass() {
        Class<?> keyClass = keyDeserializer.getKeyClass();
        Assert.assertEquals(Integer.class, keyClass);
    }

    @Test
    public void testParseInt() throws IllegalArgumentException {
        String key = "10";
        int result = keyDeserializer._parseInt(key);
        Assert.assertEquals(10, result);
    }

    @Test
    public void testParseLong() throws IllegalArgumentException {
        String key = "1000000000";
        long result = keyDeserializer._parseLong(key);
        Assert.assertEquals(1000000000, result);
    }

    @Test
    public void testParseDouble() throws IllegalArgumentException {
        String key = "3.14";
        double result = keyDeserializer._parseDouble(key);
        Assert.assertEquals(3.14, result, 0.001);
    }

    @Test
    public void testWeirdKey() throws IOException {
        DeserializationContext ctxt = null;
        String key = "abc";
        Exception e = new Exception("Invalid key");
        Object result = keyDeserializer._weirdKey(ctxt, key, e);
        Assert.assertEquals(IOException.class, result.getClass());
    }
}