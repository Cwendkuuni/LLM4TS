package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer stdDelegatingSerializer;

    @Before
    public void setUp() {
        Converter<?,?> converter = null;
        stdDelegatingSerializer = new StdDelegatingSerializer<String>(String.class, converter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = null;
        stdDelegatingSerializer.resolve(provider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = null;
        BeanProperty property = null;
        stdDelegatingSerializer.createContextual(provider, property);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        stdDelegatingSerializer.getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_IsOptional() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = true;
        stdDelegatingSerializer.getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        stdDelegatingSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}