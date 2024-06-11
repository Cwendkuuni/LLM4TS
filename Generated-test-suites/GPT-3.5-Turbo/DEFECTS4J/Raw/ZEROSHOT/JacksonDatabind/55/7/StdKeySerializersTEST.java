package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        StdKeySerializers.Default dateKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        StdKeySerializers.Default calendarKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        StdKeySerializers.Default classKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        StdKeySerializers.Default toStringKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class);

        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;

        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(result);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

        rawKeyType = Enum.class;

        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(result);
        assertEquals(defaultKeySerializer, result);

        rawKeyType = Date.class;

        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(result);
        assertEquals(dateKeySerializer, result);

        rawKeyType = Calendar.class;

        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(result);
        assertEquals(calendarKeySerializer, result);

        rawKeyType = Class.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(result);
        assertEquals(classKeySerializer, result);

        rawKeyType = java.util.UUID.class;
        result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertNotNull(result);
        assertEquals(toStringKeySerializer, result);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);

        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Enum.class;

        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(result);
        assertEquals(defaultKeySerializer, result);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertNotNull(result);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
}