package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("serial")
public class StdKeySerializersTest {

    // Variables for testing
    private StdKeySerializers stdKeySerializers;
    private SerializationConfig config;
    private Class<?> rawKeyType;
    
    @Before
    public void setUp() {
        stdKeySerializers = new StdKeySerializers();
        config = new SerializationConfig(null, null, null);
        rawKeyType = null;
    }

    @Test
    public void testGetStdKeySerializer_dynamic() {
        rawKeyType = null;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_string() {
        rawKeyType = String.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StdKeySerializers.StringKeySerializer);
    }

    @Test
    public void testGetStdKeySerializer_primitive() {
        rawKeyType = Integer.TYPE;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testGetStdKeySerializer_class() {
        rawKeyType = Class.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default resultDefault = (StdKeySerializers.Default) result;
        assertSame(resultDefault.getType(), Class.class);
        assertSame(resultDefault._typeId, StdKeySerializers.Default.TYPE_CLASS);
    }

    @Test
    public void testGetStdKeySerializer_date() {
        rawKeyType = Date.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default resultDefault = (StdKeySerializers.Default) result;
        assertSame(resultDefault.getType(), Date.class);
        assertSame(resultDefault._typeId, StdKeySerializers.Default.TYPE_DATE);
    }

    @Test
    public void testGetStdKeySerializer_calendar() {
        rawKeyType = Calendar.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default resultDefault = (StdKeySerializers.Default) result;
        assertSame(resultDefault.getType(), Calendar.class);
        assertSame(resultDefault._typeId, StdKeySerializers.Default.TYPE_CALENDAR);
    }

    @Test
    public void testGetStdKeySerializer_uuid() {
        rawKeyType = java.util.UUID.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default resultDefault = (StdKeySerializers.Default) result;
        assertSame(resultDefault.getType(), java.util.UUID.class);
        assertSame(resultDefault._typeId, StdKeySerializers.Default.TYPE_TO_STRING);
    }

    @Test
    public void testGetStdKeySerializer_default() {
        rawKeyType = Object.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testGetStdKeySerializer_noDefault() {
        rawKeyType = Object.class;
        boolean useDefault = false;
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer_dynamic() {
        rawKeyType = null;
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_enum() {
        rawKeyType = Enum.class;
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_enumValue() {
        rawKeyType = MyEnum.class;
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default resultDefault = (StdKeySerializers.Default) result;
        assertSame(resultDefault.getType(), MyEnum.class);
        assertSame(resultDefault._typeId, StdKeySerializers.Default.TYPE_ENUM);
    }

    @Test
    public void testGetFallbackKeySerializer_noEnum() {
        rawKeyType = HashMap.class;
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertSame(result, StdKeySerializers.DEFAULT_KEY_SERIALIZER);
    }

    @Test
    public void testDefaultSerializeDateKey() throws IOException {
        Date date = new Date();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator g = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        defaultSerializer.serialize(date, g, provider);
        g.flush();
    }

    // Enum for testing
    private enum MyEnum {
        VALUE1, VALUE2, VALUE3
    }
}