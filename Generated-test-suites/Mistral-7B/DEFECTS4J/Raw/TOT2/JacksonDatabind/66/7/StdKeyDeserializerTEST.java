package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        Object result = deserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = deserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);

        result = deserializer.deserializeKey("not a boolean", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        Object result = deserializer.deserializeKey("0", new TestContext());
        assertEquals((byte) 0, result);

        result = deserializer.deserializeKey("127", new TestContext());
        assertEquals((byte) 127, result);

        result = deserializer.deserializeKey("-128", new TestContext());
        assertEquals((byte) -128, result);

        result = deserializer.deserializeKey("256", new TestContext());
        assertNull(result);
    }

    @Test
    public void testShortType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        Object result = deserializer.deserializeKey("0", new TestContext());
        assertEquals((short) 0, result);

        result = deserializer.deserializeKey("32767", new TestContext());
        assertEquals((short) 32767, result);

        result = deserializer.deserializeKey("-32768", new TestContext());
        assertEquals((short) -32768, result);

        result = deserializer.deserializeKey("32768", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCharType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        Object result = deserializer.deserializeKey("'a'", new TestContext());
        assertEquals('a', result);

        result = deserializer.deserializeKey("'\u0061'", new TestContext());
        assertEquals('a', result);

        result = deserializer.deserializeKey("'z'", new TestContext());
        assertEquals('z', result);

        result = deserializer.deserializeKey("' '", new TestContext());
        assertNull(result);
    }

    @Test
    public void testIntType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        Object result = deserializer.deserializeKey("0", new TestContext());
        assertEquals(0, result);

        result = deserializer.deserializeKey("123", new TestContext());
        assertEquals(123, result);

        result = deserializer.deserializeKey("-123", new TestContext());
        assertEquals(-123, result);

        result = deserializer.deserializeKey("Integer.MAX_VALUE", new TestContext());
        assertEquals(Integer.MAX_VALUE, result);

        result = deserializer.deserializeKey("Integer.MIN_VALUE", new TestContext());
        assertEquals(Integer.MIN_VALUE, result);

        result = deserializer.deserializeKey("2147483648", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLongType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        Object result = deserializer.deserializeKey("0", new TestContext());
        assertEquals(0L, result);

        result = deserializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals(Long.MAX_VALUE, result);

        result = deserializer.deserializeKey("-9223372036854775808", new TestContext());
        assertEquals(Long.MIN_VALUE, result);

        result = deserializer.deserializeKey("9223372036854775808", new TestContext());
        assertNull(result);
    }

    @Test
    public void testFloatType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        Object result = deserializer.deserializeKey("0.0", new TestContext());
        assertEquals(0.0f, result, 0.001f);

        result = deserializer.deserializeKey("3.14", new TestContext());
        assertEquals(3.14f, result, 0.001f);

        result = deserializer.deserializeKey("-3.14", new TestContext());
        assertEquals(-3.14f, result, 0.001f);

        result = deserializer.deserializeKey("Inf", new TestContext());
        assertEquals(Float.POSITIVE_INFINITY, result);

        result = deserializer.deserializeKey("-Inf", new TestContext());
        assertEquals(Float.NEGATIVE_INFINITY, result);

        result = deserializer.deserializeKey("NaN", new TestContext());
        assertNotNull(result);
        assertEquals(Float.NaN, result);

        result = deserializer.deserializeKey("1e308", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDoubleType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        Object result = deserializer.deserializeKey("0.0", new TestContext());
        assertEquals(0.0, result, 0.00001);

        result = deserializer.deserializeKey("3.14", new TestContext());
        assertEquals(3.14, result, 0.00001);

        result = deserializer.deserializeKey("-3.14", new TestContext());
        assertEquals(-3.14, result, 0.00001);

        result = deserializer.deserializeKey("Inf", new TestContext());
        assertEquals(Double.POSITIVE_INFINITY, result);

        result = deserializer.deserializeKey("-Inf", new TestContext());
        assertEquals(Double.NEGATIVE_INFINITY, result);

        result = deserializer.deserializeKey("NaN", new TestContext());
        assertNotNull(result);
        assertEquals(Double.NaN, result);

        result = deserializer.deserializeKey("1e308", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLocaleType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class,
                FromStringDeserializer.findDeserializer(Locale.class));

        Object result = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);

        result = deserializer.deserializeKey("en_GB", new TestContext());
        assertEquals(Locale.ENGLISH, result);

        result = deserializer.deserializeKey("es_ES", new TestContext());
        assertEquals(Locale.SPAIN, result);

        result = deserializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRENCH, result);

        result = deserializer.deserializeKey("ja_JP", new TestContext());
        assertEquals(Locale.JAPAN, result);

        result = deserializer.deserializeKey("ru_RU", new TestContext());
        assertEquals(Locale.RUSSIA, result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCurrencyType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class,
                FromStringDeserializer.findDeserializer(Currency.class));

        Object result = deserializer.deserializeKey("USD", new TestContext());
        assertEquals(Currency.getInstance("USD"), result);

        result = deserializer.deserializeKey("EUR", new TestContext());
        assertEquals(Currency.getInstance("EUR"), result);

        result = deserializer.deserializeKey("JPY", new TestContext());
        assertEquals(Currency.getInstance("JPY"), result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDateType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        Object result = deserializer.deserializeKey("2019-08-30T14:30:00Z", new TestContext());
        assertEquals("Fri Aug 30 14:30:00 EDT 2019", result.toString());

        result = deserializer.deserializeKey("2019-08-30", new TestContext());
        assertEquals("Fri Aug 30 00:00:00 EDT 2019", result.toString());

        result = deserializer.deserializeKey("2019-08-31", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCalendarType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        Object result = deserializer.deserializeKey("2019-08-30T14:30:00Z", new TestContext());
        assertNotNull(result);
        assertEquals(Calendar.getInstance().getTime(), result.getTime());

        result = deserializer.deserializeKey("2019-08-30", new TestContext());
        assertNotNull(result);
        assertEquals(Calendar.getInstance().getTime(), result.getTime());

        result = deserializer.deserializeKey("2019-08-31", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURIType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        Object result = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), result);

        result = deserializer.deserializeKey("https://example.com/path", new TestContext());
        assertEquals(new URI("https://example.com/path"), result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURLType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        Object result = deserializer.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), result);

        result = deserializer.deserializeKey("http://example.com:8080/path", new TestContext());
        assertEquals(new URL("http://example.com:8080/path"), result);

        result = deserializer.deserializeKey("https://example.com", new TestContext());
        assertEquals(new URL("https://example.com"), result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testClassType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        Object result = deserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, result);

        result = deserializer.deserializeKey("java.util.ArrayList", new TestContext());
        assertEquals(ArrayList.class, result);

        result = deserializer.deserializeKey("java.util.HashMap", new TestContext());
        assertEquals(HashMap.class, result);

        result = deserializer.deserializeKey("java.io.File", new TestContext());
        assertEquals(File.class, result);

        result = deserializer.deserializeKey("java.util.List", new TestContext());
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        deserializer.deserializeKey("InvalidType", new TestContext());
    }
}