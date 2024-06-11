package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        // Arrange
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return key;
            }
        };

        // Act
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        // Assert
        Assert.assertEquals(key, result);
    }

    @Test
    public void testGetKeyClass() {
        // Arrange
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return key;
            }
        };

        // Act
        Class<?> keyClass = stdKeyDeserializer.getKeyClass();

        // Assert
        Assert.assertEquals(String.class, keyClass);
    }

    @Test
    public void test_parse() throws Exception {
        // Arrange
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return key;
            }
        };

        // Act
        Object result = stdKeyDeserializer._parse(key, ctxt);

        // Assert
        Assert.assertEquals(key, result);
    }

    @Test
    public void test_parseInt() {
        // Arrange
        String key = "123";
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected int _parseInt(String key) throws IllegalArgumentException {
                return Integer.parseInt(key);
            }
        };

        // Act
        int result = stdKeyDeserializer._parseInt(key);

        // Assert
        Assert.assertEquals(123, result);
    }

    @Test
    public void test_parseLong() {
        // Arrange
        String key = "12345";
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected long _parseLong(String key) throws IllegalArgumentException {
                return Long.parseLong(key);
            }
        };

        // Act
        long result = stdKeyDeserializer._parseLong(key);

        // Assert
        Assert.assertEquals(12345L, result);
    }

    @Test
    public void test_parseDouble() {
        // Arrange
        String key = "12.34";
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected double _parseDouble(String key) throws IllegalArgumentException {
                return NumberInput.parseDouble(key);
            }
        };

        // Act
        double result = stdKeyDeserializer._parseDouble(key);

        // Assert
        Assert.assertEquals(12.34, result, 0);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        // Arrange
        Constructor<?> ctor = String.class.getConstructor(String.class);
        StringCtorKeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(ctor);
        String key = "test";

        // Act
        Object result = keyDeserializer._parse(key, null);

        // Assert
        Assert.assertEquals(key, result);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        // Arrange
        Method factoryMethod = String.class.getMethod("valueOf", String.class);
        StringFactoryKeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(factoryMethod);
        String key = "test";

        // Act
        Object result = keyDeserializer._parse(key, null);

        // Assert
        Assert.assertEquals(key, result);
    }

}