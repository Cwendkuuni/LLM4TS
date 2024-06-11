package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        Object result = deserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = deserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);

        result = deserializer.deserializeKey("not_boolean", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        Object result = deserializer.deserializeKey("1", new TestContext());
        assertEquals((byte) 1, result);

        result = deserializer.deserializeKey("127", new TestContext());
        assertEquals((byte) 127, result);

        result = deserializer.deserializeKey("256", new TestContext());
        assertNull(result);
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        Object result = deserializer.deserializeKey("1", new TestContext());
        assertEquals((short) 1, result);

        result = deserializer.deserializeKey("32767", new TestContext());
        assertEquals((short) 32767, result);

        result = deserializer.deserializeKey("32768", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        Object result = deserializer.deserializeKey("A", new TestContext());
        assertEquals((char) 'A', result);

        result = deserializer.deserializeKey(" ABC", new TestContext());
        assertNull(result);
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        Object result = deserializer.deserializeKey("1", new TestContext());
        assertEquals(1, result);

        result = deserializer.deserializeKey("-1", new TestContext());
        assertEquals(-1, result);

        result = deserializer.deserializeKey("2147483647", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        Object result = deserializer.deserializeKey("1", new TestContext());
        assertEquals(1L, result);

        result = deserializer.deserializeKey("-1", new TestContext());
        assertEquals(-1L, result);

        result = deserializer.deserializeKey("9223372036854775807", new TestContext());
        assertNull(result);
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        Object result = deserializer.deserializeKey("1.0", new TestContext());
        assertEquals(1.0f, result);

        result = deserializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals(3.14159265358979323846f, result, 0.00001f);

        result = deserializer.deserializeKey("NaN", new TestContext());
        assertEquals(Float.NaN, result);

        result = deserializer.deserializeKey("Infinity", new TestContext());
        assertEquals(Float.POSITIVE_INFINITY, result);

        result = deserializer.deserializeKey("-Infinity", new TestContext());
        assertEquals(Float.NEGATIVE_INFINITY, result);

        result = deserializer.deserializeKey("not_float", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        Object result = deserializer.deserializeKey("1.0", new TestContext());
        assertEquals(1.0, result, 0.0);

        result = deserializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals(3.14159265358979323846, result, 0.000000000000001);

        result = deserializer.deserializeKey("NaN", new TestContext());
        assertEquals(Double.NaN, result);

        result = deserializer.deserializeKey("Infinity", new TestContext());
        assertEquals(Double.POSITIVE_INFINITY, result);

        result = deserializer.deserializeKey("-Infinity", new TestContext());
        assertEquals(Double.NEGATIVE_INFINITY, result);

        result = deserializer.deserializeKey("not_double", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        Object result = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);

        result = deserializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, result);

        result = deserializer.deserializeKey("not_locale", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        Object result = deserializer.deserializeKey("2022-03-05", new TestContext());
        assertEquals(new Date(1646726400000L), result);

        result = deserializer.deserializeKey("2022-03-05T12:15:20.123Z", new TestContext());
        assertEquals(new Date(1646751920123L), result);

        result = deserializer.deserializeKey("not_date", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        Object result = deserializer.deserializeKey("2022-03-05", new TestContext());
        assertEquals(Calendar.getInstance().set(2022, Calendar.MARCH, 5), result);

        result = deserializer.deserializeKey("2022-03-05T12:15:20.123Z", new TestContext());
        assertEquals(Calendar.getInstance().setTime(new Date(1646751920123L)), result);

        result = deserializer.deserializeKey("not_date", new TestContext());
        assertNull(result);
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        Object result = deserializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), result);

        result = deserializer.deserializeKey("11111111-1111-1111-1111-111111111111", new TestContext());
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), result);

        result = deserializer.deserializeKey("not_uuid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURILegacyKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        Object result = deserializer.deserializeKey("http://www.example.com", new TestContext());
        assertEquals(new URL("http://www.example.com"), result);

        result = deserializer.deserializeKey("https://www.example.com:8080/path?query=value#fragment", new TestContext());
        assertEquals(new URL("https://www.example.com:8080/path?query=value#fragment"), result);

        result = deserializer.deserializeKey("not_uri", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURINewKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        Object result = deserializer.deserializeKey("http://www.example.com", new TestContext());
        assertEquals(new URI("http://www.example.com"), result);

        result = deserializer.deserializeKey("https://www.example.com:8080/path?query=value#fragment", new TestContext());
        assertEquals(new URI("https://www.example.com:8080/path?query=value#fragment"), result);

        result = deserializer.deserializeKey("not_uri", new TestContext());
        assertNull(result);
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Class.class);

        Object result = deserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, result);

        result = deserializer.deserializeKey("java.util.List", new TestContext());
        assertEquals(List.class, result);

        result = deserializer.deserializeKey("not_class", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCurrencyKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);

        Object result = deserializer.deserializeKey("USD", new TestContext());
        assertEquals(Currency.getInstance("USD"), result);

        result = deserializer.deserializeKey("EUR", new TestContext());
        assertEquals(Currency.getInstance("EUR"), result);

        result = deserializer.deserializeKey("not_currency", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteArrayKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(byte[].class);

        Object result = deserializer.deserializeKey("Rg==", new TestContext());
        assertEquals(new byte[]{65, 66, 67}, result);

        result = deserializer.deserializeKey("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWw=", new TestContext());
        assertEquals(new byte[]{86, 73, 83, 72, 65, 61, 74, 68, 72, 65, 73, 73}, result);

        result = deserializer.deserializeKey("not_byte_array", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDelegateKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(-1, String.class);

        Object result = deserializer.deserializeKey("key", new TestContext());
        assertEquals("key", result);
    }

    @Test
    public void testEnumKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new EnumKD(EnumResolver.constructUnsafe(MyEnum.class), null);

        Object result = deserializer.deserializeKey("VALUE_1", new TestContext());
        assertEquals(MyEnum.VALUE_1, result);

        result = deserializer.deserializeKey("VALUE_2", new TestContext());
        assertEquals(MyEnum.VALUE_2, result);

        result = deserializer.deserializeKey("not_value", new TestContext());
        assertNull(result);
    }

    @Test
    public void testStringConstructorKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StringCtorKeyDeserializer(String.class.getDeclaredConstructor(String.class));

        Object result = deserializer.deserializeKey("hello", new TestContext());
        assertEquals("hello", result);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        StdKeyDeserializer deserializer = new StringFactoryKeyDeserializer(String.class.getDeclaredMethod("valueOf", String.class));

        Object result = deserializer.deserializeKey("hello", new TestContext());
        assertEquals("hello", result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public Object findValueInAScope(String key, DeserializationFeature feature) {
            return super.findValueInAScope(key, feature);
        }
    }
}