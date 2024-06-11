package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?> raw = String.class;
        StdKeyDeserializer actual = StdKeyDeserializer.forType(raw);
        StdKeyDeserializer expected = StringKD.forType(raw);
        assertEquals(expected, actual);

        raw = Boolean.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, raw);
        assertEquals(expected, actual);

        raw = Byte.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, raw);
        assertEquals(expected, actual);

        raw = Short.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, raw);
        assertEquals(expected, actual);

        raw = Character.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, raw);
        assertEquals(expected, actual);

        raw = Integer.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, raw);
        assertEquals(expected, actual);

        raw = Long.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, raw);
        assertEquals(expected, actual);

        raw = Float.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, raw);
        assertEquals(expected, actual);

        raw = Double.class;
        actual = StdKeyDeserializer.forType(raw);
        expected = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, raw);
        assertEquals(expected, actual);

        raw = Locale.class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StdKeyDeserializer);
        assertFalse(actual instanceof StringKD);

        raw = Currency.class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StdKeyDeserializer);
        assertFalse(actual instanceof StringKD);

        raw = UUID.class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StdKeyDeserializer);
        assertFalse(actual instanceof StringKD);

        raw = URI.class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StdKeyDeserializer);
        assertFalse(actual instanceof StringKD);

        raw = URL.class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StdKeyDeserializer);
        assertFalse(actual instanceof StringKD);

        raw = Class.class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StdKeyDeserializer);
        assertFalse(actual instanceof StringKD);

        raw = Object[].class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StringKD);
        assertEquals(StringKD.class, actual.getClass());

        raw = Object.class;
        actual = StdKeyDeserializer.forType(raw);
        assertNotNull(actual);
        assertTrue(actual instanceof StringKD);
        assertEquals(StringKD.class, actual.getClass());
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);

        key = "123";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Byte);
        assertEquals((byte) 123, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);

        key = "12345";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Short);
        assertEquals((short) 12345, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);

        key = "A";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Character);
        assertEquals('A', result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        key = "123456";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Integer);
        assertEquals(123456, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);

        key = "1234567890123456";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Long);
        assertEquals(1234567890123456L, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);

        key = "123.45";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Float);
        assertEquals(123.45f, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);

        key = "123.45";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Double);
        assertEquals(123.45, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);

        key = "http://example.com";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof URI);
        assertEquals(new URI("http://example.com"), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);

        key = "http://example.com";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com"), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);

        key = "java.lang.String";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Class);
        assertEquals(String.class, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);

        key = "en_US";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Locale);
        assertEquals(Locale.US, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);

        key = "2023-03-11";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Date);
        assertEquals(new Date(2023, 2, 11), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);

        key = "2023-03-11";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof Calendar);
        assertEquals(Calendar.getInstance(Locale.US).withTimeAtStartOfDay(new Date(2023, 2, 11).getTime()), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);

        key = "dddddddd-dddd-dddd-dddd-dddddddddddd";
        result = keyDeserializer.deserializeKey(key, new TestDeserializationContext());
        assertTrue(result instanceof UUID);
        UUID expectedUuid = new UUID(1L, 2L);
        UUID actualUuid = (UUID) result;
        assertEquals(expectedUuid.getMostSignificantBits(), actualUuid.getMostSignificantBits());
        assertEquals(expectedUuid.getLeastSignificantBits(), actualUuid.getLeastSignificantBits());

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Enum.class);

        AnnotatedClass enumClass = new TestEnumClass();
        AnnotatedMethod enumValueMethod = enumClass.findMethod("valueOf", String.class);
        StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(enumValueMethod, enumClass);

        String enumKey = "TEST_ENUM_VALUE";
        result = enumKeyDeserializer.deserializeKey(enumKey, new TestDeserializationContext());
        assertTrue(result instanceof Enum);
        assertEquals(TestEnum.TEST_ENUM_VALUE, result);
    }
}

class TestDeserializationContext extends TestBaseContext {
    @Override
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}

class TestEnumClass {
    public enum TestEnum {
        TEST_ENUM_VALUE
    }
}