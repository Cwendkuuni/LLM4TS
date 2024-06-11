package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    @org.junit.Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        Object result = stdKeyDeserializer.deserializeKey("123", ctxt);
        assert result.equals(123);
        result = stdKeyDeserializer.deserializeKey("not a valid key", ctxt);
        assert result.equals(null);

        stdKeyDeserializer = StdKeyDeserializer.forType(String.class);
        result = stdKeyDeserializer.deserializeKey("hello", ctxt);
        assert result.equals("hello");
    }

    @org.junit.Test
    public void testGetKeyClass() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Class<?> result = stdKeyDeserializer.getKeyClass();
        assert result.equals(Integer.class);
    }

    @org.junit.Test
    public void testParseInt() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        int result = 0;
        try {
            result = stdKeyDeserializer._parseInt("123");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        assert result == 123;
    }

    @org.junit.Test
    public void testParseLong() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        long result = 0;
        try {
            result = stdKeyDeserializer._parseLong("1234567890");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        assert result == 1234567890;
    }

    @org.junit.Test
    public void testParseDouble() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        double result = 0;
        try {
            result = stdKeyDeserializer._parseDouble("3.14");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        assert result == 3.14;
    }
    
    @org.junit.Test
    public void testEnumKD() {
        EnumResolver enumResolver = new EnumResolver(MockEnum.class, null);
        AnnotatedMethod annotatedMethod = null;
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(enumResolver, annotatedMethod);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        Object result = enumKD._parse("VALUE_1", ctxt);
        assert result == MockEnum.VALUE_1;
    }
        
    @org.junit.Test
    public void testStringCtorKeyDeserializer() {
        Constructor<?> constructor = null;
        try {
            constructor = String.class.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        StdKeyDeserializer.StringCtorKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);

        Object result = null;
        try {
            result = stringCtorKeyDeserializer._parse("hello", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert result.equals("hello");
    }

    @org.junit.Test
    public void testStringFactoryKeyDeserializer() {
        Method factoryMethod = null;
        try {
            factoryMethod = String.class.getDeclaredMethod("valueOf", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        StdKeyDeserializer.StringFactoryKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(factoryMethod);

        Object result = null;
        try {
            result = stringFactoryKeyDeserializer._parse("hello", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert result.equals("hello");
    }
}

enum MockEnum {
    VALUE_1,
    VALUE_2,
    VALUE_3
}