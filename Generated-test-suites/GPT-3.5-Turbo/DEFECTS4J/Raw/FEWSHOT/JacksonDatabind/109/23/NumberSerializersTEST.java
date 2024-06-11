package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class NumberSerializersTest {
    
    private NumberSerializers.ShortSerializer shortSerializer;
    private NumberSerializers.IntegerSerializer integerSerializer;
    private NumberSerializers.IntLikeSerializer intLikeSerializer;
    private NumberSerializers.LongSerializer longSerializer;
    private NumberSerializers.FloatSerializer floatSerializer;
    private NumberSerializers.DoubleSerializer doubleSerializer;
    
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;
    
    @Before
    public void setUp() {
        shortSerializer = new NumberSerializers.ShortSerializer();
        integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        longSerializer = new NumberSerializers.LongSerializer(Long.class);
        floatSerializer = new NumberSerializers.FloatSerializer();
        doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
        
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
    }
    
    @Test
    public void testShortSerializerSerialize() throws IOException {
        Number value = 100;
        
        shortSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        
        verify(jsonGeneratorMock, times(1)).writeNumber(100);
    }
    
    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        Number value = 100;
        
        integerSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        
        verify(jsonGeneratorMock, times(1)).writeNumber(100);
    }
    
    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        Number value = 100;
        
        integerSerializer.serializeWithType(value, jsonGeneratorMock, serializerProviderMock, null);
        
        verify(jsonGeneratorMock, times(1)).writeNumber(100);
    }
    
    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        Number value = 100;
        
        intLikeSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        
        verify(jsonGeneratorMock, times(1)).writeNumber(100);
    }
    
    @Test
    public void testLongSerializerSerialize() throws IOException {
        Number value = 100L;
        
        longSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        
        verify(jsonGeneratorMock, times(1)).writeNumber(100L);
    }
    
    @Test
    public void testFloatSerializerSerialize() throws IOException {
        Number value = 2.5f;
        
        floatSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        
        verify(jsonGeneratorMock, times(1)).writeNumber(2.5f);
    }
    
    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        Number value = 3.14;
        
        doubleSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        
        verify(jsonGeneratorMock, times(1)).writeNumber(3.14);
    }
}