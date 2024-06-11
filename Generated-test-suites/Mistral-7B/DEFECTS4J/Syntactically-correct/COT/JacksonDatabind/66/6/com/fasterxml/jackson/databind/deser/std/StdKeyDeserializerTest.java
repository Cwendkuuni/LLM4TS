package com.example.test; // replace with the actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {
    private ObjectMapper objectMapper;
    private DeserializationContext context;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        context = objectMapper.getDeserializationContext();
    }

    @Test
    public void testBooleanType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = serializer.deserializeKey("true", context);
        assertEquals(true, result);

        result = serializer.deserializeKey("false", context);
        assertEquals(false, result);
    }

    @Test
    public void testBooleanType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testByteType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object result = serializer.deserializeKey("127", context);
        assertEquals((byte) 127, result);

        result = serializer.deserializeKey("-128", context);
        assertEquals((byte) -128, result);
    }

    @Test
    public void testByteType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("256", context));
        assertThrows(IOException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testShortType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object result = serializer.deserializeKey("32767", context);
        assertEquals((short) 32767, result);

        result = serializer.deserializeKey("-32768", context);
        assertEquals((short) -32768, result);
    }

    @Test
    public void testShortType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("32768", context));
        assertThrows(IOException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testCharType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object result = serializer.deserializeKey("A", context);
        assertEquals('A', result);
    }

    @Test
    public void testCharType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testIntType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object result = serializer.deserializeKey("2147483647", context);
        assertEquals(2147483647, result);
    }

    @Test
    public void testIntType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("-2147483648", context));
        assertThrows(IOException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testLongType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object result = serializer.deserializeKey("9223372036854775807", context);
        assertEquals(9223372036854775807L, result);

        result = serializer.deserializeKey("-9223372036854775808", context);
        assertEquals(-9223372036854775808L, result);
    }

    @Test
    public void testLongType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("9223372036854775808", context));
        assertThrows(IOException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testFloatType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object result = serializer.deserializeKey("3.14159265359", context);
        assertEquals(3.14159265359f, result);
    }

    @Test
    public void testFloatType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testDoubleType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object result = serializer.deserializeKey("3.141592653589793238462643383279502884168724", context);
        assertEquals(3.141592653589793238462643383279502884168724, result);
    }

    @Test
    public void testDoubleType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testLocaleType_ValidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object result = serializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, result);

        result = serializer.deserializeKey("fr_FR", context);
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testLocaleType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        assertThrows(IOException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testDateType_ValidCases() throws IOException, ParseException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object result = serializer.deserializeKey("2021-10-15", context);
        assertEquals(new Date(1634371200000L), result);
    }

    @Test
    public void testDateType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testCalendarType_ValidCases() throws IOException, ParseException {
        Calendar calendar = Calendar.getInstance();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = serializer.deserializeKey("2021-10-15", context);
        assertEquals(calendar.getInstance("2021-10-15"), result);
    }

    @Test
    public void testCalendarType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testURIType_ValidCases() throws IOException, URISyntaxException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object result = serializer.deserializeKey("https://www.example.com", context);
        URI expected = new URI("https", "www.example.com", null);
        assertEquals(expected, result);
    }

    @Test
    public void testURIType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testURLType_ValidCases() throws IOException, MalformedURLException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object result = serializer.deserializeKey("https://www.example.com", context);
        URL expected = new URL("https", "www.example.com", 443);
        assertEquals(expected, result);
    }

    @Test
    public void testURLType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_string", context));
    }

    @Test
    public void testClassType_ValidCases() throws IOException, ClassNotFoundException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object result = serializer.deserializeKey("java.lang.String[]", context);
        assertEquals(String[].class, result.getClass());

        result = serializer.deserializeKey("java.util.ArrayList", context);
        assertEquals(ArrayList.class, result.getClass());
    }

    @Test
    public void testClassType_InvalidCases() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        assertThrows(JsonProcessingException.class, () -> serializer.deserializeKey("invalid_class_name", context));
    }
}