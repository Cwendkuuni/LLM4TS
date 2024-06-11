import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdSerializerTest {

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }

        };

        String value = "test";
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(value, generator, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint)
                    throws JsonMappingException {
                return createSchemaNode("string");
            }

        };

        SerializerProvider provider = new SerializerProvider();

        JsonNode schema = serializer.getSchema(provider, String.class);

        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectNode schema = createObjectNode();
        schema.put("type", type);
        return schema;
    }

    private ObjectNode createObjectNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectAnyFormat(typeHint);
            }

        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper().getFactory());

        serializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test
    public void testWrapAndThrow() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }

        };

        SerializerProvider provider = new SerializerProvider();
        String fieldName = "field";

        IOException exception = null;

        try {
            serializer.wrapAndThrow(provider, new IOException("Test exception"), "test", fieldName);
        } catch (IOException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }

        };

        SerializerProvider provider = new SerializerProvider();
        BeanProperty prop = null;
        JsonSerializer<?> existingSerializer = null;

        JsonSerializer<?> convertingSerializer = serializer.findConvertingContentSerializer(provider, prop,
                existingSerializer);

        assertNull(convertingSerializer);
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {

            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }

        };

        SerializerProvider provider = new SerializerProvider();
        Object filterId = new Object();
        Object valueToFilter = new Object();

        PropertyFilter propertyFilter = serializer.findPropertyFilter(provider, filterId, valueToFilter);

        assertNull(propertyFilter);
    }

}