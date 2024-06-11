package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdKeyDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new JsonMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBoolean() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        Object key = serializer.deserializeKey("true", null);
        Assert.assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", null);
        Assert.assertEquals(Boolean.FALSE, key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testByte() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        Object key = serializer.deserializeKey("1", null);
        Assert.assertEquals((byte) 1, key);

        key = serializer.deserializeKey("-128", null);
        Assert.assertEquals((byte) -128, key);

        key = serializer.deserializeKey("256", null);
        Assert.assertNull(key);
    }

    @Test
    public void testShort() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        Object key = serializer.deserializeKey("12345", null);
        Assert.assertEquals((short) 12345, key);

        key = serializer.deserializeKey("-32768", null);
        Assert.assertEquals((short) -32768, key);

        key = serializer.deserializeKey("65536", null);
        Assert.assertNull(key);
    }

    @Test
    public void testChar() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        Object key = serializer.deserializeKey("A", null);
        Assert.assertEquals('A', key);

        key = serializer.deserializeKey("'\u0061'", null);
        Assert.assertEquals('a', key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testInt() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object key = serializer.deserializeKey("123456", null);
        Assert.assertEquals(123456, key);

        key = serializer.deserializeKey("-123456", null);
        Assert.assertEquals(-123456, key);

        key = serializer.deserializeKey("2147483648", null);
        Assert.assertNull(key);
    }

    @Test
    public void testLong() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        Object key = serializer.deserializeKey("12345678901234567890", null);
        Assert.assertEquals(12345678901234567890L, key);

        key = serializer.deserializeKey("-12345678901234567890", null);
        Assert.assertEquals(-12345678901234567890L, key);

        key = serializer.deserializeKey("9223372036854775808", null);
        Assert.assertNull(key);
    }

    @Test
    public void testFloat() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        Object key = serializer.deserializeKey("1.2345f", null);
        Assert.assertEquals(1.2345f, key);

        key = serializer.deserializeKey("-1.2345f", null);
        Assert.assertEquals(-1.2345f, key);

        key = serializer.deserializeKey("NaN", null);
        Assert.assertEquals(Float.NaN, key);

        key = serializer.deserializeKey("Inf", null);
        Assert.assertEquals(Float.POSITIVE_INFINITY, key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testDouble() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        Object key = serializer.deserializeKey("1.2345", null);
        Assert.assertEquals(1.2345, key);

        key = serializer.deserializeKey("-1.2345", null);
        Assert.assertEquals(-1.2345, key);

        key = serializer.deserializeKey("NaN", null);
        Assert.assertEquals(Double.NaN, key);

        key = serializer.deserializeKey("Inf", null);
        Assert.assertEquals(Double.POSITIVE_INFINITY, key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testLocale() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object key = serializer.deserializeKey("en_US", null);
        Assert.assertEquals(Locale.US, key);

        key = serializer.deserializeKey("es_ES", null);
        Assert.assertEquals(Locale.SPAIN, key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testDate() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);

        Object key = serializer.deserializeKey("2022-12-31T23:59:59.999Z", null);
        Assert.assertEquals(new java.util.Date(Long.parseLong("1671531199999")), key);

        key = serializer.deserializeKey("2022-12-31T23:59:59.999-05:00", null);
        Assert.assertEquals(new java.util.Date(Long.parseLong("1671457599999")), key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testCalendar() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Calendar.class);

        Object key = serializer.deserializeKey("2022-12-31", null);
        Map<String, Object> cal = new HashMap<>();
        cal.put("YEAR", 2022);
        cal.put("MONTH", 11);
        cal.put("DAY_OF_MONTH", 31);
        Assert.assertEquals(cal, key);

        key = serializer.deserializeKey("2022-12-31T23:59:59Z", null);
        cal.put("MILLISECOND", 999);
        Assert.assertEquals(cal, key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testUUID() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.UUID.class);

        Object key = serializer.deserializeKey("00000000-0000-0000-0000-000000000000", null);
        Assert.assertEquals(new java.util.UUID("00000000-0000-0000-0000-000000000000"), key);

        key = serializer.deserializeKey("11111111-1111-1111-1111-111111111111", null);
        Assert.assertEquals(new java.util.UUID("11111111-1111-1111-1111-111111111111"), key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testURI() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        Object key = serializer.deserializeKey("http://www.example.com", null);
        Assert.assertEquals(new URI("http://www.example.com"), key);

        key = serializer.deserializeKey("https://www.example.com/path/to/resource?query=param", null);
        Assert.assertEquals(new URI("https://www.example.com/path/to/resource?query=param"), key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testURL() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        Object key = serializer.deserializeKey("http://www.example.com", null);
        Assert.assertEquals(new URL("http://www.example.com"), key);

        key = serializer.deserializeKey("https://www.example.com/path/to/resource?query=param", null);
        Assert.assertEquals(new URL("https://www.example.com/path/to/resource?query=param"), key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testEnum() throws IOException {
        EnumSet<Weekday> days = EnumSet.allOf(Weekday.class);

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Weekday.class);

        Object key = serializer.deserializeKey("MONDAY", null);
        Assert.assertEquals(Weekday.MONDAY, key);

        key = serializer.deserializeKey("TUESDAY", null);
        Assert.assertEquals(Weekday.TUESDAY, key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testClass() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);

        Object key = serializer.deserializeKey("[Ljava.lang.String;", null);
        Assert.assertArrayEquals(new String[]{"one", "two", "three"}, key);

        key = serializer.deserializeKey("[Lcom.example.MyClass;", null);
        Assert.assertEquals(MyClass[].class, key.getClass());

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testByteArray() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(byte[].class);

        Object key = serializer.deserializeKey("Rg==", null);
        Assert.assertArrayEquals(new byte[]{72, 101, 108, 101, 114, 116}, key);

        key = serializer.deserializeKey("SgVsbG8gV29ybGQh", null);
        Assert.assertArrayEquals(new byte[]{83, 118, 86, 103, 115, 116, 99, 111, 110, 111, 115, 115, 104, 111, 114, 101, 97, 116, 97}, key);

        key = serializer.deserializeKey("invalid", null);
        Assert.assertNull(key);
    }

    @Test
    public void testEnumUsingToString() throws IOException {
        EnumSet<Weekday> days = EnumSet.allOf(Weekday.class);

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Weekday.class);

        Object mapper = new ObjectMapper();
        ObjectMapper jsonMapper = (ObjectMapper) mapper;
        jsonMapper.configure(SerializationFeature.READ_ENUMS_USING_TO_STRING, true);

        Object key = serializer.deserializeKey("monday", jsonMapper);
        Assert.assertEquals(Weekday.MONDAY, key);

        key = serializer.deserializeKey("tuesday", jsonMapper);
        Assert.assertEquals(Weekday.TUESDAY, key);

        key = serializer.deserializeKey("invalid", jsonMapper);
        Assert.assertNull(key);
    }

    private enum Weekday {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY;

        public String toString() {
            return name().toLowerCase();
        }
    }

    @Test
    public void testMalformedInput() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);

        try {
            serializer.deserializeKey("!@#$%^&*()_+-=[]{}|;:,.<>/?");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}