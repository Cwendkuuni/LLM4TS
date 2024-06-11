package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

@JacksonAnnotations.JacksonTest
public class StdKeyDeserializerTest {

    @JacksonTest.Setup
    public ObjectMapper getObjectMapper() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    @Test
    public void testDeserializeKey_StringAsString() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.StringKD.forType(String.class);
        String key = "test";

        assertEquals(key, keyDeserializer.deserializeKey(key, mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", mapper.getDeserializationContext()));
        assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey("false", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalid", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);

        byte expected = 127; // Maximum value for byte
        assertEquals(expected, keyDeserializer.deserializeKey(Integer.toString(expected), mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("128", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);

        short expected = Short.MIN_VALUE;
        assertEquals(expected, keyDeserializer.deserializeKey(Integer.toString(expected), mapper.getDeserializationContext()));

        short maxValue = Short.MAX_VALUE;
        assertEquals(maxValue, keyDeserializer.deserializeKey(Integer.toString(maxValue), mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("Short.MAX_VALUE + 1", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);

        char expected = 'z';
        assertEquals(expected, keyDeserializer.deserializeKey(String.valueOf(expected), mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalidChar", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        int expected = Integer.MIN_VALUE;
        assertEquals(expected, keyDeserializer.deserializeKey(Integer.toString(expected), mapper.getDeserializationContext()));

        int maxValue = Integer.MAX_VALUE;
        assertEquals(maxValue, keyDeserializer.deserializeKey(Integer.toString(maxValue), mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("Integer.MAX_VALUE + 1", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);

        long expected = Long.MIN_VALUE;
        assertEquals(expected, keyDeserializer.deserializeKey(Long.toString(expected), mapper.getDeserializationContext()));

        long maxValue = Long.MAX_VALUE;
        assertEquals(maxValue, keyDeserializer.deserializeKey(Long.toString(maxValue), mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("Long.MAX_VALUE + 1", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Float() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);

        float expected = Float.NaN;
        assertEquals(expected, keyDeserializer.deserializeKey("NaN", mapper.getDeserializationContext()));

        float positiveValue = 123.45f;
        assertEquals(positiveValue, keyDeserializer.deserializeKey(Float.toString(positiveValue), mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("notANumber", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Double() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);

        double expected = Double.NaN;
        assertEquals(expected, keyDeserializer.deserializeKey("NaN", mapper.getDeserializationContext()));

        double positiveValue = 123.45;
        assertEquals(positiveValue, keyDeserializer.deserializeKey(Double.toString(positiveValue), mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("notANumber", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);

        Locale expected = Locale.US;
        assertEquals(expected, keyDeserializer.deserializeKey("en_US", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalidLocale", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Date() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);

        Date expected = new Date();
        assertEquals(expected, keyDeserializer.deserializeKey("2022-12-31", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalidDateFormat", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Calendar() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);

        Calendar expected = Calendar.getInstance();
        assertEquals(expected, keyDeserializer.deserializeKey("2022-12-31", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalidDateFormat", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);

        URI expected = new URI("http://example.com");
        assertEquals(expected, keyDeserializer.deserializeKey("http://example.com", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalidURI", mapper.getDeserializationContext());
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);

        URL expected = new URL("http://example.com");
        assertEquals(expected, keyDeserializer.deserializeKey("http://example.com", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalidURL", mapper.getDeserializationContext());
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        Enum enumType = MyEnum.class;
        StdKeyDeserializer keyDeserializer = new EnumKD(mapper.getAnnotationIntrospector().findValueAnnotation(enumType), mapper.findMethod(enumType, "valueOf"));

        MyEnum expected = MyEnum.FIRST;
        assertEquals(expected, keyDeserializer.deserializeKey("FIRST", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("INVALID_ENUM", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        Class<?> expectedClass = MyClass.class;
        StdKeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(mapper.getConstructors()[0]);

        assertEquals(expectedClass, keyDeserializer.deserializeKey("MyClass", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("InvalidClass", mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeKey_UUID() throws Exception {
        ObjectMapper mapper = getObjectMapper();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);

        UUID expected = new UUID(1, 2);
        assertEquals(expected, keyDeserializer.deserializeKey("1-2", mapper.getDeserializationContext()));

        try {
            keyDeserializer.deserializeKey("invalidUUID", mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}