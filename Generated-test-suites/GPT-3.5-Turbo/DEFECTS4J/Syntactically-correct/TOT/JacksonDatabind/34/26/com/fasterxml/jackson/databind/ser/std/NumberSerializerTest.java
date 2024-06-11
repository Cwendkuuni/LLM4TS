package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockGenerator;
    
    @Mock
    private SerializerProvider mockProvider;
    
    private NumberSerializer numberSerializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testSerialize_withBigDecimal_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new BigDecimal("123.45");
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber((BigDecimal) value);
    }
    
    @Test
    public void testSerialize_withBigInteger_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new BigInteger("12345678901234567890");
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber((BigInteger) value);
    }
    
    @Test
    public void testSerialize_withInteger_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new Integer(123);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withLong_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new Long(1234567890L);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerialize_withDouble_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new Double(123.45);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerialize_withFloat_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new Float(123.45f);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerialize_withByte_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new Byte((byte) 123);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withShort_shouldWriteNumber() throws IOException {
        // Arrange
        Number value = new Short((short) 123);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withUnsupportedType_shouldWriteNumberAsString() throws IOException {
        // Arrange
        Number value = mock(Number.class);
        when(value.toString()).thenReturn("123");
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema_withBigInteger_shouldReturnIntegerSchemaNode() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = BigInteger.class;
        
        // Act
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("integer", schema.asText());
        assertTrue(schema.isNumber());
        assertTrue(schema.isIntegralNumber());
        assertFalse(schema.isFloat());
    }
    
    @Test
    public void testGetSchema_withBigDecimal_shouldReturnNumberSchemaNode() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = BigDecimal.class;
        
        // Act
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        
        // Assert
        assertEquals("number", schema.asText());
        assertTrue(schema.isNumber());
        assertFalse(schema.isIntegralNumber());
        assertTrue(schema.isFloat());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withBigInteger_shouldVisitIntFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withBigDecimal_shouldVisitFloatFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert
        verify(visitor).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_DECIMAL);
    }
    
}