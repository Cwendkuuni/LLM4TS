package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ClassCastException;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Introspector;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    @Test
    public void testDefaultConstructor() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, null);
        assertNotNull(deserializer);
        assertEquals(0, deserializer._kind);
        assertNull(deserializer._keyClass);
        assertNull(deserializer._deser);
    }

    @Test
    public void testForTypeMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<?> intClass = Integer.class;
        KeyDeserializer deserializer = StdKeyDeserializer.forType(intClass);
        assertEquals(Integer.TYPE, deserializer.getKeyClass().getTypeName());
        assertNotNull(deserializer._deser);
        assertEquals(Integer.class, deserializer._deser.getClass());
    }

    @Test
    public void testDeserializeKeyMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String key = "true";
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        Object result = deserializer.deserializeKey(key, new DeserializationContext(mapper.getConfig()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = deserializer.deserializeKey(key, new DeserializationContext(mapper.getConfig()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        key = "not a valid representation";
        result = deserializer.deserializeKey(key, new DeserializationContext(mapper.getConfig()));
        assertNull(result);

        // Test for Enum class
        EnumSet<Locale> locales = EnumSet.allOf(Locale.class);
        Locale locale = locales.stream().findFirst().get();
        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        result = deserializer.deserializeKey(locale.toString(), new DeserializationContext(mapper.getConfig()));
        assertEquals(locale, result);
    }

    @Test
    public void testGetKeyClassMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        KeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.TYPE, deserializer.getKeyClass().getTypeName());
    }

    @Test
    public void testParseMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "123";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertEquals(Integer.valueOf(123), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntMethodWithInvalidInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "abc";
        deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testParseLongMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "12345678901234567890";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertEquals(Long.valueOf(12345678901234567890L), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLongMethodWithInvalidLong() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "abc";
        deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testParseDoubleMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        String key = "3.14159265358979323846";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertEquals(Double.valueOf(3.14159265358979323846), result);
    }

    @Test
    public void testParseNullMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = null;
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertNull(result);
    }

    @Test
    public void testParseEnumMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<Locale> localeClass = Locale.class;
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, localeClass);
        String key = "zh_CN";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertEquals(Locale.CHINA, result);
    }

    @Test
    public void testParseEnumInvalidMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<Locale> localeClass = Locale.class;
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, localeClass);
        String key = "invalidLocale";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEnumInvalidMethodWithUnknownEnumValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Class<MyEnum> myEnumClass = MyEnum.class;
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        Introspector introspectorAnnotation = introspector.findAnnotation(myEnumClass, JsonDeserialize.class);
        JsonDeserialize jsonDeserialize = introspectorAnnotation.getValue();
        AnnotatedClass annotatedClass = introspector.findAnnotation(myEnumClass, AnnotatedClass.class);
        AnnotatedMethod parseAnnotatedMethod = annotatedClass.findMethod("valueOf", String.class);
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, myEnumClass);
        deserializer._deser = jsonDeserialize.getValue();
        String key = "invalidEnumValue";
        deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testParseClassMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        String key = "String";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testParseUrlMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        String key = "http://example.com";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test(expected = MalformedURLException.class)
    public void testParseUrlMethodWithInvalidUrl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        String key = "http:example.com";
        deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testParseUriMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        String key = "http://example.com";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertTrue(result instanceof URI);
        assertEquals(new URI("http://example.com"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUriMethodWithInvalidUri() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        String key = "http:example.com";
        deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testParseDateMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        String key = "2023-03-20";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertTrue(result instanceof Date);
        assertEquals(new Date(2023, 2, 20), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateMethodWithInvalidDate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        String key = "2023/03/20";
        deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testParseCalendarMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        String key = "2023-03-20";
        Object result = deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
        assertTrue(result instanceof Calendar);
        assertEquals(Calendar.getInstance().getTime(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCalendarMethodWithInvalidDate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        String key = "2023/03/20";
        deserializer._parse(key, new DeserializationContext(mapper.getConfig()));
    }

    @Test
    public void testParseUriWithNullKey() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        String key = null;
        Object result = deserializer.deserializeKey(key, new DeserializationContext(mapper.getConfig()));
        assertNull(result);
    }

    @Test
    public void testParseUrlWithNullKey() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        String key = null;
        Object result = deserializer.deserializeKey(key, new DeserializationContext(mapper.getConfig()));
        assertNull(result);
    }
}

class MyEnum {
    @JsonDeserialize(using = MyEnumDeserializer.class)
    public enum MyEnumValue {
        VALUE1, VALUE2, VALUE3
    }
}

class MyEnumDeserializer extends JsonDeserializer<MyEnumValue> {

    public MyEnumDeserializer() {
        this(null);
    }

    public MyEnumDeserializer(Class<MyEnumValue> t) {
        super(t);
    }

    @Override
    public MyEnumValue deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String key = p.getValueAsString();
        return Arrays.stream(MyEnumValue.values()).filter(value -> value.name().equals(key)).findFirst().orElse(null);
    }
}