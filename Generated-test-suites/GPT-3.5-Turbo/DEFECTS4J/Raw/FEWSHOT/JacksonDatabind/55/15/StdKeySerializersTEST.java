package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.Before;
import org.junit.Test;

public class StdKeySerializersTest {
    
    private StdKeySerializers.Default defaultKeySerializer;
    private StdKeySerializers.Dynamic dynamicKeySerializer;
    private StdKeySerializers.StringKeySerializer stringKeySerializer;

    @Before
    public void setUp() {
        defaultKeySerializer = new StdKeySerializers.Default(1, Date.class);
        dynamicKeySerializer = new StdKeySerializers.Dynamic();
        stringKeySerializer = new StdKeySerializers.StringKeySerializer();
    }

    @Test
    public void testDefaultKeySerializerSerialize() throws IOException {
        Date value = new Date();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer.serialize(value, generator, provider);
        verify(provider).defaultSerializeDateKey(value, generator);
    }

    @Test
    public void testDynamicKeySerializerSerialize() throws IOException {
        Object value = "key";
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializers.PropertySerializerMap map = mock(StdKeySerializers.PropertySerializerMap.class);
        when(dynamicKeySerializer._dynamicSerializers).thenReturn(map);
        StdKeySerializers.PropertySerializerMap.SerializerAndMapResult result =
                new StdKeySerializers.PropertySerializerMap.SerializerAndMapResult(mock(JsonSerializer.class), map);
        when(map.findAndAddKeySerializer(any(), any(), any())).thenReturn(result);
        dynamicKeySerializer.serialize(value, generator, provider);
        verify(result.serializer).serialize(value, generator, provider);
    }

    @Test
    public void testStringKeySerializerSerialize() throws IOException {
        Object value = "key";
        JsonGenerator generator = mock(JsonGenerator.class);
        stringKeySerializer.serialize(value, generator, null);
        verify(generator).writeFieldName((String) value);
    }
    
    @Test
    public void testGetStdKeySerializerStringClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, result);
    }
    
    @Test
    public void testGetStdKeySerializerNullClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerPrimitiveClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = int.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetStdKeySerializerClassClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerDateClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerCalendarClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }
    
    @Test
    public void testGetStdKeySerializerUUIDClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = java.util.UUID.class;
        boolean useDefault = true;
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }
    
    @Test
    public void testGetFallbackKeySerializerEnumClass() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Enum.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }
    
    @Test
    public void testGetFallbackKeySerializerEnumIsEnum() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = EnumType.class;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }
    
    @Test
    public void testGetFallbackKeySerializerDefault() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    @Test
    public void testGetDefault() {
        JsonSerializer<Object> result = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }
    
    public enum EnumType {
        VALUE
    }
    
}