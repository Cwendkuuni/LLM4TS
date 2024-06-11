package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

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
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link StdSerializer} class.
 */
public class StdSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testStdSerializerInstance() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        assertEquals("Class of the instance should be StdSerializer<String>", StdSerializer.class, stringSerializer.getClass());
        assertEquals("Handled type of the instance should be String", String.class, stringSerializer.handledType());
    }

    @Test
    public void testStdSerializerSerialization() throws JsonProcessingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("value", "Test String");

        String serializedJson = objectMapper.writerForTree(stringSerializer).writeValueAsString(rootNode);
        assertEquals("Serialized JSON should be '{\"value\":\"Test String\"}'", "{\"value\":\"Test String\"}", serializedJson);
    }

    @Test
    public void testGetSchemaMethod() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return createSchemaNode("string");
            }
        };

        JsonNode schemaNode = stringSerializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        assertEquals("Schema node type should be OBJECT", ObjectNode.class, schemaNode.getClass());
        ObjectNode schemaObject = (ObjectNode) schemaNode;
        assertEquals("Schema should contain 'type' property with value 'string'", "string", schemaObject.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                visitor.expectStringFormat(null);
            }
        };

        JsonSerializer.TypingVisitor typingVisitor = objectMapper.typingVisitorForEmptyArray();
        stringSerializer.acceptJsonFormatVisitor(typingVisitor, String.class);
        assertEquals("Property types should be MAPPED_NUMBER or STRING", JsonSerializer.Typing.STRING, typingVisitor.getTyping());
    }

    @Test
    public void testWrapAndThrowMethod() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        try {
            stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new RuntimeException("Test Exception"), null, null);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new IOException("Test IOException"), null, null);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new JsonMappingException("Test JsonMappingException"), null, null);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            stringSerializer.wrapAndThrow(objectMapper.getSerializerProvider(), new Error("Test Error"), null, null);
            fail("Expected Error to be thrown");
        } catch (Error e) {
            // Expected
        }
    }

    @Test
    public void testIsDefaultSerializerMethod() {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        StdSerializer<Integer> integerSerializer = new StdSerializer<Integer>(Integer.class);

        assertTrue("Default serializer for String class should be instance of StdSerializer<String>", stringSerializer.isDefaultSerializer(stringSerializer));
        assertTrue("Default serializer for Integer class should be instance of StdSerializer<Integer>", integerSerializer.isDefaultSerializer(integerSerializer));
        assertFalse("StdSerializer<String> should not be default serializer for Integer class", stringSerializer.isDefaultSerializer(integerSerializer));
        assertFalse("StdSerializer<Integer> should not be default serializer for String class", integerSerializer.isDefaultSerializer(stringSerializer));
    }

    @Test
    public void testFindConvertingContentSerializerMethod() throws JsonMappingException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeNull();
            }
        };

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("values", objectMapper.valueToTree(new Integer[5]));

        StdSerializer<Integer[]> intArraySerializer = new StdSerializer<Integer[]>(Integer[].class) {
            @Override
            public void serialize(Integer[] values, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeValue(values);
            }
        };

        JsonSerializer<ObjectNode> nodeSerializer = objectMapper.findValueSerializer(ObjectNode.class);

        JsonSerializer<Integer[]> convertedIntArraySerializer = objectSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), null, rootNode);
        assertEquals("Converted serializer for ObjectNode should be instance of StdSerializer<Integer[]>", intArraySerializer.getClass(), convertedIntArraySerializer.getClass());

        StdSerializer<List<Integer>> listIntSerializer = new StdSerializer<List<Integer>>(java.util.List.class) {
            @Override
            public void serialize(List<Integer> values, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeValue(values);
            }
        };

        JsonSerializer<ObjectNode> nodeSerializerWithList = objectMapper.findValueSerializer(ObjectNode.class);

        JsonSerializer<List<Integer>> convertedListIntSerializer = objectSerializer.findConvertingContentSerializer(objectMapper.getSerializerProvider(), null, rootNode);
        assertEquals("Converted serializer for ObjectNode should be instance of StdSerializer<List<Integer>>", listIntSerializer.getClass(), convertedListIntSerializer.getClass());
    }

    @Test
    public void testFindPropertyFilterMethod() throws JsonMappingException {
        StdSerializer<Object> objectSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
                jgen.writeNull();
            }
        };

        FilterProvider filterProvider = objectMapper.getSerializerProvider().getFilterProvider();
        PropertyFilter propertyFilter = objectMapper.getSerializerProvider().getFilterProvider().findPropertyFilter(1, new Object());

        JsonSerializer<Object> filteredSerializer = objectSerializer.findPropertyFilter(objectMapper.getSerializerProvider(), 1.0, new Object());
        assertNull("Filtered serializer should be null when no filter is found", filteredSerializer);

        JsonSerializer<Object> propertyFilterSerializer = objectSerializer.findPropertyFilter(objectMapper.getSerializerProvider(), "filterId", new Object());
        assertEquals("Filtered serializer should be instance of PropertyFilter with filter id 'filterId'", propertyFilter.getClass(), propertyFilterSerializer.getClass());
    }

    private ObjectNode createSchemaNode(String type) {
        return objectMapper.getFactory().createObjectNode();
    }
}