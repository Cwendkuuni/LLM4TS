package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class StdKeySerializersTest {

    @Test
    public void testDefaultKeySerializer() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        StdKeySerializers serializers = StdKeySerializers.getDefault();
        JsonSerializer<Object> serializer = mapper.getSerializationConfig().getDefaultSerializer();

        // Test with int
        int key = 1;
        String expected = Integer.toString(key);
        JsonNode actual = mapper.writeValueAsTree(key, serializer);
        JsonNode expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with String
        String stringKey = "test";
        expected = stringKey;
        actual = mapper.writeValueAsTree(stringKey, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Date
        Date date = new Date();
        expected = StdKeySerializers.DEFAULT_STRING_SERIALIZER.serializeValuesAsStrings()
                .format(date);
        actual = mapper.writeValueAsTree(date, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Calendar
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        expected = Long.toString(timeInMillis);
        actual = mapper.writeValueAsTree(calendar, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Enum
        Enum testEnum = Enum.valueOf(TestEnum.class, "TEST");
        expected = testEnum.name();
        actual = mapper.writeValueAsTree(testEnum, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);
    }

    @Test
    public void testGetStdKeySerializer() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        // Test with null
        Class<?> rawKeyType = null;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(),
                rawKeyType, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with String
        rawKeyType = String.class;
        serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, true);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test with int
        rawKeyType = Integer.class;
        serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Class
        rawKeyType = Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, true);
        assertEquals(StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType),
                serializer);

        // Test with Date
        rawKeyType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, true);
        assertEquals(StdKeySerializers.getDefault(), serializer);

        // Test with Calendar
        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, true);
        assertEquals(StdKeySerializers.getDefault(), serializer);

        // Test with UUID
        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, true);
        assertEquals(StdKeySerializers.getDefault(), serializer);

        // Test with custom class
        rawKeyType = CustomClass.class;
        serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), rawKeyType, true);
        assertEquals(StdKeySerializers.getDefault(), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        // Test with null
        Class<?> rawKeyType = null;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(),
                rawKeyType);
        assertNull(serializer);

        // Test with String
        rawKeyType = String.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        // Test with int
        rawKeyType = Integer.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        // Test with Enum
        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
        assertEquals(StdKeySerializers.getDefault(), serializer);

        // Test with custom class
        rawKeyType = CustomClass.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), rawKeyType);
        assertEquals(StdKeySerializers.getDefault(), serializer);
    }

    @Test
    public void testDefaultSerializationFeature() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        StdKeySerializers serializers = StdKeySerializers.getDefault();
        JsonSerializer<Object> serializer = mapper.getSerializationConfig().getDefaultSerializer();

        // Test with int
        int key = 1;
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        String expected = Integer.toString(key);
        JsonNode actual = mapper.writeValueAsTree(key, serializer);
        JsonNode expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with String
        String stringKey = "test";
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        expected = stringKey;
        actual = mapper.writeValueAsTree(stringKey, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Date
        Date date = new Date();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS_0, false);
        expected = StdKeySerializers.DEFAULT_STRING_SERIALIZER.serializeValuesAsStrings()
                .format(date);
        actual = mapper.writeValueAsTree(date, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Calendar
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS_0, false);
        expected = Long.toString(timeInMillis);
        actual = mapper.writeValueAsTree(calendar, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Enum
        Enum testEnum = Enum.valueOf(TestEnum.class, "TEST");
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        expected = testEnum.name();
        actual = mapper.writeValueAsTree(testEnum, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);
    }

    @Test
    public void testDynamicKeySerializer() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        StdKeySerializers serializers = StdKeySerializers.getDefault();

        // Test with int
        int key = 1;
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.registerModule(new SimpleModule());
        JsonSerializer<Object> intSerializer = mapper2.getSerializationConfig().findValueSerializer(int.class);
        JsonSerializer<Object> serializer = serializers.getFallbackKeySerializer(mapper2.getSerializationConfig(),
                int.class);
        assertEquals(intSerializer, serializer);

        String expectedInt = Integer.toString(key);
        JsonNode actualInt = mapper2.writeValueAsTree(key, serializer);
        JsonNode expectedNodeInt = mapper2.valueToTree(expectedInt);
        assertEquals(expectedNodeInt, actualInt);

        // Test with String
        String stringKey = "test";
        ObjectMapper mapper3 = new ObjectMapper();
        mapper3.registerModule(new SimpleModule());
        JsonSerializer<Object> stringSerializer = mapper3.getSerializationConfig().findValueSerializer(String.class);
        serializer = serializers.getFallbackKeySerializer(mapper3.getSerializationConfig(), String.class);
        assertEquals(stringSerializer, serializer);

        expected = stringKey;
        JsonNode actualString = mapper3.writeValueAsTree(stringKey, serializer);
        JsonNode expectedNodeString = mapper3.valueToTree(expected);
        assertEquals(expectedNodeString, actualString);

        // Test with Date
        Date date = new Date();
        ObjectMapper mapper4 = new ObjectMapper();
        mapper4.registerModule(new SimpleModule());
        JsonSerializer<Object> dateSerializer = mapper4.getSerializationConfig().findValueSerializer(Date.class);
        serializer = serializers.getFallbackKeySerializer(mapper4.getSerializationConfig(), Date.class);
        assertEquals(dateSerializer, serializer);

        expected = StdKeySerializers.DEFAULT_STRING_SERIALIZER.serializeValuesAsStrings()
                .format(date);
        JsonNode actualDate = mapper4.writeValueAsTree(date, serializer);
        JsonNode expectedNodeDate = mapper4.valueToTree(expected);
        assertEquals(expectedNodeDate, actualDate);

        // Test with Calendar
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        ObjectMapper mapper5 = new ObjectMapper();
        mapper5.registerModule(new SimpleModule());
        JsonSerializer<Object> calendarSerializer = mapper5.getSerializationConfig().findValueSerializer(Calendar.class);
        serializer = serializers.getFallbackKeySerializer(mapper5.getSerializationConfig(), Calendar.class);
        assertEquals(calendarSerializer, serializer);

        expected = Long.toString(timeInMillis);
        JsonNode actualCalendar = mapper5.writeValueAsTree(calendar, serializer);
        JsonNode expectedNodeCalendar = mapper5.valueToTree(expected);
        assertEquals(expectedNodeCalendar, actualCalendar);

        // Test with Enum
        Enum testEnum = Enum.valueOf(TestEnum.class, "TEST");
        ObjectMapper mapper6 = new ObjectMapper();
        mapper6.registerModule(new SimpleModule());
        JsonSerializer<Object> enumSerializer = mapper6.getSerializationConfig().findValueSerializer(testEnum.getClass());
        serializer = serializers.getFallbackKeySerializer(mapper6.getSerializationConfig(), testEnum.getClass());
        assertEquals(enumSerializer, serializer);

        expected = testEnum.name();
        JsonNode actualEnum = mapper6.writeValueAsTree(testEnum, serializer);
        JsonNode expectedNodeEnum = mapper6.valueToTree(expected);
        assertEquals(expectedNodeEnum, actualEnum);
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        StdKeySerializers serializers = StdKeySerializers.getDefault();

        // Test with int
        int key = 1;
        JsonSerializer<Object> serializer = StdKeySerializers.getStringKeySerializer();
        String expected = Integer.toString(key);
        JsonNode actual = mapper.writeValueAsTree(key, serializer);
        JsonNode expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with String
        String stringKey = "test";
        serializer = StdKeySerializers.getStringKeySerializer();
        expected = stringKey;
        actual = mapper.writeValueAsTree(stringKey, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Date
        Date date = new Date();
        expected = StdKeySerializers.DEFAULT_STRING_SERIALIZER.serializeValuesAsStrings()
                .format(date);
        serializer = StdKeySerializers.getStringKeySerializer();
        actual = mapper.writeValueAsTree(date, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Calendar
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();
        expected = Long.toString(timeInMillis);
        serializer = StdKeySerializers.getStringKeySerializer();
        actual = mapper.writeValueAsTree(calendar, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);

        // Test with Enum
        Enum testEnum = Enum.valueOf(TestEnum.class, "TEST");
        expected = testEnum.name();
        serializer = StdKeySerializers.getStringKeySerializer();
        actual = mapper.writeValueAsTree(testEnum, serializer);
        expectedNode = mapper.valueToTree(expected);
        assertEquals(expectedNode, actual);
    }

    enum TestEnum {
        TEST
    }

    static class CustomClass {

    }
}