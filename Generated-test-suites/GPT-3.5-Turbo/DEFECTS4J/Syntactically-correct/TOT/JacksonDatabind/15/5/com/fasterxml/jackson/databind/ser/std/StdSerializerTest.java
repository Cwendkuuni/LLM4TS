
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdSerializerTest {

    private StdSerializer<String> serializer;

    @Before
    public void setup() {
        serializer = new TestSerializer(String.class);
    }

    @Test
    public void testHandledType() {
        Assert.assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonGenerator generator = provider.getGenerator();
        String value = "test";
        serializer.serialize(value, generator, provider);
        generator.flush();
        Assert.assertEquals(value, generator.getOutputTarget());
    }

    @Test(expected = JsonParseException.class)
    public void testSerializeWithIOException() throws IOException {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonGenerator generator = provider.getGenerator();
        String value = "test";
        generator.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        serializer.serialize(value, generator, provider);
        generator.flush();
        generator.close();
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        SchemaAware schemaAware = (SchemaAware) serializer;
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Type typeHint = String.class;
        JsonNode schema = schemaAware.getSchema(provider, typeHint);
        Assert.assertEquals("string", schema.get("type").asText());
        Assert.assertTrue(schema instanceof ObjectNode);
    }

    @Test
    public void testGetSchemaWithIsOptional() throws JsonProcessingException {
        SchemaAware schemaAware = (SchemaAware) serializer;
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Type typeHint = String.class;
        boolean isOptional = true;
        JsonNode schema = schemaAware.getSchema(provider, typeHint, isOptional);
        Assert.assertEquals("string", schema.get("type").asText());
        Assert.assertTrue(schema instanceof ObjectNode);
        Assert.assertFalse(schema.get("required").asBoolean());
    }

    private static class TestSerializer extends StdSerializer<String> {

        protected TestSerializer(Class<String> t) {
            super(t);
        }

        @SuppressWarnings("unchecked")
        protected TestSerializer(JavaType type) {
            super(type);
        }

        protected TestSerializer(Class<?> t, boolean dummy) {
            super(t, dummy);
        }

        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value);
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonProcessingException {
            return createSchemaNode("string");
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) throws JsonProcessingException {
            return createSchemaNode("string", isOptional);
        }
    }
}

