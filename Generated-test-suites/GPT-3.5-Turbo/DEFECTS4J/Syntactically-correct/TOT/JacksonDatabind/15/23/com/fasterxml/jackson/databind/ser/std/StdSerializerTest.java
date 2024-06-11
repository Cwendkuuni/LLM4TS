
package com.fasterxml.jackson.databind.ser.std;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

public class StdSerializerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final TestSerializer testSerializer = new TestSerializer(String.class);

    @Test
    public void testSerializeWithStringValue() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = mapper.getSerializerProvider();
        String value = "testString";

        testSerializer.serialize(value, generator, provider);
        generator.flush();

        String jsonString = out.toString();
        JsonNode node = mapper.readTree(jsonString);
        Assert.assertEquals(value, node.asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithWrongValueType() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Integer value = 123;
        testSerializer.serialize(value, generator, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode schema = (ObjectNode) testSerializer.getSchema(provider, String.class);

        Assert.assertTrue(schema.has("type"));
        Assert.assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithIsRequiredFalse() throws JsonMappingException {
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode schema = (ObjectNode) testSerializer.getSchema(provider, String.class, false);

        Assert.assertTrue(schema.has("type"));
        Assert.assertEquals("string", schema.get("type").asText());
        Assert.assertTrue(schema.has("required"));
        Assert.assertEquals(false, schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchemaWithIsRequiredTrue() throws JsonMappingException {
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectNode schema = (ObjectNode) testSerializer.getSchema(provider, String.class, true);

        Assert.assertTrue(schema.has("type"));
        Assert.assertEquals("string", schema.get("type").asText());
        Assert.assertTrue(schema.has("required"));
        Assert.assertEquals(true, schema.get("required").asBoolean());
    }

    @Test
    public void testCreateSchemaNode() {
        ObjectNode schema = testSerializer.createSchemaNode("testType");

        Assert.assertTrue(schema.has("type"));
        Assert.assertEquals("testType", schema.get("type").asText());
    }

    @Test
    public void testCreateSchemaNodeWithIsOptionalFalse() {
        ObjectNode schema = testSerializer.createSchemaNode("testType", false);

        Assert.assertTrue(schema.has("type"));
        Assert.assertEquals("testType", schema.get("type").asText());
        Assert.assertTrue(schema.has("required"));
        Assert.assertEquals(true, schema.get("required").asBoolean());
    }

    @Test
    public void testCreateSchemaNodeWithIsOptionalTrue() {
        ObjectNode schema = testSerializer.createSchemaNode("testType", true);

        Assert.assertTrue(schema.has("type"));
        Assert.assertEquals("testType", schema.get("type").asText());
        Assert.assertTrue(schema.has("required"));
        Assert.assertEquals(false, schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        TestVisitor visitor = new TestVisitor();
        SerializerProvider provider = mapper.getSerializerProvider();

        testSerializer.acceptJsonFormatVisitor(visitor, String.class);

        Assert.assertEquals("any", visitor.getVisitedType());
    }

    @Test(expected = IOException.class)
    public void testWrapAndThrowWithIOException() throws IOException {
        SerializerProvider provider = mapper.getSerializerProvider();
        String fieldName = "testField";

        testSerializer.wrapAndThrow(provider, new IOException("Test exception"), new Object(), fieldName);
    }

    @Test(expected = RuntimeException.class)
    public void testWrapAndThrowWithRuntimeException() throws IOException {
        SerializerProvider provider = mapper.getSerializerProvider();
        String fieldName = "testField";

        testSerializer.wrapAndThrow(provider, new RuntimeException("Test exception"), new Object(), fieldName);
    }

    @Test(expected = Error.class)
    public void testWrapAndThrowWithError() throws IOException {
        SerializerProvider provider = mapper.getSerializerProvider();
        String fieldName = "testField";

        testSerializer.wrapAndThrow(provider, new Error("Test error"), new Object(), fieldName);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrowWithJsonMappingException() throws IOException {
        SerializerProvider provider = mapper.getSerializerProvider();
        String fieldName = "testField";

        testSerializer.wrapAndThrow(provider, new JsonMappingException("Test mapping exception"), new Object(), fieldName);
    }

    @Test
    public void testIsDefaultSerializerWithDefaultSerializer() {
        JsonSerializer<String> defaultSerializer = new DefaultSerializer<>(String.class);

        Assert.assertTrue(testSerializer.isDefaultSerializer(defaultSerializer));
    }

    @Test
    public void testIsDefaultSerializerWithNonDefaultSerializer() {
        JsonSerializer<String> nonDefaultSerializer = new NonDefaultSerializer<>(String.class);

        Assert.assertFalse(testSerializer.isDefaultSerializer(nonDefaultSerializer));
    }

    private static class TestSerializer extends StdSerializer<String> {

        protected TestSerializer(Class<String> t) {
            super(t);
        }

        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value);
        }
    }

    private static class TestVisitor implements JsonFormatVisitorWrapper {
        private String visitedType;

        public String getVisitedType() {
            return visitedType;
        }

        @Override
        public JsonFormatVisitorWrapper expectAnyFormat(JavaType convertedType) throws JsonMappingException {
            visitedType = "any";
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectArrayFormat(JavaType elementType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectBooleanFormat(JavaType convertedType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectIntegerFormat(JavaType convertedType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectNullFormat(JavaType convertedType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectNumberFormat(JavaType convertedType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectObjectFormat(JavaType valueType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectStringFormat(JavaType convertedType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectMapFormat(JavaType keyType, JavaType valueType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectUnionFormat(JavaType... types) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectEnumFormat(JavaType valueType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectAnyFormat(JavaType valueType) throws JsonMappingException {
            return this;
        }

        @Override
        public JsonFormatVisitorWrapper expectBogusFormat(JavaType valueType) throws JsonMappingException {
            return this;
        }

        @Override
        public void handleError(String msg, Object... msgArgs) throws JsonMappingException {
        }
    }

    private static class DefaultSerializer<T> extends StdSerializer<T> {

        protected DefaultSerializer(Class<T> t) {
            super(t);
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        }
    }

    private static class NonDefaultSerializer<T> extends StdSerializer<T> {

        protected NonDefaultSerializer(Class<T> t) {
            super(t);
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        }
    }
}
