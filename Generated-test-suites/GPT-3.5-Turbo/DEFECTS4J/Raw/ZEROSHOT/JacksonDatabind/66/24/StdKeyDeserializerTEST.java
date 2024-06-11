package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
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
    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setUp() {
        int kind = 1;
        Class<?> cls = String.class;
        stdKeyDeserializer = new StdKeyDeserializer(kind, cls);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "test key";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);
        assert result.equals(key);
    }

    @Test
    public void testGetKeyClass() {
        Class<?> keyClass = stdKeyDeserializer.getKeyClass();
        assert keyClass.equals(String.class);
    }

    @Test
    public void testParse() throws Exception {
        String key = "test key";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer._parse(key, ctxt);
        assert result.equals(key);
    }

    @Test
    public void testParseInt() throws IllegalArgumentException {
        String key = "10";
        int result = stdKeyDeserializer._parseInt(key);
        assert result == 10;
    }

    @Test
    public void testParseLong() throws IllegalArgumentException {
        String key = "1000";
        long result = stdKeyDeserializer._parseLong(key);
        assert result == 1000;
    }

    @Test
    public void testParseDouble() throws IllegalArgumentException {
        String key = "10.5";
        double result = stdKeyDeserializer._parseDouble(key);
        assert result == 10.5;
    }
}