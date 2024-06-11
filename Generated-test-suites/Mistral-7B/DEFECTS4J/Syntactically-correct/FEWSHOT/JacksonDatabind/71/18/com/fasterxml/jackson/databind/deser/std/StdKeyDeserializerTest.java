package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StandardDeserializationProblemHandler;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Test cases for StdKeyDeserializer class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private static final DeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        StandardDeserializationProblemHandler problemHandler = new StandardDeserializationProblemHandler();
        return mapper.getDeserializationContext().withBehavior(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .withRootName("root").withContextHandler(problemHandler);
    }

    @Test
    public void testForType() throws Exception {
        Class<?> stringClass = String.class;
        Object keyDeserializer = StdKeyDeserializer.forType(stringClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StringKD.class, StdKeyDeserializer.forType(String.class));
        assertEquals(StdKeyDeserializer.class, StdKeyDeserializer.forType(Object.class));

        Class<?> booleanClass = Boolean.class;
        Object keyDeserializerBoolean = StdKeyDeserializer.forType(booleanClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerBoolean.getClass());
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, ((StdKeyDeserializer) keyDeserializerBoolean)._kind);

        Class<?> byteClass = Byte.class;
        Object keyDeserializerByte = StdKeyDeserializer.forType(byteClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerByte.getClass());
        assertEquals(StdKeyDeserializer.TYPE_BYTE, ((StdKeyDeserializer) keyDeserializerByte)._kind);

        Class<?> shortClass = Short.class;
        Object keyDeserializerShort = StdKeyDeserializer.forType(shortClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerShort.getClass());
        assertEquals(StdKeyDeserializer.TYPE_SHORT, ((StdKeyDeserializer) keyDeserializerShort)._kind);

        Class<?> charClass = Character.class;
        Object keyDeserializerChar = StdKeyDeserializer.forType(charClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerChar.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CHAR, ((StdKeyDeserializer) keyDeserializerChar)._kind);

        Class<?> intClass = Integer.class;
        Object keyDeserializerInt = StdKeyDeserializer.forType(intClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerInt.getClass());
        assertEquals(StdKeyDeserializer.TYPE_INT, ((StdKeyDeserializer) keyDeserializerInt)._kind);

        Class<?> longClass = Long.class;
        Object keyDeserializerLong = StdKeyDeserializer.forType(longClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerLong.getClass());
        assertEquals(StdKeyDeserializer.TYPE_LONG, ((StdKeyDeserializer) keyDeserializerLong)._kind);

        Class<?> floatClass = Float.class;
        Object keyDeserializerFloat = StdKeyDeserializer.forType(floatClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerFloat.getClass());
        assertEquals(StdKeyDeserializer.TYPE_FLOAT, ((StdKeyDeserializer) keyDeserializerFloat)._kind);

        Class<?> doubleClass = Double.class;
        Object keyDeserializerDouble = StdKeyDeserializer.forType(doubleClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerDouble.getClass());
        assertEquals(StdKeyDeserializer.TYPE_DOUBLE, ((StdKeyDeserializer) keyDeserializerDouble)._kind);

        Class<?> localeClass = Locale.class;
        Object keyDeserializerLocale = StdKeyDeserializer.forType(localeClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerLocale.getClass());
        assertEquals(StdKeyDeserializer.TYPE_LOCALE, ((StdKeyDeserializer) keyDeserializerLocale)._kind);

        Class<?> dateClass = Date.class;
        Object keyDeserializerDate = StdKeyDeserializer.forType(dateClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerDate.getClass());
        assertEquals(StdKeyDeserializer.TYPE_DATE, ((StdKeyDeserializer) keyDeserializerDate)._kind);

        Class<?> calendarClass = Calendar.class;
        Object keyDeserializerCalendar = StdKeyDeserializer.forType(calendarClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerCalendar.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CALENDAR, ((StdKeyDeserializer) keyDeserializerCalendar)._kind);

        Class<?> uriClass = URI.class;
        Object keyDeserializerURI = StdKeyDeserializer.forType(uriClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerURI.getClass());
        assertEquals(StdKeyDeserializer.TYPE_URI, ((StdKeyDeserializer) keyDeserializerURI)._kind);

        Class<?> urlClass = URL.class;
        Object keyDeserializerURL = StdKeyDeserializer.forType(urlClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerURL.getClass());
        assertEquals(StdKeyDeserializer.TYPE_URL, ((StdKeyDeserializer) keyDeserializerURL)._kind);

        Class<?> enumClass = MyEnum.class;
        Object keyDeserializerEnum = StdKeyDeserializer.forType(enumClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerEnum.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CLASS, ((StdKeyDeserializer) keyDeserializerEnum)._kind);

        Class<?> uuidClass = UUID.class;
        Object keyDeserializerUUID = StdKeyDeserializer.forType(uuidClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerUUID.getClass());
        assertEquals(StdKeyDeserializer.TYPE_UUID, ((StdKeyDeserializer) keyDeserializerUUID)._kind);

        Class<?> currencyClass = Currency.class;
        Object keyDeserializerCurrency = StdKeyDeserializer.forType(currencyClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializerCurrency.getClass());
        assertEquals(StdKeyDeserializer.TYPE_CURRENCY, ((StdKeyDeserializer) keyDeserializerCurrency)._kind);
    }

    @Test
    public void testDeserializeKey() throws Exception {
        Class<?> stringClass = String.class;
        Object keyDeserializer = StdKeyDeserializer.forType(stringClass);

        String key = "true";
        Object deserializedKey = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals("true", deserializedKey);

        key = "false";
        deserializedKey = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals("false", deserializedKey);

        key = "123";
        deserializedKey = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(123, deserializedKey);

        key = "1234567890";
        deserializedKey = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(1234567890L, deserializedKey);

        key = "123.45";
        deserializedKey = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(123.45f, deserializedKey);

        key = "123.456";
        deserializedKey = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(123.456d, deserializedKey);

        key = "en_US";
        Object deserializedLocale = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(Locale.US, deserializedLocale);

        key = "2022-01-01";
        Object deserializedDate = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(new Date(1642304000000L), deserializedDate);

        key = "2022-01-01T12:34:56Z";
        Object deserializedDateTime = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(new Date(1642347496000L), deserializedDateTime);

        key = "2022-01-01T12:34:56.123Z";
        Object deserializedDatetimeWithMillis = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(new Date(1642347496123L), deserializedDatetimeWithMillis);

        key = "MyEnum.ONE";
        Object deserializedEnum = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(MyEnum.ONE, deserializedEnum);

        key = "MyEnum.TWO";
        deserializedEnum = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(MyEnum.TWO, deserializedEnum);

        key = "MyEnum.THREE";
        deserializedEnum = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(MyEnum.THREE, deserializedEnum);

        key = "MyEnum.FOUR";
        deserializedEnum = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(MyEnum.FOUR, deserializedEnum);

        key = "UUID('123e4567-89ab-cdef-1234-567890123456')";
        Object deserializedUuid = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(new UUID("123e4567-89ab-cdef-1234-567890123456"), deserializedUuid);

        key = "URL('http://example.com')";
        Object deserializedUrl = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(new URL("http://example.com"), deserializedUrl);

        key = "URI('http://example.com')";
        Object deserializedUri = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(new URI("http://example.com"), deserializedUri);

        key = "MyClass(arg1=1, arg2=2)";
        AnnotatedClass ac = new AnnotatedClass(MyClass.class);
        Constructor<?> ctor = ac.getConstructors().next();
        Object arg1 = 1;
        Object arg2 = 2;
        Object deserializedInstance = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext(), ctor, arg1, arg2);
        assertEquals(MyClass.class, deserializedInstance.getClass());
        assertEquals(1, ((MyClass) deserializedInstance).arg1);
        assertEquals(2, ((MyClass) deserializedInstance).arg2);

        key = "MyClass.STATIC_FIELD";
        Object deserializedStaticField = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(MyClass.STATIC_FIELD, deserializedStaticField);

        key = "MyClass.STATIC_METHOD()";
        Object deserializedStaticMethod = ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
        assertEquals(MyClass.STATIC_METHOD, deserializedStaticMethod);
    }

    @Test
    public void testDeserializeKey_Invalid() throws Exception {
        Class<?> stringClass = String.class;
        Object keyDeserializer = StdKeyDeserializer.forType(stringClass);

        String key = "invalid";
        try {
            ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
            fail("Expected DeserializationException");
        } catch (JsonMappingException e) {
            // expected
        }

        key = "true invalid";
        try {
            ((StdKeyDeserializer) keyDeserializer).deserializeKey(key, createContext());
            fail("Expected DeserializationException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testForType_Unknown() throws Exception {
        Class<?> unknownClass = MyUnknownClass.class;
        Object keyDeserializer = StdKeyDeserializer.forType(unknownClass);
        assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
        assertEquals(StdKeyDeserializer.class, ((StdKeyDeserializer) keyDeserializer)._deser);
        assertNull(((StdKeyDeserializer) keyDeserializer)._kind);
    }

    private static class MyEnum {
        public static final MyEnum ONE = new MyEnum("ONE");
        public static final MyEnum TWO = new MyEnum("TWO");
        public static final MyEnum THREE = new MyEnum("THREE");
        public static final MyEnum FOUR = new MyEnum("FOUR");

        private final String name;

        MyEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class MyClass {
        public int arg1;
        public int arg2;

        public MyClass() {
        }

        public MyClass(int arg1, int arg2) {
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        public static String STATIC_FIELD = "staticField";
        public static Method STATIC_METHOD = MyClass.class.getMethod("staticMethod");

        public int getArg1() {
            return arg1;
        }

        public int getArg2() {
            return arg2;
        }
    }

    private static class MyUnknownClass {
    }
}