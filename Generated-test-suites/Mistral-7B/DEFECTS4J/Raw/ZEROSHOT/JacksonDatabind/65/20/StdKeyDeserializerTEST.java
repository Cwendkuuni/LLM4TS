package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

/**
 * Test class for StdKeyDeserializer
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        // Test forType method
        Class<?> stringClass = String.class;
        StdKeyDeserializer stringKd = StdKeyDeserializer.forType(stringClass);
        Assert.assertEquals(StdKeyDeserializer.StringKD.class, stringKd.getClass());

        Class<?> objectClass = Object.class;
        StdKeyDeserializer objectKd = StdKeyDeserializer.forType(objectClass);
        Assert.assertEquals(StdKeyDeserializer.StringKD.class, objectKd.getClass());

        Class<?> booleanClass = Boolean.class;
        StdKeyDeserializer booleanKd = StdKeyDeserializer.forType(booleanClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_BOOLEAN, booleanKd._kind);

        Class<?> byteClass = Byte.class;
        StdKeyDeserializer byteKd = StdKeyDeserializer.forType(byteClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_BYTE, byteKd._kind);

        Class<?> shortClass = Short.class;
        StdKeyDeserializer shortKd = StdKeyDeserializer.forType(shortClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_SHORT, shortKd._kind);

        Class<?> charClass = Character.class;
        StdKeyDeserializer charKd = StdKeyDeserializer.forType(charClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_CHAR, charKd._kind);

        Class<?> intClass = Integer.class;
        StdKeyDeserializer intKd = StdKeyDeserializer.forType(intClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_INT, intKd._kind);

        Class<?> longClass = Long.class;
        StdKeyDeserializer longKd = StdKeyDeserializer.forType(longClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_LONG, longKd._kind);

        Class<?> floatClass = Float.class;
        StdKeyDeserializer floatKd = StdKeyDeserializer.forType(floatClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_FLOAT, floatKd._kind);

        Class<?> doubleClass = Double.class;
        StdKeyDeserializer doubleKd = StdKeyDeserializer.forType(doubleClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_DOUBLE, doubleKd._kind);

        Class<?> localeClass = Locale.class;
        StdKeyDeserializer localeKd = StdKeyDeserializer.forType(localeClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_LOCALE, localeKd._kind);

        Class<?> dateClass = Date.class;
        StdKeyDeserializer dateKd = StdKeyDeserializer.forType(dateClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_DATE, dateKd._kind);

        Class<?> calendarClass = Calendar.class;
        StdKeyDeserializer calendarKd = StdKeyDeserializer.forType(calendarClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_CALENDAR, calendarKd._kind);

        Class<?> uriClass = URI.class;
        StdKeyDeserializer uriKd = StdKeyDeserializer.forType(uriClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_URI, uriKd._kind);

        Class<?> urlClass = URL.class;
        StdKeyDeserializer urlKd = StdKeyDeserializer.forType(urlClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_URL, urlKd._kind);

        Class<?> classClass = Class.class;
        StdKeyDeserializer classKd = StdKeyDeserializer.forType(classClass);
        Assert.assertEquals(StdKeyDeserializer.StdKeyDeserializer.TYPE_CLASS, classKd._kind);

        Class<?> enumClass = Enum.class;
        StdKeyDeserializer enumKd = StdKeyDeserializer.forType(enumClass);
        Assert.assertNull(enumKd);

        Class<?> currencyClass = Currency.class;
        StdKeyDeserializer currencyKd = StdKeyDeserializer.forType(currencyClass);
        Assert.assertNull(currencyKd);
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        // Test deserializeKey method for basic types
        StdKeyDeserializer booleanKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String key = "true";
        Object result = booleanKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(Boolean.TRUE, result);

        key = "false";
        result = booleanKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(Boolean.FALSE, result);

        StdKeyDeserializer byteKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_BYTE, Byte.class);
        key = "127";
        result = byteKd.deserializeKey(key, new TestContext());
        Assert.assertEquals((byte) 127, result);

        StdKeyDeserializer shortKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_SHORT, Short.class);
        key = "-128";
        result = shortKd.deserializeKey(key, new TestContext());
        Assert.assertEquals((short) -128, result);

        StdKeyDeserializer charKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_CHAR, Character.class);
        key = "A";
        result = charKd.deserializeKey(key, new TestContext());
        Assert.assertEquals('A', result);

        StdKeyDeserializer intKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_INT, Integer.class);
        key = "12345";
        result = intKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(12345, result);

        StdKeyDeserializer longKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_LONG, Long.class);
        key = "123456789012345";
        result = longKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(123456789012345L, result);

        StdKeyDeserializer floatKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_FLOAT, Float.class);
        key = "1.2345";
        result = floatKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(1.2345f, result);

        StdKeyDeserializer doubleKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        key = "1.23456789";
        result = doubleKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(1.23456789, result);

        // Test deserializeKey method for non-basic types
        StdKeyDeserializer localeKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        key = "en_US";
        result = localeKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(Locale.US, result);

        StdKeyDeserializer uriKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_URI, URI.class);
        key = "http://example.com";
        result = uriKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(new URI("http://example.com"), result);

        StdKeyDeserializer urlKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_URL, URL.class);
        key = "http://example.com";
        result = urlKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(new URL("http://example.com"), result);

        StdKeyDeserializer classKd = new StdKeyDeserializer(StdKeyDeserializer.StdKeyDeserializer.TYPE_CLASS, String.class);
        key = "java.lang.String";
        result = classKd.deserializeKey(key, new TestContext());
        Assert.assertEquals(String.class, result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper _getConfig() {
            return null;
        }

        @Override
        public AnnotatedMethod _findMethod(Class<?> cls, String name) {
            return null;
        }

        @Override
        public ObjectMapper _getFactory() {
            return null;
        }

        @Override
        public Object _valueInstantiate(Class<?> cls, Object params[]) throws InstantiationException, IllegalAccessException {
            return null;
        }

        @Override
        public void _reportBadInitialization(Class<?> clazz, String message, Object initialValue)
                throws JsonMappingException {
            // empty implementation
        }

        @Override
        public Object _findValueInMap(Map<String, Object> m, String key) {
            return null;
        }

        @Override
        public void _createContextualValueFactory(Object factory) {
            // empty implementation
        }

        @Override
        public Object _valueInstantiate(Class<?> cls) throws InstantiationException, IllegalAccessException {
            return null;
        }

        @Override
        public Object _createObjectInstance(Class<?> cls, Object[] args) throws InstantiationException, IllegalAccessException {
            return null;
        }

        @Override
        public void _finishInitialization() {
            // empty implementation
        }

        @Override
        public Object _constructInstance(Class<?> cls, Object[] args) throws InstantiationException, IllegalAccessException {
            return null;
        }

        @Override
        public AnnotatedClass _getAnnotatedClass(Class<?> cls) {
            return null;
        }

        @Override
        public Object _findValueInMap(Map<?, ?> m, String key) {
            return null;
        }
    }
}