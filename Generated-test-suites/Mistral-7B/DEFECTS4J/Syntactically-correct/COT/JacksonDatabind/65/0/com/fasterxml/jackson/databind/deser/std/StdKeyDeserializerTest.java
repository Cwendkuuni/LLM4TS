package com.example.junit; // replace with your test package

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBooleanKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("true", context);
        assertTrue((Boolean) key);

        key = serializer.deserializeKey("false", context);
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("127", context);
        assertEquals((Byte) 127, key);

        try {
            key = serializer.deserializeKey("-128", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testShortKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("32767", context);
        assertEquals((Short) 32767, key);

        try {
            key = serializer.deserializeKey("-32768", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testCharKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("A", context);
        assertEquals((Character) 'A', key);

        try {
            key = serializer.deserializeKey("\\u0000", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testIntKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("123456", context);
        assertEquals((Integer) 123456, key);
    }

    @Test
    public void testLongKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("12345678901234567890", context);
        assertEquals((Long) 12345678901234567890L, key);
    }

    @Test
    public void testFloatKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("3.14159265358979323846", context);
        assertEquals((Float) 3.14159265358979323846f, key);
    }

    @Test
    public void testDoubleKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("3.14159265358979323846264338327950288416", context);
        assertEquals((Double) 3.14159265358979323846264338327950288416d, key);
    }

    @Test
    public void testLocaleKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, key);

        try {
            key = serializer.deserializeKey("invalid_locale", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testDateKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("2022-12-31", context);
        assertNotNull(key);
        assertTrue(key instanceof Date);

        try {
            key = serializer.deserializeKey("invalid_date_format", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testCalendarKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("2022-12-31", context);
        assertNotNull(key);
        assertTrue(key instanceof Calendar);

        try {
            key = serializer.deserializeKey("invalid_date_format", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testURIKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("http://example.com", context);
        assertEquals(new URI("http://example.com"), key);

        try {
            key = serializer.deserializeKey("invalid_uri", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testURLKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), key);

        try {
            key = serializer.deserializeKey("invalid_url_format", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testClassKeyDeserializer() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mapper.getDeserializationContext();
        Object key = serializer.deserializeKey("java.lang.String", context);
        assertEquals(String.class, key);

        try {
            key = serializer.deserializeKey("com.example.MyClass", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }
}