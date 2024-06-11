package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

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

@JacksonStdImpl
public class StdKeyDeserializerTest extends ObjectMapperTestBase {

    @Test
    public void testBooleanDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", getContext()));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", getContext()));
    }

    @Test
    public void testByteDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((byte) 10, deserializer.deserializeKey("10", getContext()));
        assertEquals((byte) -5, deserializer.deserializeKey("-5", getContext()));
        assertNull(deserializer.deserializeKey("200", getContext()));
    }

    @Test
    public void testShortDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((short) 10, deserializer.deserializeKey("10", getContext()));
        assertEquals((short) -5, deserializer.deserializeKey("-5", getContext()));
        assertNull(deserializer.deserializeKey("20000", getContext()));
    }

    @Test
    public void testCharDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals('a', deserializer.deserializeKey("a", getContext()));
        assertEquals('\\u00E9', deserializer.deserializeKey("\u00E9", getContext()));
        assertNull(deserializer.deserializeKey("B", getContext()));
    }

    @Test
    public void testIntDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(10, deserializer.deserializeKey("10", getContext()));
        assertEquals(-5, deserializer.deserializeKey("-5", getContext()));
        assertNull(deserializer.deserializeKey("2000000", getContext()));
    }

    @Test
    public void testLongDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(10L, deserializer.deserializeKey("10", getContext()));
        assertEquals(-5L, deserializer.deserializeKey("-5", getContext()));
        assertNull(deserializer.deserializeKey("2000000000000000000", getContext()));
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(10.0f, deserializer.deserializeKey("10.0", getContext()), 0.001F);
        assertEquals(-5.0f, deserializer.deserializeKey("-5.0", getContext()), 0.001F);
        assertNull(deserializer.deserializeKey("NaN", getContext()));
        assertNull(deserializer.deserializeKey("Infinity", getContext()));
        assertNull(deserializer.deserializeKey("-Infinity", getContext()));
    }

    @Test
    public void testDateDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Date date = deserializer.deserializeKey("2022-12-31", getContext());
        assertEquals("Fri Dec 31 00:00:00 CET 2022", date.toString());
        assertNull(deserializer.deserializeKey("InvalidDate", getContext()));
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = deserializer.deserializeKey("2022-12-31", getContext());
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertNull(deserializer.deserializeKey("InvalidDate", getContext()));
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        UUID uuid = deserializer.deserializeKey("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6", getContext());
        assertEquals("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6", uuid.toString());
        assertNull(deserializer.deserializeKey("InvalidUUID", getContext()));
    }

    @Test
    public void testURIDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        URI uri = deserializer.deserializeKey("http://example.com/path/to/resource", getContext());
        assertEquals("http", uri.getScheme());
        assertEquals("/path/to/resource", uri.getPath());
        assertNull(deserializer.deserializeKey("InvalidURI", getContext()));
    }

    @Test
    public void testURLDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        URL url = deserializer.deserializeKey("http://example.com/path/to/resource", getContext());
        assertEquals("http", url.getProtocol());
        assertEquals("/path/to/resource", url.getFile());
        assertNull(deserializer.deserializeKey("InvalidURL", getContext()));
    }

    @Test
    public void testClassDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertNotNull(deserializer.deserializeKey("java.lang.String", getContext()));
        assertNull(deserializer.deserializeKey("java.util.List", getContext()));

        StdKeyDeserializer stringClassDeserializer = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, stringClassDeserializer.deserializeKey("java.lang.String", getContext()));
        assertNull(stringClassDeserializer.deserializeKey("java.util.List", getContext()));

        StdKeyDeserializer listClassDeserializer = StdKeyDeserializer.forType(ArrayList.class);
        assertNull(listClassDeserializer.deserializeKey("java.lang.String", getContext()));
        assertNotNull(listClassDeserializer.deserializeKey("java.util.ArrayList", getContext()));
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Locale locale = deserializer.deserializeKey("en_US", getContext());
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("en_US", locale.toLanguageTag());
        assertNull(deserializer.deserializeKey("InvalidLocale", getContext()));
    }

    @Test
    public void testCurrencyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
        Currency currency = deserializer.deserializeKey("EUR", getContext());
        assertEquals("EUR", currency.getCurrencyCode());
        assertEquals("Euro", currency.getDisplayName(Locale.ENGLISH));
        assertNull(deserializer.deserializeKey("InvalidCurrency", getContext()));
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        EnumSet<DaysOfWeek> daysOfWeek = EnumSet.allOf(DaysOfWeek.class);
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, daysOfWeek.getClass());
        Enum<?> enumValue = deserializer.deserializeKey("SUNDAY", getContext());
        assertEquals(DaysOfWeek.SUNDAY, enumValue);
        enumValue = deserializer.deserializeKey("MONDAY", getContext());
        assertEquals(DaysOfWeek.MONDAY, enumValue);
        // Test unknown value
        enumValue = deserializer.deserializeKey("TUESDAY_FOO", getContext());
        assertNull(enumValue);
    }

    @Test
    public void testDelegatingKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(
                StdKeyDeserializer.TYPE_STRING, String.class) {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return super.delegate().deserializeKey(key, ctxt);
            }
        };
        assertNotNull(deserializer.deserializeKey("foo", getContext()));
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(
                MethodHandles.lookup().findStatic(String.class, "valueOf", String[].class), String.class) {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return super._parse(key, ctxt);
            }
        };
        assertEquals("foo", deserializer.deserializeKey("\"foo\"", getContext()));
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Constructor<String> stringConstructor = String.class.getConstructor(String.class);
        StdKeyDeserializer deserializer = new StdKeyDeserializer(stringConstructor, String.class) {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return super._parse(key, ctxt);
            }
        };
        assertEquals("foo", deserializer.deserializeKey("\"foo\"", getContext()));
    }
}