package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {
    
    @Mock
    JsonGenerator mockGenerator;
    
    @Mock
    SerializerProvider mockProvider;
    
    NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    
    public NumberSerializerTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("10");
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("10");
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        Long value = 10L;
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 10.0;
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 10.0f;
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 10;
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerializeOtherNumber() throws IOException {
        Short value = 10;
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        Mockito.verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testGetSchema() {
        assertNotNull(numberSerializer.getSchema(mockProvider, null));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        assertNotNull(numberSerializer.acceptJsonFormatVisitor(null, null));
    }
}