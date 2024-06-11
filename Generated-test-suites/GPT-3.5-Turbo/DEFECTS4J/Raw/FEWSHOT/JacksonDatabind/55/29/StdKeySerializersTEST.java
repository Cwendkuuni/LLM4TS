package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {
    private StdKeySerializers stdKeySerializers;
    private JsonSerializer<Object> defaultKeySerializer;
    private JsonSerializer<Object> defaultStringSerializer;
    private SerializationConfig config;

    @Before
    public void setUp() {
        stdKeySerializers = new StdKeySerializers();
        defaultKeySerializer = mock(JsonSerializer.class);
        defaultStringSerializer = mock(JsonSerializer.class);
        config = mock(SerializationConfig.class);
        when(stdKeySerializers.getDefault()).thenReturn(defaultKeySerializer);
    }

    @Test
    public void testGetStdKeySerializer() {
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(stdKeySerializers.DEFAULT_STRING_SERIALIZER, result);

        rawKeyType = Date.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, rawKeyType), result);

        rawKeyType = Calendar.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, rawKeyType), result);

        rawKeyType = java.util.UUID.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, rawKeyType), result);

        rawKeyType = Object.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(new StdKeySerializers.Dynamic(), result);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(new StdKeySerializers.Dynamic(), result);

        rawKeyType = TestEnum.class;
        result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, rawKeyType), result);
    }

    @Test
    public void testDefaultSerializerSerialize() throws Exception {
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date date = new Date();

        defaultSerializer.serialize(date, g, provider);

        // Verify the method calls
        mock(JsonGenerator.class);
        g.defaultSerializeDateKey(date, g);
    }

    @Test
    public void testDynamicSerializerSerialize() throws Exception {
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TestEnum testEnum = TestEnum.TEST;

        dynamicSerializer.serialize(testEnum, g, provider);

        // Verify the method calls
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        dynamicSerializer._dynamicSerializers = mock(PropertySerializerMap.class);
        when(dynamicSerializer._dynamicSerializers.serializerFor(TestEnum.class)).thenReturn(serializer);
        serializer.serialize(testEnum, g, provider);
    }

    @Test
    public void testStringKeySerializerSerialize() throws Exception {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        String key = "key";

        stringKeySerializer.serialize(key, g, provider);

        // Verify the method calls
        g.writeFieldName(key);
    }

    public enum TestEnum {
        TEST
    }
}