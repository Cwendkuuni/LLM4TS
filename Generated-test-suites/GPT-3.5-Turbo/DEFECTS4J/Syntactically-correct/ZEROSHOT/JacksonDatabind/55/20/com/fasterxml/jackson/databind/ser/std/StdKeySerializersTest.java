package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public class StdKeySerializersTest {

    private static final JsonSerializer<Object> DEFAULT_KEY_SERIALIZER = new StdKeySerializer();
    private static final JsonSerializer<Object> DEFAULT_STRING_SERIALIZER = new StringKeySerializer();
    private StdKeySerializers stdKeySerializers;

    @Before
    public void setUp() {
        stdKeySerializers = new StdKeySerializers();
    }

    @Test
    public void testGetStdKeySerializer1() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = null;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer2() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Object.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new Dynamic(), result);
    }

    @Test
    public void testGetStdKeySerializer3() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = String.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(DEFAULT_STRING_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer4() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Integer.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer5() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Class.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new Default(Default.TYPE_CLASS, rawKeyType), result);
    }

    @Test
    public void testGetStdKeySerializer6() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Date.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new Default(Default.TYPE_DATE, rawKeyType), result);
    }

    @Test
    public void testGetStdKeySerializer7() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new Default(Default.TYPE_CALENDAR, rawKeyType), result);
    }

    @Test
    public void testGetStdKeySerializer8() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = java.util.UUID.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new Default(Default.TYPE_TO_STRING, rawKeyType), result);
    }

    @Test
    public void testGetStdKeySerializer9() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Integer.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetFallbackKeySerializer1() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = null;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetFallbackKeySerializer2() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(new Dynamic(), result);
    }

    @Test
    public void testGetFallbackKeySerializer3() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Class.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(new Default(Default.TYPE_CLASS, rawKeyType), result);
    }

    @Test
    public void testGetFallbackKeySerializer4() {
        SerializationConfig config = new SerializationConfig(null);
        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(new Dynamic(), result);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }
}