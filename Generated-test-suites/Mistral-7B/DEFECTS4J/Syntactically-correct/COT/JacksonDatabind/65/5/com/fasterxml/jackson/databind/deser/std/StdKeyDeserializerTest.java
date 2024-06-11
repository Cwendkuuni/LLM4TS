package com.example; // replace with your test package name

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    

    @Test
    public void testForType() throws Exception {
        Class<?> raw = String.class;
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());

        raw = Boolean.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertTrue(keyDeserializer instanceof StdKeyDeserializer);
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, keyDeserializer._kind);

        raw = Integer.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertTrue(keyDeserializer instanceof StdKeyDeserializer);
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializer._kind);

        // test unsupported types
        raw = Double.class;
        keyDeserializer = StdKeyDeserializer.forType(raw);
        assertNull(keyDeserializer);
    }

    @Test
    public void testDeserializeKey() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getFactory().createContext();

        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        String key = "true";
        Object deserializedKey = stdKeyDeserializer.deserializeKey(key, ctxt);
        assertFalse(deserializedKey instanceof Boolean);

        key = "123";
        deserializedKey = stdKeyDeserializer.deserializeKey(key, ctxt);
        assertEquals(123, deserializedKey);

        // test unsupported types
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        key = "3.14";
        try {
            deserializedKey = stdKeyDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testForType_Enum() throws Exception {
        Enum<?> expectedEnumValue = Enum.values()[0];
        Class<Enum> enumClass = expectedEnumValue.getClass();

        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(enumClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.TYPE_ENUM, keyDeserializer._kind);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getFactory().createContext();

        String key = expectedEnumValue.name();
        Object deserializedKey = keyDeserializer.deserializeKey(key, ctxt);
        assertEquals(expectedEnumValue, deserializedKey);

        // test unsupported value
        key = "unsupportedValue";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("123", ctxt);
        assertEquals(123, deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("abc", ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_Boolean() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("true", ctxt);
        assertTrue((Boolean) deserializedKey);

        deserializedKey = stdKeyDeserializer._parse("false", ctxt);
        assertFalse((Boolean) deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("invalid", ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_Byte() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("127", ctxt);
        assertEquals((byte) 127, deserializedKey);

        try {
            deserializedKey = stdKeyDeserializer._parse("-128", ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_Short() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("32767", ctxt);
        assertEquals((short) 32767, deserializedKey);

        try {
            deserializedKey = stdKeyDeserializer._parse("-32768", ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_Char() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("A", ctxt);
        assertEquals('A', deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("\\u0001", ctxt);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_Int() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("123456", ctxt);
        assertEquals(123456, deserializedKey);
    }

    @Test
    public void testParse_Long() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("12345678901234567890", ctxt);
        assertEquals(12345678901234567890L, deserializedKey);
    }

    @Test
    public void testParse_Float() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("3.14", ctxt);
        assertEquals(3.14f, deserializedKey);
    }

    @Test
    public void testParse_Double() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("3.14", ctxt);
        assertEquals(3.14, deserializedKey);
    }

    @Test
    public void testParse_Locale() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("en_US", ctxt);
        assertEquals(Locale.US, deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("en_US_INVALID", ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testParse_Enum() throws Exception {
        Enum<?> expectedEnumValue = Enum.values()[0];
        Class<Enum> enumClass = expectedEnumValue.getClass();

        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, enumClass);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse(expectedEnumValue.name(), ctxt);
        assertEquals(expectedEnumValue, deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("unsupportedValue", ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testParse_URI() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("http://example.com", ctxt);
        assertEquals(new URI("http://example.com"), deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("http://example.com/invalid", ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testParse_URL() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("http://example.com", ctxt);
        assertEquals(new URL("http://example.com"), deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("http://example.com/invalid", ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testParse_Class() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);

        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        Object deserializedKey = stdKeyDeserializer._parse("java.lang.String", ctxt);
        assertEquals(String.class, deserializedKey);

        // test invalid input
        try {
            deserializedKey = stdKeyDeserializer._parse("java.lang.Object", ctxt);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    
}