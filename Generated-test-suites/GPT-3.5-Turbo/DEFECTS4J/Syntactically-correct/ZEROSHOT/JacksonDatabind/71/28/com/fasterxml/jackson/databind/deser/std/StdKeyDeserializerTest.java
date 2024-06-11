package com.fasterxml.jackson.databind.deser.std;

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

import org.junit.*;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testStdKeyDeserializer_deserializeKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DefaultDeserializationConfig(), null);
        Object result = deserializer.deserializeKey("key", context);
        assertEquals("key", result);
    }

    @Test
    public void testStdKeyDeserializer_getKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class);
        Class<?> result = deserializer.getKeyClass();
        assertEquals(String.class, result);
    }

    @Test
    public void testStdKeyDeserializer__parse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(1, String.class);
        DeserializationContext context = new DeserializationContext(new ObjectMapper(), new DefaultDeserializationConfig(), null);
        Object result = deserializer._parse("key", context);
        assertEquals("key", result);
    }

    @Test
    public void testStdKeyDeserializer__parseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(5, Integer.class);
        int result = deserializer._parseInt("10");
        assertEquals(10, result);
    }

    @Test
    public void testStdKeyDeserializer__parseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(6, Long.class);
        long result = deserializer._parseLong("10000000000");
        assertEquals(10000000000L, result);
    }

    @Test
    public void testStdKeyDeserializer__parseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(8, Double.class);
        double result = deserializer._parseDouble("3.14");
        assertEquals(3.14, result, 0.0001);
    }

}