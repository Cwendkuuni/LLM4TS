package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {
    
    @Mock
    JsonGenerator jsonGenerator;
    
    @Mock
    SerializerProvider serializerProvider;
    
    @Mock
    JsonFormatVisitorWrapper jsonFormatVisitorWrapper;
    
    @Mock
    JavaType javaType;
    
    @Test
    public void testSerialize_whenNumberIsBigDecimal_thenWriteNumberAsBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("123.45");
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_whenNumberIsBigInteger_thenWriteNumberAsBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("12345");
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_whenNumberIsInteger_thenWriteNumberAsInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        Integer value = 123;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_whenNumberIsLong_thenWriteNumberAsLong() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        Long value = 123L;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerialize_whenNumberIsDouble_thenWriteNumberAsDouble() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        Double value = 123.45;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerialize_whenNumberIsFloat_thenWriteNumberAsFloat() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        Float value = 123.45f;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerialize_whenNumberIsByte_thenWriteNumberAsInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        Byte value = 123;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_whenNumberIsShort_thenWriteNumberAsInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        Short value = 123;
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_whenNumberIsCustomType_thenWriteNumberAsString() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        CustomNumber value = new CustomNumber(123.45);
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema_whenTypeIsBigInteger_thenReturnIntegerSchemaNode() {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        
        JsonNode schemaNode = numberSerializer.getSchema(serializerProvider, javaType);
        
        assertTrue(schemaNode.isInt());
        assertTrue(schemaNode.isNumber());
    }
    
    @Test
    public void testGetSchema_whenTypeIsBigDecimal_thenReturnNumberSchemaNode() {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        
        JsonNode schemaNode = numberSerializer.getSchema(serializerProvider, javaType);
        
        assertTrue(schemaNode.isNumber());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_whenTypeIsBigInteger_thenVisitIntFormat() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        
        verify(jsonFormatVisitorWrapper).visitIntFormat(javaType, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_whenTypeIsBigDecimal_thenReturnNumberSchemaNode() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        
        verify(jsonFormatVisitorWrapper).visitFloatFormat(javaType, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_whenTypeIsCustomType_thenExpectNumberFormat() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        
        verify(jsonFormatVisitorWrapper).expectNumberFormat(javaType);
    }
    
    private class CustomNumber extends Number {
        
        private double value;
        
        public CustomNumber(double value) {
            this.value = value;
        }
        
        @Override
        public int intValue() {
            return (int) value;
        }
        
        @Override
        public long longValue() {
            return (long) value;
        }
        
        @Override
        public float floatValue() {
            return (float) value;
        }
        
        @Override
        public double doubleValue() {
            return value;
        }
    }
}