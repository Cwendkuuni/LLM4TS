package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializerTests;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link StdSerializer}.
 */
public class StdSerializerTest extends StdSerializerTests {

    private AnnotationIntrospector annotationIntrospector;
    private FilterProvider filterProvider;

    @Before
    public void setup() {
        annotationIntrospector = mock(AnnotationIntrospector.class);
        filterProvider = mock(FilterProvider.class);
    }

    /**
     * Test for {@link StdSerializer#StdSerializer(Class)} constructor.
     */
    @Test
    public void testStdSerializerConstructorWithClass() throws JsonMappingException, JsonProcessingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals(String.class, stringSerializer.handledType());
    }

    /**
     * Test for {@link StdSerializer#StdSerializer(JavaType)} constructor.
     */
    @Test
    public void testStdSerializerConstructorWithJavaType() throws JsonMappingException, JsonProcessingException {
        JavaType stringType = objectMapper.getTypeFactory().constructType(String.class);
        StdSerializer<String> stringSerializer = new StdSerializer<String>(stringType);
        assertEquals(String.class, stringSerializer.handledType());
    }

    /**
     * Test for {@link StdSerializer#StdSerializer(Class, boolean)} constructor.
     */
    @Test
    public void testStdSerializerConstructorWithClassBoolean() throws JsonMappingException, JsonProcessingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class, false);
        assertEquals(String.class, stringSerializer.handledType());
    }

    /**
     * Test for {@link StdSerializer#serialize(Object, JsonGenerator, SerializerProvider)} method.
     */
    @Test
    public void testSerializeMethod() throws JsonGenerationException, JsonMappingException, IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectNode resultNode = mapper.createObjectNode();
        when(annotationIntrospector.findSerializationContentConverter(null)).thenReturn(null);

        stringSerializer.serialize("test", mapper.getFactory().createGenerator(resultNode), objectMapper);
        ObjectNode serializedNode = resultNode;
        assertEquals("\"test\"", serializedNode.get("value").asText());
    }

    /**
     * Test for {@link StdSerializer#getSchema(SerializerProvider, Type)} method.
     */
    @Test
    public void testGetSchemaMethod() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = stringSerializer.getSchema(objectMapper, String.class);
        ObjectNode expectedSchemaNode = createSchemaNode("string");
        assertEquals(expectedSchemaNode, schemaNode);
    }

    /**
     * Test for {@link StdSerializer#getSchema(SerializerProvider, Type, boolean)} method.
     */
    @Test
    public void testGetSchemaWithOptionalMethod() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        ObjectNode schemaNode = stringSerializer.getSchema(objectMapper, String.class, true);
        ObjectNode expectedSchemaNode = createSchemaNode("string");
        ObjectNode optionalSchemaNode = createSchemaNode("string", true);
        assertEquals(optionalSchemaNode, schemaNode);
    }

    /**
     * Test for {@link StdSerializer#acceptJsonFormatVisitor(JsonFormatVisitorWrapper, JavaType)} method.
     */
    @Test
    public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = objectMapper.getTypeFactory().constructType(String.class);
        stringSerializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);
        ObjectNode expectedNode = createSchemaNode("string");
        verify(visitorWrapper).expectAnyFormat(typeHint);
    }

    /**
     * Test for {@link StdSerializer#wrapAndThrow(SerializerProvider, Throwable, Object, String)} method.
     */
    @Test
    public void testWrapAndThrowWithStringMethod() throws JsonMappingException, IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        Object bean = new Object();
        when(annotationIntrospector.findSerializationContentConverter(null)).thenReturn(null);

        try {
            stringSerializer.serialize(null, mapper.getFactory().createGenerator(objectMapper.getOutputFactory()), objectMapper);
        } catch (JsonMappingException e) {
            stringSerializer.wrapAndThrow(objectMapper, e, bean, "testProperty");
            fail();
        }

        try {
            stringSerializer.serialize("test", mapper.getFactory().createGenerator(objectMapper.getOutputFactory()), objectMapper);
        } catch (JsonProcessingException e) {
            stringSerializer.wrapAndThrow(objectMapper, e, bean, "testProperty");
        }
    }

    /**
     * Test for {@link StdSerializer#wrapAndThrow(SerializerProvider, Throwable, Object, int)} method.
     */
    @Test
    public void testWrapAndThrowWithIntMethod() throws JsonMappingException, IOException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        Object bean = new Object();
        int index = 0;
        when(annotationIntrospector.findSerializationContentConverter(null)).thenReturn(null);

        try {
            stringSerializer.serialize(null, mapper.getFactory().createGenerator(objectMapper.getOutputFactory()), objectMapper);
        } catch (JsonMappingException e) {
            stringSerializer.wrapAndThrow(objectMapper, e, bean, index);
            fail();
        }

        try {
            stringSerializer.serialize("test", mapper.getFactory().createGenerator(objectMapper.getOutputFactory()), objectMapper);
        } catch (JsonProcessingException e) {
            stringSerializer.wrapAndThrow(objectMapper, e, bean, index);
        }
    }

    /**
     * Test for {@link StdSerializer#isDefaultSerializer(JsonSerializer)} method.
     */
    @Test
    public void testIsDefaultSerializerMethod() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
        assertTrue(stringSerializer.isDefaultSerializer(jsonSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(new CustomSerializer<>()));
    }

    /**
     * Test for {@link StdSerializer#findConvertingContentSerializer(SerializerProvider, BeanProperty, JsonSerializer)} method.
     */
    @Test
    public void testFindConvertingContentSerializerMethod() throws JsonMappingException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);
        ObjectNode expectedNode = mapper.createObjectNode();
        ObjectNode actualNode = mapper.createObjectNode();

        when(annotationIntrospector.findSerializationContentConverter(null)).thenReturn(adapterFactory.getConverter(String.class, String[].class));
        when(filterProvider.findPropertyFilter(eq("filterId"), any())).thenReturn(mock(PropertyFilter.class));

        JsonSerializer<String> stringSerializer = objectSerializer.findConvertingContentSerializer(objectMapper, null, objectSerializer);
        String[] array = new String[]{"test"};
        stringSerializer.serialize(array, mapper.getFactory().createGenerator(expectedNode), objectMapper);
        stringSerializer.serialize(array[0], mapper.getFactory().createGenerator(actualNode), objectMapper);

        assertEquals(expectedNode, actualNode);
    }

    /**
     * Test for {@link StdSerializer#findPropertyFilter(SerializerProvider, Object, Object)} method.
     */
    @Test
    public void testFindPropertyFilterMethod() throws JsonMappingException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class);
        Object filterId = "filterId";
        Object filterableValue = new Object();

        when(filterProvider.findPropertyFilter(eq(filterId), any())).thenReturn(mock(PropertyFilter.class));

        PropertyFilter propertyFilter = objectSerializer.findPropertyFilter(objectMapper, filterId, filterableValue);
        assertNotNull(propertyFilter);
    }
}