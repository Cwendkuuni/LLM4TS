package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    private StdSerializer<String> stringSerializer;

    @Before
    public void setup() {
        stringSerializer = new StdSerializer<String>(String.class);
    }

    @Test
    public void testHandledType() {
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testSerialize() throws JsonMappingException, JsonGenerationException {
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
        String value = "Hello, World!";
        JsonGenerator gen = new TestJsonGenerator();

        stringSerializer.serialize(value, gen, new SerializerProvider());

        ObjectNode resultNode = (ObjectNode) gen.getOutput();
        assertEquals("Hello, World!", resultNode.getTextValue());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        assertEquals(createSchemaNode("string"), stringSerializer.getSchema(new SerializerProvider(), String.class));
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        ObjectNode schema = stringSerializer.getSchema(new SerializerProvider(), String.class, true);
        assertTrue(schema.get("required").booleanValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializerProvider(), null);
        stringSerializer.acceptJsonFormatVisitor(visitor, String.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testWrapAndThrow() throws IOException {
        SerializerProvider provider = new StandardSerializerProvider();
        throw stringSerializer.wrapAndThrow(provider, new RuntimeException("Test Exception"), null, null);
    }

    @Test
    public void testIsDefaultSerializer() {
        assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
        assertFalse(stringSerializer.isDefaultSerializer(new CustomSerializer<>()));
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        SerializerProvider provider = new StandardSerializerProvider();
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        JsonSerializer<ObjectNode> objectSerializer = new StdSerializer<ObjectNode>(ObjectNode.class);
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class);

        Converter<ObjectNode, String> converter = new StringConverter();
        JsonSerializer<String> convertingSerializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonSerializer<?> createContextualSerializer(SerializerProvider provider,
                    BeanProperty property, JsonSerializer<?> existingSerializer) throws JsonMappingException {
                return new StdDelegatingSerializer<>(converter, String.class, existingSerializer);
            }
        };

        assertEquals(convertingSerializer, stringSerializer.findConvertingContentSerializer(provider, null, node));
        assertNull(stringSerializer.findConvertingContentSerializer(provider, null, node));
        assertNull(stringSerializer.findConvertingContentSerializer(provider, null, null));
        assertNull(stringSerializer.findConvertingContentSerializer(provider, null, new int[0]));
    }

    @Test
    public void testFindPropertyFilter() throws JsonMappingException {
        SerializerProvider provider = new StandardSerializerProvider();
        String filterId = "testFilter";
        Object filterValue = new Object();
        PropertyFilter filter = stringSerializer.findPropertyFilter(provider, filterId, filterValue);
        assertNull(filter);
    }

    private ObjectNode createSchemaNode(String type) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", type);
        return schema;
    }

    private static class CustomSerializer<T> extends StdSerializer<T> {
        @JacksonStdImpl
        public CustomSerializer() {
            super(String.class);
        }

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {}
    }

    private static class StringConverter implements Converter<ObjectNode, String> {
        @Override
        public String convert(ObjectNode src, SerializerProvider provider) throws IOException {
            return src.getTextValue();
        }
    }

    private static class StdDelegatingSerializer<T> extends JsonSerializer<T> {
        private final Converter<ObjectNode, T> converter;
        private final Class<T> delegateType;
        private final JsonSerializer<T> existingSerializer;

        public StdDelegatingSerializer(Converter<ObjectNode, T> converter, Class<T> delegateType, JsonSerializer<T> existingSerializer) {
            this.converter = converter;
            this.delegateType = delegateType;
            this.existingSerializer = existingSerializer;
        }

        @Override
        public void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
            ObjectNode node = converter.convertValue(value, JsonNode.class, provider);
            existingSerializer.serialize(node, jgen, provider);
        }

        @Override
        public Class<T> handledType() {
            return delegateType;
        }
    }

    private static class TestJsonGenerator extends JsonGenerator {
        private ObjectNode rootNode;

        public TestJsonGenerator() {
            super(new StringWriter());
            this.rootNode = createObjectNode();
        }

        @Override
        public void flush() throws IOException {
            // No-op
        }

        @Override
        public void close() throws IOException {
            // No-op
        }

        @Override
        public void writeStartObject() throws IOException {
            writeTree(rootNode);
        }

        @Override
        public void writeEndObject() throws IOException {
            // No-op
        }

        @Override
        public void writeFieldName(String fieldName) throws IOException {
            // No-op
        }

        @Override
        public void writeValue(String value) throws IOException {
            // No-op
        }

        @Override
        public void writeObject(JsonSerializable value) throws IOException {
            // No-op
        }

        @Override
        public void writeRawValue(String value) throws IOException {
            super.writeValue(value);
        }

        @Override
        public void writeTree(JsonNode value) throws IOException {
            rootNode.set(value.get("name").asText(), value);
        }

        public ObjectNode getOutput() {
            return rootNode;
        }
    }
}