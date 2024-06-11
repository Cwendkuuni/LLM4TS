
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonFormatVisitorWrapper jsonFormatVisitorWrapper;

    @Mock
    private JavaType javaType;

    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Converter<Object, Object> converter = mock(Converter.class);
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        JsonSerializer<?> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        serializer.resolve(serializerProvider);

        verify((ResolvableSerializer) delegateSerializer, times(1)).resolve(serializerProvider);
    }

    @Test
    public void testCreateContextualWithNullDelSer() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        when(serializer._delegateType).thenReturn(null);
        when(serializer._converter.getOutputType(serializerProvider.getTypeFactory())).thenReturn(javaType);
        JsonSerializer<?> delSer = mock(JsonSerializer.class);
        when(serializerProvider.findValueSerializer(javaType)).thenReturn(delSer);
        when(delSer instanceof ContextualSerializer).thenReturn(true);
        when(serializerProvider.handleSecondaryContextualization(delSer, property)).thenReturn(delSer);

        JsonSerializer<?> result = serializer.createContextual(serializerProvider, property);

        assertEquals(serializer, result);
        verify(serializerProvider, times(1)).findValueSerializer(javaType);
        verify(serializerProvider, times(1)).handleSecondaryContextualization(delSer, property);
    }

    @Test
    public void testCreateContextualWithNonNullDelSer() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        when(serializer._delegateType).thenReturn(null);
        when(serializer._delegateSerializer).thenReturn(mock(JsonSerializer.class));

        JsonSerializer<?> result = serializer.createContextual(serializerProvider, property);

        assertEquals(serializer, result);
        verify(serializerProvider, times(0)).findValueSerializer(javaType);
        verify(serializerProvider, times(0)).handleSecondaryContextualization(serializer._delegateSerializer, property);
    }

    @Test
    public void testSerializeNull() throws IOException {
        Object value = null;

        serializer.serialize(value, jsonGenerator, serializerProvider);

        verify(serializerProvider, times(1)).defaultSerializeNull(jsonGenerator);
    }

    @Test
    public void testSerializeNonNull() throws IOException {
        Object value = new Object();

        Object delegateValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer._converter = converter;

        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        serializer.serialize(value, jsonGenerator, serializerProvider);

        verify(serializerProvider, times(0)).defaultSerializeNull(jsonGenerator);
        verify(delegateSerializer, times(1)).serialize(delegateValue, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object value = new Object();

        Object delegateValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer._converter = converter;

        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        serializer._delegateSerializer = delegateSerializer;

        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        serializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);

        verify(serializerProvider, times(0)).defaultSerializeNull(jsonGenerator);
        verify(delegateSerializer, times(1)).serializeWithType(delegateValue, jsonGenerator, serializerProvider, typeSerializer);
    }

    @Test
    public void testIsEmpty() {
        Object value = new Object();

        Object delegateValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer._converter = converter;

        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);
        serializer._delegateSerializer = delegateSerializer;

        boolean result1 = serializer.isEmpty(serializerProvider, value);
        boolean result2 = serializer.isEmpty(serializerProvider, null);

        assertEquals(true, result1);
        assertEquals(true, result2);
        verify(delegateSerializer, times(1)).isEmpty(delegateValue);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type type = mock(Type.class);
        JsonNode schema = mock(JsonNode.class);
        when(serializer._delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) serializer._delegateSerializer).getSchema(serializerProvider, type)).thenReturn(schema);

        JsonNode result = serializer.getSchema(serializerProvider, type);

        assertEquals(schema, result);
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        Type type = mock(Type.class);
        boolean isOptional = true;
        JsonNode schema = mock(JsonNode.class);
        when(serializer._delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) serializer._delegateSerializer).getSchema(serializerProvider, type, isOptional)).thenReturn(schema);

        JsonNode result = serializer.getSchema(serializerProvider, type, isOptional);

        assertEquals(schema, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        verify(serializer._delegateSerializer, times(1)).acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);
    }

    @Test
    public void testConvertValue() {
        Object value = new Object();

        Object delegateValue = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer._converter = converter;

        Object result = serializer.convertValue(value);

        assertEquals(delegateValue, result);
        verify(converter, times(1)).convert(value);
    }
}

