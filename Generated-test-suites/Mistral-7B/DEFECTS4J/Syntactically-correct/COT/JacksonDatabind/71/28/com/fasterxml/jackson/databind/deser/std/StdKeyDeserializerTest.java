package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest extends ObjectMapperTestBase {

    

    @org.junit.Test
    public void testStdKeyDeserializerForType() throws Exception {
        // Test forType method
        Class<?> stringClass = String.class;
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(stringClass);
        assertEquals("StringKeyDeserializer", serializer.getClass().getSimpleName());
        assertEquals(StdKeyDeserializer.TYPE_STRING, serializer.getKind());
        assertEquals(String.class, serializer.getKeyClass());

        Class<?> uuidClass = UUID.class;
        serializer = StdKeyDeserializer.forType(uuidClass);
        assertEquals("UUIDKeyDeserializer", serializer.getClass().getSimpleName());
        assertEquals(StdKeyDeserializer.TYPE_UUID, serializer.getKind());
        assertEquals(UUID.class, serializer.getKeyClass());

        // Test for other types
        Class<?> booleanClass = Boolean.class;
        Class<?> byteClass = Byte.class;
        Class<?> shortClass = Short.class;
        Class<?> charClass = Character.class;
        Class<?> intClass = Integer.class;
        Class<?> longClass = Long.class;
        Class<?> floatClass = Float.class;
        Class<?> doubleClass = Double.class;
        Class<?> localeClass = Locale.class;
        Class<?> dateClass = Date.class;
        Class<?> calendarClass = Calendar.class;
        Class<?> uriClass = URI.class;
        Class<?> urlClass = URL.class;
        Class<?> classClass = Class.class;
        Class<?> currencyClass = Currency.class;

        // Test for invalid type
        Class<?> invalidClass = Integer[].class;
        assertNull(StdKeyDeserializer.forType(invalidClass));
    }

    @org.junit.Test
    public void testDeserializeKey() throws Exception {
        // Test deserializeKey method with valid keys
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = "test";
        Object deserializedKey = serializer.deserializeKey(key, context());
        assertEquals(key, deserializedKey);

        serializer = StdKeyDeserializer.forType(Boolean.class);
        key = "true";
        deserializedKey = serializer.deserializeKey(key, context());
        assertTrue((Boolean) deserializedKey);

        key = "false";
        deserializedKey = serializer.deserializeKey(key, context());
        assertFalse((Boolean) deserializedKey);

        serializer = StdKeyDeserializer.forType(Byte.class);
        key = "1";
        deserializedKey = serializer.deserializeKey(key, context());
        assertEquals((byte) 1, deserializedKey);

        // Test deserializeKey method with invalid keys
        serializer = StdKeyDeserializer.forType(String.class);
        key = null;
        try {
            serializer.deserializeKey(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Boolean.class);
        key = "invalid";
        try {
            serializer.deserializeKey(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Byte.class);
        key = "123";
        try {
            serializer.deserializeKey(key, context());
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        // Test deserializeKey method with enum keys
        serializer = StdKeyDeserializer.forType(Locale.class);
        key = "en_US";
        Locale locale = (Locale) serializer.deserializeKey(key, context());
        assertEquals(Locale.US, locale);

        serializer = StdKeyDeserializer.forType(Currency.class);
        key = "USD";
        Currency currency = (Currency) serializer.deserializeKey(key, context());
        assertEquals(Currency.getInstance("USD"), currency);

        // Test deserializeKey method with custom deserializers
        serializer = new DelegatingKD(String.class, context().findValueDeserializer(String.class));
        key = "test";
        deserializedKey = serializer.deserializeKey(key, context());
        assertEquals(key, deserializedKey);
    }

    @org.junit.Test
    public void testParse() throws Exception {
        // Test parse method with valid keys
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "true";
        Boolean result = (Boolean) serializer._parse(key, context());
        assertTrue(result);

        key = "false";
        result = (Boolean) serializer._parse(key, context());
        assertFalse(result);

        serializer = StdKeyDeserializer.forType(Byte.class);
        key = "1";
        result = (Byte) serializer._parse(key, context());
        assertEquals((byte) 1, result);

        serializer = StdKeyDeserializer.forType(Short.class);
        key = "123";
        result = (Short) serializer._parse(key, context());
        assertEquals((short) 123, result);

        serializer = StdKeyDeserializer.forType(Character.class);
        key = "A";
        result = (Character) serializer._parse(key, context());
        assertEquals('A', result);

        serializer = StdKeyDeserializer.forType(Integer.class);
        key = "123";
        result = (Integer) serializer._parse(key, context());
        assertEquals(123, result.intValue());

        serializer = StdKeyDeserializer.forType(Long.class);
        key = "12345678901234567890";
        result = (Long) serializer._parse(key, context());
        assertEquals(12345678901234567890L, result.longValue());

        serializer = StdKeyDeserializer.forType(Float.class);
        key = "123.45";
        result = (Float) serializer._parse(key, context());
        assertEquals(123.45f, result, 0.001f);

        serializer = StdKeyDeserializer.forType(Double.class);
        key = "123.45";
        result = (Double) serializer._parse(key, context());
        assertEquals(123.45, result, 0.001);

        serializer = StdKeyDeserializer.forType(Locale.class);
        EnumSet<Locale.LanguageRange> localeLanguages = EnumSet.of(Locale.LanguageRange.parse("en", "US"));
        EnumResolver enumResolver = new EnumResolver(Locale.LanguageRange.class, localeLanguages);
        serializer = new EnumKD(enumResolver, context().findMethod(Locale.class, "valueOf", String.class));
        key = "en_US";
        Locale locale = (Locale) serializer._parse(key, context());
        assertEquals(Locale.US, locale);

        serializer = StdKeyDeserializer.forType(Currency.class);
        EnumResolver enumResolverCurrency = new EnumResolver(Currency.class, EnumSet.allOf(Currency.class));
        serializer = new EnumKD(enumResolverCurrency, context().findMethod(Currency.class, "getInstance", String.class));
        key = "USD";
        Currency currency = (Currency) serializer._parse(key, context());
        assertEquals(Currency.getInstance("USD"), currency);

        serializer = StdKeyDeserializer.forType(URI.class);
        key = "http://example.com";
        URI uri = (URI) serializer._parse(key, context());
        assertEquals(new URI("http://example.com"), uri);

        serializer = StdKeyDeserializer.forType(URL.class);
        key = "http://example.com";
        URL url = (URL) serializer._parse(key, context());
        assertEquals(new URL("http://example.com"), url);

        serializer = StdKeyDeserializer.forType(Class.class);
        key = "java.lang.String";
        Class<?> clazz = (Class<?>) serializer._parse(key, context());
        assertEquals(String.class, clazz);

        // Test parse method with invalid keys
        serializer = StdKeyDeserializer.forType(Boolean.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Byte.class);
        key = "1234";
        try {
            serializer._parse(key, context());
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Short.class);
        key = "12345";
        try {
            serializer._parse(key, context());
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Character.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Integer.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Long.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Float.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Double.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(URI.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(URL.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        serializer = StdKeyDeserializer.forType(Class.class);
        key = "invalid";
        try {
            serializer._parse(key, context());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testParseLong() throws Exception {
        // Test _parseLong method
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        String key = "12345678901234567890";
        long result = serializer._parseLong(key);
        assertEquals(12345678901234567890L, result);

        key = "123456789012345678901234567890";
        result = serializer._parseLong(key);
        assertEquals(123456789012345678901234567890L, result);

        key = "12345e6";
        try {
            serializer._parseLong(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        key = "12345e+6";
        try {
            serializer._parseLong(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        key = "12345e-6";
        try {
            serializer._parseLong(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testParseDouble() throws Exception {
        // Test _parseDouble method
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        String key = "123.45";
        double result = serializer._parseDouble(key);
        assertEquals(123.45, result, 0.001);

        key = "123.456";
        result = serializer._parseDouble(key);
        assertEquals(123.456, result, 0.001);

        key = "123.45E+6";
        try {
            serializer._parseDouble(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        key = "123.45E-6";
        try {
            serializer._parseDouble(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testParseInt() throws Exception {
        // Test _parseInt method
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        String key = "123";
        int result = serializer._parseInt(key);
        assertEquals(123, result);

        key = "12345";
        result = serializer._parseInt(key);
        assertEquals(12345, result);

        key = "123ab";
        try {
            serializer._parseInt(key);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    
}