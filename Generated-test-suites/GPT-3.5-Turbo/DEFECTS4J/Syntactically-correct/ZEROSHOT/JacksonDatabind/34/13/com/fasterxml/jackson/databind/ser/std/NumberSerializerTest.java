package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {
    
    @Mock
    private JsonGenerator generator;
    
    @Mock
    private SerializerProvider provider;
    
    private NumberSerializer serializer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value);
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("12345678901234567890");
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value);
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 123;
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value.intValue());
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        Long value = 123L;
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value.longValue());
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 123.45;
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value.doubleValue());
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 123.45f;
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value.floatValue());
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 123;
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value.intValue());
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        Short value = 123;
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value.intValue());
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testSerializeOtherNumber() throws IOException {
        Number value = mock(Number.class);
        when(value.toString()).thenReturn("123");
        
        serializer.serialize(value, generator, provider);
        
        verify(generator).writeNumber(value.toString());
        verifyNoMoreInteractions(generator);
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Number.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        assertEquals("integer", schema.get("type").textValue());
        assertTrue(schema.get("required").booleanValue());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        if (serializer._isInt) {
            verify(visitor).expectIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
        } else {
            Class<?> handledType = serializer.handledType();
            if (handledType == BigDecimal.class) {
                verify(visitor).expectFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
            } else {
                verify(visitor).expectNumberFormat(typeHint);
            }
        }
    }
}