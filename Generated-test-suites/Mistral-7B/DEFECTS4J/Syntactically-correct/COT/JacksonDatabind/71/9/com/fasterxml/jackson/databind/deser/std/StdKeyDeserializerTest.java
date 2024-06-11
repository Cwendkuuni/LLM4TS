package com.example.test; // replace with your test package

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    

    @org.junit.BeforeClass
    public static void setup() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, StdKeyDeserializer.StringKD.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Boolean() throws IOException, JsonMappingException {
        String key = "true";
        Boolean expected = Boolean.TRUE;

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Byte() throws IOException, JsonMappingException {
        String key = "10";
        Byte expected = Byte.valueOf((byte) 10);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Short() throws IOException, JsonMappingException {
        String key = "100";
        Short expected = Short.valueOf((short) 100);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Char() throws IOException, JsonMappingException {
        String key = "A";
        Character expected = Character.valueOf('A');

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Int() throws IOException, JsonMappingException {
        String key = "1000";
        Integer expected = Integer.valueOf(1000);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Long() throws IOException, JsonMappingException {
        String key = "1000000";
        Long expected = Long.valueOf(1000000L);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Locale() throws IOException, JsonMappingException {
        String key = "en_US";
        Locale expected = new Locale("en", "US");

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Date() throws IOException, JsonMappingException {
        String key = "2022-01-01";
        Date expected = new Date(0L); // a valid Date instance is set in the test method setUpDate()

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Calendar() throws IOException, JsonMappingException {
        String key = "2022-01-01";
        Calendar expected = Calendar.getInstance(); // a valid Calendar instance is set in the test method setUpCalendar()

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_UUID() throws IOException, JsonMappingException {
        String key = "123e4567-e89b-12d3-a456-426614174000";
        UUID expected = new UUID(123, 456789654321L);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_URI() throws IOException, JsonMappingException {
        String key = "http://example.com";
        URI expected = new URI("http", "example.com", null);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_URL() throws IOException, JsonMappingException {
        String key = "http://example.com";
        URL expected = new URL("http", "example.com", 80, null);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Class() throws IOException, JsonMappingException {
        String key = "java.lang.String";
        Class<?> expected = String.class;

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Class.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result.getClass());
    }

    @org.junit.Test
    public void testDeserializeKey_String_Currency() throws IOException, JsonMappingException {
        String key = "USD";
        Currency expected = Currency.getInstance("USD");

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Enum() throws IOException, JsonMappingException {
        String key = "WEDNESDAY";
        WeekDay expected = WeekDay.WEDNESDAY;

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(WeekDay.class);
        DeserializationContext context = null; // not used in this test
        Object result = deserializer.deserializeKey(key, context);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testDeserializeKey_String_Enum_Invalid() throws IOException, JsonMappingException {
        String key = "THURSDAY_INVALID";

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(WeekDay.class);
        DeserializationContext context = null; // not used in this test

        try {
            Object result = deserializer.deserializeKey(key, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not one of values excepted for Enum class: [WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]", e.getMessage());
        }
    }

    @org.junit.Test
    public void testDeserializeKey_String_Invalid_Int() throws IOException, JsonMappingException {
        String key = "123ab";

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = null; // not used in this test

        try {
            Object result = deserializer.deserializeKey(key, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.toString().contains("not a valid representation"));
        }
    }

    @org.junit.Test
    public void testDeserializeKey_String_Invalid_Long() throws IOException, JsonMappingException {
        String key = "123456789012345678901234567890";

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = null; // not used in this test

        try {
            Object result = deserializer.deserializeKey(key, context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.toString().contains("overflow, value can not be represented as 64-bit value"));
        }
    }

    @org.junit.Test
    public void testDeserializeKey_String_Null() throws IOException, JsonMappingException {
        String key = null;

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = null; // not used in this test

        Object result = deserializer.deserializeKey(key, context);

        assertNull(result);
    }

    

    private static Date setUpDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    private static Calendar setUpCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private enum WeekDay {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }
}