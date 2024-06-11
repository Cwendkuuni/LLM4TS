package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer stdDelegatingSerializer;
    private Converter<Object, Object> converter;
    private JavaType delegateType;
    private JsonSerializer<Object> delegateSerializer;
    private Object value;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private TypeSerializer typeSerializer;

    @Before
    public void setUp() {
        converter = mock(Converter.class);
        delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);
        value = new Object();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
        typeSerializer = mock(TypeSerializer.class);

        stdDelegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);

        stdDelegatingSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(delegateSerializer, times(1)).serialize(convertedValue, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        when(converter.convert(value)).thenReturn(null);

        stdDelegatingSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNull();
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);

        stdDelegatingSerializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);

        verify(delegateSerializer, times(1)).serializeWithType(convertedValue, jsonGenerator, serializerProvider, typeSerializer);
    }

    @Test
    public void testIsEmpty() {
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);
        when(delegateSerializer.isEmpty(convertedValue)).thenReturn(true);

        assertEquals(true, stdDelegatingSerializer.isEmpty(serializerProvider, value));
    }

    @Test
    public void testIsEmptyWithNullValue() {
        when(converter.convert(value)).thenReturn(null);

        assertEquals(true, stdDelegatingSerializer.isEmpty(serializerProvider, value));
    }

    @Test
    public void testGetSchemaWithSchemaAwareDelegateSerializer() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        JsonNode expectedSchema = mock(JsonNode.class);
        when(((SchemaAware) delegateSerializer).getSchema(serializerProvider, delegateType)).thenReturn(expectedSchema);

        JsonNode schema = stdDelegatingSerializer.getSchema(serializerProvider, delegateType);

        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testGetSchemaWithNonSchemaAwareDelegateSerializer() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);

        JsonNode schema = stdDelegatingSerializer.getSchema(serializerProvider, delegateType);

        assertNull(schema);
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        JsonNode expectedSchema = mock(JsonNode.class);
        when(((SchemaAware) delegateSerializer).getSchema(serializerProvider, delegateType, true)).thenReturn(expectedSchema);

        JsonNode schema = stdDelegatingSerializer.getSchema(serializerProvider, delegateType, true);

        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testGetSchemaWithoutOptional() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);

        JsonNode schema = stdDelegatingSerializer.getSchema(serializerProvider, delegateType, true);

        assertNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        stdDelegatingSerializer.acceptJsonFormatVisitor(visitor, delegateType);

        verify(delegateSerializer, times(1)).acceptJsonFormatVisitor(visitor, delegateType);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithDelegateError() {
        StdDelegatingSerializer subclassSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer) {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
                return this;
            }
        };

        subclassSerializer.withDelegate(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testWithDelegate() {
        StdDelegatingSerializer subclassSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer) {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
                return subclassSerializer;
            }
        };

        StdDelegatingSerializer newSerializer = subclassSerializer.withDelegate(converter, delegateType, delegateSerializer);

        assertEquals(subclassSerializer, newSerializer);
    }

    // Remaining methods are simple, and not included in the test cases as the implementation is straightforward
}