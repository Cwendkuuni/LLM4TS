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

    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setup() {
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String key = "test";
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        //assertions
        assert result instanceof String;
        assert result.equals(key);
    }

    @Test
    public void testGetKeyClass() {
        Class<?> keyClass = stdKeyDeserializer.getKeyClass();

        //assertions
        assert keyClass.equals(String.class);
    }

    @Test
    public void testParse() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String key = "test";
        Object result = stdKeyDeserializer._parse(key, ctxt);

        //assertions
        assert result instanceof String;
        assert result.equals(key);
    }

    @Test
    public void testParseInt() {
        String key = "123";
        int result = stdKeyDeserializer._parseInt(key);

        //assertions
        assert result == 123;
    }

    @Test
    public void testParseLong() {
        String key = "123";
        long result = stdKeyDeserializer._parseLong(key);

        //assertions
        assert result == 123L;
    }

    @Test
    public void testParseDouble() {
        String key = "123.45";
        double result = stdKeyDeserializer._parseDouble(key);

        //assertions
        assert result == 123.45;
    }
}