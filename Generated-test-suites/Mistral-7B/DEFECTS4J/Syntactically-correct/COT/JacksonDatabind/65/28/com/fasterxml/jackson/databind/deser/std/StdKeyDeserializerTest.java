package com.example.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void testDeserializeKey_StringKD() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.StringKD.forType(String.class);
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper));
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKey_ObjectKD() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.StringKD.forType(Object.class);
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper));
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String keyTrue = "true";
        Object resultTrue = keyDeserializer.deserializeKey(keyTrue, new DeserializationContext(objectMapper));
        assertTrue((Boolean) resultTrue);

        String keyFalse = "false";
        Object resultFalse = keyDeserializer.deserializeKey(keyFalse, new DeserializationContext(objectMapper));
        assertFalse((Boolean) resultFalse);
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        String key1 = "10";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals((byte) 10, result1);

        String keyNegative = "-10";
        Object resultNegative = keyDeserializer.deserializeKey(keyNegative, new DeserializationContext(objectMapper));
        assertEquals((byte) -10, resultNegative);
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        String key1 = "10";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals((short) 10, result1);

        String keyMinValue = Short.MIN_VALUE + "";
        Object resultMinValue = keyDeserializer.deserializeKey(keyMinValue, new DeserializationContext(objectMapper));
        assertEquals(Short.MIN_VALUE, resultMinValue);

        String keyMaxValue = Short.MAX_VALUE + "";
        Object resultMaxValue = keyDeserializer.deserializeKey(keyMaxValue, new DeserializationContext(objectMapper));
        assertEquals(Short.MAX_VALUE, resultMaxValue);
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        String key1 = "A";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals('A', result1);
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key1 = "10";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(10, result1);
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key1 = "10";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(10L, result1);

        String keyMinValue = Long.MIN_VALUE + "";
        Object resultMinValue = keyDeserializer.deserializeKey(keyMinValue, new DeserializationContext(objectMapper));
        assertEquals(Long.MIN_VALUE, resultMinValue);

        String keyMaxValue = Long.MAX_VALUE + "";
        Object resultMaxValue = keyDeserializer.deserializeKey(keyMaxValue, new DeserializationContext(objectMapper));
        assertEquals(Long.MAX_VALUE, resultMaxValue);
    }

    @Test
    public void testDeserializeKey_Float() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        String key1 = "10.0";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(Float.valueOf(10.0f), result1);
    }

    @Test
    public void testDeserializeKey_Double() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        String key1 = "10.0";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(Double.valueOf(10.0), result1);
    }

    @Test
    public void testDeserializeKey_Locale() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        String key1 = "en_US";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(Locale.US, result1);

        String keyInvalid = "invalid_locale";
        Object resultInvalid = keyDeserializer.deserializeKey(keyInvalid, new DeserializationContext(objectMapper));
        assertNull(resultInvalid);
    }

    @Test
    public void testDeserializeKey_Date() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        String key1 = "2022-03-01";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(new Date(1646304000000L), result1);
    }

    @Test
    public void testDeserializeKey_Calendar() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        String key1 = "2022-03-01";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(Calendar.getInstance(), result1);

        String keyNull = null;
        Object resultNull = keyDeserializer.deserializeKey(keyNull, new DeserializationContext(objectMapper));
        assertNull(resultNull);
    }

    @Test
    public void testDeserializeKey_UUID() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        String key1 = "00000000-0000-0000-0000-000000000001";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), result1);

        String keyInvalid = "invalid_uuid";
        Object resultInvalid = keyDeserializer.deserializeKey(keyInvalid, new DeserializationContext(objectMapper));
        assertNull(resultInvalid);
    }

    @Test
    public void testDeserializeKey_URI() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        String key1 = "https://example.com";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(new URI(key1), result1);

        String keyNull = null;
        Object resultNull = keyDeserializer.deserializeKey(keyNull, new DeserializationContext(objectMapper));
        assertNull(resultNull);

        String keyInvalid = "invalid_uri_format";
        Object resultInvalid = keyDeserializer.deserializeKey(keyInvalid, new DeserializationContext(objectMapper));
        assertNull(resultInvalid);
    }

    @Test
    public void testDeserializeKey_URL() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        String key1 = "https://example.com";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(new URL(key1), result1);

        String keyNull = null;
        Object resultNull = keyDeserializer.deserializeKey(keyNull, new DeserializationContext(objectMapper));
        assertNull(resultNull);

        String keyInvalid = "invalid_url_format";
        Object resultInvalid = keyDeserializer.deserializeKey(keyInvalid, new DeserializationContext(objectMapper));
        assertNull(resultInvalid);
    }

    @Test
    public void testDeserializeKey_Enum() throws Exception {
        Class<MyEnum> enumClass = MyEnum.class;
        KeyDeserializer keyDeserializer = new EnumKD(new EnumResolver(enumClass), new AnnotatedMethod(null, "values", new Class[]{EnumSet.class}));
        String keyInvalid = "InvalidValue";
        Object resultInvalid = keyDeserializer.deserializeKey(keyInvalid, new DeserializationContext(objectMapper));
        assertNull(resultInvalid);

        String keyValid1 = MyEnum.VALUE1.name();
        Object resultValid1 = keyDeserializer.deserializeKey(keyValid1, new DeserializationContext(objectMapper));
        assertEquals(MyEnum.VALUE1, resultValid1);

        String keyValid2 = MyEnum.VALUE2.name();
        Object resultValid2 = keyDeserializer.deserializeKey(keyValid2, new DeserializationContext(objectMapper));
        assertEquals(MyEnum.VALUE2, resultValid2);
    }

    @Test
    public void testDeserializeKey_Class() throws Exception {
        KeyDeserializer keyDeserializer = new DelegatingKD(String.class, objectMapper.findValueDeserializer(String.class));
        String keyClassName = String.class.getName();
        Object resultClassName = keyDeserializer.deserializeKey(keyClassName, new DeserializationContext(objectMapper));
        assertEquals(String.class, resultClassName);

        String keyInvalidClassName = "InvalidClass";
        Object resultInvalidClassName = keyDeserializer.deserializeKey(keyInvalidClassName, new DeserializationContext(objectMapper));
        assertNull(resultInvalidClassName);
    }

    @Test
    public void testDeserializeKey_StringCtorKeyDeserializer() throws Exception {
        Class<MyClass> clazz = MyClass.class;
        Constructor<MyClass> constructor = clazz.getConstructor(String.class);
        KeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(constructor);
        String key1 = "Test";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(new MyClass("Test"), result1);
    }

    @Test
    public void testDeserializeKey_StringFactoryKeyDeserializer() throws Exception {
        Class<MyClass> clazz = MyClass.class;
        Method factoryMethod = clazz.getMethod("create", String.class);
        KeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(factoryMethod);
        String key1 = "Test";
        Object result1 = keyDeserializer.deserializeKey(key1, new DeserializationContext(objectMapper));
        assertEquals(MyClass.create("Test"), result1);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKey_InvalidInput() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String keyInvalid = "invalid_input";
        keyDeserializer.deserializeKey(keyInvalid, new DeserializationContext(objectMapper));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidKeyClass() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, Object.class);
        String key = "test";
        keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_NullKey() throws Exception {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = null;
        keyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper));
    }

    private static class MyClass {
        private final String name;

        public MyClass(String name) {
            this.name = name;
        }

        public static MyClass create(String name) {
            return new MyClass(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static enum MyEnum {
        VALUE1,
        VALUE2
    }
}