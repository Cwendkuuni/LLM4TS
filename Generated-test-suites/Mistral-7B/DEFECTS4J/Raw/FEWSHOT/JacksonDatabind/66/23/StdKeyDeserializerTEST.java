package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFactory;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringKD;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Test class for StdKeyDeserializer.
 */
public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() throws Exception {
        StringKD stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        assertEquals(stringKD.getClass(), StringKD.class);
        assertEquals(stringKD.getKeyClass(), String.class);
        Object result = stringKD.deserializeKey("foo", null);
        assertSame(result, "foo");
    }

    @Test
    public void testObjectKD() throws Exception {
        StringKD objectKD = StdKeyDeserializer.StringKD.forType(Object.class);
        assertEquals(objectKD.getClass(), StringKD.class);
        assertEquals(objectKD.getKeyClass(), Object.class);
        Object result = objectKD.deserializeKey("foo", null);
        assertSame(result, "foo");
    }

    @Test
    public void testBooleanKD() throws Exception {
        KeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(booleanKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(booleanKD.getKeyClass(), Boolean.class);
        Object result = booleanKD.deserializeKey("true", null);
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.TRUE);
        result = booleanKD.deserializeKey("false", null);
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.FALSE);
    }

    @Test
    public void testByteKD() throws Exception {
        KeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);
        assertEquals(byteKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(byteKD.getKeyClass(), Byte.class);
        Object result = byteKD.deserializeKey("10", null);
        assertEquals(result, Byte.decode("10"));
    }

    @Test
    public void testShortKD() throws Exception {
        KeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);
        assertEquals(shortKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(shortKD.getKeyClass(), Short.class);
        Object result = shortKD.deserializeKey("123", null);
        assertEquals(result, Short.valueOf((short) 123));
    }

    @Test
    public void testCharKD() throws Exception {
        KeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);
        assertEquals(charKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(charKD.getKeyClass(), Character.class);
        Object result = charKD.deserializeKey("A", null);
        assertEquals(result, Character.valueOf('A'));
    }

    @Test
    public void testIntKD() throws Exception {
        KeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);
        assertEquals(intKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(intKD.getKeyClass(), Integer.class);
        Object result = intKD.deserializeKey("12345", null);
        assertEquals(result, Integer.valueOf(12345));
    }

    @Test
    public void testLongKD() throws Exception {
        KeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);
        assertEquals(longKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(longKD.getKeyClass(), Long.class);
        Object result = longKD.deserializeKey("123456789012345", null);
        assertEquals(result, Long.valueOf(123456789012345L));
    }

    @Test
    public void testFloatKD() throws Exception {
        KeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);
        assertEquals(floatKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(floatKD.getKeyClass(), Float.class);
        Object result = floatKD.deserializeKey("3.14159", null);
        assertEquals(result, Float.valueOf(3.14159f));
    }

    @Test
    public void testDoubleKD() throws Exception {
        KeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);
        assertEquals(doubleKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(doubleKD.getKeyClass(), Double.class);
        Object result = doubleKD.deserializeKey("3.14159265358979323846264338327950288416", null);
        assertEquals(result, Double.valueOf(3.14159265358979323846264338327950288416d));
    }

    @Test
    public void testLocaleKD() throws Exception {
        KeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        assertEquals(localeKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(localeKD.getKeyClass(), Locale.class);
        Object result = localeKD.deserializeKey("en_US", null);
        assertEquals(result, Locale.US);
    }

    @Test
    public void testDateKD() throws Exception {
        KeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        assertEquals(dateKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(dateKD.getKeyClass(), Date.class);
        Object result = dateKD.deserializeKey("2023-03-27", null);
        assertEquals(result, new Date(1679644800000L));
    }

    @Test
    public void testCalendarKD() throws Exception {
        KeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
        assertEquals(calendarKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(calendarKD.getKeyClass(), Calendar.class);
        Object result = calendarKD.deserializeKey("2023-03-27", null);
        Calendar calendar = (Calendar) result;
        assertEquals(calendar.get(Calendar.YEAR), 2023);
        assertEquals(calendar.get(Calendar.MONTH), Calendar.MARCH);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 27);
    }

    @Test
    public void testUUIDKD() throws Exception {
        KeyDeserializer uuidKD = StdKeyDeserializer.forType(UUID.class);
        assertEquals(uuidKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(uuidKD.getKeyClass(), UUID.class);
        Object result = uuidKD.deserializeKey("123e4567-e89b-12d3-a456-426614174000", null);
        assertEquals(result, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
    }

    @Test
    public void testURIKD() throws Exception {
        KeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        assertEquals(uriKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(uriKD.getKeyClass(), URI.class);
        Object result = uriKD.deserializeKey("http://example.com", null);
        assertEquals(result, new URI("http://example.com"));
    }

    @Test
    public void testURLKD() throws Exception {
        KeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        assertEquals(urlKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(urlKD.getKeyClass(), URL.class);
        Object result = urlKD.deserializeKey("http://example.com", null);
        assertEquals(result, new URL("http://example.com"));
    }

    @Test
    public void testClassKD() throws Exception {
        KeyDeserializer classKD = StdKeyDeserializer.forType(String.class);
        assertEquals(classKD.getClass(), com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.class);
        assertEquals(classKD.getKeyClass(), String.class);
        Object result = classKD.deserializeKey("java.lang.String", null);
        assertEquals(result, String.class);
    }

    @Test
    public void testEnumKD() throws Exception {
        Enum enumValue = Enum.values()[0];
        AnnotatedClass annotatedClass = ObjectMapper.getDefaultInstance().getAnnotationIntrospector().findClassAnnotations(enumValue.getClass());
        AnnotatedMethod factoryMethod = annotatedClass.findMethod(JsonFactory.class.getMethod("valueFromString", String.class));
        KeyDeserializer enumKD = new StdKeyDeserializer.EnumKD(null, factoryMethod);
        Object result = enumKD.deserializeKey("EAGER", null);
        assertEquals(result, enumValue);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Constructor<MyKey> constructor = MyKey.class.getDeclaredConstructor(String.class);
        StringFactoryKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(constructor);
        Object result = stringFactoryKeyDeserializer.deserializeKey("foo", null);
        MyKey myKey = new MyKey("foo");
        assertEquals(result, myKey);
    }

}

class MyKey {
    private final String name;

    @JsonCreator
    public MyKey(String name) {
        this.name = name;
    }

    @JsonValue
    public String toString() {
        return name;
    }
}