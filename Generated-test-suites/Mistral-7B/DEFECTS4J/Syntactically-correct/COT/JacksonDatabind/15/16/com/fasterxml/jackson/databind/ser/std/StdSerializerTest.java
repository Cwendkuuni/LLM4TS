package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class StdSerializerTest {

    private StdSerializer<Object> serializer;
    private ObjectNode schemaNode;
    private JsonSerializer<Object> delegateSerializer;
    private FilterProvider filterProvider;

    @Before
    public void setup() throws Exception {
        serializer = new StdSerializer<Object>(Object.class) {};
        schemaNode = mock(ObjectNode.class);
        delegateSerializer = mock(JsonSerializer.class);
        filterProvider = mock(FilterProvider.class);
    }

    @Test
    public void testHandledType() throws Exception {
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws Exception {
        Object value = new Object();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(delegateSerializer.serialize(value, generator, provider)).thenReturn(null);

        serializer.serialize(value, generator, provider);

        // Delegate serializer should be called
        ArgumentCaptor<Object> valueArg = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<JsonGenerator> genArg = ArgumentCaptor.forClass(JsonGenerator.class);
        ArgumentCaptor<SerializerProvider> provArg = ArgumentCaptor.forClass(SerializerProvider.class);
        when(delegateSerializer.serialize(valueArg.capture(), genArg.capture(), provArg.capture()))
                .thenReturn(null);

        assertEquals(value, valueArg.getValue());
        assertEquals(generator, genArg.getValue());
        assertEquals(provider, provArg.getValue());
    }

    @Test
    public void testGetSchema() throws Exception {
        when(serializer.getSchema(mock(SerializerProvider.class), null)).thenReturn(schemaNode);
        assertEquals(schemaNode, serializer.getSchema(mock(SerializerProvider.class), null));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaWithException() throws Exception {
        when(serializer.getSchema(mock(SerializerProvider.class), null)).thenThrow(new JsonMappingException("Exception message"));
        serializer.getSchema(mock(SerializerProvider.class), null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(serializer.acceptJsonFormatVisitor(visitor, Object.class)).thenReturn(null);
        assertNull(serializer.acceptJsonFormatVisitor(visitor, Object.class));
    }

    @Test
    public void testWrapAndThrow() throws Exception {
        Throwable t = new IOException("IO exception message");
        when(serializer.wrapAndThrow(mock(SerializerProvider.class), t, null, 0)).thenThrow(JsonMappingException.class);
        JsonMappingException e = (JsonMappingException) serializer.wrapAndThrow(mock(SerializerProvider.class), t, null, 0);
        assertEquals("IO exception message", e.getCause().getMessage());
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        Converter<Object, Object> converter = mock(Converter.class);
        when(provider.converterInstance(mock(AnnotatedMember.class), eq(JsonSerialize.With.class))).thenReturn(converter);
        when(provider.findValueSerializer(Object.class)).thenReturn(delegateSerializer);

        JsonSerializer<Object> result = serializer.findConvertingContentSerializer(provider, null, null);
        assertEquals(delegateSerializer, result);

        ObjectNode schemaNodeSpy = mock(ObjectNode.class);
        when(provider.getAnnotationIntrospector().findSerializationContentConverter(mock(AnnotatedMember.class))).thenReturn(JsonSerialize.With.class);
        when(converter.getOutputType(mock(JsonTypeFactory.class))).thenReturn(JsonTypeFactory.instance.constructType(String.class));
        when(provider.findValueSerializer(JsonTypeFactory.instance.constructType(String.class))).thenReturn(mock(JsonSerializer.class));

        JsonSerializer<Object> result2 = serializer.findConvertingContentSerializer(provider, null, null);
        assertNotNull(result2);
        assertEquals(JsonSerializer.class, result2.getClass());
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        PropertyFilter filter = mock(PropertyFilter.class);
        when(filterProvider.findPropertyFilter("filterId", null)).thenReturn(filter);
        assertEquals(filter, serializer.findPropertyFilter(provider, "filterId", null));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithException() throws Exception {
        when(filterProvider.findPropertyFilter("filterId", null)).thenThrow(new JsonMappingException("Exception message"));
        serializer.findPropertyFilter(provider, "filterId", null);
    }
}