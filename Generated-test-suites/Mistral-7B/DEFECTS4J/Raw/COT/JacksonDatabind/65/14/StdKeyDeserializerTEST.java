package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Test class for StdKeyDeserializer
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    

    // Basic tests for each method

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "true";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        String key = "123";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Byte);
        assertEquals((byte) 123, result);
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        String key = "12345";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Short);
        assertEquals((short) 12345, result);
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        String key = "A";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Character);
        assertEquals('A', result);
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        String key = "123456";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Integer);
        assertEquals(123456, result);
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        String key = "123456789012345";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Long);
        assertEquals(123456789012345L, result);
    }

    @Test
    public void testDeserializeKey_Float() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        String key = "123.45";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Float);
        assertEquals((float) 123.45, result);
    }

    @Test
    public void testDeserializeKey_Double() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        String key = "123.45";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Double);
        assertEquals(123.45, result);
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        String key = "en_US";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Locale);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeKey_Date() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        String key = "2022-12-31";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Date);
        assertEquals(new Date("Fri Dec 31 00:00:00 CET 2022"), result);
    }

    @Test
    public void testDeserializeKey_Calendar() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        String key = "2022-12-31";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Calendar);
        GregorianCalendar gregorianCalendar = (GregorianCalendar) result;
        assertEquals(2022, gregorianCalendar.get(Calendar.YEAR));
        assertEquals(11, gregorianCalendar.get(Calendar.MONTH));
        assertEquals(31, gregorianCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testDeserializeKey_UUID() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof UUID);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), result);
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        String key = "http://example.com";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof URI);
        assertEquals(new URI("http://example.com"), result);
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        String key = "http://example.com";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        String key = "com.example.MyClass";
        Object result = serializer.deserializeKey(key, new TestContext());
        assertTrue(result instanceof Class<?>);
        assertEquals(Class.forName("com.example.MyClass"), result);
    }

    // Edge case and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidBoolean() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_OutOfRangeByte() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        String key = "256";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_OutOfRangeShort() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        String key = "32768";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializeKey_InvalidChar() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_OutOfRangeInt() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        String key = "2147483648";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_OutOfRangeLong() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        String key = "9223372036854775808";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidFloat() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidDouble() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidLocale() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidDate() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidCalendar() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidUUID() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidURI() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeKey_InvalidURL() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        String key = "invalid";
        serializer.deserializeKey(key, new TestContext());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeKey_InvalidClass() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        String key = "com.example.MyClassThatDoesntExist";
        serializer.deserializeKey(key, new TestContext());
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonFactory _getFactory() {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonParser _createParser(String text) throws IOException, JsonProcessingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonParser _createParser(File file) throws IOException, JsonProcessingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonParser _createParser(InputStream ios) throws IOException, JsonProcessingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonParser _createParser(byte[] bytes) throws IOException, JsonProcessingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void _reportError(String msg) throws IOException {
            fail("Unexpected error: " + msg);
        }

        @Override
        public void _reportError(String msg, Throwable t) throws IOException {
            fail("Unexpected error: " + msg + ", cause: " + t.getMessage());
        }

        @Override
        public void _close() throws IOException {
        }

        @Override
        public ObjectMapper _getConfig() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BaseAccessorFactory _getAccessorFactory() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BeanDescription _getRootValueDescription() {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType _getTypeFactory() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AnnotationIntrospector _getAnnotationIntrospector() {
            throw new UnsupportedOperationException();
        }
    }

    
}