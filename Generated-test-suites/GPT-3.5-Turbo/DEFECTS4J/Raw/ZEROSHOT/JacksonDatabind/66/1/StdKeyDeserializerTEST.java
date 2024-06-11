package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
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

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "testKey";
        DeserializationContext ctxt = new DefaultDeserializationContext(new ObjectMapper(), null);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_STRING, Object.class);
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assert(result.equals(key));
    }

    @Test
    public void testParse() throws Exception {
        String key = "1";
        DeserializationContext ctxt = new DefaultDeserializationContext(new ObjectMapper(), null);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_INT, Object.class);
        Object result = keyDeserializer._parse(key, ctxt);
        assert(result.equals(1));
    }

    @Test
    public void testParseInt() throws Exception {
        String key = "123";
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_INT, Object.class);
        int result = keyDeserializer._parseInt(key);
        assert(result == 123);
    }

    @Test
    public void testParseLong() throws Exception {
        String key = "1234567890";
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_LONG, Object.class);
        long result = keyDeserializer._parseLong(key);
        assert(result == 1234567890);
    }

    @Test
    public void testParseDouble() throws Exception {
        String key = "3.14";
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_DOUBLE, Object.class);
        double result = keyDeserializer._parseDouble(key);
        assert(result == 3.14);
    }
}