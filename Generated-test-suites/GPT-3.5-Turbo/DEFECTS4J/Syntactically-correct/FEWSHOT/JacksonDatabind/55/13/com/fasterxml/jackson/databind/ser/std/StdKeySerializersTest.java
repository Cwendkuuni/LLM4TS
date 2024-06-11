package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {
    
    private StdKeySerializers.Default defaultKeySerializer;
    private StdKeySerializers.Dynamic dynamicKeySerializer;
    private StdKeySerializers.StringKeySerializer stringKeySerializer;
    
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    
    @Before
    public void setUp() {
        defaultKeySerializer = new StdKeySerializers.Default(1, Date.class);
        dynamicKeySerializer = new StdKeySerializers.Dynamic();
        stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }
    
    @Test
    public void testDefaultSerialize() throws IOException {
        Date date = new Date();
        defaultKeySerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
    }
    
    @Test
    public void testDefaultSerialize_withCalendar() throws IOException {
        Calendar calendar = Calendar.getInstance();
        defaultKeySerializer = new StdKeySerializers.Default(2, Calendar.class);
        defaultKeySerializer.serialize(calendar, jsonGenerator, serializerProvider);
        verify(serializerProvider).defaultSerializeDateKey(calendar.getTimeInMillis(), jsonGenerator);
    }
    
    @Test
    public void testDefaultSerialize_withClass() throws IOException {
        Class<?> clazz = String.class;
        defaultKeySerializer = new StdKeySerializers.Default(3, Class.class);
        defaultKeySerializer.serialize(clazz, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(clazz.getName());
    }
    
    @Test
    public void testDefaultSerialize_withEnum() throws IOException {
        Enum<?> enumValue = Enum.TEST_ENUM;
        defaultKeySerializer = new StdKeySerializers.Default(4, Enum.class);
        defaultKeySerializer.serialize(enumValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(enumValue.toString());
    }
    
    @Test
    public void testDefaultSerialize_withToString() throws IOException {
        String str = "test";
        defaultKeySerializer = new StdKeySerializers.Default(5, Object.class);
        defaultKeySerializer.serialize(str, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(str);
    }
    
    @Test
    public void testDynamicSerialize() throws IOException {
        Class<?> cls = String.class;
        dynamicKeySerializer.serialize(cls, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(cls.getName());
    }
    
    @Test
    public void testDynamicSerialize_withSerializerInMap() throws IOException {
        Class<?> cls = String.class;
        PropertySerializerMap map = new PropertySerializerMap.Empty();
        dynamicKeySerializer._dynamicSerializers = map;
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        map = map.newWith(cls, serializer);
        dynamicKeySerializer._dynamicSerializers = map;
        
        dynamicKeySerializer.serialize(cls, jsonGenerator, serializerProvider);
        verify(serializer).serialize(cls, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testStringSerialize() throws IOException {
        String str = "test";
        stringKeySerializer.serialize(str, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(str);
    }
    
    enum Enum {
        TEST_ENUM;
    }

}