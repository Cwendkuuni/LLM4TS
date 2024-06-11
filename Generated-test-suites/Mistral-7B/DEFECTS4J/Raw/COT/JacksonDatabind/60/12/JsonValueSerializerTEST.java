package com.example.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonValueSerializerTest {

    // Create an instance of the JsonObjectSerializer under test
    private ObjectMapper mapper;
    private JsonValueSerializer serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        serializer = mapper.findValueSerializer(String.class, null);
    }

    @Test
    public void testDefaultSerialization() throws Exception {
        String input = "Hello World";
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
        serializer.serializeWithType(input, gen, null, mapper.getTypeSerializer());
        String output = gen.getOutputContent();
        assertEquals("\"Hello World\"", output);
    }

    @Test
    public void testSerializationWithTypeSerializer() throws Exception {
        String input = "Hello World";
        String expectedType = "HelloWorldType";

        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
        serializer.serializeWithType(input, gen, null, mapper.getTypeSerializer());
        String output = gen.getOutputContent();
        assertEquals("{\"@type\":\"" + expectedType + "\",\"value\":\"Hello World\"}", output);
    }

    @Test
    public void testSerializationWithSchemaAwareValueSerializer() throws Exception {
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class, null);
        assertNotNull(stringSerializer);
        assertNotNull(stringSerializer.getSchemaProvider());

        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) serializer;
        JsonSerializer<Object> valueSerializer = jsonValueSerializer._valueSerializer;
        if (valueSerializer instanceof SchemaAware) {
            Schema providerSchema = ((SchemaAware) valueSerializer).getSchema(mapper.getFactory(), null);
            JsonNode schemaNode = JsonSchema.construct(providerSchema);
            assertEquals(JsonSchema.Type.STRING, schemaNode.path("/$id/type").asInt());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullValue() throws Exception {
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
        serializer.serializeWithType(null, gen, null, mapper.getTypeSerializer());
    }

    @Test
    public void testSerializationWithEnum() throws Exception {
        EnumTest enumTest = new EnumTest();

        // Test serialization for an enum value
        String input = "RED";
        JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
        JsonFormatVisitorWrapper visitor = mapper.schemaManager().getSchemaProvider().findSchemaVisitor(JsonFormatVisitorWrapper.Std.class);
        JsonNode expectedSchema = JsonSchema.construct(mapper.getTypeFactory()
                .constructType(EnumTest.Color.class));
        visitor.setSchemaProvider(mapper.getSchemaProvider());
        visitor.visitObject(EnumTest.Color.class, null, null);
        JsonNode schema = visitor.getSchemaProvider().getSchemaProvider().getSchema(mapper.getFactory(), null);
        assertEquals(expectedSchema, schema);

        serializer.acceptJsonFormatVisitor(visitor, null);

        serializer.serializeWithType(enumTest.RED, gen, null, mapper.getTypeSerializer());
        String output = gen.getOutputContent();
        assertEquals("{\"@type\":\"Color\",\"value\":\"RED\"}", output);

        // Test deserialization for an enum value
        ObjectMapper deserializer = new ObjectMapper();
        Object deserialized = deserializer.readValue(output, EnumTest.Color.class);
        assertEquals(enumTest.RED, deserialized);
    }

    static class EnumTest {
        public enum Color {
            RED, GREEN, BLUE;

            @JsonSerialize(using = JsonValueSerializer.class)
            public Object getValue() {
                return name();
            }
        }
    }

}