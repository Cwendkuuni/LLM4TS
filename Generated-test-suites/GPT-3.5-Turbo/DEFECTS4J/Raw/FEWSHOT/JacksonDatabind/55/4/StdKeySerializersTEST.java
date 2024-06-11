package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {
    
    private StdKeySerializers.Default defaultKeySerializer;
    private StdKeySerializers.Dynamic dynamicKeySerializer;
    private StdKeySerializers.StringKeySerializer stringKeySerializer;
    
    @Before
    public void setup() {
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, Object.class);
        dynamicKeySerializer = new StdKeySerializers.Dynamic();
        stringKeySerializer = new StdKeySerializers.StringKeySerializer();
    }
    
    @Test
    public void testDefaultSerialize() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer.serialize("test", g, provider);
        verify(g, times(1)).writeFieldName("test");
    }
    
    @Test
    public void testDefaultSerializeWithDate() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        Date date = new Date();
        defaultKeySerializer.serialize(date, g, provider);
        verify(provider, times(1)).defaultSerializeDateKey(date, g);
    }
    
    @Test
    public void testDefaultSerializeWithCalendar() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        Calendar calendar = Calendar.getInstance();
        defaultKeySerializer.serialize(calendar, g, provider);
        verify(provider, times(1)).defaultSerializeDateKey(calendar.getTimeInMillis(), g);
    }
    
    @Test
    public void testDefaultSerializeWithEnum() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        Enum<?> enumValue = mock(Enum.class);
        when(enumValue.name()).thenReturn("testEnum");
        defaultKeySerializer.serialize(enumValue, g, provider);
        verify(g, times(1)).writeFieldName("testEnum");
    }
    
    @Test
    public void testDefaultSerializeWithToString() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, Object.class);
        Object object = new Object();
        defaultKeySerializer.serialize(object, g, provider);
        verify(g, times(1)).writeFieldName(object.toString());
    }
    
    @Test
    public void testDynamicSerialize() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        dynamicKeySerializer.serialize("test", g, provider);
        verify(g, times(1)).writeFieldName("test");
    }
    
    @Test
    public void testDynamicSerializeWithSerializer() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        dynamicKeySerializer._dynamicSerializers = mock(PropertySerializerMap.class);
        when(dynamicKeySerializer._dynamicSerializers.serializerFor(any(Class.class))).thenReturn(serializer);
        Object object = new Object();
        dynamicKeySerializer.serialize(object, g, provider);
        verify(serializer, times(1)).serialize(object, g, provider);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDynamicSerializeWithSerializerException() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        dynamicKeySerializer._dynamicSerializers = mock(PropertySerializerMap.class);
        when(dynamicKeySerializer._dynamicSerializers.serializerFor(any(Class.class))).thenReturn(serializer);
        when(serializer.serialize(any(), any(), any())).thenThrow(new JsonMappingException(""));
        Object object = new Object();
        dynamicKeySerializer.serialize(object, g, provider);
    }
    
    @Test
    public void testStringSerialize() throws IOException {
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        stringKeySerializer.serialize("test", g, provider);
        verify(g, times(1)).writeFieldName("test");
    }
    
}