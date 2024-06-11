package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class StdSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;
    @Mock
    private SerializerProvider serializerProvider;
    @Mock
    private JsonFormatVisitorWrapper jsonFormatVisitorWrapper;
    @Mock
    private JavaType javaType;
    @Mock
    private BeanProperty beanProperty;
    @Mock
    private AnnotatedMember annotatedMember;
    @Mock
    private FilterProvider filterProvider;
    @Mock
    private PropertyFilter propertyFilter;
    @Mock
    private Converter<Object, Object> converter;
    @Mock
    private JsonSerializer<Object> valueSerializer;

    private StdSerializer<Object> stdSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) throws JsonMappingException {
                return null;
            }
        };
    }

    @Test
    public void testHandledType() {
        Class<?> handledType = stdSerializer.handledType();
        assert (handledType.equals(Object.class));
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        Throwable throwable = new Throwable();
        Object bean = new Object();
        String fieldName = "fieldName";
        stdSerializer.wrapAndThrow(serializerProvider, throwable, bean, fieldName);
        verify(serializerProvider, times(1)).isEnabled(SerializationFeature.WRAP_EXCEPTIONS);
        verify(serializerProvider, times(1)).throwSerializeException(eq(jsonGenerator), eq("test"), eq(throwable));
    }

    @Test
    public void testWrapAndThrowWithDefaultValue() throws IOException {
        Throwable throwable = new Throwable();
        Object bean = new Object();
        String fieldName = "fieldName";
        SerializerProvider testSerializerProvider = null;
        stdSerializer.wrapAndThrow(testSerializerProvider, throwable, bean, fieldName);
        verify(jsonGenerator, times(1)).writeStartObject();
    }

    @Test
    public void testWrapAndThrowIfInstanceOfError() throws IOException {
        Throwable error = new Error();
        Object bean = new Object();
        String fieldName = "fieldName";
        try {
            stdSerializer.wrapAndThrow(serializerProvider, error, bean, fieldName);
        } catch (Error e) {
            assert (e.equals(error));
        }
    }

    @Test
    public void testWrapAndThrowIfInstanceOfIOException() throws IOException {
        Throwable ioException = new IOException();
        Object bean = new Object();
        String fieldName = "fieldName";
        stdSerializer.wrapAndThrow(serializerProvider, ioException, bean, fieldName);
        verify(serializerProvider, times(2)).isEnabled(SerializationFeature.WRAP_EXCEPTIONS);
        verify(serializerProvider, times(1)).throwWriteException(eq(jsonGenerator), eq(ioException));
    }

    @Test
    public void testWrapAndThrowIfInstanceOfIOExceptionWithoutWrapping() throws IOException {
        Throwable ioException = new IOException();
        Object bean = new Object();
        String fieldName = "fieldName";
        boolean wrap = false;
        when(serializerProvider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(wrap);
        stdSerializer.wrapAndThrow(serializerProvider, ioException, bean, fieldName);
        verify(jsonGenerator, times(1)).writeStartObject();
    }

    @Test
    public void testWrapAndThrowIfInstanceOfRuntimeException() throws IOException {
        Throwable runtimeException = new RuntimeException();
        Object bean = new Object();
        String fieldName = "fieldName";
        boolean wrap = false;
        when(serializerProvider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(wrap);
        try {
            stdSerializer.wrapAndThrow(serializerProvider, runtimeException, bean, fieldName);
        } catch (RuntimeException e) {
            assert (e.equals(runtimeException));
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        Object prop = new Object();
        when(serializerProvider.getAnnotationIntrospector()).thenReturn(null);
        JsonSerializer<?> convertingContentSerializer = stdSerializer.findConvertingContentSerializer(serializerProvider, (BeanProperty) prop, valueSerializer);
        assert (convertingContentSerializer.equals(valueSerializer));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        Object filterId = new Object();
        Object valueToFilter = new Object();
        when(serializerProvider.getFilterProvider()).thenReturn(filterProvider);
        when(filterProvider.findPropertyFilter(filterId, valueToFilter)).thenReturn(propertyFilter);
        PropertyFilter testPropertyFilter = stdSerializer.findPropertyFilter(serializerProvider, filterId, valueToFilter);
        assert (testPropertyFilter.equals(propertyFilter));
    }

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<?> nonDefaultSerializer = mock(JsonSerializer.class);
        when(nonDefaultSerializer.getClass()).thenReturn(JsonSerializer.class);
        assert (!stdSerializer.isDefaultSerializer(nonDefaultSerializer));

        JsonSerializer<?> defaultSerializer = mock(JsonSerializer.class);
        when(defaultSerializer.getClass()).thenReturn(JsonSerializer.class);
        assert (!stdSerializer.isDefaultSerializer(defaultSerializer));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = new Object().getClass();
        JsonNode schema = stdSerializer.getSchema(serializerProvider, typeHint);
        assert (schema == null);
    }

    @Test
    public void testGetSchemaWithTypeHint() throws JsonMappingException {
        Type typeHint = new Object().getClass();
        boolean isOptional = true;
        JsonNode schema = stdSerializer.getSchema(serializerProvider, typeHint, isOptional);
        assert (schema == null);
    }

    @Test
    public void testCreateObjectNode() {
        ObjectNode objectNode = stdSerializer.createObjectNode();
        assert (objectNode instanceof ObjectNode);
    }

    @Test
    public void testCreateSchemaNode() {
        String type = "type";
        ObjectNode objectNode = stdSerializer.createSchemaNode(type);
        assert (objectNode instanceof ObjectNode);
    }

    @Test
    public void testCreateSchemaNodeWithOptional() {
        String type = "type";
        boolean isOptional = true;
        ObjectNode objectNode = stdSerializer.createSchemaNode(type, isOptional);
        assert (objectNode instanceof ObjectNode);
        assert (objectNode.path("required").asBoolean() == !isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        stdSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        verify(jsonFormatVisitorWrapper, times(1)).expectAnyFormat(javaType);
    }

    @Test
    public void testToJsonFieldValue() {
        stdSerializer.toBeanFieldValue(Object.class, "value");
    }

    @Test
    public void testToBeanFieldValue() {
        stdSerializer.toBeanFieldValue(Object.class, "value");
    }

    @Test
    public void testToStringValue() {
        stdSerializer.toStringValue(Object.class, "value");
    }
}