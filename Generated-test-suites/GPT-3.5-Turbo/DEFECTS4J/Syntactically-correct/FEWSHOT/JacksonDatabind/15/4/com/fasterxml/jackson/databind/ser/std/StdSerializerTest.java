package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {
    private StdSerializer<String> serializer;
    private JsonSerializer<?> existingSerializer;
    private SerializerProvider provider;
    private BeanProperty prop;

    @Before
    public void setup() {
        serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                // Do nothing
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // Do nothing
            }
        };

        existingSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // Do nothing
            }
        };

        provider = new DefaultSerializerProvider.Impl();

        prop = new BeanProperty.Std(null, String.class, null, null);
    }

    @Test
    public void testHandledType() {
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);

        // Should do nothing
        serializer.serialize("test", jgen, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        assertNull(serializer.getSchema(provider, null));
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        assertNull(serializer.getSchema(provider, null, true));

        ObjectNode schema = (ObjectNode) serializer.getSchema(provider, null, false);
        assertEquals(false, schema.get("required").asBoolean());
    }

    @Test
    public void testCreateObjectNode() {
        ObjectNode objectNode = serializer.createObjectNode();
        assertTrue(objectNode.isObject());
    }

    @Test
    public void testCreateSchemaNode() {
        ObjectNode schema = serializer.createSchemaNode("string");
        assertTrue(schema.isObject());
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeWithOptional() {
        ObjectNode schema = serializer.createSchemaNode("string", true);
        assertTrue(schema.isObject());
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.has("required"));
        assertFalse(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(provider.getTypeFactory());
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testIsDefaultSerializerWithDefaultSerializer() {
        assertTrue(serializer.isDefaultSerializer(existingSerializer));
    }

    @Test
    public void testIsDefaultSerializerWithCustomSerializer() {
        assertFalse(serializer.isDefaultSerializer(serializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        JsonSerializer<?> serializer = serializer.findConvertingContentSerializer(provider, prop,
                existingSerializer);
        assertNull(serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithoutFilterProvider() throws JsonMappingException {
        serializer.findPropertyFilter(null, "filterId", "value");
    }
}