
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private Converter<Object, ?> mockedConverter;
    private JavaType mockedDelegateType;
    private JsonSerializer<Object> mockedDelegateSerializer;

    @Before
    public void setup() {
        mockedConverter = Mockito.mock(Converter.class);
        mockedDelegateType = Mockito.mock(JavaType.class);
        mockedDelegateSerializer = Mockito.mock(JsonSerializer.class);

        serializer = new StdDelegatingSerializer(mockedConverter, mockedDelegateType, mockedDelegateSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);
        Mockito.when(mockedDelegateSerializer instanceof ResolvableSerializer).thenReturn(true);

        serializer.resolve(mockedProvider);

        Mockito.verify(((ResolvableSerializer) mockedDelegateSerializer)).resolve(mockedProvider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);
        BeanProperty mockedProperty = Mockito.mock(BeanProperty.class);
        ContextualSerializer mockedDelSer = Mockito.mock(ContextualSerializer.class);

        Mockito.when(mockedProvider.findValueSerializer(mockedDelegateType)).thenReturn(mockedDelegateSerializer);
        Mockito.when(mockedDelegateSerializer instanceof ContextualSerializer).thenReturn(true);
        Mockito.when(mockedProvider.handleSecondaryContextualization(mockedDelSer, mockedProperty)).thenReturn(mockedDelSer);

        JsonSerializer<?> returnedSerializer = serializer.createContextual(mockedProvider, mockedProperty);

        Assert.assertEquals(mockedDelSer, returnedSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        Object mockedValue = Mockito.mock(Object.class);
        JsonGenerator mockedGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);
        Object mockedDelegateValue = Mockito.mock(Object.class);
        Mockito.when(mockedConverter.convert(mockedValue)).thenReturn(mockedDelegateValue);

        serializer.serialize(mockedValue, mockedGenerator, mockedProvider);

        Mockito.verify(mockedDelegateSerializer).serialize(mockedDelegateValue, mockedGenerator, mockedProvider);
    }

    @Test
    public void testSerializeNull() throws IOException {
        JsonGenerator mockedGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);

        serializer.serialize(null, mockedGenerator, mockedProvider);

        Mockito.verify(mockedProvider).defaultSerializeNull(mockedGenerator);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object mockedValue = Mockito.mock(Object.class);
        JsonGenerator mockedGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);
        TypeSerializer mockedTypeSerializer = Mockito.mock(TypeSerializer.class);
        Object mockedDelegateValue = Mockito.mock(Object.class);
        Mockito.when(mockedConverter.convert(mockedValue)).thenReturn(mockedDelegateValue);

        serializer.serializeWithType(mockedValue, mockedGenerator, mockedProvider, mockedTypeSerializer);

        Mockito.verify(mockedDelegateSerializer).serializeWithType(mockedDelegateValue, mockedGenerator, mockedProvider, mockedTypeSerializer);
    }

    @Test
    public void testIsEmpty() {
        Object mockedValue = Mockito.mock(Object.class);
        Object mockedDelegateValue = Mockito.mock(Object.class);
        Mockito.when(mockedConverter.convert(mockedValue)).thenReturn(mockedDelegateValue);
        Mockito.when(mockedDelegateSerializer.isEmpty(mockedDelegateValue)).thenReturn(true);

        boolean isEmpty = serializer.isEmpty(mockedValue);

        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testIsEmptyWithProvider() {
        Object mockedValue = Mockito.mock(Object.class);
        Object mockedDelegateValue = Mockito.mock(Object.class);
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);
        Mockito.when(mockedConverter.convert(mockedValue)).thenReturn(mockedDelegateValue);
        Mockito.when(mockedDelegateSerializer.isEmpty(mockedProvider, mockedDelegateValue)).thenReturn(true);

        boolean isEmpty = serializer.isEmpty(mockedProvider, mockedValue);

        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);
        Type mockedType = Mockito.mock(Type.class);
        JsonNode mockedSchema = Mockito.mock(JsonNode.class);

        Mockito.when(mockedDelegateSerializer instanceof SchemaAware).thenReturn(true);
        Mockito.when(((SchemaAware) mockedDelegateSerializer).getSchema(mockedProvider, mockedType)).thenReturn(mockedSchema);

        JsonNode schema = serializer.getSchema(mockedProvider, mockedType);

        Assert.assertEquals(mockedSchema, schema);
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider mockedProvider = Mockito.mock(SerializerProvider.class);
        Type mockedType = Mockito.mock(Type.class);
        JsonNode mockedSchema = Mockito.mock(JsonNode.class);

        Mockito.when(mockedDelegateSerializer instanceof SchemaAware).thenReturn(true);
        Mockito.when(((SchemaAware) mockedDelegateSerializer).getSchema(mockedProvider, mockedType, true)).thenReturn(mockedSchema);

        JsonNode schema = serializer.getSchema(mockedProvider, mockedType, true);

        Assert.assertEquals(mockedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper mockedVisitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType mockedTypeHint = Mockito.mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(mockedVisitor, mockedTypeHint);

        Mockito.verify(mockedDelegateSerializer).acceptJsonFormatVisitor(mockedVisitor, mockedTypeHint);
    }
}
