package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private JsonSerializer<Object> delegateSerializer;

    @Before
    public void setUp() {
        Converter<Object, Object> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize() {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(serializer.convertValue(value)).thenReturn(value);

        serializer.serialize(value, gen, provider);

        verify(delegateSerializer).serialize(value, gen, provider);
    }

    @Test
    public void testSerializeWithType() {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        when(serializer.convertValue(value)).thenReturn(value);

        serializer.serializeWithType(value, gen, provider, typeSer);

        verify(delegateSerializer).serializeWithType(value, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty() {
        Object value = new Object();

        when(serializer.convertValue(value)).thenReturn(value);
        when(delegateSerializer.isEmpty(value)).thenReturn(true);

        boolean result = serializer.isEmpty(value);

        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithProvider() {
        Object value = new Object();
        SerializerProvider prov = mock(SerializerProvider.class);

        when(serializer.convertValue(value)).thenReturn(value);
        when(delegateSerializer.isEmpty(prov, value)).thenReturn(true);

        boolean result = serializer.isEmpty(prov, value);

        assertEquals(true, result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode expected = mock(JsonNode.class);

        when(delegateSerializer).thenReturn(delegateSerializer);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint)).thenReturn(expected);

        JsonNode result = serializer.getSchema(provider, typeHint);

        assertEquals(expected, result);
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class, RETURNS_DEEP_STUBS);
        boolean isOptional = true;
        JsonNode expected = mock(JsonNode.class);

        when(delegateSerializer).thenReturn(delegateSerializer);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint, isOptional)).thenReturn(expected);

        JsonNode result = serializer.getSchema(provider, typeHint, isOptional);

        assertEquals(expected, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() {
        Object value = new Object();
        Object expected = new Object();

        when(serializer.getConverter().convert(value)).thenReturn(expected);

        Object result = serializer.convertValue(value);

        assertEquals(expected, result);
    }
}