package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() throws Exception {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(String.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);

        String key = "true";
        Object result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        key = "not_a_valid_boolean";
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertNull(result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        byte value = Byte.parseByte("127");
        key = Integer.toString(value);
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(Byte.valueOf(value), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        short valueShort = Short.parseShort("12345");
        key = Integer.toString(valueShort);
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(Short.valueOf(valueShort), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        char valueChar = 'A';
        key = Character.toString(valueChar);
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(Character.valueOf(valueChar), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        int valueInt = 12345;
        key = Integer.toString(valueInt);
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(Integer.valueOf(valueInt), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        long valueLong = 1234567890L;
        key = Long.toString(valueLong);
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(Long.valueOf(valueLong), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        float valueFloat = 123.45f;
        key = Float.toString(valueFloat);
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(Float.valueOf(valueFloat), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        double valueDouble = 123.456789;
        key = Double.toString(valueDouble);
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(Double.valueOf(valueDouble), result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        Locale valueLocale = Locale.US;
        key = valueLocale.toString();
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(valueLocale, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        Date valueDate = new Date();
        key = "2022-12-31T12:00:00Z"; // ISO formatted date
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new ISODateFormat());
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), mapper));
        assertEquals(valueDate, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, java.util.Calendar.class);
        Calendar valueCalendar = java.util.GregorianCalendar.getInstance();
        key = "2022-12-31T12:00:00Z"; // ISO formatted date
        mapper.setDateFormat(new ISODateFormat());
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), mapper));
        assertEquals(valueCalendar, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        URI valueURI = new URI("https://www.example.com/");
        key = valueURI.toString();
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(valueURI, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        URL valueURL = new URL("https://www.example.com/");
        key = valueURL.toString();
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(valueURL, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
        Class<?> valueClass = String.class;
        key = valueClass.getName();
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(valueClass, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        UUID valueUuid = UUID.randomUUID();
        key = valueUuid.toString();
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(valueUuid, result);

        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, java.math.BigDecimal.class);
        java.math.BigDecimal valueBigDecimal = new java.math.BigDecimal("123.45");
        key = valueBigDecimal.toString();
        result = keyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(valueBigDecimal, result);
    }

    @Test
    public void testStringKeyDeserializer() throws Exception {
        StdKeyDeserializer stringKeyDeserializer = StdKeyDeserializer.StringKD.forType(String.class);
        Object result = stringKeyDeserializer.deserializeKey("test_string", new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals("test_string", result);

        stringKeyDeserializer = StdKeyDeserializer.StringKD.forType(Object.class);
        result = stringKeyDeserializer.deserializeKey("test_object", new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals("test_object", result);
    }

    @Test
    public void testDelegatingKeyDeserializer() throws Exception {
        Class<MyBean> beanClass = MyBean.class;
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getAnnotationIntrospector().findAnnotation(beanClass);
        AnnotatedField annotatedField = annotatedClass.findAnnotatedMember("myField", Field.class);
        AnnotatedMethod getter = annotatedField.get(beanClass);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class, getter);

        StdKeyDeserializer delegatingKeyDeserializer = new StdKeyDeserializer(beanClass, stringDeserializer);
        String key = "test_string";
        Object result = delegatingKeyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(stringDeserializer.deserialize("test_string", new DeserializationContext(new Object(), new ObjectMapper())), result);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        Class<MyEnum> enumClass = MyEnum.class;
        ObjectMapper mapper = new ObjectMapper();
        AnnotatedClass annotatedClass = mapper.getAnnotationIntrospector().findAnnotation(enumClass);
        AnnotatedMethod factoryMethod = annotatedClass.findAnnotatedMember("valueOf", Method.class);

        StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(enumClass, factoryMethod);
        String key = "MyEnum.VALUE1";
        Object result = enumKeyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        assertEquals(MyEnum.VALUE1, result);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Class<MyBean> beanClass = MyBean.class;
        Constructor<MyBean> constructor = beanClass.getConstructor(String.class);

        StdKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer(constructor);
        String key = "test_string";
        Object result = stringCtorKeyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        MyBean instance = constructor.newInstance(key);
        assertEquals(instance, result);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Class<MyBean> beanClass = MyBean.class;
        Method factoryMethod = MyBean.class.getMethod("create", String.class);

        StdKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer(factoryMethod);
        String key = "test_string";
        Object result = stringFactoryKeyDeserializer.deserializeKey(key, new DeserializationContext(new Object(), new ObjectMapper()));
        Object instance = factoryMethod.invoke(null, key);
        assertEquals(instance, result);
    }

    private static class MyBean {
        private String myField;

        public MyBean(String field) {
            this.myField = field;
        }

        public static MyBean create(String name) {
            return new MyBean(name);
        }
    }

    @JsonEnumDefaultValue("DEFAULT_VALUE")
    enum MyEnum {
        VALUE1,
        VALUE2,
        DEFAULT_VALUE
    }
}