package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link StdSerializer} abstract class.
 */
public class StdSerializerTest {

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        assertTrue(StdSerializer.class.isInstance(serializer));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(null, false)));
        assertTrue(StdSerializer.class.isInstance(new StdSerializer<Object>(new TypeReference<Object>() {})));
        assertFalse(StdSerializer.class.isInstance(new MyCustomSerializer<>()));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        ObjectNode schema = serializer.getSchema(mock(SerializerProvider.class), Object.class);
        assertEquals("string", schema.path("type").asText());
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        ObjectNode schema = serializer.createSchemaNode("number");
        assertEquals("number", schema.path("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        verify(visitor).expectAnyFormat(Object.class);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        Object bean = new Object();
        String fieldName = "field";

        // Test with null exception
        assertThrows(IOException.class, () -> serializer.wrapAndThrow(provider, null, bean, fieldName));

        // Test with non-IOException exception
        Throwable exception = new RuntimeException("Test exception");
        assertThrows(JsonMappingException.class, () -> serializer.wrapAndThrow(provider, exception, bean, fieldName));

        // Test with JsonMappingException
        JsonMappingException jsonMappingException = new JsonMappingException("Test mapping exception");
        assertThrows(JsonMappingException.class, () -> serializer.wrapAndThrow(provider, jsonMappingException, bean, fieldName));

        // Test with IOException
        IOException ioException = new IOException("Test IO exception");
        assertThrows(IOException.class, () -> serializer.wrapAndThrow(provider, ioException, bean, fieldName));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        JsonSerializer<String> stringSerializer = mock(JsonSerializer.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);

        // Test with no converter
        assertNull(new StdSerializer<Object>(Object.class).findConvertingContentSerializer(provider, property, stringSerializer));

        // Test with converter defined on property
        Object converterAnnotation = new Object() {};
        when(introspector.findSerializationContentConverter(property)).thenReturn(converterAnnotation);
        Converter<Object, Object> converter = mock(Converter.class);
        when(provider.converterInstance(property
            .getMember(), converterAnnotation)).thenReturn(converter);
        JavaType delegateType = mock(JavaType.class);
        when(delegateType.getRawClass()).thenReturn(String.class);
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
        JsonSerializer<String> result = serializer.findConvertingContentSerializer(provider, property, stringSerializer);
        assertEquals(StdDelegatingSerializer.class, result.getClass());
        StdDelegatingSerializer stdDelegatingSerializer = (StdDelegatingSerializer) result;
        assertEquals(converter, stdDelegatingSerializer.getDelegatingConverter());
        assertEquals(delegateType, stdDelegatingSerializer.getDelegateType());
        assertEquals(stringSerializer, stdDelegatingSerializer.getDelegateSerializer());
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Object filterId = "filterId";
        Object valueToFilter = new Object();

        // Test with null filter provider
        assertThrows(JsonMappingException.class, () -> new StdSerializer<Object>(Object.class).findPropertyFilter(provider, filterId, valueToFilter));

        // Test with filter not found
        FilterProvider filters = mock(FilterProvider.class);
        when(provider.getFilterProvider()).thenReturn(filters);
        when(filters.findPropertyFilter(filterId, valueToFilter)).thenReturn(null);
        assertNull(new StdSerializer<Object>(Object.class).findPropertyFilter(provider, filterId, valueToFilter));

        // Test with filter found
        PropertyFilter filter = mock(PropertyFilter.class);
        when(filters.findPropertyFilter(filterId, valueToFilter)).thenReturn(filter);
        assertEquals(filter, new StdSerializer<Object>(Object.class).findPropertyFilter(provider, filterId, valueToFilter));
    }
}

class MyCustomSerializer<T> extends StdSerializer<T> {
    public MyCustomSerializer() {
        this(String.class);
    }

    public MyCustomSerializer(Class<T> t) {
        super(t);
    }
}