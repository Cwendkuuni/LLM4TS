package com.example.junit; // replace with your test package

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testForType_String() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        String key = "test";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(key, deserializedKey);
    }

    @Test
    public void testForType_Object() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Object.class);
        String key = "test";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(key, deserializedKey);
    }

    @Test
    public void testForType_Boolean() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        String keyTrue = "true";
        String keyFalse = "false";
        Object deserializedKeyTrue = serializer.deserializeKey(keyTrue, createContext());
        Object deserializedKeyFalse = serializer.deserializeKey(keyFalse, createContext());
        Assert.assertTrue((Boolean) deserializedKeyTrue);
        Assert.assertFalse((Boolean) deserializedKeyFalse);
    }

    @Test
    public void testForType_Byte() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        String key = "127";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals((byte) 127, deserializedKey);
    }

    @Test
    public void testForType_Short() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        String key = "32767";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals((short) 32767, deserializedKey);
    }

    @Test
    public void testForType_Char() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        String key = "A";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals('A', deserializedKey);
    }

    @Test
    public void testForType_Int() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        String key = "12345";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(12345, deserializedKey);
    }

    @Test
    public void testForType_Long() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        String key = "12345678901234567890";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(12345678901234567890L, deserializedKey);
    }

    @Test
    public void testForType_Float() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        String key = "3.14";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(3.14f, deserializedKey);
    }

    @Test
    public void testForType_Double() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        String key = "3.14159265358979323846";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(3.14159265358979323846, deserializedKey);
    }

    @Test
    public void testForType_Locale() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        String key = "en_US";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(Locale.forLanguageTag("en_US"), deserializedKey);
    }

    @Test
    public void testForType_Date() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        String key = "2023-03-26";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(new Date(2023, 3, 26), deserializedKey);
    }

    @Test
    public void testForType_Calendar() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        String key = "2023-03-26";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2023, 3, 26));
        Assert.assertEquals(calendar, deserializedKey);
    }

    @Test
    public void testForType_UUID() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        String key = "3b9d6b1f-b0a3-4e72-8c56-c1bef4923c69";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(new UUID("3b9d6b1f-b0a3-4e72-8c56-c1bef4923c69"), deserializedKey);
    }

    @Test
    public void testForType_URI() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        String key = "https://example.com";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(new URI(key), deserializedKey);
    }

    @Test
    public void testForType_URL() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        String key = "https://example.com";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Assert.assertEquals(new URL(key), deserializedKey);
    }

    @Test
    public void testForType_Enum() throws IOException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Enum.class);
        String key = "TestEnum.VALUE_1";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        EnumTestEnum expectedEnum = EnumTestEnum.VALUE_1;
        Assert.assertEquals(expectedEnum.name(), deserializedKey.getClass().getSimpleName());
        Assert.assertEquals(expectedEnum.getValue(), deserializedKey);
    }

    @Test
    public void testForType_CustomClassWithNoArgsConstructor() throws IOException {
        String className = "com.example.CustomClass";
        StdKeyDeserializer serializer = new StdKeyDeserializer(-1, Class.forName(className));
        String key = "test";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Class<?> customClass = Class.forName(className);
        Constructor<?> constructor = customClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        Assert.assertEquals(instance, deserializedKey);
    }

    @Test
    public void testForType_CustomClassWithSingleStringArgConstructor() throws IOException {
        String className = "com.example.CustomClassWithSingleStringArgConstructor";
        StdKeyDeserializer serializer = new StringCtorKeyDeserializer(Class.forName(className).getDeclaredConstructor(String.class));
        String key = "test";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Class<?> customClass = Class.forName(className);
        Constructor<?> constructor = customClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object instance = constructor.newInstance(key);
        Assert.assertEquals(instance, deserializedKey);
    }

    @Test
    public void testForType_CustomClassWithStaticNoArgsFactoryMethod() throws IOException {
        String className = "com.example.CustomClassWithStaticNoArgsFactoryMethod";
        Method factoryMethod = Class.forName(className).getDeclaredMethod("createInstance");
        StdKeyDeserializer serializer = new StringFactoryKeyDeserializer(factoryMethod);
        String key = "test";
        Object deserializedKey = serializer.deserializeKey(key, createContext());
        Object instance = factoryMethod.invoke(null, key);
        Assert.assertEquals(instance, deserializedKey);
    }

    @Test(expected = IOException.class)
    public void testForType_InvalidClass() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer[].class);
        String key = "test";
        serializer.deserializeKey(key, createContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForType_NegativeNumber() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        String key = "-128";
        serializer.deserializeKey(key, createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testForType_InvalidEnum() throws IOException {
        String className = "com.example.InvalidEnum";
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Enum.class);
        String key = className + ".INVALID_VALUE";
        serializer.deserializeKey(key, createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testForType_InvalidLocale() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        String key = "invalid_locale";
        serializer.deserializeKey(key, createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testForType_InvalidDate() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        String key = "invalid_date";
        serializer.deserializeKey(key, createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testForType_InvalidCalendar() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        String key = "invalid_calendar";
        serializer.deserializeKey(key, createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testForType_InvalidURI() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        String key = "invalid_uri";
        serializer.deserializeKey(key, createContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testForType_InvalidURL() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        String key = "invalid_url";
        serializer.deserializeKey(key, createContext());
    }

    private DeserializationContext createContext() {
        return objectMapper.getDeserializationContext();
    }

    private static class EnumTestEnum {
        public static final EnumTestEnum VALUE_1 = new EnumTestEnum("VALUE_1", 1);
        public static final EnumTestEnum VALUE_2 = new EnumTestEnum("VALUE_2", 2);

        private final String name;
        private final int value;

        public EnumTestEnum(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}