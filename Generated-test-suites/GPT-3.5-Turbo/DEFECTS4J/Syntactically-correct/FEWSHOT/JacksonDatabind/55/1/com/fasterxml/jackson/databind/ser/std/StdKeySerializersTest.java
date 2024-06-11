package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

@SuppressWarnings("serial")
public class StdKeySerializersTest {
    
    // Helper method for testing JsonSerializer.serialize(Object, JsonGenerator, SerializerProvider) method
    private void testSerialization(JsonSerializer<Object> serializer, Object object, JsonGenerator generator, SerializerProvider provider) throws IOException {
        serializer.serialize(object, generator, provider);
        verify(generator).writeFieldName(anyString());
    }

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = mock(Class.class);
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_NullType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_StringType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_PrimitiveType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = int.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_NumberType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Long.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_ClassType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, ((StdKeySerializers.Default)serializer).getTypeId());
    }

    @Test
    public void testGetStdKeySerializer_DateType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default)serializer).getTypeId());
    }

    @Test
    public void testGetStdKeySerializer_CalendarType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default)serializer).getTypeId());
    }
    
    @Test
    public void testGetStdKeySerializer_UUIDType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = java.util.UUID.class;
        boolean useDefault = true;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default)serializer).getTypeId());
    }
    
    @Test
    public void testGetStdKeySerializer_UseDefaultFalse() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Object.class;
        boolean useDefault = false;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);
        
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Calendar.class;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_NullType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = null;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_EnumType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = Enum.class;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_EnumType_RealEnum() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = TestEnum.class;
        
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default)serializer).getTypeId());
    }
    
    @Test
    public void testGetDefault() {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testDefaultSerialization() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        testSerialization(serializer, Object.class, generator, provider);
        
        serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        testSerialization(serializer, new Date(), generator, provider);
        
        serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        testSerialization(serializer, Calendar.getInstance(), generator, provider);
        
        serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, TestEnum.class);
        testSerialization(serializer, TestEnum.TEST_VALUE, generator, provider);
        
        serializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class);
        testSerialization(serializer, java.util.UUID.randomUUID(), generator, provider);
        
        serializer = new StdKeySerializers.Default(0, null);
        testSerialization(serializer, "test", generator, provider);
    }

    @Test
    public void testStringKeySerializerSerialization() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        StdKeySerializers.StringKeySerializer serializer = new StdKeySerializers.StringKeySerializer();
        testSerialization(serializer, "test", generator, provider);
    }

    @Test
    public void testDynamicSerialization() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        PropertySerializerMap map = mock(PropertySerializerMap.class);
        when(map.serializerFor(TestEnum.class)).thenReturn(mock(JsonSerializer.class));
        
        StdKeySerializers.Dynamic serializer = new StdKeySerializers.Dynamic();
        serializer._dynamicSerializers = map;
        testSerialization(serializer, TestEnum.TEST_VALUE, generator, provider);
        
        when(map.serializerFor(TestEnum.class)).thenReturn(null);
        testSerialization(serializer, TestEnum.TEST_VALUE, generator, provider);
    }
    
    private enum TestEnum {
        TEST_VALUE;
    }
}