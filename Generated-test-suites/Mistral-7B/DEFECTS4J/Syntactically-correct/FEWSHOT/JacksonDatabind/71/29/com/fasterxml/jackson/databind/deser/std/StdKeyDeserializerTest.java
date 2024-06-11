package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerProvider;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

/**
 * Test cases for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testForType() {
        Class<?>[] types = new Class<?>[]{
                String.class, Object.class, Boolean.class, Byte.TYPE, Character.TYPE,
                Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE,
                Locale.class, Date.class, Calendar.class, UUID.class, URI.class, URL.class,
                Class.class, Currency.class, Enum.class
        };

        for (Class<?> type : types) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertEquals(StdKeyDeserializer.class, keyDeserializer.getClass());
            assertEquals(type, keyDeserializer.getKeyClass());
        }

        assertNull(StdKeyDeserializer.forType(Integer[].class));
        assertNull(StdKeyDeserializer.forType(Map.class));
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        assertEquals("foo", stringKD.deserializeKey("foo", null));

        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, booleanKD.deserializeKey("true", null));
        assertEquals(Boolean.FALSE, booleanKD.deserializeKey("false", null));

        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.TYPE);
        assertEquals((byte) 0, byteKD.deserializeKey("0", null));
        assertEquals((byte) 127, byteKD.deserializeKey("127", null));
        try {
            byteKD.deserializeKey("256", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.TYPE);
        assertEquals((short) 0, shortKD.deserializeKey("0", null));
        assertEquals((short) 127, shortKD.deserializeKey("127", null));
        try {
            shortKD.deserializeKey("32768", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.TYPE);
        assertEquals('\u0000', charKD.deserializeKey("\u0000", null));
        assertEquals('\u0026', charKD.deserializeKey("\u0026", null));

        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.TYPE);
        assertEquals(0, intKD.deserializeKey("0", null));
        assertEquals(123, intKD.deserializeKey("123", null));
        try {
            intKD.deserializeKey("-128", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.TYPE);
        assertEquals(0L, longKD.deserializeKey("0", null));
        assertEquals(123456789123456789L, longKD.deserializeKey("123456789123456789", null));
        try {
            longKD.deserializeKey("-9223372036854775808", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.TYPE);
        assertEquals(0.0f, floatKD.deserializeKey("0.0", null));
        assertEquals(123.456f, floatKD.deserializeKey("123.456", null));
        try {
            floatKD.deserializeKey("NaN", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.TYPE);
        assertEquals(0.0d, doubleKD.deserializeKey("0.0", null));
        assertEquals(123.456d, doubleKD.deserializeKey("123.456", null));
        try {
            doubleKD.deserializeKey("Inf", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.ROOT, localeKD.deserializeKey("", null));
        assertEquals(Locale.ENGLISH, localeKD.deserializeKey("en_US", null));
        assertEquals(Locale.FRENCH, localeKD.deserializeKey("fr_FR", null));

        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);
        assertEquals(new Date(0L), dateKD.deserializeKey("", null));
        assertEquals(new Date(1609459200000L), dateKD.deserializeKey("2020-11-01", null));
        try {
            dateKD.deserializeKey("2020-11-31", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        assertEquals(calendar, calendarKD.deserializeKey("2020-11-01", null));
        try {
            calendarKD.deserializeKey("2020-11-31", null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);
        assertEquals(URI.create(""), uriKD.deserializeKey("", null));
        assertEquals(URI.create("http://example.com"), uriKD.deserializeKey("http://example.com", null));

        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);
        assertEquals(new URL(""), urlKD.deserializeKey("", null));
        assertEquals(new URL("http://example.com"), urlKD.deserializeKey("http://example.com", null));

        StdKeyDeserializer enumKD = StdKeyDeserializer.forType(Enum.class);
        Enum e = Enum.valueOf(String.class, "FOO");
        assertEquals(e, enumKD.deserializeKey("FOO", null));

        EnumSet<MyEnum> expected = EnumSet.allOf(MyEnum.class);
        StdKeyDeserializer enumSetKD = StdKeyDeserializer.forType(EnumSet.class);
        assertEquals(expected, enumSetKD.deserializeKey(expected.toString(), null));

        String className = MyEnum.class.getName();
        StdKeyDeserializer enumMapKD = StdKeyDeserializer.forType(java.util.Map.class);
        java.util.Map<String, MyEnum> map = new java.util.LinkedHashMap<>();
        map.put("FOO", e);
        map.put("BAR", MyEnum.BAR);
        assertSame(map, enumMapKD.deserializeKey(className + "{FOO=FOO,BAR=BAR}", null));

        StdKeyDeserializer classKD = StdKeyDeserializer.forType(Class.class);
        assertSame(String.class, classKD.deserializeKey(String.class.getName(), null));

        StdKeyDeserializer currencyKD = StdKeyDeserializer.forType(Currency.class);
        assertEquals(Currency.getInstance("USD"), currencyKD.deserializeKey("USD", null));
    }
}

class MyEnum {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "name")
    enum Foo {
        FOO, BAR
    }
}