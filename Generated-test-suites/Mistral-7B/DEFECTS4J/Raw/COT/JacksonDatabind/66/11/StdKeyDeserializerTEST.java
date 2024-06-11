package com.example.test; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdKeyDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private DeserializationContext context;
    private DeserializerProvider provider;

    @Before
    public void setup() throws Exception {
        context = MAPPER.getDeserializationContext();
        provider = context.getDeserializerProvider();
    }

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", context));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", context));
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 1, deserializer.deserializeKey("1", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("123", context));
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 1, deserializer.deserializeKey("1", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("12345", context));
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('a', deserializer.deserializeKey("a", context));
        assertNull(deserializer.deserializeKey("ab", context));
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(1, deserializer.deserializeKey("1", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("12345", context));
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(1L, deserializer.deserializeKey("1", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("1234567890", context));
    }

    @Test
    public void testDeserializeKey_Float() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals((float) 1.0, deserializer.deserializeKey("1.0", context), 0.01f);
        assertThrows(IOException.class, () -> deserializer.deserializeKey("1234.56", context));
    }

    @Test
    public void testDeserializeKey_Double() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(1.0, deserializer.deserializeKey("1.0", context), 0.001);
        assertThrows(IOException.class, () -> deserializer.deserializeKey("1234.567890", context));
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en_US", context));
        assertNull(deserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Date() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(java.util.Date.class);
        assertEquals(new java.util.Date(), deserializer.deserializeKey("", context));
        assertEquals(new java.util.Date(0), deserializer.deserializeKey("0", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Calendar() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(java.util.Calendar.class);
        assertNull(deserializer.deserializeKey("", context));
        assertEquals(java.util.Calendar.getInstance(), deserializer.deserializeKey("", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_UUID() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        assertEquals(UUID.randomUUID(), deserializer.deserializeKey("", context));
        assertEquals(UUID.fromString("123e4567-78fe-49b3-a27a-1ab345678901"), deserializer.deserializeKey("123e4567-78fe-49b3-a27a-1ab345678901", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        assertEquals(new URI(""), deserializer.deserializeKey("", context));
        assertEquals(new URI("http://example.com"), deserializer.deserializeKey("http://example.com", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        assertEquals(new URL(""), deserializer.deserializeKey("", context));
        assertEquals(new URL("http://example.com"), deserializer.deserializeKey("http://example.com", context));
        assertThrows(MalformedURLException.class, () -> deserializer.deserializeKey("invalid", context));
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, deserializer.deserializeKey("java.lang.String", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("java.lang.Integer", context));
    }

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        EnumSet<Locale> locales = EnumSet.allOf(Locale.class);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        for (Locale locale : locales) {
            assertEquals(locale, deserializer.deserializeKey(locale.toString(), context));
        }

        assertThrows(IOException.class, () -> deserializer.deserializeKey("INVALID", context));
    }

    @Test
    public void testDeserializeKey_Exception() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        assertNull(deserializer.deserializeKey("", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("null", context));
    }

    @Test
    public void testDeserializeKey_UnknownType() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        assertNull(deserializer.deserializeKey("", context));
    }

    @Test
    public void testHandleWeirdKey() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        String message = "not a valid representation, problem: (%s) %s";

        assertNull(deserializer.deserializeKey("", context));
        assertThrows(IOException.class, () -> {
            String errorMessage = deserializer.handleWeirdKey(Object.class, "", message, new RuntimeException("Test error"));
            assertEquals("not a valid representation, problem: (RuntimeException) Test error", errorMessage);
        });

        assertThrows(IOException.class, () -> {
            String errorMessage = deserializer.handleWeirdKey(Object.class, "", message, new IOException("Test exception"));
            assertEquals("not a valid representation, problem: (IOException) Test exception", errorMessage);
        });

        assertThrows(JsonProcessingException.class, () -> {
            String errorMessage = deserializer.handleWeirdKey(Object.class, "", message, new JsonProcessingException("Test json processing exception"));
            assertEquals("not a valid representation, problem: (com.fasterxml.jackson.core.JsonProcessingException) Test json processing exception", errorMessage);
        });
    }

    @Test
    public void testFindClass() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);
        Class<?> clazz = Class.forName("java.util.Date");

        assertEquals(clazz, deserializer.deserializeKey(clazz.getName(), context));
        assertNull(deserializer.deserializeKey("java.lang.String", context));
        assertThrows(IOException.class, () -> deserializer.deserializeKey("java.lang.Integer", context));
    }

    @Test
    public void testDeserializeKey_EnumUsingToString() throws Exception {
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        AnnotatedClass annotatedClass = introspector.findClass(Locale.class);
        AnnotatedMethod method = annotatedClass.findMethod("value");
        StringFactoryKeyDeserializer deserializer = new StringFactoryKeyDeserializer(method);

        assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en_US", context));
        assertEquals(Locale.FRENCH, deserializer.deserializeKey("fr_FR", context));
        assertEquals(Locale.CHINESE, deserializer.deserializeKey("zh_CN", context));
        assertNull(deserializer.deserializeKey("invalid", context));
    }
}