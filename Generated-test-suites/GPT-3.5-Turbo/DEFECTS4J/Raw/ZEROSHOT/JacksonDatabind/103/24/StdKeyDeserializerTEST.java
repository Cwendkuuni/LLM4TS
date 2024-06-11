package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    // Test method for deserializeKey(String, DeserializationContext)
    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        KeyDeserializerMockContext ctxt = new KeyDeserializerMockContext();
        try {
            Integer key = (Integer) deserializer.deserializeKey("4", ctxt);
            assertEquals(4, key.intValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test method for getKeyClass()
    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, deserializer.getKeyClass());
    }

    // Test method for _weirdKey(DeserializationContext, String, Exception)
    @Test
    public void testWeirdKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        KeyDeserializerMockContext ctxt = new KeyDeserializerMockContext();
        IOException exception = new IOException("Test Exception");
        Object result = deserializer._weirdKey(ctxt, "testKey", exception);
        assertTrue(result instanceof IOException);
        assertEquals("Test Exception", ((IOException) result).getMessage());
    }

    // Test method for _parseInt(String)
    @Test
    public void testParseInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        try {
            int value = (int) deserializer._parseInt("5");
            assertEquals(5, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // Test method for _parseLong(String)
    @Test
    public void testParseLong() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        try {
            long value = (long) deserializer._parseLong("987654321");
            assertEquals(987654321, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // Test method for _parseDouble(String)
    @Test
    public void testParseDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        try {
            double value = (double) deserializer._parseDouble("3.14");
            assertEquals(3.14, value, 0.001);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}