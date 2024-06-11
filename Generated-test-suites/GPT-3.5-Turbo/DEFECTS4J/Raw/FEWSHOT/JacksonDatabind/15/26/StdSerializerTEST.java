package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
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

@JacksonStdImpl
public class StdSerializerTest {
    @Test
    public void testConstructor() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        assertEquals(Object.class, serializer.handleType());
    }

    @Test
    public void testConstructorWithJavaType() {
        StdSerializer<Object> serializer = new StdSerializer<>(TypeFactory.defaultInstance().constructFromCanonical("java.lang.Object")) {
        };
        assertEquals(Object.class, serializer.handleType());
    }

    @Test
    public void testConstructorWithClassAndDummy() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class, true) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };
        assertEquals(Object.class, serializer.handleType());
    }

    @Test
    public void testHandledType() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        JsonNode jsonNode = serializer.getSchema(null, null);
        assertTrue(jsonNode.isObject());
        assertTrue(jsonNode.has("type"));
        assertEquals("string", jsonNode.get("type").asText());
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        JsonNode jsonNode = serializer.getSchema(null, null, false);
        assertTrue(jsonNode.isObject());
        assertTrue(jsonNode.has("type"));
        assertTrue(jsonNode.has("required"));
        assertEquals("string", jsonNode.get("type").asText());
        assertFalse(jsonNode.get("required").asBoolean());
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        ObjectNode objectNode = serializer.createObjectNode();
        assertTrue(objectNode.isObject());
        assertTrue(objectNode.isEmpty());
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        ObjectNode schemaNode = serializer.createSchemaNode("string");
        assertTrue(schemaNode.isObject());
        assertTrue(schemaNode.has("type"));
        assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeWithOptional() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        ObjectNode schemaNode = serializer.createSchemaNode("string", false);
        assertTrue(schemaNode.isObject());
        assertTrue(schemaNode.has("type"));
        assertTrue(schemaNode.has("required"));
        assertEquals("string", schemaNode.get("type").asText());
        assertFalse(schemaNode.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = TypeFactory.defaultInstance().constructFromCanonical("java.lang.String");

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(typeHint, visitor.getValueType());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithoutFilterProvider() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            }
        };

        serializer.findPropertyFilter(null, "filterId", "valueToFilter");
    }
}