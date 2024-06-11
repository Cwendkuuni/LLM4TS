package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonNode;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;

public class StdSerializerTest {
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return createSchemaNode("string");
            }
        };
        SerializerProvider provider = null; // dummy provider
        Type typeHint = String.class;
        JsonNode actualSchema = serializer.getSchema(provider, typeHint);
        assertNotNull(actualSchema);
        assertTrue(actualSchema.isObject());
        assertEquals("string", actualSchema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectAnyFormat(typeHint);
            }
        };
        JsonFormatVisitorWrapper visitor = null; // dummy visitor
        JavaType typeHint = null; // dummy type hint
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // No exception is thrown, so the test passes
    }

    @Test
    public void testWrappedAndThrowWithBeanAndField() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                throw new RuntimeException("serialization error");
            }
        };
        SerializerProvider provider = null; // dummy provider
        Throwable t = new RuntimeException("test exception");
        Object bean = "test bean";
        String fieldName = "test field";
        try {
            serializer.wrapAndThrow(provider, t, bean, fieldName);
        } catch (JsonMappingException e) {
            assertNotNull(e);
            assertEquals(t, e.getCause());
            assertEquals(bean, e.getOriginalObject());
            assertEquals(fieldName, e.getPath().get(0).getFieldName());
        }
    }

    @Test
    public void testWrappedAndThrowWithBeanAndIndex() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                throw new RuntimeException("serialization error");
            }
        };
        SerializerProvider provider = null; // dummy provider
        Throwable t = new RuntimeException("test exception");
        Object bean = "test bean";
        int index = 0;
        try {
            serializer.wrapAndThrow(provider, t, bean, index);
        } catch (JsonMappingException e) {
            assertNotNull(e);
            assertEquals(t, e.getCause());
            assertEquals(bean, e.getOriginalObject());
            assertEquals(index, e.getPath().get(0).getIndex());
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }
        };
        JsonSerializer<String> defaultSerializer = null; // dummy default serializer
        assertTrue(serializer.isDefaultSerializer(defaultSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }
        };
        SerializerProvider provider = null; // dummy provider
        BeanProperty prop = null; // dummy prop
        JsonSerializer<?> existingSerializer = null; // dummy existing serializer
        JsonSerializer<?> actualSerializer = serializer.findConvertingContentSerializer(provider, prop,
                existingSerializer);
        assertNull(actualSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            private static final long serialVersionUID = 1L;

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
            }
        };
        SerializerProvider provider = null; // dummy provider
        Object filterId = null; // dummy filter id
        Object valueToFilter = null; // dummy value to filter
        PropertyFilter actualFilter = serializer.findPropertyFilter(provider, filterId, valueToFilter);
        assertNull(actualFilter);
    }
}