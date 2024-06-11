package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.*;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key1 = "true";
        String key2 = "false";
        Object result1 = keyDeserializer.deserializeKey(key1, context);
        Object result2 = keyDeserializer.deserializeKey(key2, context);

        assertTrue(result1 instanceof Boolean);
        assertEquals(Boolean.TRUE, result1);

        assertTrue(result2 instanceof Boolean);
        assertEquals(Boolean.FALSE, result2);
    }

    @Test
    public void testByteType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "123";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof Byte);
        assertEquals((byte) 123, result);
    }

    @Test
    public void testShortType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "321";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof Short);
        assertEquals((short) 321, result);
    }

    @Test
    public void testCharType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "A";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof Character);
        assertEquals('A', result);
    }

    @Test
    public void testIntType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "456";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof Integer);
        assertEquals(456, result);
    }

    @Test
    public void testLongType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "789";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof Long);
        assertEquals(789L, result);
    }

    @Test
    public void testFloatType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "123.45";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof Float);
        assertEquals(123.45f, result);
    }

    @Test
    public void testDoubleType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "789.12";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof Double);
        assertEquals(789.12, result);
    }

    @JsonDeserialize(using = StringKD.class)
    static class TestEnum {
        public String name;
    }

    @Test
    public void testEnumType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "TestEnum_ONE";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof TestEnum);
        TestEnum testEnum = (TestEnum) result;
        assertEquals("TestEnum_ONE", testEnum.name);
    }

    @Test
    public void testUriType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "http://example.com";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof URI);
        URI uri = (URI) result;
        assertEquals("http://example.com", uri.toString());
    }

    @Test
    public void testUrlType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "http://example.com";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof URL);
        URL url = (URL) result;
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testClassType() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Class<?> testClass = String[].class;
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(testClass);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "[Ljava.lang.String;";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof String[]);
        assertEquals(0, ((String[]) result).length);
    }

    @Test
    public void testUriWithSlashAtBeginning() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "/example.com";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof URI);
        URI uri = (URI) result;
        assertEquals("example.com", uri.getHost());
    }

    @Test
    public void testUrlWithSlashAtBeginning() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = "/example.com";
        Object result = keyDeserializer.deserializeKey(key, context);

        assertTrue(result instanceof URL);
        URL url = (URL) result;
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testNullKey() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = mock(DeserializationContext.class);

        String key = null;
        Object result = keyDeserializer.deserializeKey(key, context);

        assertNull(result);
    }
}