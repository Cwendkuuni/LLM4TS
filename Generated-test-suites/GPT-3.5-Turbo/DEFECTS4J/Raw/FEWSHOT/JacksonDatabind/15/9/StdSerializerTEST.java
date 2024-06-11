package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {
    
    private StdSerializer<?> serializer;
    
    @Mock
    private SerializerProvider mockProvider;
    
    @Before
    public void setUp() {
        serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                
            }
        };
    }

    @Test
    public void testHandledType() {
        assertEquals(Object.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        serializer.serialize(new Object(), mockGenerator, mockProvider);
        verify(mockGenerator, times(1)).writeObject(any());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        assertNull(serializer.getSchema(mockProvider, null));
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonMappingException {
        assertNull(serializer.getSchema(mockProvider, null, true));
    }

    @Test
    public void testGetSchemaNode() {
        ObjectNode schema = serializer.createSchemaNode("string");
        assertNotNull(schema);
        assertEquals("string", schema.get("type").textValue());
    }

    @Test
    public void testGetSchemaNodeWithIsOptional() {
        ObjectNode schema = serializer.createSchemaNode("string", true);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").textValue());
        assertFalse(schema.get("required").booleanValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
        verify(mockVisitor, times(1)).expectAnyFormat(mockTypeHint);
    }

    @Test(expected = IOException.class)
    public void testWrapAndThrowWithIOException() throws IOException {
        serializer.wrapAndThrow(mockProvider, new IOException(), new Object(), "field");
    }

    @Test(expected = RuntimeException.class)
    public void testWrapAndThrowWithRuntimeException() throws IOException {
        serializer.wrapAndThrow(mockProvider, new RuntimeException(), new Object(), "field");
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        serializer.wrapAndThrow(mockProvider, new JsonMappingException(""), new Object(), "field");
    }

    @Test(expected = Error.class)
    public void testWrapAndThrowWithError() throws IOException {
        serializer.wrapAndThrow(mockProvider, new Error(), new Object(), "field");
    }

    @Test(expected = IOException.class)
    public void testWrapAndThrowWithIOExceptionAndIndex() throws IOException {
        serializer.wrapAndThrow(mockProvider, new IOException(), new Object(), 0);
    }

    @Test
    public void testIsDefaultSerializerWithDefaultSerializer() {
        JsonSerializer<?> mockSerializer = mock(JsonSerializer.class);
        when(ClassUtil.isJacksonStdImpl(mockSerializer)).thenReturn(true);
        assertTrue(serializer.isDefaultSerializer(mockSerializer));
    }

    @Test
    public void testIsDefaultSerializerWithCustomSerializer() {
        JsonSerializer<?> mockSerializer = mock(JsonSerializer.class);
        when(ClassUtil.isJacksonStdImpl(mockSerializer)).thenReturn(false);
        assertFalse(serializer.isDefaultSerializer(mockSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        BeanProperty mockProperty = mock(BeanProperty.class);
        AnnotatedMember mockMember = mock(AnnotatedMember.class);
        when(mockProperty.getMember()).thenReturn(mockMember);
        when(mockMember.getRawType()).thenReturn(Object.class);
        when(mockProperty.getMember()).thenReturn(mockMember);
        JsonSerializer<Object> mockValueSerializer = mock(JsonSerializer.class);
        when(mockProvider.findValueSerializer(Object.class)).thenReturn(mockValueSerializer);
        assertNull(serializer.findConvertingContentSerializer(mockProvider, mockProperty, null));
        verify(mockProvider, times(0)).converterInstance(any(), any());
    }
    
    @Test
    public void testFindConvertingContentSerializerWithConverter() throws JsonMappingException {
        BeanProperty mockProperty = mock(BeanProperty.class);
        AnnotatedMember mockMember = mock(AnnotatedMember.class);
        when(mockProperty.getMember()).thenReturn(mockMember);
        when(mockMember.getRawType()).thenReturn(Object.class);
        Converter<Object, Object> mockConverter = mock(Converter.class);
        JavaType mockDelegateType = mock(JavaType.class);
        when(mockConverter.getOutputType(mockProvider.getTypeFactory())).thenReturn(mockDelegateType);
        JsonSerializer<Object> mockValueSerializer = mock(JsonSerializer.class);
        when(mockProvider.findValueSerializer(mockDelegateType)).thenReturn(mockValueSerializer);
        StdDelegatingSerializer expectedSerializer = new StdDelegatingSerializer(mockConverter, mockDelegateType, mockValueSerializer);
        assertEquals(expectedSerializer, serializer.findConvertingContentSerializer(mockProvider, mockProperty, null));
        verify(mockProvider, times(1)).converterInstance(mockMember, null);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        FilterProvider mockFilterProvider = mock(FilterProvider.class);
        when(mockProvider.getFilterProvider()).thenReturn(mockFilterProvider);
        assertNull(serializer.findPropertyFilter(mockProvider, null, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithNullProvider() throws JsonMappingException {
        Mock mock = mock(Mock.class);
        assertNull(serializer.findPropertyFilter(null, mock, mock));
    }
    
    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithUnknownId() throws JsonMappingException {
        FilterProvider mockFilterProvider = mock(FilterProvider.class);
        when(mockProvider.getFilterProvider()).thenReturn(mockFilterProvider);
        when(mockFilterProvider.findPropertyFilter(null, null)).thenReturn(null);
        serializer.findPropertyFilter(mockProvider, null, null);
    }
}