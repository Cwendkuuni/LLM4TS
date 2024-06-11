package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

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

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(String.class, keyDeserializer.getKeyClass());
        
        raw = UUID.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(UUID.class, keyDeserializer.getKeyClass());
        
        raw = Integer.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(Integer.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testBooleanType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = keyDeserializer.deserializeKey("true", null);
        assertTrue((Boolean) deserializedKey);
        
        deserializedKey = keyDeserializer.deserializeKey("false", null);
        assertFalse((Boolean) deserializedKey);
    }

    @Test
    public void testByteType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = keyDeserializer.deserializeKey("123", null);
        assertEquals((Byte) 123, deserializedKey);
    }

    @Test
    public void testShortType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = keyDeserializer.deserializeKey("12345", null);
        assertEquals((Short) 12345, deserializedKey);
    }

    @Test
    public void testCharType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = keyDeserializer.deserializeKey("A", null);
        assertEquals((Character) 'A', deserializedKey);
    }

    @Test
    public void testIntType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = keyDeserializer.deserializeKey("123456", null);
        assertEquals((Integer) 123456, deserializedKey);
    }

    @Test
    public void testLongType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = keyDeserializer.deserializeKey("1234567890123456", null);
        assertEquals((Long) 1234567890123456L, deserializedKey);
    }

    @Test
    public void testFloatType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object deserializedKey = keyDeserializer.deserializeKey("123.45", null);
        assertEquals((Float) 123.45f, deserializedKey);
    }

    @Test
    public void testDoubleType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object deserializedKey = keyDeserializer.deserializeKey("123.45.67", null);
        assertEquals((Double) 123.4567, deserializedKey);
    }

    @Test
    public void testLocaleType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object deserializedKey = keyDeserializer.deserializeKey("en_US", null);
        assertEquals(Locale.US, deserializedKey);
    }

    @Test
    public void testDateType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object deserializedKey = keyDeserializer.deserializeKey("2023-03-01", null);
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof Date);
    }

    @Test
    public void testCalendarType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object deserializedKey = keyDeserializer.deserializeKey("2023-03-01", null);
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof Calendar);
    }

    @Test
    public void testUriType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object deserializedKey = keyDeserializer.deserializeKey("https://example.com", null);
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof URI);
    }

    @Test
    public void testUrlType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object deserializedKey = keyDeserializer.deserializeKey("https://example.com", null);
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof URL);
    }

    @Test
    public void testClassType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class); // fallback to StringKD
        Object deserializedKey = keyDeserializer.deserializeKey("java.lang.String", null);
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof Class);
        assertEquals(String.class, deserializedKey);

        keyDeserializer = StdKeyDeserializer.forType(Class.class); // custom StdKeyDeserializer for Class
        deserializedKey = keyDeserializer.deserializeKey("java.util.Date", null);
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof Class);
        assertEquals(Date.class, deserializedKey);
    }

    @Test
    public void testCurrencyType() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        Object deserializedKey = keyDeserializer.deserializeKey("EUR", null);
        assertNotNull(deserializedKey);
        assertTrue(deserializedKey instanceof Currency);
        assertEquals(Currency.getInstance("EUR"), deserializedKey);
    }

    @Test
    public void testThrowExceptionOnInvalidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        try {
            keyDeserializer.deserializeKey("invalidKey", null);
            fail("Expected an IllegalArgumentException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}