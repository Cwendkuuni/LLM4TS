package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testDefaultConstruction() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, keyDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializer.getType());
        assertNull(keyDeserializer._deser);
    }

    @Test
    public void testConstructionWithDeserializer() {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, localeDeserializer);
        assertEquals(Locale.class, keyDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_LOCALE, keyDeserializer.getType());
        assertInstanceOf(FromStringDeserializer.class, keyDeserializer._deser);
    }

    @Test
    public void testForTypeMethod() {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        assertEquals(StringKD.class, keyDeserializer.getClass());
        keyDeserializer = StdKeyDeserializer.forType(Object.class);
        assertEquals(StringKD.class, keyDeserializer.getClass());
        keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        assertEquals(StdKeyDeserializer.TYPE_UUID, keyDeserializer.getType());
        keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(StdKeyDeserializer.TYPE_INT, keyDeserializer.getType());
        keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(StdKeyDeserializer.TYPE_LOCALE, keyDeserializer.getType());
        keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        assertEquals(StdKeyDeserializer.TYPE_CURRENCY, keyDeserializer.getType());
    }

    @Test
    public void testDeserializeKeyMethodWithNullKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertNull(keyDeserializer.deserializeKey(null, DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithBooleanKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", DeserializationContext.empty()));
        assertEquals(Boolean.FALSE, keyDeserializer.deserializeKey("false", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithByteKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals((byte) 5, keyDeserializer.deserializeKey("5", DeserializationContext.empty()));
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("-128", DeserializationContext.empty()));
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("256", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithShortKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        assertEquals((short) 10, keyDeserializer.deserializeKey("10", DeserializationContext.empty()));
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("-32768", DeserializationContext.empty()));
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("32768", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithCharKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        assertEquals('A', keyDeserializer.deserializeKey("A", DeserializationContext.empty()));
        assertEquals('_', keyDeserializer.deserializeKey("\u005F", DeserializationContext.empty()));
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithIntKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(10, keyDeserializer.deserializeKey("10", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithLongKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(10L, keyDeserializer.deserializeKey("10", DeserializationContext.empty()));
        assertEquals(1000L, keyDeserializer.deserializeKey("1000", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithFloatKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        assertEquals(1.0f, keyDeserializer.deserializeKey("1.0", DeserializationContext.empty()));
        assertEquals(1.5f, keyDeserializer.deserializeKey("1.5", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithDoubleKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(1.0, keyDeserializer.deserializeKey("1.0", DeserializationContext.empty()));
        assertEquals(1.5, keyDeserializer.deserializeKey("1.5", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithLocaleKey() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, localeDeserializer);
        assertEquals(Locale.GERMANY, keyDeserializer.deserializeKey("de_DE", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithCurrencyKey() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, currencyDeserializer);
        assertEquals(Currency.getInstance("EUR"), keyDeserializer.deserializeKey("EUR", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithDateKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        assertEquals(new Date(), keyDeserializer.deserializeKey("2023-02-19", DeserializationContext.empty()));
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey("2023-02-31", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithCalendarKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.FEBRUARY, 19);
        assertEquals(calendar, keyDeserializer.deserializeKey("2023-02-19", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithUuidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        assertEquals(uuid, keyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithUriKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        URI uri = URI.create("https://example.com/resource");
        assertEquals(uri, keyDeserializer.deserializeKey("https://example.com/resource", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithUrlKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        URL url = new URL("https://example.com/resource");
        assertEquals(url, keyDeserializer.deserializeKey("https://example.com/resource", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithClassKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        assertThrows(IllegalArgumentException.class, () -> keyDeserializer.deserializeKey(String.class.getName(), DeserializationContext.empty()));
        assertEquals(String.class, keyDeserializer.deserializeKey("java.lang.String", DeserializationContext.empty()));
    }

    @Test
    public void testDeserializeKeyMethodWithUnknownTypeKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertThrows(IOException.class, () -> keyDeserializer.deserializeKey("X", DeserializationContext.empty()));
    }
}