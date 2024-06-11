package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {
    
    @Mock
    private SerializationConfig config;
    
    @Mock
    private Class<?> rawKeyType;
    
    @Mock
    private JsonGenerator g;
    
    @Mock
    private SerializerProvider provider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetStdKeySerializerDynamic() throws IOException {
        Mockito.when(rawKeyType == null).thenReturn(true);
        Mockito.when(rawKeyType == Object.class).thenReturn(true);

        StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        
        // perform assertions
    }
    
    @Test
    public void testGetStdKeySerializerString() throws IOException {
        Mockito.when(rawKeyType == null).thenReturn(false);
        Mockito.when(rawKeyType == Object.class).thenReturn(false);
        Mockito.when(rawKeyType == String.class).thenReturn(true);
        
        StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        
        // perform assertions
    }
    
    @Test
    public void testGetStdKeySerializerPrimitive() throws IOException {
        Mockito.when(rawKeyType == null).thenReturn(false);
        Mockito.when(rawKeyType == Object.class).thenReturn(false);
        Mockito.when(rawKeyType == String.class).thenReturn(false);
        Mockito.when(rawKeyType.isPrimitive()).thenReturn(true);
        
        StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        
        // perform assertions
    }
    
    @Test
    public void testGetStdKeySerializerDate() throws IOException {
        Mockito.when(rawKeyType == null).thenReturn(false);
        Mockito.when(rawKeyType == Object.class).thenReturn(false);
        Mockito.when(rawKeyType == String.class).thenReturn(false);
        Mockito.when(rawKeyType.isPrimitive()).thenReturn(false);
        Mockito.when(Number.class.isAssignableFrom(rawKeyType)).thenReturn(false);
        Mockito.when(rawKeyType == Class.class).thenReturn(false);
        Mockito.when(Date.class.isAssignableFrom(rawKeyType)).thenReturn(true);
        
        StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        
        // perform assertions
    }
    
    @Test
    public void testGetStdKeySerializerCalendar() throws IOException {
        Mockito.when(rawKeyType == null).thenReturn(false);
        Mockito.when(rawKeyType == Object.class).thenReturn(false);
        Mockito.when(rawKeyType == String.class).thenReturn(false);
        Mockito.when(rawKeyType.isPrimitive()).thenReturn(false);
        Mockito.when(Number.class.isAssignableFrom(rawKeyType)).thenReturn(false);
        Mockito.when(rawKeyType == Class.class).thenReturn(false);
        Mockito.when(Date.class.isAssignableFrom(rawKeyType)).thenReturn(false);
        Mockito.when(Calendar.class.isAssignableFrom(rawKeyType)).thenReturn(true);
        
        StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        
        // perform assertions
    }
    
    @Test
    public void testGetStdKeySerializerUUID() throws IOException {
        Mockito.when(rawKeyType == null).thenReturn(false);
        Mockito.when(rawKeyType == Object.class).thenReturn(false);
        Mockito.when(rawKeyType == String.class).thenReturn(false);
        Mockito.when(rawKeyType.isPrimitive()).thenReturn(false);
        Mockito.when(Number.class.isAssignableFrom(rawKeyType)).thenReturn(false);
        Mockito.when(rawKeyType == Class.class).thenReturn(false);
        Mockito.when(Date.class.isAssignableFrom(rawKeyType)).thenReturn(false);
        Mockito.when(Calendar.class.isAssignableFrom(rawKeyType)).thenReturn(false);
        Mockito.when(rawKeyType == java.util.UUID.class).thenReturn(true);
        
        StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        
        // perform assertions
    }
    
    @Test
    public void testGetStdKeySerializerFallback() throws IOException {
        Mockito.when(rawKeyType != null).thenReturn(true);
        Mockito.when(Enum.class == rawKeyType).thenReturn(true);
        
        StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        
        // perform assertions
    }
    
    @Test
    public void testGetFallbackKeySerializer() throws IOException {
        StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        
        // perform assertions
    }
    
    @Test
    public void testGetDefault() throws IOException {
        StdKeySerializers.getDefault();
        
        // perform assertions
    }
    
    @Test
    public void testDefaultSerializerSerializeDate() throws IOException {
        Default defaultSerializer = new Default(Default.TYPE_DATE, Date.class);
        
        defaultSerializer.serialize(new Date(), g, provider);
        
        // perform assertions
    }
    
    @Test
    public void testDefaultSerializerSerializeCalendar() throws IOException {
        Default defaultSerializer = new Default(Default.TYPE_CALENDAR, Calendar.class);
        
        defaultSerializer.serialize(Calendar.getInstance(), g, provider);
        
        // perform assertions
    }
    
    @Test
    public void testDefaultSerializerSerializeClass() throws IOException {
        Default defaultSerializer = new Default(Default.TYPE_CLASS, Class.class);
        
        defaultSerializer.serialize(getClass(), g, provider);
        
        // perform assertions
    }
    
    @Test
    public void testDefaultSerializerSerializeEnum() throws IOException {
        Default defaultSerializer = new Default(Default.TYPE_ENUM, Enum.class);
        
        defaultSerializer.serialize(Enum.TEST_ENUM, g, provider);
        
        // perform assertions
    }
    
    @Test
    public void testDefaultSerializerSerializeToString() throws IOException {
        Default defaultSerializer = new Default(Default.TYPE_TO_STRING, Object.class);
        
        defaultSerializer.serialize("Test", g, provider);
        
        // perform assertions
    }
    
    @Test
    public void testDynamicSerialize() throws IOException {
        Dynamic dynamic = new Dynamic();
        
        dynamic.serialize("Test", g, provider);
        
        // perform assertions
    }
    
    @Test
    public void testDynamicFindAndAddDynamic() throws IOException {
        Dynamic dynamic = new Dynamic();
        
        dynamic._findAndAddDynamic(Mockito.mock(PropertySerializerMap.class), String.class, provider);
        
        // perform assertions
    }
    
    @Test
    public void testStringKeySerializerSerialize() throws IOException {
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        
        stringKeySerializer.serialize("Test", g, provider);
        
        // perform assertions
    }

    private enum Enum {
        TEST_ENUM
    }
}