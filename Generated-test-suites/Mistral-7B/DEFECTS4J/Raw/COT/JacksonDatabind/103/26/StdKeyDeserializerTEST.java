package com.example.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    

    @Test
    public void testForType() throws Exception {
        Class<?> testClass = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(testClass);
        assertEquals(StdKeyDeserializer.Type.TYPE_STRING, keyDeserializer._kind);
        assertEquals(String.class, keyDeserializer._keyClass);
        assertNull(keyDeserializer._deser);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        String testKey = "true";
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = keyDeserializer.deserializeKey(testKey, new DeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        testKey = "false";
        result = keyDeserializer.deserializeKey(testKey, new DeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        String testKey1 = "123";
        Object result1 = keyDeserializer._parse(testKey1, new DeserializationContext());
        assertEquals(Integer.valueOf(123), result1);

        String testKey2 = "456";
        Object result2 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(Integer.valueOf(456), result2);

        testKey2 = "1234567890";
        Object result3 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(Long.valueOf(1234567890L), result3);

        testKey2 = "-123";
        Object result4 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(Integer.valueOf(-123), result4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInteger() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        String testKey = "2147483648";
        keyDeserializer._parse(testKey, new DeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidByte() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        String testKey = "256";
        keyDeserializer._parse(testKey, new DeserializationContext());
    }

    @Test
    public void testParseEnum() throws Exception {
        Enum<?> testEnumDefaultValue = Enum.valueOf(DayOfWeek.class, "MONDAY");
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(DayOfWeek.class);

        String testKey1 = "MONDAY";
        Object result1 = keyDeserializer._parse(testKey1, new DeserializationContext());
        assertEquals(testEnumDefaultValue, result1);

        String testKey2 = "TUESDAY";
        Object result2 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(Enum.valueOf(DayOfWeek.class, "TUESDAY"), result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnknownEnum() throws Exception {
        Enum<?> testEnumDefaultValue = Enum.valueOf(DayOfWeek.class, "MONDAY");
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(DayOfWeek.class);
        String testKey = "INVALID_DAY";
        keyDeserializer._parse(testKey, new DeserializationContext());
    }

    @Test
    public void testParseCurrency() throws Exception {
        Currency testCurrencyDefaultValue = Currency.getInstance("USD");
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);

        String testKey1 = "USD";
        Object result1 = keyDeserializer._parse(testKey1, new DeserializationContext());
        assertEquals(testCurrencyDefaultValue, result1);

        String testKey2 = "EUR";
        Object result2 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(Currency.getInstance("EUR"), result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnknownCurrency() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        String testKey = "INVALID_CURRENCY";
        keyDeserializer._parse(testKey, new DeserializationContext());
    }

    @Test
    public void testParseUri() throws Exception {
        URI testUriDefaultValue = new URI("http://example.com");
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        String testKey1 = "http://example.com";
        Object result1 = keyDeserializer._parse(testKey1, new DeserializationContext());
        assertEquals(testUriDefaultValue, result1);

        String testKey2 = "https://example.com";
        Object result2 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(new URI("https://example.com"), result2);
    }

    @Test(expected = MalformedURLException.class)
    public void testParseInvalidUri() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        String testKey = "http://example.com/invalid";
        keyDeserializer._parse(testKey, new DeserializationContext());
    }

    @Test
    public void testParseUrl() throws Exception {
        URL testUrlDefaultValue = new URL("http://example.com");
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        String testKey1 = "http://example.com";
        Object result1 = keyDeserializer._parse(testKey1, new DeserializationContext());
        assertEquals(testUrlDefaultValue, result1);

        String testKey2 = "https://example.com";
        Object result2 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(new URL("https://example.com"), result2);
    }

    @Test(expected = MalformedURLException.class)
    public void testParseInvalidUrl() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        String testKey = "http://example.com/invalid";
        keyDeserializer._parse(testKey, new DeserializationContext());
    }

    @Test
    public void testParseClass() throws Exception {
        Class<?> testClassDefaultValue = String[].class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Class.class);

        String testKey1 = "String[]";
        Object result1 = keyDeserializer._parse(testKey1, new DeserializationContext());
        assertEquals(testClassDefaultValue, result1);

        String testKey2 = "List<Integer>";
        Object result2 = keyDeserializer._parse(testKey2, new DeserializationContext());
        assertEquals(List.class, result2);

        String testKey3 = "java.util.Map";
        Object result3 = keyDeserializer._parse(testKey3, new DeserializationContext());
        assertEquals(Map.class, result3);
    }

    @Test(expected = InstantiationException.class)
    public void testParseInvalidClass() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Class.class);
        String testKey = "java.lang.String";
        keyDeserializer._parse(testKey, new DeserializationContext());
    }

    
}