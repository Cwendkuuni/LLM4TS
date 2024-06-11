package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {
    
    private SerializationConfig config;
    
    @Before
    public void setUp() {
        config = new SerializationConfig(null, null, null);
    }
    
    @Test
    public void testGetStdKeySerializer_NullRawKeyType_ReturnsDynamic() {
        Class<?> rawKeyType = null;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetStdKeySerializer_ObjectRawKeyType_ReturnsDynamic() {
        Class<?> rawKeyType = Object.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetStdKeySerializer_StringRawKeyType_ReturnsDefaultStringSerializer() {
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        assertSame(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
    }
    
    @Test
    public void testGetStdKeySerializer_PrimitiveIntRawKeyType_ReturnsDefaultKeySerializer() {
        Class<?> rawKeyType = int.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetStdKeySerializer_NumberRawKeyType_ReturnsDefaultKeySerializer() {
        Class<?> rawKeyType = Integer.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetStdKeySerializer_ClassRawKeyType_ReturnsDefaultWithClassType() {
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertSame(rawKeyType, defaultSerializer.handledType());
    }
    
    @Test
    public void testGetStdKeySerializer_DateRawKeyType_ReturnsDefaultWithDateType() {
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertSame(rawKeyType, defaultSerializer.handledType());
    }
    
    @Test
    public void testGetStdKeySerializer_CalendarRawKeyType_ReturnsDefaultWithCalendarType() {
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertSame(rawKeyType, defaultSerializer.handledType());
    }
    
    @Test
    public void testGetStdKeySerializer_UuidRawKeyType_ReturnsDefaultWithTypeToString() {
        Class<?> rawKeyType = java.util.UUID.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
        assertSame(rawKeyType, defaultSerializer.handledType());
    }
    
    @Test
    public void testGetStdKeySerializer_NoUseDefault_ReturnsNull() {
        Class<?> rawKeyType = String.class;
        boolean useDefault = false;
        
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertNull(result);
    }
    
    @Test
    public void testGetFallbackKeySerializer_NullRawKeyType_ReturnsDynamic() {
        Class<?> rawKeyType = null;
        
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetFallbackKeySerializer_ObjectRawKeyType_ReturnsDynamic() {
        Class<?> rawKeyType = Object.class;
        
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetFallbackKeySerializer_EnumRawKeyType_ReturnsDynamic() {
        Class<?> rawKeyType = Enum.class;
        
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetFallbackKeySerializer_EnumClassRawKeyType_ReturnsDefaultWithEnumType() {
        Class<?> rawKeyType = MyEnum.class;
        
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, defaultSerializer._typeId);
        assertSame(rawKeyType, defaultSerializer.handledType());
    }
    
    @Test
    public void testGetFallbackKeySerializer_NoRawKeyType_ReturnsDefault() {
        Class<?> rawKeyType = null;
        
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetDefault_ReturnsDefaultKeySerializer() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        
        assertSame(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    // Test helper enum
    private enum MyEnum {
        A, B, C
    }

}