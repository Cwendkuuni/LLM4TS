package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

@SuppressWarnings("serial")
public class StdKeySerializersTest {

    private StdKeySerializers stdKeySerializers;
    private SerializationConfig config;
    private Class<?> rawKeyType;
    private boolean useDefault;
    private JsonSerializer<Object> result;

    @Before
    public void setUp() {
        stdKeySerializers = new StdKeySerializers();
        config = mock(SerializationConfig.class);
        rawKeyType = mock(Class.class);
        useDefault = false;
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsNull_returnDynamicInstance() {
        rawKeyType = null;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsObjectClass_returnDynamicInstance() {
        rawKeyType = Object.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsStringClass_returnDefaultStringKeySerializerInstance() {
        rawKeyType = String.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof StringKeySerializer);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsPrimitive_returnDefaultKeySerializerInstance() {
        when(rawKeyType.isPrimitive()).thenReturn(true);
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsNumberClassOrAssignableFrom_returnDefaultKeySerializerInstance() {
        when(Number.class.isAssignableFrom(rawKeyType)).thenReturn(true);
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsClass_returnDefaultInstance() {
        rawKeyType = Class.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsDateClass_returnDefaultInstance() {
        when(Date.class.isAssignableFrom(rawKeyType)).thenReturn(true);
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsCalendarClass_returnDefaultInstance() {
        when(Calendar.class.isAssignableFrom(rawKeyType)).thenReturn(true);
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testGetStdKeySerializer_rawKeyTypeIsUUIDClass_returnDefaultInstance() {
        rawKeyType = java.util.UUID.class;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testGetStdKeySerializer_useDefaultIsTrue_returnDefaultKeySerializerInstance() {
        useDefault = true;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testGetStdKeySerializer_useDefaultIsFalse_returnNull() {
        useDefault = false;
        result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer_rawKeyTypeIsNull_returnDynamicInstance() {
        rawKeyType = null;
        result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(result instanceof Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_rawKeyTypeIsEnum_returnDynamicInstance() {
        rawKeyType = Enum.class;
        result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(result instanceof Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_rawKeyTypeIsEnum_returnDefaultInstance() {
        rawKeyType = mock(Class.class);
        when(rawKeyType.isEnum()).thenReturn(true);
        result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(result instanceof Default);
    }

    @Test
    public void testDefault_serializeTypeIdIsDate_defaultSerializeDateKeyCalled() throws IOException {
        Default defaultKeySerializer = stdKeySerializers.new Default(Default.TYPE_DATE, rawKeyType);
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer.serialize(value, g, provider);
        verify(provider).defaultSerializeDateKey((Date) value, g);
    }

    @Test
    public void testDefault_serializeTypeIdIsCalendar_defaultSerializeDateKeyCalled() throws IOException {
        Default defaultKeySerializer = stdKeySerializers.new Default(Default.TYPE_CALENDAR, rawKeyType);
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Calendar calendar = mock(Calendar.class);
        when(calendar.getTimeInMillis()).thenReturn(1L);
        defaultKeySerializer.serialize(calendar, g, provider);
        verify(provider).defaultSerializeDateKey(1L, g);
    }

    @Test
    public void testDefault_serializeTypeIdIsClass_writeFieldNameCalled() throws IOException {
        Default defaultKeySerializer = stdKeySerializers.new Default(Default.TYPE_CLASS, rawKeyType);
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Class<?> cls = mock(Class.class);
        when(cls.getName()).thenReturn("ClassName");
        defaultKeySerializer.serialize(cls, g, provider);
        verify(g).writeFieldName("ClassName");
    }

    @Test
    public void testDefault_serializeTypeIdIsEnumWithProviderEnabled_writeFieldValueCalled() throws IOException {
        Default defaultKeySerializer = stdKeySerializers.new Default(Default.TYPE_ENUM, rawKeyType);
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Enum<?> enumValue = mock(Enum.class);
        when(enumValue.name()).thenReturn("enumValue");
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        defaultKeySerializer.serialize(enumValue, g, provider);
        verify(g).writeFieldName("enumValue");
    }

    @Test
    public void testDefault_serializeTypeIdIsEnumWithProviderDisabled_writeFieldNameCalled() throws IOException {
        Default defaultKeySerializer = stdKeySerializers.new Default(Default.TYPE_ENUM, rawKeyType);
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Enum<?> enumValue = mock(Enum.class);
        when(enumValue.toString()).thenReturn("enumValue");
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        defaultKeySerializer.serialize(enumValue, g, provider);
        verify(g).writeFieldName("enumValue");
    }

    @Test
    public void testDefault_serializeTypeIdIsDefault_writeFieldNameCalled() throws IOException {
        Default defaultKeySerializer = stdKeySerializers.new Default(Default.TYPE_TO_STRING, rawKeyType);
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer.serialize(value, g, provider);
        verify(g).writeFieldName(value.toString());
    }

    @Test
    public void testDynamic_serialize_serializerFound_serializesValue() throws IOException, JsonMappingException {
        Dynamic dynamicKeySerializer = stdKeySerializers.new Dynamic();
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        PropertySerializerMap map = mock(PropertySerializerMap.class);
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(map.serializerFor(Object.class)).thenReturn(serializer);
        when(dynamicKeySerializer._findAndAddDynamic(any(PropertySerializerMap.class), any(Class.class), any(SerializerProvider.class))).thenReturn(serializer);
        dynamicKeySerializer.serialize(value, g, provider);
        verify(serializer).serialize(value, g, provider);
    }

    @Test(expected = NullPointerException.class)
    public void testDynamic_serialize_serializerNotFound_throwException() throws IOException, JsonMappingException {
        Dynamic dynamicKeySerializer = stdKeySerializers.new Dynamic();
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonSerializer<Object> serializer = null;
        PropertySerializerMap map = mock(PropertySerializerMap.class);
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(map.serializerFor(Object.class)).thenReturn(serializer);
        when(dynamicKeySerializer._findAndAddDynamic(any(PropertySerializerMap.class), any(Class.class), any(SerializerProvider.class))).thenReturn(serializer);
        dynamicKeySerializer.serialize(value, g, provider);
    }

    @Test
    public void testStringKeySerializer_serialize_writeFieldNameCalled() throws IOException {
        StringKeySerializer stringKeySerializer = stdKeySerializers.new StringKeySerializer();
        Object value = new Object();
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        stringKeySerializer.serialize(value, g, provider);
        verify(g).writeFieldName((String) value);
    }
}