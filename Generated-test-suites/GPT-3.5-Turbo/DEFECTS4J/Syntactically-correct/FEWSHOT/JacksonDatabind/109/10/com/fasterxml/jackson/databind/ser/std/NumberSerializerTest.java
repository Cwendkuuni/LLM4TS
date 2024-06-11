package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializerTest {
    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    
    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        JsonSerializer<?> contextualSerializer = numberSerializer.createContextual(serializerProvider, beanProperty);
        assertEquals(numberSerializer, contextualSerializer);
        
        beanProperty = null;
        contextualSerializer = numberSerializer.createContextual(serializerProvider, beanProperty);
        assertEquals(numberSerializer, contextualSerializer);
    }
    
    @Test
    public void testSerialize() throws IOException {
        Number value = mock(BigDecimal.class);
        Mockito.when(value instanceof BigDecimal).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber((BigDecimal) value);
        
        value = mock(BigInteger.class);
        Mockito.when(value instanceof BigInteger).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber((BigInteger) value);
        
        value = mock(Long.class);
        Mockito.when(value instanceof Long).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(value.longValue());
        
        value = mock(Double.class);
        Mockito.when(value instanceof Double).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(value.doubleValue());
        
        value = mock(Float.class);
        Mockito.when(value instanceof Float).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(value.floatValue());
        
        value = mock(Integer.class);
        Mockito.when(value instanceof Integer).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
        
        value = mock(Byte.class);
        Mockito.when(value instanceof Byte).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
        
        value = mock(Short.class);
        Mockito.when(value instanceof Short).thenReturn(true);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(value.intValue());
        
        value = mock(Number.class);
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema() {
        Type typeHint = mock(Type.class);
        JsonNode schema = numberSerializer.getSchema(serializerProvider, typeHint);
        assertEquals("number", schema.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        Class<?> handledType = BigInteger.class;
        numberSerializer = new NumberSerializer(handledType);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).expectNumberFormat(typeHint);
        
        handledType = BigDecimal.class;
        numberSerializer = new NumberSerializer(handledType);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).visitFloatFormat(numberSerializer, typeHint, JsonParser.NumberType.BIG_DECIMAL);
        
        handledType = Integer.class;
        numberSerializer = new NumberSerializer(handledType);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).visitIntFormat(numberSerializer, typeHint, JsonParser.NumberType.BIG_INTEGER);
        
        handledType = Float.class;
        numberSerializer = new NumberSerializer(handledType);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).visitIntFormat(numberSerializer, typeHint, JsonParser.NumberType.BIG_INTEGER);
    }
}