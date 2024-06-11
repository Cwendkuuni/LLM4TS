package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;
    
    @Mock
    private SerializerProvider mockSerializerProvider;
    
    private NumberSerializer numberSerializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void serialize_shouldWriteBigDecimalValueWhenNumberIsOfBigDecimalType() throws IOException {
        BigDecimal value = new BigDecimal("123.456");
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value);
    }
    
    @Test
    public void serialize_shouldWriteBigIntegerValueWhenNumberIsOfBigIntegerType() throws IOException {
        BigInteger value = new BigInteger("12345678901234567890");
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value);
    }
    
    @Test
    public void serialize_shouldWriteIntegerValueWhenNumberIsOfIntegerType() throws IOException {
        Integer value = 123;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }
    
    @Test
    public void serialize_shouldWriteLongValueWhenNumberIsOfLongType() throws IOException {
        Long value = 123L;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value.longValue());
    }
    
    @Test
    public void serialize_shouldWriteDoubleValueWhenNumberIsOfDoubleType() throws IOException {
        Double value = 123.456;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value.doubleValue());
    }
    
    @Test
    public void serialize_shouldWriteFloatValueWhenNumberIsOfFloatType() throws IOException {
        Float value = 123.456f;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value.floatValue());
    }
    
    @Test
    public void serialize_shouldWriteByteValueWhenNumberIsOfByteType() throws IOException {
        Byte value = 123;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }
    
    @Test
    public void serialize_shouldWriteShortValueWhenNumberIsOfShortType() throws IOException {
        Short value = 123;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }
    
    @Test
    public void serialize_shouldWriteStringValueWhenNumberIsNotMatchedToOtherTypes() throws IOException {
        Number value = 123.456;
        
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        
        verify(mockJsonGenerator, times(1)).writeNumber(value.toString());
    }
    
    @Test
    public void getSchema_shouldReturnIntegerSchemaNodeWhenNumberIsOfBigIntegerType() {
        JsonNode schemaNode = numberSerializer.getSchema(mockSerializerProvider, BigInteger.class);
        
        assertThat(schemaNode.asText(), is("integer"));
    }
    
    @Test
    public void getSchema_shouldReturnNumberSchemaNodeWhenNumberIsNotOfBigIntegerType() {
        JsonNode schemaNode = numberSerializer.getSchema(mockSerializerProvider, Long.class);
        
        assertThat(schemaNode.asText(), is("number"));
    }
    
    @Test
    public void acceptJsonFormatVisitor_shouldVisitIntFormatWhenNumberIsOfBigIntegerType() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void acceptJsonFormatVisitor_shouldVisitFloatFormatWhenNumberIsOfBigDecimalType() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void acceptJsonFormatVisitor_shouldExpectNumberFormatWhenNumberIsNotOfBigIntegerOrBigDecimalType() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).expectNumberFormat(typeHint);
    }
}