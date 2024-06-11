package com.example.junit; // Replace com.example.junit with the actual package name of your test class

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.EnumSet;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class StdKeyDeserializerTest {

    @Test
    public void testForTypeMethod() throws Exception {
        StdKeyDeserializer actual = StdKeyDeserializer.forType(String.class);
        StdKeyDeserializer expected = StringKD.forType(String.class);
        assertEquals(expected, actual);

        actual = StdKeyDeserializer.forType(UUID.class);
        assertEquals(TYPE_UUID, actual._kind);
    }

    @Test(expected = IllegalStateException.class)
    public void testForTypeMethodWithInvalidClass() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(new Object() {}.getClass());
    }

    @Test
    public void testDeserializeKeyMethod_StringAsString() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        StdKeyDeserializer deserializer = StringKD.forType(String.class);
        String key = "test";

        String actual = (String) deserializer.deserializeKey(key, context);
        assertEquals(key, actual);
    }

    @Test
    public void testDeserializeKeyMethod_ObjectAsObject() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        StdKeyDeserializer deserializer = StringKD.forType(Object.class);
        String key = "test";

        Object actual = deserializer.deserializeKey(key, context);
        assertEquals(key, actual.toString());
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyMethod_NullKey() throws Exception {
        DeserializationContext context = new TestDeserializationContext();
        StdKeyDeserializer deserializer = StringKD.forType(String.class);
        String key = null;

        deserializer.deserializeKey(key, context);
    }

    @Test
    public void testParseMethod_Boolean() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        DeserializationContext context = new TestDeserializationContext();
        String key = "true";

        Boolean actual = (Boolean) deserializer.deserializeKey(key, context);
        assertEquals(true, actual);

        key = "false";
        actual = (Boolean) deserializer.deserializeKey(key, context);
        assertEquals(false, actual);
    }

    @Test(expected = IOException.class)
    public void testParseMethod_InvalidBooleanKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        DeserializationContext context = new TestDeserializationContext();
        String key = "invalid";

        deserializer.deserializeKey(key, context);
    }

    @Test
    public void testParseMethod_Byte() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        DeserializationContext context = new TestDeserializationContext();
        String key = "127";

        Byte actual = (Byte) deserializer.deserializeKey(key, context);
        assertEquals((byte) 127, actual);
    }

    @Test(expected = IOException.class)
    public void testParseMethod_InvalidByteKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_BYTE, Byte.class);
        DeserializationContext context = new TestDeserializationContext();
        String key = "256";

        deserializer.deserializeKey(key, context);
    }

    // Add test cases for other supported types (Short, Char, Int, Long, Float, Double, Locale, Date, Calendar, UUID, URI, URL, Class, Currency)

    @Test(expected = JsonProcessingException.class)
    public void testParseMethod_UnknownTypeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class);
        DeserializationContext context = new TestDeserializationContext();
        String key = "test";

        deserializer.deserializeKey(key, context);
    }

    @Test
    public void testDeserializeKeyMethod_LocaleWithFromStringDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_LOCALE, Locale.class);
        DeserializationContext context = new TestDeserializationContext();
        FromStringDeserializer<?> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);

        String key = "en_US";
        Locale actual = (Locale) deserializer.deserializeKey(key, context);
        assertEquals(Locale.forLanguageTag("en_US"), actual);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyMethod_InvalidLocaleKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_LOCALE, Locale.class);
        DeserializationContext context = new TestDeserializationContext();
        String key = "invalid";

        deserializer.deserializeKey(key, context);
    }

    @Test
    public void testDeserializeKeyMethod_CurrencyWithFromStringDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_CURRENCY, Currency.class);
        DeserializationContext context = new TestDeserializationContext();
        FromStringDeserializer<?> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);

        String key = "USD";
        Currency actual = (Currency) deserializer.deserializeKey(key, context);
        assertEquals(Currency.getInstance("USD"), actual);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyMethod_InvalidCurrencyKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(TYPE_CURRENCY, Currency.class);
        DeserializationContext context = new TestDeserializationContext();
        String key = "invalid";

        deserializer.deserializeKey(key, context);
    }

    @Test
    public void testDeserializeKeyMethod_EnumWithEnumDeserializer() throws Exception {
        EnumSet<TestEnum> enumSet = EnumSet.allOf(TestEnum.class);
        StdKeyDeserializer deserializer = new EnumKD(context.getAnnotationIntrospector().findValueAnnotation(TestEnum[].class),
                new Method(TestEnum.class, "valueOf", new Class[]{String.class}));

        String key = TestEnum.TEST_ENUM_1.name();
        TestEnum actual = (TestEnum) deserializer.deserializeKey(key, context);
        assertEquals(TestEnum.TEST_ENUM_1, actual);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKeyMethod_InvalidEnumKey() throws Exception {
        StdKeyDeserializer deserializer = new EnumKD(context.getAnnotationIntrospector().findValueAnnotation(TestEnum[].class),
                new Method(TestEnum.class, "valueOf", new Class[]{String.class}));
        String key = "InvalidEnum";

        deserializer.deserializeKey(key, context);
    }

    @Test
    public void testDeserializeKeyMethod_CustomKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new CustomKeyDeserializer();
        DeserializationContext context = new TestDeserializationContext();
        String key = "test";

        String actual = (String) deserializer.deserializeKey(key, context);
        assertEquals("CustomizedValue", actual);
    }

    @Test
    public void testDeserializeKeyMethod_NullCustomKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class);
        DeserializationContext context = new TestDeserializationContext();

        assertNull(deserializer.deserializeKey(null, context));
    }

    private static class TestDeserializationContext extends TestBaseContext {
        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return new TestAnnotationIntrospector();
        }

        @JsonDeserialize(using = CustomKeyDeserializer.class)
        enum TestEnum {
            TEST_ENUM_1,
            TEST_ENUM_2,
            TEST_ENUM_3,
            TEST_ENUM_4,
            TEST_ENUM_5,
            TEST_ENUM_6,
            TEST_ENUM_7,
            TEST_ENUM_8;

            @JsonValue
            public String name() {
                return name().replace("_", "");
            }

            public static final TestEnum TEST_ENUM_1 = TEST_ENUM_1;
            public static final TestEnum TEST_ENUM_2 = TEST_ENUM_2;
            public static final TestEnum TEST_ENUM_3 = TEST_ENUM_3;
            public static final TestEnum TEST_ENUM_4 = TEST_ENUM_4;
            public static final TestEnum TEST_ENUM_5 = TEST_ENUM_5;
            public static final TestEnum TEST_ENUM_6 = TEST_ENUM_6;
            public static final TestEnum TEST_ENUM_7 = TEST_ENUM_7;
            public static final TestEnum TEST_ENUM_8 = TEST_ENUM_8;
        }
    }

    private static class TestAnnotationIntrospector extends TestBaseAnnotationIntrospector {
        @Override
        public AnnotationIntrospector findValueAnnotation(Class<?> componentType) {
            if (componentType == TestEnum[].class) {
                return this;
            }
            return null;
        }
    }

    private static class TestBaseContext extends MinimalBaseContext {
        @Override
        public ObjectReader getFactory() {
            return null;
        }
    }

    private static class TestBaseAnnotationIntrospector extends MinimalAnnotationIntrospector {
        @Override
        public AnnotationIntrospector findAnnotationIntrospector() {
            return this;
        }
    }

    private static class CustomKeyDeserializer extends StdKeyDeserializer {
        protected CustomKeyDeserializer() {
            super(-1, Object.class);
        }

        @Override
        public Object _parse(String key, DeserializationContext ctxt) throws Exception {
            return "CustomizedValue";
        }
    }
}