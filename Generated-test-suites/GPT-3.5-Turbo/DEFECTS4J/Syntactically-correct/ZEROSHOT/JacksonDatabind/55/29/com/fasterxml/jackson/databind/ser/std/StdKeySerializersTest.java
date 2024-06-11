package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

@SuppressWarnings("serial")
public class StdKeySerializersTest {

    private StdKeySerializers keySerializers;
    private SerializationConfig config;
    private Class<?> rawKeyType;
    private boolean useDefault;

    @Before
    public void setUp() {
        keySerializers = new StdKeySerializers();
        config = new SerializationConfig();
        rawKeyType = Object.class;
        useDefault = true;
    }

    @Test
    public void testGetStdKeySerializer() {
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, new StdKeySerializer());
    }

    @Test
    public void testGetStdKeySerializerWithStringKeyType() {
        rawKeyType = String.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, keySerializers.DEFAULT_STRING_SERIALIZER);
    }

    @Test
    public void testGetStdKeySerializerWithPrimitiveKeyType() {
        rawKeyType = int.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, keySerializers.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testGetStdKeySerializerWithNumberKeyType() {
        rawKeyType = Double.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, keySerializers.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testGetStdKeySerializerWithClassKeyType() {
        rawKeyType = Class.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, rawKeyType));
    }

    @Test
    public void testGetStdKeySerializerWithDateKeyType() {
        rawKeyType = Date.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, rawKeyType));
    }

    @Test
    public void testGetStdKeySerializerWithCalendarKeyType() {
        rawKeyType = Calendar.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, rawKeyType));
    }

    @Test
    public void testGetStdKeySerializerWithUUIDKeyType() {
        rawKeyType = java.util.UUID.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(result, new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, rawKeyType));
    }

    @Test
    public void testGetStdKeySerializerWithNonMatchingKeyType() {
        rawKeyType = Boolean.class;
        JsonSerializer<Object> result = keySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        JsonSerializer<Object> result = keySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(result, keySerializers.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumKeyType() {
        rawKeyType = Enum.class;
        JsonSerializer<Object> result = keySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(result, new StdKeySerializers.Dynamic());
    }

    @Test
    public void testGetFallbackKeySerializerWithNonEnumKeyType() {
        rawKeyType = int.class;
        JsonSerializer<Object> result = keySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(result, new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, rawKeyType));
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = keySerializers.getDefault();
        assertEquals(result, keySerializers.DEFAULT_KEY_SERIALIZER);
    }

}