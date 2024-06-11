package com.fasterxml.jackson.databind.ser.std; // make sure the package name is correct

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for StdSerializer class.
 */
public class StdSerializerTest {

    private AnnotationIntrospector annotationIntrospector;
    private BeanProperty beanProperty;
    private Converter<Object, Object> converter;
    private FilterProvider filterProvider;
    private JsonNodeFactory jsonNodeFactory;
    private ObjectNode objectNode;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        annotationIntrospector = mock(AnnotationIntrospector.class);
        beanProperty = mock(BeanProperty.class);
        converter = mock(Converter.class);
        filterProvider = mock(FilterProvider.class);
        jsonNodeFactory = mock(JsonNodeFactory.class);
        objectNode = mock(ObjectNode.class);
        serializerProvider = new SerializerProvider() {
            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return annotationIntrospector;
            }

            @Override
            public Object readValue(Class<?> type, Object value) {
                return null;
            }

            @Override
            public ObjectMapper getObjectMapper() {
                return null;
            }

            @Override
            public JsonNodeFactory getNodeFactory() {
                return jsonNodeFactory;
            }

            @Override
            public Converter<?> findValueSerializer(Class<?> type) {
                return null;
            }

            @Override
            public Converter<Object, String> findValueSerializer(Class<String> type) {
                return null;
            }

            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
                return null;
            }

            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer, FilterProvider filterProvider) {
                return null;
            }

            @Override
            public FilterProvider getFilterProvider() {
                return filterProvider;
            }

            @Override
            public void setFilterProvider(FilterProvider filterProvider) {
                this.filterProvider = filterProvider;
            }
        };
    }

    @Test
    public void testStdSerializerConstructorWithClass() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    public void testStdSerializerConstructorWithJavaType() throws Exception {
        JavaType javaType = mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(String.class);
        StdSerializer<String> stdSerializer = new StdSerializer<String>(javaType);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    public void testStdSerializerConstructorWithClassAndBoolean() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class, false);
        assertEquals(String.class, stdSerializer.handledType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdSerializerConstructorWithNullType() throws Exception {
        new StdSerializer<String>(null);
    }

    @Test
    public void testSerializeMethod() throws Exception {
        String value = "test value";
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = new SerializerProvider();

        when(annotationIntrospector.findPropertySerializer(beanProperty, serializerProvider)).thenReturn(stdSerializer);
        when(jsonGenerator.writeString(value)).thenReturn(value);

        stdSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeFieldName("test value");
        verify(jsonGenerator).writeString(value);
        verify(jsonGenerator).writeEndObject();
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeMethodWithException() throws Exception {
        String value = "test value";
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = new SerializerProvider();

        when(annotationIntrospector.findPropertySerializer(beanProperty, serializerProvider)).thenReturn(stdSerializer);
        doThrow(JsonProcessingException.class).when(jsonGenerator).writeString(value);

        stdSerializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetSchemaMethod() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        SchemaAware schemaAware = mock(SchemaAware.class);
        SerializerProvider serializerProvider = new SerializerProvider();

        when(schemaAware.getSchemaProvider()).thenReturn(serializerProvider);

        JsonNode jsonNode = stdSerializer.getSchema(serializerProvider, String.class);
        assertEquals("string", jsonNode.toString());
    }

    @Test
    public void testGetSchemaMethodWithIsOptionalTrue() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        SchemaAware schemaAware = mock(SchemaAware.class);
        SerializerProvider serializerProvider = new SerializerProvider();

        when(schemaAware.getSchemaProvider()).thenReturn(serializerProvider);

        JsonNode jsonNode = stdSerializer.getSchema(serializerProvider, String.class, true);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        assertTrue(objectNode.has("required"));
        assertFalse(objectNode.get("required").asBoolean());
        assertEquals("string", objectNode.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);

        when(jsonFormatVisitorWrapper.expectAnyFormat(String.class)).thenReturn(null);

        stdSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, String.class);
    }

    @Test
    public void testWrapAndThrowMethodWithNullProvider() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        Throwable t = new IOException("test exception");

        stdSerializer.wrapAndThrow(null, t, null, null);

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testWrapAndThrowMethodWithNullBeanOrFieldName() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        Throwable t = new IOException("test exception");
        SerializerProvider serializerProvider = new SerializerProvider();

        stdSerializer.wrapAndThrow(serializerProvider, t, null, 0);

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testWrapAndThrowMethodWithJsonMappingException() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        Throwable t = new JsonMappingException("test exception");
        SerializerProvider serializerProvider = new SerializerProvider();

        stdSerializer.wrapAndThrow(serializerProvider, t, null, 0);

        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testWrapAndThrowMethodWithNonMappingException() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        Throwable t = new RuntimeException("test exception");
        SerializerProvider serializerProvider = new SerializerProvider();

        serializerProvider.setEnabled(SerializationFeature.WRAP_EXCEPTIONS, false);

        stdSerializer.wrapAndThrow(serializerProvider, t, null, 0);

        fail("Expected RuntimeException to be thrown");
    }

    @Test
    public void testWrapAndThrowMethodWithWrappingEnabled() throws Exception {
        StdSerializer<String> stdSerializer = new StdSerializer<String>(String.class);
        Throwable t = new RuntimeException("test exception");
        SerializerProvider serializerProvider = new SerializerProvider();

        stdSerializer.wrapAndThrow(serializerProvider, t, null, 0);

        JsonMappingException jsonMappingException = assertThrows(JsonMappingException.class, () -> {
            throw stdSerializer.wrapAndThrow(serializerProvider, t, null, 0);
        });
        assertEquals("test exception", jsonMappingException.getCause().getMessage());
    }

    @Test
    public void testFindConvertingContentSerializerMethod() throws Exception {
        StdSerializer<List<String>> stdSerializer = new StdSerializer<List<String>>(List.class);
        JavaType javaType = mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(String[].class);
        JsonSerializer<String[]> jsonSerializer = mock(JsonSerializer.class);

        Converter<Object, Object> converter = mock(Converter.class);
        when(converter.getOutputType(ObjectNode.class)).thenReturn(javaType);

        ObjectNode objectNode = mock(ObjectNode.class);
        when(jsonSerializer.serializeValues(standardSerializedForm(objectNode), serializerProvider)).thenReturn(objectNode);

        when(serializerProvider.findValueSerializer(javaType)).thenReturn(jsonSerializer);

        PropertyFilter propertyFilter = mock(PropertyFilter.class);
        when(filterProvider.findPropertyFilter("test", null)).thenReturn(propertyFilter);

        StdSerializer<Object> stdWrapperSerializer = new StdSerializer<Object>(Object.class);

        JsonSerializer<Object> result = stdSerializer.findConvertingContentSerializer(serializerProvider, beanProperty, stdWrapperSerializer);

        assertEquals(jsonSerializer.getClass(), result.getClass());
    }

    @Test
    public void testFindPropertyFilterMethod() throws Exception {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class);
        Object filterId = "test";
        Object valueToFilter = new Object();

        PropertyFilter propertyFilter = stdSerializer.findPropertyFilter(serializerProvider, filterId, valueToFilter);

        assertEquals(propertyFilter.getClass(), PropertyFilter.class);
    }

    private static ObjectNode standardSerializedForm(ObjectNode node) {
        node.put("value", "test");
        node.setNull("disallow-null");
        return node;
    }

    
    
}