package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Test;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    // Helper method to create a mock DeserializationContext
    private DeserializationContext createMockContext() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }

    // Helper method to create a mock EnumResolver
    private EnumResolver createMockEnumResolver() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getAnnotationIntrospector().findEnumValueResolver(Map.class, null);
    }

    // Helper method to create a mock FromStringDeserializer
    private FromStringDeserializer mockFromStringDeserializer() {
        FromStringDeserializer mockDeserializer = mock(FromStringDeserializer.class);
        when(mockDeserializer._deserialize(anyString(), any(DeserializationContext.class)))
                .thenReturn("MockedFromStringDeserializerResult");
        return mockDeserializer;
    }

    @Test
    public void testForTypeBoolean() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, key);

        key = keyDeserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, key);

        key = keyDeserializer.deserializeKey("not_a_boolean", context);
        assertNull(key);
    }

    @Test
    public void testForTypeByte() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("127", context);
        assertEquals((byte) 127, key);

        try {
            keyDeserializer.deserializeKey("-128", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }

        try {
            keyDeserializer.deserializeKey("256", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeShort() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("32767", context);
        assertEquals((short) 32767, key);

        try {
            keyDeserializer.deserializeKey("-32768", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }

        try {
            keyDeserializer.deserializeKey("65536", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeInt() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("2147483647", context);
        assertEquals(Integer.MAX_VALUE, key);

        try {
            keyDeserializer.deserializeKey("-2147483648", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }

        try {
            keyDeserializer.deserializeKey("4294967297", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeLong() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("9223372036854775807", context);
        assertEquals(Long.MAX_VALUE, key);

        try {
            keyDeserializer.deserializeKey("-9223372036854775808", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }

        try {
            keyDeserializer.deserializeKey("18446744073709551616", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeFloat() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("3.14159265358979323846", context);
        assertEquals(3.14159265358979323846f, key, 0.000001f);

        try {
            keyDeserializer.deserializeKey("NaN", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }

        try {
            keyDeserializer.deserializeKey("Infinity", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeDouble() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("6.28318530718", context);
        assertEquals(6.28318530718, key, 0.0000001);

        try {
            keyDeserializer.deserializeKey("NaN", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }

        try {
            keyDeserializer.deserializeKey("Infinity", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeLocale() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext context = createMockContext();
        FromStringDeserializer fromStringDeserializer = mockFromStringDeserializer();
        keyDeserializer =
                new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, fromStringDeserializer);

        Object key = keyDeserializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, key);

        key = keyDeserializer.deserializeKey("es_ES", context);
        assertEquals(Locale.SPAIN, key);

        try {
            keyDeserializer.deserializeKey("invalid_locale", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeURISpec() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("http://www.example.com", context);
        assertEquals(new URI("http://www.example.com"), key);

        try {
            keyDeserializer.deserializeKey("invalid_uri", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeURL() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("http://www.example.com", context);
        assertEquals(new URL("http://www.example.com"), key);

        try {
            keyDeserializer.deserializeKey("invalid_url", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test
    public void testForTypeEnum() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(DayOfWeek.class);

        DeserializationContext context = createMockContext();
        EnumResolver enumResolver = createMockEnumResolver();

        Object key = keyDeserializer.deserializeKey("MONDAY", context);
        assertEquals(DayOfWeek.MONDAY, key);

        try {
            keyDeserializer.deserializeKey("INVALID_DAY_OF_WEEK", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }

        context = createMockContext();
        context.disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, DayOfWeek.class);
        key = keyDeserializer.deserializeKey("INVALID_DAY_OF_WEEK", context);
        assertNull(key);
    }

    @Test
    public void testForTypeClass() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);

        DeserializationContext context = createMockContext();
        Object key = keyDeserializer.deserializeKey("[Ljava.lang.String;", context);
        assertEquals(new String[]{"string1", "string2"}, key);

        try {
            keyDeserializer.deserializeKey("invalid_class_representation", context);
            assert false; // expected exception
        } catch (IOException e) {
            // expected exception
        }
    }
}