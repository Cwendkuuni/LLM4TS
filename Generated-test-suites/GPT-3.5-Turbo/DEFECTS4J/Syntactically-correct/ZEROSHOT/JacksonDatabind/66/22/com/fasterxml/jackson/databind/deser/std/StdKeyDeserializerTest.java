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

public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class) {
            @Override
            protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return key;
            }
        };
    }

    @Test
    public void testDeserializeKey() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        keyDeserializer.deserializeKey("test", ctxt);
        // Add asserts as necessary
    }

    @Test
    public void testGetKeyClass() {
        Class<?> keyClass = keyDeserializer.getKeyClass();
        // Add asserts as necessary
    }

    @Test
    public void testParseString() throws Exception {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null,null,null);
        keyDeserializer._parse(key, ctxt);
        // Add asserts as necessary
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInt() throws Exception {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null,null,null);
        keyDeserializer._parseInt(key);
        // Add asserts as necessary
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLong() throws Exception {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null,null,null);
        keyDeserializer._parseLong(key);
        // Add asserts as necessary
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDouble() throws Exception {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null,null,null);
        keyDeserializer._parseDouble(key);
        // Add asserts as necessary
    }
}