package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

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
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, Boolean.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return null;
            }
        };
        DeserializationContext context = new DeserializationContext(null, null);
        Assert.assertNull(keyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, Boolean.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return null;
            }
        };
        Assert.assertEquals(Boolean.class, keyDeserializer.getKeyClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInt() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, Boolean.class) {
            @Override
            protected int _parseInt(String key) throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }

            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return null;
            }
        };
        keyDeserializer.deserializeKey("123", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLong() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, Boolean.class) {
            @Override
            protected long _parseLong(String key) throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }

            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return null;
            }
        };
        keyDeserializer.deserializeKey("123L", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDouble() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, Boolean.class) {
            @Override
            protected double _parseDouble(String key) throws IllegalArgumentException {
                throw new IllegalArgumentException();
            }

            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return null;
            }
        };
        keyDeserializer.deserializeKey("123.0", null);
    }

    @Test
    public void testStringKD() throws IOException {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);
        DeserializationContext context = new DeserializationContext(null, null);
        Assert.assertEquals("test", stringKD.deserializeKey("test", context));
    }

    @Test
    public void testDelegatingKD() throws IOException {
        JsonDeserializer jsonDeserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        StdKeyDeserializer.DelegatingKD delegatingKD = new StdKeyDeserializer.DelegatingKD(Boolean.class, jsonDeserializer);
        DeserializationContext context = new DeserializationContext(null, null);
        Assert.assertNull(delegatingKD.deserializeKey(null, context));
    }

    @Test
    public void testEnumKD() throws IOException {
        EnumResolver enumResolver = new EnumResolver(Enum.class);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(null, null, null, null);
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(enumResolver, annotatedMethod);
        DeserializationContext context = new DeserializationContext(null, null);
        Assert.assertNull(enumKD.deserializeKey(null, context));
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Constructor constructor = String.class.getConstructor(String.class);
        StdKeyDeserializer.StringCtorKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(constructor);
        DeserializationContext context = new DeserializationContext(null, null);
        Assert.assertEquals("test", stringCtorKeyDeserializer.deserializeKey("test", context));
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Method method = String.class.getMethod("valueOf", String.class);
        StdKeyDeserializer.StringFactoryKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(method);
        DeserializationContext context = new DeserializationContext(null, null);
        Assert.assertEquals("test", stringFactoryKeyDeserializer.deserializeKey("test", context));
    }
}