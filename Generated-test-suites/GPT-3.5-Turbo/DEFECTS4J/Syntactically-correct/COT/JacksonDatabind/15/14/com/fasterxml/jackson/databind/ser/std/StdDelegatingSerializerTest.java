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

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer stdDelegatingSerializer;

    @Before
    public void setup() {
        stdDelegatingSerializer = mock(StdDelegatingSerializer.class);
    }

    @Test
    public void testSerialize() throws IOException {
        // Prepare
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Execute
        stdDelegatingSerializer.serialize(value, gen, provider);

        // Verify
        verify(stdDelegatingSerializer, times(1)).serialize(value, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        // Prepare
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        // Execute
        stdDelegatingSerializer.serializeWithType(value, gen, provider, typeSer);

        // Verify
        verify(stdDelegatingSerializer, times(1)).serializeWithType(value, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty_deprecated() {
        // Prepare
        Object value = new Object();

        // Execute
        boolean result = stdDelegatingSerializer.isEmpty(value);

        // Verify
        verify(stdDelegatingSerializer, times(1)).isEmpty(value);
    }

    @Test
    public void testIsEmpty() {
        // Prepare
        SerializerProvider provider = mock(SerializerProvider.class);
        Object value = new Object();

        // Execute
        boolean result = stdDelegatingSerializer.isEmpty(provider, value);

        // Verify
        verify(stdDelegatingSerializer, times(1)).isEmpty(provider, value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        // Prepare
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        // Execute
        JsonNode result = stdDelegatingSerializer.getSchema(provider, typeHint);

        // Verify
        verify(stdDelegatingSerializer, times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_optional() throws JsonMappingException {
        // Prepare
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;

        // Execute
        JsonNode result = stdDelegatingSerializer.getSchema(provider, typeHint, isOptional);

        // Verify
        verify(stdDelegatingSerializer, times(1)).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Prepare
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        // Execute
        stdDelegatingSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Verify
        verify(stdDelegatingSerializer, times(1)).acceptJsonFormatVisitor(visitor, typeHint);
    }
}