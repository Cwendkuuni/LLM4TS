package com.example.test; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class StdKeyDeserializerTest extends TestBase {

    // Test method for deserializeKey() for boolean type
    @Test
    public void testDeserializeKeyForBoolean() throws IOException, JsonProcessingException {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "true";
        Object result = booleanKD.deserializeKey(key.toString(), context);
        assertEquals(Boolean.TRUE, result);
        key = "false";
        result = booleanKD.deserializeKey(key.toString(), context);
        assertEquals(Boolean.FALSE, result);
    }

    // Test method for deserializeKey() for byte type
    @Test
    public void testDeserializeKeyForByte() throws IOException, JsonProcessingException {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "127";
        Object result = byteKD.deserializeKey(key.toString(), context);
        assertEquals((byte) 127, result);

        key = "128";
        try {
            result = byteKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to byte overflow.");
        } catch (IOException e) {
            // Pass
        }

        key = "-128";
        try {
            result = byteKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to byte underflow.");
        } catch (IOException e) {
            // Pass
        }
    }

    // Test method for deserializeKey() for short type
    @Test
    public void testDeserializeKeyForShort() throws IOException, JsonProcessingException {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "32767";
        Object result = shortKD.deserializeKey(key.toString(), context);
        assertEquals((short) 32767, result);

        key = "32768";
        try {
            result = shortKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to short overflow.");
        } catch (IOException e) {
            // Pass
        }

        key = "-32768";
        try {
            result = shortKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to short underflow.");
        } catch (IOException e) {
            // Pass
        }
    }

    // Test method for deserializeKey() for char type
    @Test
    public void testDeserializeKeyForChar() throws IOException, JsonProcessingException {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "a";
        Object result = charKD.deserializeKey(key.toString(), context);
        assertEquals(Character.valueOf('a'), result);

        key = "ab";
        try {
            result = charKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to char limit.");
        } catch (IOException e) {
            // Pass
        }
    }

    // Test method for deserializeKey() for int type
    @Test
    public void testDeserializeKeyForInt() throws IOException, JsonProcessingException {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "2147483647";
        Object result = intKD.deserializeKey(key.toString(), context);
        assertEquals(Integer.MAX_VALUE, result);

        key = "2147483648";
        try {
            result = intKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to int overflow.");
        } catch (IOException e) {
            // Pass
        }

        key = "-2147483648";
        try {
            result = intKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to int underflow.");
        } catch (IOException e) {
            // Pass
        }
    }

    // Test method for deserializeKey() for long type
    @Test
    public void testDeserializeKeyForLong() throws IOException, JsonProcessingException {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "9223372036854775807";
        Object result = longKD.deserializeKey(key.toString(), context);
        assertEquals(Long.MAX_VALUE, result);

        key = "9223372036854775808";
        try {
            result = longKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to long overflow.");
        } catch (IOException e) {
            // Pass
        }

        key = "-9223372036854775808";
        try {
            result = longKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to long underflow.");
        } catch (IOException e) {
            // Pass
        }
    }

    // Test method for deserializeKey() for float type
    @Test
    public void testDeserializeKeyForFloat() throws IOException, JsonProcessingException {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "3.4e38";
        Object result = floatKD.deserializeKey(key.toString(), context);
        assertEquals(Float.POSITIVE_INFINITY, result);

        key = "-3.4e38";
        try {
            result = floatKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to float underflow.");
        } catch (IOException e) {
            // Pass
        }
    }

    // Test method for deserializeKey() for double type
    @Test
    public void testDeserializeKeyForDouble() throws IOException, JsonProcessingException {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "1.8e308";
        Object result = doubleKD.deserializeKey(key.toString(), context);
        assertEquals(Double.POSITIVE_INFINITY, result);

        key = "-1.8e308";
        try {
            result = doubleKD.deserializeKey(key.toString(), context);
            fail("Expected IOException due to double underflow.");
        } catch (IOException e) {
            // Pass
        }
    }

    // Test method for deserializeKey() for Locale type
    @Test
    public void testDeserializeKeyForLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "en_US";

        Object result = localeKD.deserializeKey(key.toString(), context);
        assertEquals(Locale.US, result);

        key = "fr_FR";
        result = localeKD.deserializeKey(key.toString(), context);
        assertEquals(Locale.FRANCE, result);

        key = "es_ES@variant=classic";
        result = localeKD.deserializeKey(key.toString(), context);
        assertEquals(Locale.class, result.getClass());
        assertEquals("es_ES@variant=classic", result.toString());
    }

    // Test method for deserializeKey() for UUID type
    @Test
    public void testDeserializeKeyForUUID() throws IOException, JsonProcessingException {
        StdKeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "00000000-0123-4567-89AB-123456789012";
        Object result = uuidKD.deserializeKey(key.toString(), context);
        assertEquals(UUID.fromString(key.toString()), result);

        key = "01234567-89AB-1234-5678-123456789012";
        result = uuidKD.deserializeKey(key.toString(), context);
        assertEquals(UUID.fromString(key.toString()), result);
    }

    // Test method for deserializeKey() for URI type
    @Test
    public void testDeserializeKeyForURI() throws IOException, JsonProcessingException {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "http://example.com";
        Object result = uriKD.deserializeKey(key.toString(), context);
        assertEquals(new URI(key.toString()), result);

        key = "https://www.example.org/path/to/resource";
        result = uriKD.deserializeKey(key.toString(), context);
        assertEquals(new URI(key.toString()), result);
    }

    // Test method for deserializeKey() for URL type
    @Test
    public void testDeserializeKeyForURL() throws IOException, JsonProcessingException {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "http://example.com";
        Object result = urlKD.deserializeKey(key.toString(), context);
        assertEquals(new URL(key.toString()), result);

        key = "https://www.example.org/path/to/resource";
        result = urlKD.deserializeKey(key.toString(), context);
        assertEquals(new URL(key.toString()), result);
    }

    // Test method for deserializeKey() for Class type
    @Test
    public void testDeserializeKeyForClass() throws IOException, JsonProcessingException {
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object key = "com.example.MyClass";
        Object result = classKD.deserializeKey(key.toString(), context);
        assertEquals(Class.forName((String) key), result);

        try {
            // Test for invalid class name
            key = "java.lang.String#parseBoolean";
            result = classKD.deserializeKey(key.toString(), context);
            fail("Expected IllegalArgumentException due to invalid class name.");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}