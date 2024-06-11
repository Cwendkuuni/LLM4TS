package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBooleanType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("true", mapper.getDeserializationContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        key = keyDeserializer.deserializeKey("false", mapper.getDeserializationContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.FALSE, key);

        key = keyDeserializer.deserializeKey("notBoolean", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testByteType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = keyDeserializer.deserializeKey("127", mapper.getDeserializationContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) 127, key);

        key = keyDeserializer.deserializeKey("-128", mapper.getDeserializationContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) -128, key);

        key = keyDeserializer.deserializeKey("256", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testShortType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = keyDeserializer.deserializeKey("32767", mapper.getDeserializationContext());
        assertTrue(key instanceof Short);
        assertEquals((short) 32767, key);

        key = keyDeserializer.deserializeKey("-32768", mapper.getDeserializationContext());
        assertTrue(key instanceof Short);
        assertEquals((short) -32768, key);

        key = keyDeserializer.deserializeKey("65536", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testCharType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = keyDeserializer.deserializeKey("A", mapper.getDeserializationContext());
        assertTrue(key instanceof Character);
        assertEquals('A', key);

        key = keyDeserializer.deserializeKey("\\u0061", mapper.getDeserializationContext());
        assertTrue(key instanceof Character);
        assertEquals('a', key);

        key = keyDeserializer.deserializeKey("notChar", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testIntType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = keyDeserializer.deserializeKey("123456", mapper.getDeserializationContext());
        assertTrue(key instanceof Integer);
        assertEquals(123456, key);

        key = keyDeserializer.deserializeKey("-123456", mapper.getDeserializationContext());
        assertTrue(key instanceof Integer);
        assertEquals(-123456, key);

        key = keyDeserializer.deserializeKey("2147483648", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testLongType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = keyDeserializer.deserializeKey("9223372036854775807", mapper.getDeserializationContext());
        assertTrue(key instanceof Long);
        assertEquals(9223372036854775807L, key);

        key = keyDeserializer.deserializeKey("-9223372036854775808", mapper.getDeserializationContext());
        assertTrue(key instanceof Long);
        assertEquals(-9223372036854775808L, key);

        key = keyDeserializer.deserializeKey("18446744073709551617", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testFloatType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = keyDeserializer.deserializeKey("3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117068", mapper.getDeserializationContext());
        assertTrue(key instanceof Float);
        assertEquals(3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117068f, key);

        key = keyDeserializer.deserializeKey("NAN", mapper.getDeserializationContext());
        assertTrue(key instanceof Float);
        assertEquals(Float.NaN, key);

        key = keyDeserializer.deserializeKey("INF", mapper.getDeserializationContext());
        assertTrue(key instanceof Float);
        assertEquals(Float.POSITIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("-INF", mapper.getDeserializationContext());
        assertTrue(key instanceof Float);
        assertEquals(Float.NEGATIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("notFloat", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testDoubleType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = keyDeserializer.deserializeKey("6.2831853071862866443707811024", mapper.getDeserializationContext());
        assertTrue(key instanceof Double);
        assertEquals(6.2831853071862866443707811024, key);

        key = keyDeserializer.deserializeKey("NAN", mapper.getDeserializationContext());
        assertTrue(key instanceof Double);
        assertEquals(Double.NaN, key);

        key = keyDeserializer.deserializeKey("INF", mapper.getDeserializationContext());
        assertTrue(key instanceof Double);
        assertEquals(Double.POSITIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("-INF", mapper.getDeserializationContext());
        assertTrue(key instanceof Double);
        assertEquals(Double.NEGATIVE_INFINITY, key);

        key = keyDeserializer.deserializeKey("notDouble", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testLocaleType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = keyDeserializer.deserializeKey("en_US", mapper.getDeserializationContext());
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);

        key = keyDeserializer.deserializeKey("es_ES", mapper.getDeserializationContext());
        assertTrue(key instanceof Locale);
        assertEquals(Locale.SPAIN, key);

        key = keyDeserializer.deserializeKey("notLocale", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testDateType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object key = keyDeserializer.deserializeKey("2019-07-15", mapper.getDeserializationContext());
        assertTrue(key instanceof Date);
        assertEquals(new Date(1563532800000L), key);

        key = keyDeserializer.deserializeKey("notDate", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testCalendarType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = keyDeserializer.deserializeKey("2019-07-15", mapper.getDeserializationContext());
        assertTrue(key instanceof Calendar);
        Calendar calendar = (Calendar) key;
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, calendar.get(Calendar.MONTH));
        assertEquals(15, calendar.get(Calendar.DAY_OF_MONTH));

        key = keyDeserializer.deserializeKey("notCalendar", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testUUIDType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = keyDeserializer.deserializeKey("12b34567-12d3-4567-a1b2-c3d4e5f6g7h8", mapper.getDeserializationContext());
        assertTrue(key instanceof UUID);
        assertEquals(new UUID("12b34567-12d3-4567-a1b2-c3d4e5f6g7h8"), key);

        key = keyDeserializer.deserializeKey("notUUID", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testURIType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = keyDeserializer.deserializeKey("http://www.example.com", mapper.getDeserializationContext());
        assertTrue(key instanceof URI);
        assertEquals(new URI("http://www.example.com"), key);

        key = keyDeserializer.deserializeKey("notURI", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testURLType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = keyDeserializer.deserializeKey("http://www.example.com", mapper.getDeserializationContext());
        assertTrue(key instanceof URL);
        assertEquals(new URL("http://www.example.com"), key);

        key = keyDeserializer.deserializeKey("notURL", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testClassType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        Object key = keyDeserializer.deserializeKey("[Ljava.lang.String;", mapper.getDeserializationContext());
        assertTrue(key instanceof Object);
        assertTrue(key.getClass().isArray());
        assertTrue(key.getClass().getComponentType() == String.class);

        String[] expectedArray = {"test1", "test2"};
        key = keyDeserializer.deserializeKey("[Ljava.lang.String;", mapper.getDeserializationContext(), expectedArray);
        assertEquals(expectedArray, (Object) key);

        key = keyDeserializer.deserializeKey("notClass", mapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testCurrencyType() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        Object key = keyDeserializer.deserializeKey("USD", mapper.getDeserializationContext());
        assertTrue(key instanceof Currency);
        assertEquals(Currency.getInstance("USD"), key);

        key = keyDeserializer.deserializeKey("EUR", mapper.getDeserializationContext());
        assertTrue(key instanceof Currency);
        assertEquals(Currency.getInstance("EUR"), key);

        key = keyDeserializer.deserializeKey("notCurrency", mapper.getDeserializationContext());
        assertNull(key);
    }
}