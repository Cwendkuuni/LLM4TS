import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class StdSerializerTest {
    @Test
    public void testCreateObjectNode() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        ObjectNode objectNode = serializer.createObjectNode();
        assertNotNull(objectNode);
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        ObjectNode schemaNode = serializer.createSchemaNode("string");
        assertNotNull(schemaNode);
        assertTrue(schemaNode.has("type"));
    }

    @Test
    public void testCreateSchemaNodeWithOptional() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        ObjectNode schemaNode = serializer.createSchemaNode("string", true);
        assertNotNull(schemaNode);
        assertTrue(schemaNode.has("type"));
        assertTrue(schemaNode.has("required"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getVisitor(ObjectMapper.DefaultTyping.NON_FINAL, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testWrapAndThrowWithIOException() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        IOException exception = new IOException("Test IOException");
        try {
            serializer.wrapAndThrow(null, exception, null, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testWrapAndThrowWithRuntimeException() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        RuntimeException exception = new RuntimeException("Test RuntimeException");
        try {
            serializer.wrapAndThrow(null, exception, null, null);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        assertFalse(serializer.isDefaultSerializer(null));

        StdSerializer<Object> defaultSerializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };
        assertTrue(serializer.isDefaultSerializer(defaultSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        SerializerProvider provider = new SerializerProvider();
        BeanProperty prop = null;
        JsonSerializer<?> existingSerializer = null;

        try {
            serializer.findConvertingContentSerializer(provider, prop, existingSerializer);
        } catch (JsonMappingException e) {
            // Ignore
        }
    }

    @Test
    public void testFindPropertyFilter() {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        SerializerProvider provider = new SerializerProvider();
        try {
            serializer.findPropertyFilter(provider, null, null);
        } catch (JsonMappingException e) {
            // Ignore
        }
    }
}