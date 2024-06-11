package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

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

/**
 * Unit tests for {@link StdKeyDeserializer}.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() throws Exception {
        // Test for various types
        Class<?>[] types = {String.class, Object.class, Boolean.class, Byte.class, Short.class,
                Character.class, Integer.class, Long.class, Float.class, Double.class,
                URI.class, URL.class, Class.class, Locale.class, Currency.class, UUID.class,
                Date.class, Calendar.class};
        for (Class<?> type : types) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
            assertEquals(type, keyDeserializer.getKeyClass());
        }

        // Test for unsupported types
        Class<?>[] unsupportedTypes = {Double.TYPE, Date.class, Calendar.class, int[].class};
        for (Class<?> unsupportedType : unsupportedTypes) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(unsupportedType);
            assertNull(keyDeserializer);
        }
    }

    @Test
    public void testDeserializeKey() throws Exception {
        // Test deserialization for various types
        String key;
        Object result;

        // String
        key = "foo";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class).deserializeKey(key, null);
        assertEquals(key, result);

        // Boolean
        key = "true";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey(key, null);
        assertTrue((boolean) result);

        key = "false";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class).deserializeKey(key, null);
        assertFalse((boolean) result);

        // Byte
        key = "127";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, null);
        assertEquals((byte) 127, result);

        key = "-128";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, null);
        assertEquals((byte) -128, result);

        key = "256"; // should fail
        try {
            new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class).deserializeKey(key, null);
            fail("Expected IOException for key '256'");
        } catch (IOException e) {
            // Expected
        }

        // Short
        key = "127";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class).deserializeKey(key, null);
        assertEquals((short) 127, result);

        key = "-32768";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class).deserializeKey(key, null);
        assertEquals((short) -32768, result);

        key = "32768"; // should fail
        try {
            new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class).deserializeKey(key, null);
            fail("Expected IOException for key '32768'");
        } catch (IOException e) {
            // Expected
        }

        // Char
        key = "A";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey(key, null);
        assertEquals((char) 'A', result);

        key = "\u00EF";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class).deserializeKey(key, null);
        assertEquals((char) 0xEF, result);

        // Int
        key = "12345";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class).deserializeKey(key, null);
        assertEquals(12345, result.intValue());

        // Long
        key = "123456789012345";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class).deserializeKey(key, null);
        assertEquals(123456789012345L, result.longValue());

        // Float
        key = "123.45";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class).deserializeKey(key, null);
        assertEquals((float) 123.45, result);

        // Double
        key = "123.456";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class).deserializeKey(key, null);
        assertEquals(123.456, result.doubleValue(), 0.0);

        // URI
        key = "http://example.com";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class).deserializeKey(key, null);
        assertInstanceOf(URI.class, result);
        URI uri = (URI) result;
        assertEquals("http://example.com", uri.toString());

        // URL
        key = "http://example.com/";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class).deserializeKey(key, null);
        assertInstanceOf(URL.class, result);
        URL url = (URL) result;
        assertEquals("http://example.com/", url.toString());

        // Class
        key = "java.lang.String";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class).deserializeKey(key, null);
        assertEquals(String.class, result);

        // Unsupported type - Enum
        key = "RED";
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Enum.class);
        try {
            keyDeserializer.deserializeKey(key, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParse() throws Exception {
        // Test _parse method for various types
        String key;
        Object result;

        // Boolean
        key = "true";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class)._parse(key, null);
        assertTrue((boolean) result);

        key = "false";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class)._parse(key, null);
        assertFalse((boolean) result);

        // Byte
        key = "127";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class)._parse(key, null);
        assertEquals((byte) 127, result);

        // Short
        key = "127";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class)._parse(key, null);
        assertEquals((short) 127, result);

        // Char
        key = "A";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class)._parse(key, null);
        assertEquals((char) 'A', result);

        // Int
        key = "12345";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class)._parse(key, null);
        assertEquals(12345, result.intValue());

        // Long
        key = "123456789012345";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class)._parse(key, null);
        assertEquals(123456789012345L, result.longValue());

        // Float
        key = "123.45";
        try {
            result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class)._parse(key, null);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }

        // Double
        key = "123.456";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class)._parse(key, null);
        assertEquals(123.456, result.doubleValue(), 0.0);

        // Enum
        EnumResolver enumResolver = new EnumResolver(EnumTest.class, null);
        StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Object.class);
        enumKeyDeserializer._deser = enumResolver;

        key = "RED";
        result = enumKeyDeserializer._parse(key, null);
        assertEquals(EnumTest.Red.class, result.getClass());
        EnumTest.Red enumValue = (EnumTest.Red) result;
        assertEquals(EnumTest.Red.RED, enumValue);

        key = "GREEN";
        try {
            result = enumKeyDeserializer._parse(key, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // URI
        key = "http://example.com";
        FromStringDeserializer uriDeserializer = new FromStringDeserializer(URI.class);
        StdKeyDeserializer uriKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, Object.class);
        uriKeyDeserializer._deser = uriDeserializer;
        result = uriKeyDeserializer._parse(key, null);
        assertInstanceOf(URI.class, result);
        URI uri = (URI) result;
        assertEquals("http://example.com", uri.toString());

        // URL
        key = "http://example.com/";
        FromStringDeserializer urlDeserializer = new FromStringDeserializer(URL.class);
        StdKeyDeserializer urlKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, Object.class);
        urlKeyDeserializer._deser = urlDeserializer;
        result = urlKeyDeserializer._parse(key, null);
        assertInstanceOf(URL.class, result);
        URL url = (URL) result;
        assertEquals("http://example.com/", url.toString());

        // Class
        key = "java.lang.String";
        result = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class)._parse(key, null);
        assertEquals(String.class, result);
    }
}