package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonValueSerializerTest {

    @Test
    public void testSerializeWithType() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        Object value = new Object() {
            @JsonValue
            public String getValue() {
                return "value";
            }
        };
        TypeSerializer typeSer = mapper.getTypeSerializer();

        // Test serialization with no type information
        JsonNode result = serializer.serializeWithType(value, null, mapper.getSerializerProvider(), typeSer);
        ObjectNode expectedNode = mapper.createObjectNode();
        expectedNode.put("value", "value");
        assertEquals(expectedNode, result);

        // Test serialization with type information
        result = serializer.serializeWithType(value, null, mapper.getSerializerProvider(), typeSer);
        expectedNode.put("value", "value");
        expectedNode.put("@type", "java.lang.Object");
        assertEquals(expectedNode, result);
    }

    @Test
    public void testSerialize() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        Object value = new Object() {
            @JsonValue
            public String getValue() {
                return "value";
            }
        };

        JsonGenerator gen = mapper.getFactory().createGenerator();
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();

        assertEquals("value", gen.getOutputContent().toString());
    }

    @Test
    public void testContextualSerialization() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        Object value = new Object() {
            @JsonValue
            public String getValue() {
                return "value";
            }
        };

        JsonSerializer<Object> contextualSerializer = mapper.findValueSerializer(value.getClass(), null);
        JsonValueSerializer contextualizedSerializer = serializer.withResolved(null, contextualSerializer, false);

        JsonNode result = contextualizedSerializer.serializeWithType(value, null, mapper.getSerializerProvider(), mapper.getTypeSerializer());
        ObjectNode expectedNode = mapper.createObjectNode();
        expectedNode.put("value", "value");
        assertEquals(expectedNode, result);
    }

    @Test
    public void testCreateContextualSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        Object value = new Object() {
            @JsonValue
            public String getValue() {
                return "value";
            }
        };

        JavaType valueType = mapper.getTypeFactory().constructType(value.getClass());
        JsonSerializer<Object> contextualSerializer = mapper.findPrimaryPropertySerializer(valueType, null);
        JsonValueSerializer contextualizedSerializer = serializer.withResolved(null, contextualSerializer, false);

        assertEquals(contextualSerializer, contextualizedSerializer._valueSerializer);
    }

    @Test
    public void testCreateContextualSerializerWithForceTypeInfo() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, true);
        Object value = new Object() {
            @JsonValue
            public String getValue() {
                return "value";
            }
        };

        JsonSerializer<Object> contextualSerializer = mapper.findValueSerializer(value.getClass(), null);
        JsonValueSerializer contextualizedSerializer = serializer.withResolved(null, contextualSerializer, true);

        JsonNode result = contextualizedSerializer.serializeWithType(value, null, mapper.getSerializerProvider(), mapper.getTypeSerializer());
        ObjectNode expectedNode = mapper.createObjectNode();
        expectedNode.put("value", "value");
        expectedNode.put("@type", "java.lang.Object");
        assertEquals(expectedNode, result);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        Object value = new Object() {
            @JsonValue
            public String getValue() {
                return "value";
            }
        };

        ObjectNode expectedNode = mapper.createObjectNode();
        expectedNode.put("title", "Object");
        expectedNode.put("type", "object");

        JsonNode result = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertEquals(expectedNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        Object value = new Object() {
            @JsonValue
            public String getValue() {
                return "value";
            }
        };

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultVisitorProvider().findRootValueSerializer(Object.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        Enum testEnum = Enum.valueOf(MyEnum.class, "VALUE1");

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getExtensionLoader(JsonFormatVisitorWrapper.class).findConverter(MyEnum.class);
        serializer._acceptJsonFormatVisitorForEnum(visitor, null, MyEnum.class);
    }
}