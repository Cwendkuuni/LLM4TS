package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {
    
    @Mock
    private SerializerProvider provider;
    
    @Mock
    private BeanProperty property;
    
    @Mock
    private JsonGenerator g;
    
    @Mock
    private JsonFormatVisitorWrapper visitor;
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.createContextual(provider, property);
        // TODO: Add assertions
    }
    
    @Test
    public void testSerialize() throws IOException {
        Number value = Mockito.mock(Number.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.serialize(value, g, provider);
        // TODO: Add assertions
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.getSchema(provider, Number.class);
        // TODO: Add assertions
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = Mockito.mock(JavaType.class);
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // TODO: Add assertions
    }
}