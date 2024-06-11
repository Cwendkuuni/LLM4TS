package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    @Test
    public void testStdSerializer() throws JsonMappingException, JsonProcessingException {
        // Test constructor with Class<T>
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals("String serializer instance of handledType class String", String.class, stringSerializer.handledType());

        // Test constructor with JavaType
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        StdSerializer<String> stringSerializerWithJavaType = new StdSerializer<String>(javaType);
        assertEquals("String serializer instance of handledType JavaType<String>", javaType, stringSerializerWithJavaType.handledType());

        // Test constructor with Class<T> and boolean dummy
        Class<String> stringClass = String.class;
        StdSerializer<String> stringSerializerWithBoolean = new StdSerializer<String>(stringClass, false);
        assertEquals("String serializer instance of handledType class String", stringClass, stringSerializerWithBoolean.handledType());

        // Test getSchema method
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        StdSerializer<ObjectNode> objectNodeSerializer = mapper.findValueSerializer(ObjectNode.class);
        ObjectNode schema = objectNodeSerializer.getSchema(mapper.getSerializerProvider(), ObjectNode.class);
        assertNotNull("Schema should not be null", schema);
        assertEquals("Schema type should be 'object'", "object", schema.getType().getSimpleName());

        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper testVisitor = new JsonFormatVisitorWrapper(mapper.getSerializerProvider().getSerializerConfig());
        objectNodeSerializer.acceptJsonFormatVisitor(testVisitor, ObjectNode.class);
        assertTrue("acceptJsonFormatVisitor should accept any format", testVisitor.isAccepted(ObjectNode.class));

        // Test serialize method
        String testValue = "Test Value";
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("Test Key", testValue);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        stringSerializer.serialize(testValue, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        ObjectNode actualNode = mapper.readTree(System.in);
        assertEquals("Serialized value should be 'Test Value'", testValue, actualNode.get("Test Key").asText());

        // Test wrapAndThrow method
        try {
            stringSerializer.wrapAndThrow(mapper.getSerializerProvider(), new IOException(), null, "Test Key");
            fail("wrapAndThrow should throw exception");
        } catch (IOException e) {
            assertEquals("Exception message should be 'JsonMappingException with path [Test Key]'", "JsonMappingException: Test Key (wrapAndThrow) (null)", e.getMessage());
        }

        // Test isDefaultSerializer method
        StdSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        assertTrue("Default serializer for Integer should be true", StdSerializer.class.isInstance(intSerializer));
        assertFalse("Default serializer for String should be false", StdSerializer.class.isInstance(stringSerializer));

        // Test findConvertingContentSerializer method
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode testNode = objectMapper.createObjectNode();
        testNode.put("TestList", new ObjectNode());
        testNode.put("TestMap", new ObjectNode());
        StdSerializer<ObjectNode> listSerializer = objectMapper.findValueSerializer(ObjectNode[].class);
        StdSerializer<ObjectNode> mapSerializer = objectMapper.findValueSerializer(Map.class);
        StdSerializer<ObjectNode> defaultSerializer = objectMapper.findValueSerializer(ObjectNode.class);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ObjectNode resultNode = null;
        try {
            resultNode = (ObjectNode) listSerializer.serialize(testNode.get("TestList"), jsonGenerator, provider);
            assertEquals("Result node class should be 'ObjectNode[]'", ObjectNode[].class, resultNode.getClass().getComponentType());
            resultNode = (ObjectNode) mapSerializer.serialize(testNode.get("TestMap"), jsonGenerator, provider);
            assertEquals("Result node class should be 'Map<ObjectNode, ObjectNode>'", Map.class, resultNode.getClass());
            resultNode = (ObjectNode) defaultSerializer.serialize(testNode, jsonGenerator, provider);
            assertNull("Result node should be null when finding no converting content serializer", resultNode);
        } catch (IOException e) {
            fail("serialize method should not throw exception");
        } finally {
            jsonGenerator.close();
        }

        // Test findPropertyFilter method
        FilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.setFilterID("TestFilterID", new TestFilter());
        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("TestProperty", String.class);
        StdSerializer<String> stringSerializerWithFilter = new StdSerializer<String>(String.class) {
            @Override
            protected PropertyFilter findPropertyFilter(SerializerProvider provider, Object filterId, Object valueToFilter)
                    throws JsonMappingException {
                return introspector.findPropertyFilter(propertyDescriptor, filterProvider, "TestFilterID");
            }
        };
        String testFilterValue = "Test Filter Value";
        ObjectNode testFilterNode = mapper.createObjectNode();
        testFilterNode.put("TestProperty", testFilterValue);
        ObjectNode resultFilterNode = null;
        try {
            resultFilterNode = (ObjectNode) stringSerializerWithFilter.serialize(testFilterNode, jsonGenerator, provider);
            assertNull("Result node should be null when filter is not found", resultFilterNode);
        } catch (IOException e) {
            fail("serialize method should not throw exception");
        } finally {
            jsonGenerator.close();
        }

        filterProvider.setFilterID("TestFilterID", new TestFilter());
        ObjectNode testFilterNodeWithFilter = mapper.createObjectNode();
        testFilterNodeWithFilter.put("TestProperty", testFilterValue);
        ObjectNode resultFilterNodeWithFilter = null;
        try {
            resultFilterNodeWithFilter = (ObjectNode) stringSerializerWithFilter.serialize(testFilterNodeWithFilter, jsonGenerator, provider);
            assertNotNull("Result node should not be null when filter is found", resultFilterNodeWithFilter);
            assertEquals("Result node 'TestProperty' value should be filtered", "*****", resultFilterNodeWithFilter.get("TestProperty").asText());
        } catch (IOException e) {
            fail("serialize method should not throw exception");
        } finally {
            jsonGenerator.close();
        }
    }

    static class TestFilter implements PropertyFilter {
        @Override
        public void serializeWithFilter(JsonGenerator jgen, SerializerProvider provider, Object bean, PropertyFilter pf)
                throws IOException {
            jgen.writeString("*****");
        }

        @Override
        public void serializeAsGetters(JsonGenerator jgen, SerializerProvider provider, Object bean)
                throws IOException {
            fail("TestFilter should not call serializeAsGetters");
        }

        @Override
        public void serializeAsFields(JsonGenerator jgen, SerializerProvider provider, Object bean)
                throws IOException {
            fail("TestFilter should not call serializeAsFields");
        }

        @Override
        public Object filterId() {
            return "TestFilterID";
        }
    }
}