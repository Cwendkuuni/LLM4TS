package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonValue;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringCtorKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.EnumKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.DelegatingKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdKeyDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testStringKeyDeserializer() throws Exception {
        StringKD stringKeyDeserializer = StdKeyDeserializer.StringKD;
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "test";
        assertEquals(key, stringKeyDeserializer.deserializeKey(key, ctxt));
    }

    @Test
    public void testDelegatingKeyDeserializer() throws Exception {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String key = "test";
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class, ctxt.getAnnotationIntrospector());
        DelegatingKD delegatingKeyDeserializer = new DelegatingKD(String.class, stringDeserializer);
        Object result = delegatingKeyDeserializer.deserializeKey(key, ctxt);
        assertEquals(key, result);
    }

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        String key = "true";
        Boolean expected = Boolean.TRUE;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        String key = "127";
        Byte expected = Byte.parseByte(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        String key = "32767";
        Short expected = Short.parseShort(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        String key = "A";
        Character expected = 'A';
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        String key = "42";
        Integer expected = Integer.parseInt(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        String key = "4294967295";
        Long expected = Long.parseLong(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        String key = "3.1415";
        Float expected = Float.parseFloat(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result, 0.0f);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        String key = "3.14159265358979323846";
        Double expected = Double.parseDouble(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        String key = "en_US";
        Locale expected = Locale.US;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        String key = "2022-01-01";
        Date expected = new Date(0);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new StdDateFormat());
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        String key = "2022-01-01";
        Calendar expected = Calendar.getInstance();
        expected.setTime(new Date(0));
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new StdDateFormat());
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        String key = "123e4567-e89b-12d3-a456-426614174000";
        UUID expected = UUID.fromString(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        String key = "http://test.com";
        URI expected = new URI(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        String key = "http://test.com";
        URL expected = new URL(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        String key = "java.lang.String";
        Class<?> expected = Class.forName(key);
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Class.class);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result.getClass());
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        EnumSet<Color> colors = EnumSet.allOf(Color.class);
        String key = colors.stream().map(Enum::name).collect(Collectors.joining(","));
        Color expected = Color.RED;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Color.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectMapper.DefaultTyping typing = new ObjectMapper.DefaultTyping();
        typings.setRootValueClass(Color[].class);
        mapper.setDefaultTyping(typing);
        Object result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testCustomKeyDeserializer() throws Exception {
        String key = "{\"name\":\"Test\",\"age\":42}";
        MyKey myKey = new MyKey("Test", 42);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultTyping(null);
        ObjectReader reader = mapper.readerForClass(MyKey.class);
        Object result = reader.readValue(key);
        assertEquals(myKey, result);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        String key = "Test";
        StringFactoryKeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(MyFactory.class);
        MyKey result = (MyKey) keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals("Test", result.getName());
        assertEquals(42, result.getAge());
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        String key = "Test";
        String ctorArg = "Test";
        Class<MyKey> myKeyClass = MyKey.class;
        Constructor<MyKey> constructor = myKeyClass.getConstructor(String.class);
        StringCtorKeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(constructor);
        MyKey result = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(ctorArg, result.getName());
        assertEquals(42, result.getAge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.handleUnexpectedValue(null, null, "test", null);
        Object result = keyDeserializer.deserializeKey("test", ctxt);
        assertNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidURLKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.handleUnexpectedValue(null, null, "test%{foo}", null);
        Object result = keyDeserializer.deserializeKey("test%{foo}", ctxt);
        assertNull(result);
    }

    @Test(expected = URISyntaxException.class)
    public void testInvalidURIKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.handleUnexpectedValue(null, null, "test%{foo}", null);
        Object result = keyDeserializer.deserializeKey("test%{foo}", ctxt);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEnumKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Color.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.handleUnexpectedValue(null, null, "Invalid", null);
        Object result = keyDeserializer.deserializeKey("Invalid", ctxt);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidJsonStringKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.handleUnexpectedValue(null, null, "[1, 2]", null);
        Object result = keyDeserializer.deserializeKey("[1, 2]", ctxt);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidJsonNumberKeyDeserializer() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.handleUnexpectedValue(null, null, "3.14", null);
        Object result = keyDeserializer.deserializeKey("3.14", ctxt);
        assertNull(result);
    }

    static class MyKey {
        private final String name;
        private final int age;

        public MyKey(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @JsonValue
        public String toString() {
            return "{\"name\":\"" + name + "\",\"age\":" + age + "}";
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @JsonDeserialize(using = FromStringDeserializer.class)
        public static MyKey fromString(String s) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(s, MyKey.class);
        }
    }

    static class MyFactory {
        public static MyKey create(String name, int age) {
            return new MyKey(name, age);
        }
    }
}