package com.fasterxml.jackson.databind.ser.std; // package import

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


public class StdSerializerTest {

    private ObjectNode schemaNode;
    private JsonSerializer<Object> standardSerializer;
    private FilterProvider filterProvider;

    @Before
    public void setUp() throws Exception {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        schemaNode = (ObjectNode) nodeFactory.objectNode();
        standardSerializer = mock(JsonSerializer.class);
        filterProvider = mock(FilterProvider.class);
    }

    @Test
    public void testAbstractStdSerializerDefaultConstructor() {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        assertEquals(Object.class, stdSerializer.handledType());
    }

    @Test
    public void testAbstractStdSerializerTypeConstructor() throws Exception {
        JavaType javaType = Mockito.mock(JavaType.class);
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(javaType);
        assertEquals(Object.class, stdSerializer.handledType());
    }

    @Test
    public void testAbstractStdSerializerAlternateConstructor() throws Exception {
        Class<Object> clazz = Object.class;
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(clazz, false);
        assertEquals(Object.class, stdSerializer.handledType());
    }

    @Test
    public void testSerializeMethod() throws IOException {
        Tester tester = new Tester();
        when(standardSerializer.serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class)))
                .then(tester);
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        stdSerializer.serialize(any(Object.class), mock(JsonGenerator.class), mock(SerializerProvider.class));
        verify(standardSerializer).serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class));
    }

    @Test
    public void testGetSchemaMethod() throws JsonMappingException {
        when(standardSerializer.getSchema(any(SerializerProvider.class), any(Type.class)))
                .thenReturn(schemaNode);
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        ObjectNode result = stdSerializer.getSchema(mock(SerializerProvider.class), Object.class);
        assertEquals(schemaNode, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetSchemaMethodException() throws JsonMappingException {
        when(standardSerializer.getSchema(any(SerializerProvider.class), any(Type.class)))
                .thenThrow(new JsonMappingException());
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        stdSerializer.getSchema(mock(SerializerProvider.class), Object.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        when(visitorWrapper.expectAnyFormat(any(Type.class))).thenReturn(null);
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        stdSerializer.acceptJsonFormatVisitor(visitorWrapper, Object.class);
        verify(visitorWrapper).expectAnyFormat(Object.class);
    }

    @Test
    public void testWrapAndThrowMethod() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Throwable t = new IOException("Test Exception");
        Object bean = new Object();
        String fieldName = "testField";
        when(provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);
        when(provider.getSchemaFactory()).thenReturn(JsonNodeFactory.instance);

        stdSerializer.wrapAndThrow(provider, t, bean, fieldName);
        verify(provider).getSchemaFactory();
        verify(provider).wrapErrors(eq(provider), eq(bean), anyString(), eq(fieldName), eq(t));
    }

    @Test
    public void testFindConvertingContentSerializerMethod() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonSerializer<Object> existingSerializer = mock(JsonSerializer.class);
        BeanProperty prop = mock(BeanProperty.class);

        Converter<Object, Object> conv = mock(Converter.class);
        when(conv.getOutputType(any(JsonTypeFactory.class))).thenReturn(String.class);

        when(provider.converterInstance(any(AnnotatedMember.class), any(Annotation.class)))
                .thenReturn(conv);
        when(provider.findValueSerializer(String.class)).thenReturn(existingSerializer);

        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        JsonSerializer<Object> result = stdSerializer.findConvertingContentSerializer(provider, prop, existingSerializer);

        verify(conv).getOutputType(any(JsonTypeFactory.class));
        verify(provider).findValueSerializer(String.class);
        assertEquals(existingSerializer, result);
    }

    @Test
    public void testFindPropertyFilterMethod() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Object filterId = "testFilterId";
        Object valueToFilter = new Object();
        PropertyFilter filter = mock(PropertyFilter.class);

        when(filterProvider.findPropertyFilter(filterId, valueToFilter)).thenReturn(filter);

        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        PropertyFilter result = stdSerializer.findPropertyFilter(provider, filterId, valueToFilter);

        verify(filterProvider).findPropertyFilter(filterId, valueToFilter);
        assertEquals(filter, result);
    }

    private static class Tester implements Function<Object, Void> {

        @Override
        public Void apply(Object o) throws IOException {
            return null;
        }
    }
}
