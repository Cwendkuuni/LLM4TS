package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;
import org.mockito.Mockito;

public class NumberSerializerTest {
    @Test
    public void testConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertEquals(Number.class, numberSerializer.handledType());
        assertEquals(false, numberSerializer.isEmpty());
    }
    
    @Test
    public void testCreateContextual_noFormat() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        
        JsonSerializer<?> result = numberSerializer.createContextual(mockProvider, mockProperty);
        
        assertEquals(numberSerializer, result);
    }
    
    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        BigDecimal value = new BigDecimal(10.5);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        BigInteger value = new BigInteger("10");
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_Long() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Long value = Long.valueOf(10);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerialize_Double() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Double value = Double.valueOf(10.5);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerialize_Float() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Float value = Float.valueOf(10.5f);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerialize_Integer() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Integer value = Integer.valueOf(10);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_Byte() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Byte value = Byte.valueOf("10");
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_Short() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Short value = Short.valueOf("10");
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_else() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        Number value = Double.valueOf(10.5);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        Mockito.verify(mockGenerator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Type mockType = mock(Type.class);
        
        JsonNode schema = numberSerializer.getSchema(mockProvider, mockType);
        
        assertEquals("integer", schema.get("type").asText());
        assertEquals(true, schema.get("isInt").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_isInt() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
        
        Mockito.verify(mockVisitor).expectNumberFormat(mockTypeHint);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_BigDecimal() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
        
        Mockito.verify(mockVisitor).visitFloatFormat(mockTypeHint, JsonParser.NumberType.BIG_DECIMAL);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_else() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
        
        Mockito.verify(mockVisitor, Mockito.times(0)).expectNumberFormat(mockTypeHint);
        Mockito.verify(mockVisitor, Mockito.times(0)).visitIntFormat(mockTypeHint, JsonParser.NumberType.BIG_INTEGER);
        Mockito.verify(mockVisitor).expectNumberFormat(mockTypeHint);
    }
}