package com.example.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBooleanDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertTrue((Boolean) result);

        key = "false";
        result = keyDeserializer.deserializeKey(key, createContext());
        assertFalse((Boolean) result);
    }

    @Test
    public void testByteDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        String key = "127";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Byte) 127, result);

        key = "-128";
        result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Byte) -128, result);

        key = "256";
        try {
            result = keyDeserializer.deserializeKey(key, createContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation, problem: overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    @Test
    public void testShortDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        String key = "32767";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Short) 32767, result);

        key = "-32768";
        result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Short) -32768, result);

        key = "65536";
        try {
            result = keyDeserializer.deserializeKey(key, createContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation, problem: overflow, value can not be represented as 16-bit value", e.getMessage());
        }
    }

    @Test
    public void testCharDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        String key = "A";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Character) 'A', result);

        key = "\u00E9";
        Object result2 = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Character) '\u00E9', result2);

        key = "123";
        try {
            result = keyDeserializer.deserializeKey(key, createContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("can only convert 1-character Strings", e.getMessage());
        }
    }

    @Test
    public void testIntDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        String key = "42";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Integer) 42, result);
    }

    @Test
    public void testLongDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        String key = "9223372036854775807";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Long) 9223372036854775807L, result);
    }

    @Test
    public void testFloatDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        String key = "3.14";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Float) 3.14F, result);
    }

    @Test
    public void testDoubleDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        String key = "1.729e-324";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals((Double) 1.729E-324, result);
    }

    @Test
    public void testLocaleDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        String key = "en_US";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(Locale.forLanguageTag("en_US"), result);

        key = "fr_FR@Europe";
        Object result2 = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(Locale.forLanguageTag("fr_FR@Europe"), result2);
    }

    @Test
    public void testUriDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        String key = "http://example.com";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(new URI(key), result);
    }

    @Test
    public void testUrlDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        String key = "http://example.com";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(new URL(key), result);

        key = "ftp://user:pass@example.com/path";
        Object result2 = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(new URL(key), result2);
    }

    @Test
    public void testClassDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        String key = "java.lang.String[]";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(String[].class, result.getClass());
    }

    @Test
    public void testDeserializationWithEnumValues() throws IOException {
        @JsonDeserialize(using = EnumKD.class)
        enum TestEnum {
            VAL1, VAL2, VAL3
        }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);
        String key = "VAL2";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(TestEnum.VAL2, result);

        key = "unknown";
        Object result2 = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(TestEnum.VAL1, result2); // Default value when deserializing unknown enum values as null is not enabled
    }

    @Test
    public void testDeserializationWithUnknownEnumValuesAsNull() throws IOException {
        @JsonDeserialize(using = EnumKD.class)
        enum TestEnum {
            VAL1, VAL2, VAL3
        }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);
        String key = "unknown";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertNull(result);
    }

    @Test
    public void testStringDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(key, result);
    }

    @Test
    public void testObjectDeserializer() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Object.class);
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertEquals(key, result.toString());
    }

    @Test
    public void testDeserializationWithNullKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        String key = null;
        Object result = keyDeserializer.deserializeKey(key, createContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidKey() throws IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        String key = "invalid";
        Object result = keyDeserializer.deserializeKey(key, createContext());
        fail("Expected JsonMappingException");
    }

    private DeserializationContext createContext() {
        return objectMapper.getDeserializationContext();
    }
}