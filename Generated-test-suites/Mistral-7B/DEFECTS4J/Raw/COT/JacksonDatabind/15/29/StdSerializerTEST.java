package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.Wrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link StdSerializer}.
 */
public class StdSerializerTest
{
    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    @Mock
    private AnnotatedMember mockAnnotatedMember;

    @Mock
    private FilterProvider mockFilterProvider;

    @Mock
    private JsonNodeFactory mockJsonNodeFactory;

    @Mock
    private TypeFactory mockTypeFactory;

    @Mock
    private ObjectNode mockObjectNode;

    @Mock
    private JsonMappingException mockJsonMappingException;

    @Mock
    private Converter<Object, Object> mockConverter;

    @Mock
    private JsonSerializer<Object> mockDelegateSerializer;

    @InjectMocks
    private StdSerializer<Object> stdSerializer;

    @Before
    public void setupMocks()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStdSerializerConstructorWithClass()
    {
        Class<Object> handledType = Object.class;

        StdSerializer<Object> serializer = new StdSerializer<>(handledType);

        assertEquals(handledType, serializer.handledType());
    }

    @Test
    public void testStdSerializerConstructorWithJavaType()
    {
        JavaType javaType = mock(JavaType.class);
        Class<Object> handledType = Object.class;

        StdSerializer<Object> serializer = new StdSerializer<>(javaType);

        assertEquals(handledType, serializer.handledType());
        verify(javaType).getRawClass();
    }

    @Test
    public void testStdSerializerConstructorWithClassAndBoolean()
    {
        Class<Object> handledType = Object.class;
        boolean dummy = true;

        StdSerializer<Object> serializer = new StdSerializer<>(handledType, dummy);

        assertEquals(handledType, serializer.handledType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdSerializerWithNullHandledType()
    {
        StdSerializer<Object> serializer = new StdSerializer<>(null);
    }

    @Test
    public void testSerializeWithNullValueAndProvider() throws IOException
    {
        Object value = null;

        stdSerializer.serialize(value, mockJsonGenerator, null);

        verify(mockJsonGenerator, never()).writeStartObject();
        verify(mockJsonGenerator, never()).writeFieldName("_class");
        verify(mockJsonGenerator, never()).writeString("null");
    }

    @Test
    public void testSerializeWithValueAndProvider() throws IOException
    {
        Object value = new Object();

        when(mockJsonGenerator.writeStartObject()).thenReturn(mockObjectNode);

        stdSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeStartObject();
        verify(mockJsonGenerator).writeFieldName("_class");
        verify(mockJsonGenerator).writeString(Object.class.getName());
        verify(mockJsonGenerator).writeEndObject();
    }

    @Test
    public void testGetSchemaWithNullProvider()
    {
        Type typeHint = Object.class;

        Object schema = stdSerializer.getSchema(null, typeHint);

        assertNull(schema);
    }

    @Test
    public void testGetSchemaWithProvider()
    {
        Object schema = stdSerializer.getSchema(mockSerializerProvider, typeHint());

        assertEquals(createSchemaNode("string").valueSeqAsString(), schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithNullProviderAndTypeHint()
    {
        stdSerializer.acceptJsonFormatVisitor(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithNullProviderAndTypeHintThrowException()
    {
        stdSerializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithProviderAndTypeHint()
    {
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        stdSerializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);

        verify(visitorWrapper).expectAnyFormat(typeHint);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithNullProvider() throws IOException
    {
        Throwable t = new RuntimeException("Test error");

        stdSerializer.wrapAndThrow(null, t, null, null);
    }

    @Test
    public void testWrapAndThrowWithProvider() throws IOException
    {
        Throwable t = new RuntimeException("Test error");
        when(mockSerializerProvider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);

        stdSerializer.wrapAndThrow(mockSerializerProvider, t, null, null);

        verify(mockJsonMappingException).wrapWithPath(t, null, null);
    }

    @Test
    public void testIsDefaultSerializerWithDefaultSerializer()
    {
        JsonSerializer<Object> defaultSerializer = mock(JsonSerializer.class);
        when(defaultSerializer.getClass()).thenReturn(JacksonStdImpl.class);

        assertTrue(stdSerializer.isDefaultSerializer(defaultSerializer));
    }

    @Test
    public void testIsDefaultSerializerWithCustomSerializer()
    {
        JsonSerializer<Object> customSerializer = mock(JsonSerializer.class);
        when(customSerializer.getClass()).thenReturn(new Class<>("com.example.CustomSerializer"));

        assertFalse(stdSerializer.isDefaultSerializer(customSerializer));
    }

    @Test
    public void testFindConvertingContentSerializerWithNullProvider() throws JsonMappingException
    {
        BeanProperty prop = mock(BeanProperty.class);

        JsonSerializer<Object> serializer = stdSerializer.findConvertingContentSerializer(null, prop, null);

        assertNull(serializer);
    }

    @Test
    public void testFindConvertingContentSerializerWithProvider() throws JsonMappingException
    {
        BeanProperty prop = mock(BeanProperty.class);
        when(mockSerializerProvider.getFilterProvider()).thenReturn(mockFilterProvider);
        when(mockFilterProvider.findPropertyFilter(eq("converter.id"), anyObject())).thenReturn(mockPropertyFilter());

        JsonSerializer<Object> serializer = stdSerializer.findConvertingContentSerializer(mockSerializerProvider, prop, null);

        verify(mockSerializerProvider).getFilterProvider();
        verify(mockFilterProvider).findPropertyFilter(eq("converter.id"), anyObject());

        assertEquals(mockPropertyFilter(), serializer);
    }

    @Test
    public void testFindPropertyFilterWithNullProvider() throws JsonMappingException
    {
        Object filterId = "filter.id";
        Object valueToFilter = new Object();

        PropertyFilter propertyFilter = stdSerializer.findPropertyFilter(null, filterId, valueToFilter);

        assertNull(propertyFilter);
    }

    @Test
    public void testFindPropertyFilterWithProvider() throws JsonMappingException
    {
        Object filterId = "filter.id";
        Object valueToFilter = new Object();

        PropertyFilter propertyFilter = stdSerializer.findPropertyFilter(mockSerializerProvider, filterId, valueToFilter);

        verify(mockFilterProvider).findPropertyFilter(eq(filterId), eq(valueToFilter));

        assertEquals(mockPropertyFilter(), propertyFilter);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithNullFilterProvider() throws JsonMappingException
    {
        Object filterId = "filter.id";
        Object valueToFilter = new Object();

        stdSerializer.findPropertyFilter(null, filterId, valueToFilter);
    }

    private JavaType typeHint()
    {
        return mockTypeFactory.constructType(Object[].class);
    }

    private PropertyFilter mockPropertyFilter()
    {
        return mock(PropertyFilter.class);
    }

    private ObjectNode createSchemaNode(String type)
    {
        return (ObjectNode) stdSerializer.createSchemaNode(type, false);
    }
}