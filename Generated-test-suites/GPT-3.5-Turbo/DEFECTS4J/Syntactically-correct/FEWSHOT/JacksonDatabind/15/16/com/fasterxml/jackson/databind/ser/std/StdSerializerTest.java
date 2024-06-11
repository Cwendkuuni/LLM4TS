package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {
    
    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        // TODO: Implement test case
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return createSchemaNode("object");
            }
        };
        JsonNode schema = stdSerializer.getSchema(null, null);
        assertNotNull(schema);
        assertTrue(schema.has("type"));
        assertEquals("object", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
                    throws JsonMappingException {
                ObjectNode schema = (ObjectNode) getSchema(provider, typeHint);
                if (!isOptional) {
                    schema.put("required", !isOptional);
                }
                return schema;
            }
        };
        JsonNode schema = stdSerializer.getSchema(null, null, true);
        assertNotNull(schema);
        assertTrue(schema.has("type"));
        assertEquals("object", schema.get("type").asText());
        assertTrue(schema.has("required"));
        assertEquals(true, schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectAnyFormat(typeHint);
            }
        };
        // TODO: Implement test case
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }
        };
        assertTrue(stdSerializer.isDefaultSerializer(stdSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }
        };
        JsonSerializer<?> contentSerializer = stdSerializer.findConvertingContentSerializer(null, null, null);
        assertNull(contentSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<Object> stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }
        };
        PropertyFilter propertyFilter = stdSerializer.findPropertyFilter(null, null, null);
        assertNull(propertyFilter);
    }
}