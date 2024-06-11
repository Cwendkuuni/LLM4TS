package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class StdSerializerTest {

    private StdSerializer<Object> serializer;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider serializerProviderMock;

    @Before
    public void setUp() {
        serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException {
                // do nothing
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                // do nothing
            }
        };
        jsonGeneratorMock = mock(JsonGenerator.class);
        serializerProviderMock = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        serializer.serialize(value, jsonGeneratorMock, serializerProviderMock);
        verify(serializerProviderMock).getTypeFactory();
        verify(serializerProviderMock).findValueSerializer(Object.class);
        verifyNoMoreInteractions(serializerProviderMock);
        verify(jsonGeneratorMock).writeString(anyString());
        verifyNoMoreInteractions(jsonGeneratorMock);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = mock(Type.class);
        serializer.getSchema(serializerProviderMock, typeHint);
        verify(serializerProviderMock).isEnabled(SerializationFeature.WRAP_EXCEPTIONS);
        verifyNoMoreInteractions(serializerProviderMock);
        verify(serializerProviderMock).getAnnotationIntrospector();
        verify(serializerProviderMock).getTypeFactory();
        verifyNoMoreInteractions(serializerProviderMock);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), typeHint);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        Throwable t = mock(Throwable.class);
        Object bean = new Object();
        String fieldName = "fieldName";
        serializer.wrapAndThrow(serializerProviderMock, t, bean, fieldName);
        verify(serializerProviderMock).isEnabled(SerializationFeature.WRAP_EXCEPTIONS);
        verify(serializerProviderMock).isEnabled(SerializationFeature.WRAP_EXCEPTIONS);
        verifyNoMoreInteractions(serializerProviderMock);
        verify(t, times(2)).getCause();
        verify(t).getCause();
        verify(t).getCause();
        verify(jsonGeneratorMock).writeObjectFieldStart(anyString());
        verifyNoMoreInteractions(jsonGeneratorMock);
        verify(t).getCause();
        verify(jsonGeneratorMock).writeString(anyString());
        verifyNoMoreInteractions(jsonGeneratorMock);
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        BeanProperty prop = mock(BeanProperty.class);
        JsonSerializer<?> existingSerializer = mock(JsonSerializer.class);
        serializer.findConvertingContentSerializer(serializerProviderMock, prop, existingSerializer);
        verify(serializerProviderMock).getAnnotationIntrospector();
        verify(serializerProviderMock).getTypeFactory();
        verifyNoMoreInteractions(serializerProviderMock);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        Object filterId = new Object();
        Object valueToFilter = new Object();
        serializer.findPropertyFilter(serializerProviderMock, filterId, valueToFilter);
        verify(serializerProviderMock).getFilterProvider();
        verifyNoMoreInteractions(serializerProviderMock);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilter_noFilterProvider() throws JsonMappingException {
        Object filterId = new Object();
        Object valueToFilter = new Object();
        when(serializerProviderMock.getFilterProvider()).thenReturn(null);
        serializer.findPropertyFilter(serializerProviderMock, filterId, valueToFilter);
    }
}