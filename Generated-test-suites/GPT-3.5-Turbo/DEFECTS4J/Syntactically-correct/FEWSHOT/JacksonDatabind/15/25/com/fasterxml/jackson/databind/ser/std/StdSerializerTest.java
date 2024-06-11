package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNull;

public class StdSerializerTest {
    @Test
    public void testHandledType() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode node = serializer.createObjectNode();
        assertEquals("object", node.get("type").asText());
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode node = serializer.createSchemaNode("string");
        assertEquals("string", node.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeWithBoolean() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode node = serializer.createSchemaNode("string", true);
        assertEquals("string", node.get("type").asText());
        assertEquals(true, node.get("required").asBoolean());
    }

    @Test
    public void testSerialize() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException
            {
                assertEquals("test", value);
                jgen.writeStartObject();
                jgen.writeEndObject();
            }
        };

        try {
            JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            serializer.serialize("test", jgen, provider);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                throws JsonMappingException
            {
                ObjectNode schema = createObjectNode();
                schema.put("type", "string");
                schema.put("required", true);
                return schema;
            }
        };

        try {
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            JsonNode node = serializer.getSchema(provider, String.class);
            assertEquals("string", node.get("type").asText());
            assertEquals(true, node.get("required").asBoolean());
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws JsonMappingException
            {
                visitor.expectAnyFormat(typeHint);
            }
        };

        try {
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(provider) {};
            serializer.acceptJsonFormatVisitor(visitor, String.class);
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        }
    }

    @Test
    public void testWrapAndThrowWithJsonMappingException() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);

        try {
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            serializer.wrapAndThrow(provider, new IOException(), "test", "field");
            fail("IOException not thrown");
        } catch (JsonMappingException e) {
            assertEquals("test", e.getPathReference());
            assertEquals("field", e.getFieldName());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test(expected = IOException.class)
    public void testWrapAndThrowWithIOException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);

        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.wrapAndThrow(provider, new IOException(), "test", "field");
    }

    @Test(expected = Error.class)
    public void testWrapAndThrowWithError() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);

        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.wrapAndThrow(provider, new Error(), "test", "field");
    }

    @Test(expected = RuntimeException.class)
    public void testWrapAndThrowWithRuntimeException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);

        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        serializer.wrapAndThrow(provider, new RuntimeException(), "test", "field");
    }

    @Test
    public void testFindConvertingContentSerializer() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public JsonSerializer<?> findConvertingContentSerializer(SerializerProvider provider,
                    BeanProperty prop, JsonSerializer<?> existingSerializer)
                throws JsonMappingException
            {
                return new StdDelegatingSerializer(null, null, null);
            }
        };

        try {
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            serializer.findConvertingContentSerializer(provider, null, null);
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        }
    }

    @Test
    public void testFindPropertyFilter() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class) {
            @Override
            public PropertyFilter findPropertyFilter(SerializerProvider provider,
                    Object filterId, Object valueToFilter)
                throws JsonMappingException
            {
                return new NullPropertyFilter();
            }

            class NullPropertyFilter implements PropertyFilter {
                @Override
                public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
                    throws Exception
                {
                    jgen.writeFieldName("");
                    jgen.writeString("");
                }
            }
        };

        try {
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            serializer.findPropertyFilter(provider, null, null).serializeAsField(null, null, provider, null);
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}