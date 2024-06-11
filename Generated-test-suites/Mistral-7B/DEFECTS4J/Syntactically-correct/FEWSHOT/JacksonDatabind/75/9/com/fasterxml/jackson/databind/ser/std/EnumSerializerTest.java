package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test cases for {@link EnumSerializer}.
 */
@JacksonStdImpl
public class EnumSerializerTest {

    @Test
    public void testEnumSerializerConstruction() throws Exception {
        Enum<?> enumValue = MyEnum.ONE;
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromClass(MyEnum.class), null);
        assertSame(MyEnum.class, serializer.handledType());

        JsonGenerator gen = createTestJsonGenerator();
        ObjectNode rootNode = createTestObjectNode();

        serializer.serialize(enumValue, gen, new SerializerProvider());
        JsonNode serializedNode = gen.getOutputContent();
        assertEquals("ONE", serializedNode.asText());

        // Test with _serializeAsIndex = true
        EnumSerializer indexSerializer = new EnumSerializer(EnumValues.constructFromClass(MyEnum.class), Boolean.TRUE);
        gen = createTestJsonGenerator();
        serializer.serialize(enumValue, gen, new SerializerProvider());
        serializedNode = gen.getOutputContent();
        assertEquals("0", serializedNode.asText());
    }

    @Test
    public void testEnumSerializerContextualSerialization() throws Exception {
        Enum<?> enumValue = MyEnum.ONE;
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, null, null, null);

        JsonGenerator gen = createTestJsonGenerator();
        ObjectNode rootNode = createTestObjectNode();

        serializer.serialize(enumValue, gen, new SerializerProvider());
        JsonNode serializedNode = gen.getOutputContent();
        assertEquals("ONE", serializedNode.asText());

        // Test with _serializeAsIndex = true
        JsonSerializer<?> indexSerializer = serializer.createContextual(new SerializerProvider(), null);
        gen = createTestJsonGenerator();
        serializedNode = indexSerializer.serialize(enumValue, gen, new SerializerProvider());
        assertEquals("0", serializedNode.asText());
    }

    @Test
    public void testEnumSerializerSchemaGeneration() throws Exception {
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, null, null, null);
        JsonNode schemaNode = serializer.getSchema(new SerializerProvider(), null);

        ArrayNode enumNode = schemaNode.path("properties").get("myEnum").path("enum");
        assertEquals(2, enumNode.size());
        assertEquals("ONE", enumNode.get(0).asText());
        assertEquals("TWO", enumNode.get(1).asText());
    }

    @Test
    public void testEnumSerializerJsonFormatVisitor() throws Exception {
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, null, null, null);

        JsonFormatVisitorWrapper visitor = createTestJsonFormatVisitor();
        serializer.acceptJsonFormatVisitor(visitor, null);

        ArrayNode enumNode = visitor.getAcceptedNodes().get("properties").get("myEnum").get("enum");
        assertEquals(2, enumNode.size());
        assertEquals("ONE", enumNode.get(0).asText());
        assertEquals("TWO", enumNode.get(1).asText());
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    enum MyEnum {
        ONE, TWO
    }

    protected JsonGenerator createTestJsonGenerator() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        return jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig());
    }

    protected ObjectNode createTestObjectNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createObjectNode();
    }

    protected JsonFormatVisitorWrapper createTestJsonFormatVisitor() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializationConfig())
                .getRootValueSerializer().acceptJsonFormatVisitor(objectMapper.getFactory().createSerializerProvider(), null);
    }
}