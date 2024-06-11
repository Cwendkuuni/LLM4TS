package com.example; // replace with your test package

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    public StdKeyDeserializerTest() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.FAIL_ON_EMPTY_Beans);
    }

    @Test
    public void testForType() throws Exception {
        Class<?> stringClass = String.class;
        StdKeyDeserializer stringStdKeyDeserializer = StdKeyDeserializer.forType(stringClass);
        assertEquals(stringClass, stringStdKeyDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_STRING, stringStdKeyDeserializer._kind);

        // Test other types
        Class<?> booleanClass = Boolean.class;
        StdKeyDeserializer booleanStdKeyDeserializer = StdKeyDeserializer.forType(booleanClass);
        assertEquals(booleanClass, booleanStdKeyDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_BOOLEAN, booleanStdKeyDeserializer._kind);

        Class<?> intClass = Integer.class;
        StdKeyDeserializer intStdKeyDeserializer = StdKeyDeserializer.forType(intClass);
        assertEquals(intClass, intStdKeyDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_INT, intStdKeyDeserializer._kind);

        Class<?> doubleClass = Double.class;
        StdKeyDeserializer doubleStdKeyDeserializer = StdKeyDeserializer.forType(doubleClass);
        assertEquals(doubleClass, doubleStdKeyDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_DOUBLE, doubleStdKeyDeserializer._kind);

        Class<?> dateClass = Date.class;
        StdKeyDeserializer dateStdKeyDeserializer = StdKeyDeserializer.forType(dateClass);
        assertEquals(dateClass, dateStdKeyDeserializer.getKeyClass());
        assertEquals(StdKeyDeserializer.TYPE_DATE, dateStdKeyDeserializer._kind);
    }

    @Test
    public void testDeserializeKey() throws IOException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();

        StdKeyDeserializer intStdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        String intKey = "42";
        Object deserializedKey = intStdKeyDeserializer.deserializeKey(intKey, new DeserializationContext(objectMapper));
        assertEquals(Integer.valueOf(42), deserializedKey);

        String booleanKey = "true";
        deserializedKey = intStdKeyDeserializer.deserializeKey(booleanKey, new DeserializationContext(objectMapper));
        assertEquals(Boolean.TRUE, deserializedKey);

        String invalidBooleanKey = "invalid";
        try {
            intStdKeyDeserializer.deserializeKey(invalidBooleanKey, new DeserializationContext(objectMapper));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
            assertEquals("not a valid representation: not a Boolean", e.getMessage());
        }
    }

    @Test
    public void testParse() throws IOException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();

        StdKeyDeserializer intStdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        String intKey = "42";
        Object deserializedKey = intStdKeyDeserializer._parse(intKey, new DeserializationContext(objectMapper));
        assertEquals(Integer.valueOf(42), deserializedKey);

        String invalidIntKey = "invalid";
        try {
            intStdKeyDeserializer._parse(invalidIntKey, new DeserializationContext(objectMapper));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
            assertEquals("not a valid representation: not an integer", e.getMessage());
        }

        // Test other types
        String booleanKey = "true";
        StdKeyDeserializer booleanStdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        Object deserializedBooleanKey = booleanStdKeyDeserializer._parse(booleanKey, new DeserializationContext(objectMapper));
        assertEquals(Boolean.TRUE, deserializedBooleanKey);

        String invalidBooleanKey = "invalid";
        try {
            booleanStdKeyDeserializer._parse(invalidBooleanKey, new DeserializationContext(objectMapper));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
            assertEquals("not a valid representation: not a Boolean", e.getMessage());
        }

        String dateString = "2022-01-01";
        StdKeyDeserializer dateStdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        Object deserializedDate = dateStdKeyDeserializer._parse(dateString, new DeserializationContext(objectMapper));
        assertEquals(new Date(0L), deserializedDate); // assumes the Date constructor with no arguments sets it to Jan 1, 1970

        String invalidDateString = "invalid";
        try {
            dateStdKeyDeserializer._parse(invalidDateString, new DeserializationContext(objectMapper));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
            assertEquals("not a valid representation: cannot construct java.util.Date from value 'invalid'", e.getMessage());
        }
    }

    @Test
    public void testParseInt() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        StdKeyDeserializer intStdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        String intKey1 = "42";
        int parsedInt = intStdKeyDeserializer._parseInt(intKey1);
        assertEquals(42, parsedInt);

        String intKey2 = "2147483647";
        try {
            intStdKeyDeserializer._parseInt(intKey2);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
            assertEquals("For input string: \"2147483647\"", e.getMessage());
        }

        String intKey3 = "2147483648";
        try {
            intStdKeyDeserializer._parseInt(intKey3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
            assertEquals("For input string: \"2147483648\"", e.getMessage());
        }
    }

    @Test
    public void testParseLong() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        StdKeyDeserializer longStdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);

        String longKey1 = "42";
        long parsedLong = longStdKeyDeserializer._parseLong(longKey1);
        assertEquals(42L, parsedLong);

        String longKey2 = "9223372036854775807";
        try {
            longStdKeyDeserializer._parseLong(longKey2);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
            assertEquals("For input string: \"9223372036854775807\"", e.getMessage());
        }

        String longKey3 = "9223372036854775808";
        try {
            longStdKeyDeserializer._parseLong(longKey3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
            assertEquals("For input string: \"9223372036854775808\"", e.getMessage());
        }
    }

    @Test
    public void testParseDouble() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        StdKeyDeserializer doubleStdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);

        String doubleKey1 = "42.0";
        double parsedDouble = doubleStdKeyDeserializer._parseDouble(doubleKey1);
        assertEquals(42.0D, parsedDouble, 0.0);

        String doubleKey2 = "3.1415926535897932384626433832795";
        double parsedDouble2 = doubleStdKeyDeserializer._parseDouble(doubleKey2);
        assertEquals(3.1415926535897932384626433832795D, parsedDouble2, 0.0);

        String doubleKey3 = "invalid";
        try {
            doubleStdKeyDeserializer._parseDouble(doubleKey3);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
            assertEquals("For input string: \"invalid\"", e.getMessage());
        }
    }

    @Test
    public void testDeserializeKeyWithDelegate() throws IOException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = "{\"someKey\": \"someValue\"}";
        ObjectMapper delegateObjectMapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = delegateObjectMapper.findValueDeserializer(String.class);

        StdKeyDeserializer stringDelegateKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        StdKeyDeserializer stringDelegateKeyDeserializerWithDelegate = new DelegatingKD(String.class, stringDeserializer);

        String key = "someKey";
        Object deserializedKeyWithDelegate = stringDelegateKeyDeserializerWithDelegate.deserializeKey(key, new DeserializationContext(objectMapper));
        assertEquals("someValue", deserializedKeyWithDelegate);

        Object deserializedKey = stringDelegateKeyDeserializer.deserializeKey(key, new DeserializationContext(objectMapper));
        assertEquals(key, deserializedKey);
    }
}