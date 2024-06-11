package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class StdSerializerTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static <T> StdSerializer<T> createStdSerializerInstance(Class<T> t) {
        return new StdSerializer<T>(t) {};
    }

    @SuppressWarnings("unchecked")
    private static <T> StdSerializer<T> createStdSerializerInstance(JavaType type) {
        return new StdSerializer<T>(type) {};
    }

    @SuppressWarnings("unchecked")
    private static <T> StdSerializer<T> createStdSerializerInstance(Class<?> t, boolean dummy) {
        return new StdSerializer<T>(t, dummy) {};
    }

    // Test for constructor with handedType
    @Test
    public void testStdSerializer_constructor_withHandledType() {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass);
        assertEquals(String.class, serializer.handledType());
    }

    // Test for constructor with JavaType
    @Test
    public void testStdSerializer_constructor_withJavaType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        StdSerializer<String> serializer = createStdSerializerInstance(stringType);
        assertEquals(String.class, serializer.handledType());
    }

    // Test for constructor with Class and boolean
    @Test
    public void testStdSerializer_constructor_withClassAndBoolean() {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass, true);
        assertEquals(String.class, serializer.handledType());
    }

    // Test for abstract method serialize
    @Test(expected = AssertionError.class)
    public void testAbstractMethodSerialize() throws IOException {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass);
        String value = "test";
        serializer.serialize(value, null, null);
    }

    // Test for method getSchema
    @Test
    public void testGetSchema_defaultImplementation() {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass);
        ObjectNode schema = serializer.getSchema(null, String.class);
        assertEquals("string", schema.get("type").asText());
    }

    // Test for method getSchema with isOptional
    @Test
    public void testGetSchema_withIsOptional() {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass);
        ObjectNode schema = serializer.getSchema(null, String.class, false);
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    // Test for createObjectNode and createSchemaNode
    @Test
    public void testCreateObjectNodeAndCreateSchemaNode() {
        StdSerializer<Object> serializer = createStdSerializerInstance(Object.class);
        ObjectNode objectNode = serializer.createObjectNode();
        ObjectNode schemaNode = serializer.createSchemaNode("number");
        ObjectNode schemaNodeWithOptional = serializer.createSchemaNode("number", false);

        assertEquals(true, objectNode.isObject());
        assertEquals("number", schemaNode.get("type").asText());
        assertEquals(false, schemaNodeWithOptional.get("required").asBoolean());
    }

    // Test for method acceptJsonFormatVisitor
    @Test(expected = AssertionError.class)
    public void testAcceptJsonFormatVisitor_defaultImplementation() throws JsonMappingException {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass);
        JsonFormatVisitorWrapper visitor = getTestJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    // Test for method wrapAndThrow
    @Test
    public void testWrapAndThrow() throws IOException {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass);
        Object obj = new Object();
        String fieldName = "fieldName";
        Throwable throwable = new IOException("Test IOException");

        serializer.wrapAndThrow(null, throwable, obj, fieldName);
        fail("Expected IOException to be thrown");
    }

    // Test for method wrapAndThrow with index
    @Test
    public void testWrapAndThrow_withIndex() throws IOException {
        Class<String> stringClass = String.class;
        StdSerializer<String> serializer = createStdSerializerInstance(stringClass);
        Object obj = new Object();
        int index = 0;
        Throwable throwable = new IOException("Test IOException");

        serializer.wrapAndThrow(null, throwable, obj, index);
        fail("Expected IOException to be thrown");
    }

    // Test for method isDefaultSerializer
    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<Object> serializer = new DefaultSerializerProvider().getDefaultSerializerProvider().findValueSerializer(Object.class);
        assertTrue(StdSerializer.isDefaultSerializer(serializer));
    }

    // Test for method findConvertingContentSerializer
    @JsonSerialize(using = CustomConverter.class)
    static class CustomClass {
        private int value;
    }

    static class CustomConverter extends StdConverter<Object, CustomClass> {
        @Override
        public CustomClass convert(Object value, JsonSerializerProvider serializers) {
            // Custom conversion logic
            return null;
        }

        @Override
        public Object convertValue(CustomClass value, JsonSerializerProvider serializers) {
            // Custom conversion logic
            return null;
        }
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new AnnotatedClass(CustomClass.class).findProperty("value");
        CustomClass value = new CustomClass();
        value.setValue(42);
        JsonSerializer<Object> existingSerializer = null;

        JsonSerializer<CustomClass> serializer = provider.findValueSerializer(CustomClass.class);
        JsonSerializer<Object> convertingSerializer = serializer.findConvertingContentSerializer(provider, property, existingSerializer);
        assertTrue(convertingSerializer instanceof StdDelegatingSerializer);
    }

    // Test for method findPropertyFilter
    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        FilterProvider filters = provider.getFilterProvider();
        Object valueToFilter = new Object();
        PropertyFilter propertyFilter = serializer.findPropertyFilter(provider, "testFilterId", valueToFilter);
        assertNotNull(propertyFilter);
    }
}