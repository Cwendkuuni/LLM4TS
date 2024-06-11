package com.fasterxml.jackson.databind.ser.std; // make sure to import all necessary packages

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for StdSerializer
 */
public class StdSerializerTest {

    // Test instance of StdSerializer
    private StdSerializer<String> stringSerializer;

    // Test object mapper
    private ObjectMapper objectMapper;

    // Set up test environment
    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        stringSerializer = new StdSerializer<String>(String.class);
    }

    // Test for method 'serialize'
    @Test
    public void testSerialize() throws JsonProcessingException, IOException {
        String value = "Hello, World!";
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        // Test serialization of null value
        stringSerializer.serialize(null, jsonGenerator, serializerProvider);
        assertNull(jsonGenerator.getOutputBuffer().toString());

        // Test serialization of empty string
        stringSerializer.serialize("", jsonGenerator, serializerProvider);
        String expectedEmptyString = "\"\"";
        assertEquals(expectedEmptyString, jsonGenerator.getOutputBuffer().toString());

        // Test serialization of non-empty string
        stringSerializer.serialize(value, jsonGenerator, serializerProvider);
        String expectedString = "\"" + value + "\"";
        assertEquals(expectedString, jsonGenerator.getOutputBuffer().toString());

        // Verify that 'serialize' method calls 'wrapAndThrow' if an exception is thrown
        ArgumentCaptor<Throwable> exceptionArgumentCaptor = ArgumentCaptor.forClass(Throwable.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS)).thenReturn(true);
        when(serializerProvider.wrapAndFind(JsonMappingException.class)).thenReturn(mock(JsonMappingException.class));
        when(serializerProvider.findValueSerializer(String.class)).thenReturn(mock(JsonSerializer.class));

        try {
            stringSerializer.serialize("Exception should be wrapped", jsonGenerator, serializerProvider);
            assertFalse("'serialize' method did not throw an exception", true);
        } catch (JsonMappingException e) {
            // expected
        }

        verify(serializerProvider).wrapAndThrow(eq(stringSerializer), eq(value), eq(null), anyInt());
    }

    // Test for method 'getSchema'
    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type typeHint = String.class;

        ObjectNode expectedSchema = createSchemaNode("string");
        ObjectNode actualSchema = stringSerializer.getSchema(serializerProvider, typeHint);
        assertEquals(expectedSchema.toString(), actualSchema.toString());

        // Test 'getSchema' method with optional parameter 'isOptional' set to true
        boolean isOptional = true;
        expectedSchema = createSchemaNode("string", isOptional);
        actualSchema = stringSerializer.getSchema(serializerProvider, typeHint, isOptional);
        assertEquals(expectedSchema.toString(), actualSchema.toString());
    }

    // Test for method 'acceptJsonFormatVisitor'
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = String.class;

        stringSerializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);
        verify(visitorWrapper).expectAnyFormat(typeHint);
    }

    // Test for method 'wrapAndThrow'
    @Test
    public void testWrapAndThrow() throws IOException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Object bean = new Object();
        String fieldName = "testField";
        String exceptionMessage = "Test exception message";
        Throwable throwable = new IOException(exceptionMessage);

        stringSerializer.wrapAndThrow(serializerProvider, throwable, bean, fieldName);
        JsonMappingException expectedJsonMappingException = new JsonMappingException(exceptionMessage, bean, fieldName);
        assertEquals(expectedJsonMappingException.toString(), serializerProvider.getCurrentException().getMessage());

        // Test 'wrapAndThrow' method with 'index' parameter
        int index = 0;
        stringSerializer.wrapAndThrow(serializerProvider, throwable, bean, index);
        assertEquals(expectedJsonMappingException.toString(), serializerProvider.getCurrentException().getMessage());
    }

    // Test for method 'isDefaultSerializer'
    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<Object> otherSerializer = mock(JsonSerializer.class);
        assertTrue(stringSerializer.isDefaultSerializer(otherSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(new StdSerializer<Integer>(Integer.class)));
    }

    // Test for method 'findConvertingContentSerializer'
    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        Object valueToFilter = new Object();
        Converter<Object, Object> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        StdSerializer<Object> existingSerializer = mock(StdSerializer.class);

        when(property.getMember()).thenReturn(null);
        when(serializerProvider.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
        when(serializerProvider.findValueSerializer(delegateType)).thenReturn(existingSerializer);
        when(existingSerializer.getClass()).thenReturn(StdSerializer.class);

        PropertyFilter filter = mock(PropertyFilter.class);
        FilterProvider filters = mock(FilterProvider.class);
        when(filters.findPropertyFilter(eq("testFilterId"), any())).thenReturn(filter);
        when(serializerProvider.getFilterProvider()).thenReturn(filters);

        JsonSerializer<Object> returnedSerializer = stringSerializer.findConvertingContentSerializer(serializerProvider, property, null);
        assertNotNull(returnedSerializer);
        assertEquals(StdDelegatingSerializer.class.getName(), returnedSerializer.getClass().getName());
        verify(serializerProvider, times(1)).getConverterInstancer();
        verify(serializerProvider, times(1)).findValueSerializer(delegateType);
    }

    // Test for method 'findPropertyFilter'
    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        Object filterId = "testFilterId";
        Object valueToFilter = new Object();
        PropertyFilter expectedFilter = mock(PropertyFilter.class);

        PropertyFilter actualFilter = stringSerializer.findPropertyFilter(mock(SerializerProvider.class), filterId, valueToFilter);
        assertEquals(expectedFilter, actualFilter);
    }

    // Helper method to create ObjectNode with the given schema definition
    private ObjectNode createSchemaNode(String type, boolean isOptional) {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", type);
        if (isOptional) {
            schema.put("required", false);
        }
        return schema;
    }
}