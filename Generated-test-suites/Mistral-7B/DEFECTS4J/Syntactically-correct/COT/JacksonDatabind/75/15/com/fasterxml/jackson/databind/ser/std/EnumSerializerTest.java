package com.fasterxml.jackson.databind.ser.std; // import necessary packages here

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test cases for EnumSerializer class
 */
public class EnumSerializerTest {

    /**
     * Test constructor and getters
     */
    @Test
    public void testConstructorAndGetters() {
        EnumValues enumValues = EnumValues.all(MyEnum.class);
        EnumSerializer serializer = new EnumSerializer(enumValues, false);
        EnumSerializer contextualSerializer = serializer.createContextual(null, null);

        assertEquals(MyEnum.class, serializer.handledType());
        assertEquals(enumValues, serializer.getEnumValues());
        assertEquals(false, serializer.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING));
        assertEquals(false, contextualSerializer.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING));
    }

    /**
     * Test serialization with index
     */
    @Test
    public void testSerializationWithIndex() throws IOException {
        EnumValues enumValues = EnumValues.all(MyEnum.class);
        EnumSerializer serializer = new EnumSerializer(enumValues, true);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        serializer.serialize(MyEnum.VALUE_1, gen, serializers);
        verify(gen).writeNumber(0);
    }

    /**
     * Test serialization with toString()
     */
    @Test
    public void testSerializationWithToString() throws IOException {
        EnumValues enumValues = EnumValues.all(MyEnum.class);
        EnumSerializer serializer = new EnumSerializer(enumValues, null);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        serializer.serialize(MyEnum.VALUE_1, gen, serializers);
        verify(gen).writeString("VALUE_1");
    }

    /**
     * Test serialization with name()
     */
    @Test
    public void testSerializationWithName() throws IOException {
        EnumValues enumValues = EnumValues.all(MyEnum.class);
        EnumSerializer serializer = new EnumSerializer(enumValues, null);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        serializer.serialize(MyEnum.VALUE_1, gen, serializers);
        verify(gen).writeString("VALUE_1_NAME");
    }

    /**
     * Test serialization with custom serialization format
     */
    @Test
    public void testSerializationWithCustomFormat() throws IOException {
        EnumValues enumValues = EnumValues.all(MyEnum.class);
        EnumSerializer serializer = new EnumSerializer(enumValues, null);

        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.STRING);
        format.setSerializationInclusion(JsonFormat.Serialization.INCLUDE);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        ObjectNode rootNode = mapper.createObjectNode();
        JsonSerializer serializerForTest = mapper.findValueSerializer(MyEnum.class, rootNode, null);
        assertSame(serializer, serializerForTest);

        ObjectNode node = mapper.treeToObject(mapper.readTree(mapper.writerWithRootValue(serializer).writeValueAsString(MyEnum.VALUE_1)));
        assertEquals("VALUE_1_NAME", node.get("value").asText());

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectStringFormat(MyEnum.class)).thenReturn(mock(JsonStringFormatVisitor.class));
        serializer.acceptJsonFormatVisitor(visitor, null);
        verify(visitor).enumTypes(Collections.singletonList("VALUE_1_NAME"));
    }

    /**
     * Test serialization with null value
     */
    @Test(expected = NullPointerException.class)
    public void testSerializationWithNullValue() throws IOException {
        EnumSerializer serializer = new EnumSerializer(null, null);
        serializer.serialize(null, mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    /**
     * Test deserialization with index
     */
    @Test
    public void testDeserializationWithIndex() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add(0);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("value", arrayNode);
        String jsonString = mapper.writerWithRootValue(rootNode).writeValueAsString(rootNode);

        EnumSerializer serializer = new EnumSerializer(null, true);
        Enum<?> deserializedEnum = mapper.readValue(jsonString, Enum.class);
        assertEquals(MyEnum.VALUE_1, deserializedEnum);
    }

    /**
     * Test deserialization with toString()
     */
    @Test
    public void testDeserializationWithToString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("value", "VALUE_1");
        String jsonString = mapper.writerWithRootValue(rootNode).writeValueAsString(rootNode);

        EnumSerializer serializer = new EnumSerializer(null, null);
        Enum<?> deserializedEnum = mapper.readValue(jsonString, Enum.class);
        assertEquals(MyEnum.VALUE_1, deserializedEnum);
    }

    /**
     * Test deserialization with name()
     */
    @Test
    public void testDeserializationWithName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("value", "VALUE_1_NAME");
        String jsonString = mapper.writerWithRootValue(rootNode).writeValueAsString(rootNode);

        EnumSerializer serializer = new EnumSerializer(null, null);
        Enum<?> deserializedEnum = mapper.readValue(jsonString, Enum.class);
        assertEquals(MyEnum.VALUE_1, deserializedEnum);
    }

    /**
     * Test deserialization with custom deserializer
     */
    @Test
    public void testDeserializationWithCustomSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("value", "CUSTOM_VALUE");
        String jsonString = mapper.writerWithRootValue(rootNode).writeValueAsString(rootNode);

        MyEnumDeserializer customDeserializer = new MyEnumDeserializer();
        mapper.registerModule(new SimpleModule().addSerializer(MyEnum.class, customDeserializer));

        Enum<?> deserializedEnum = mapper.readValue(jsonString, Enum.class);
        assertEquals(MyEnum.CUSTOM_VALUE, deserializedEnum);
    }

    /**
     * Test deserialization with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializationWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = null;

        Enum<?> deserializedEnum = mapper.readValue(mapper.writerWithRootValue(rootNode).writeValueAsString(rootNode), Enum.class);
    }

    /**
     * Test schema generation
     */
    @Test
    public void testSchemaGeneration() {
        EnumSerializer serializer = new EnumSerializer(null, null);
        JsonNode schema = serializer.getSchema(mock(SerializerProvider.class), null);

        JsonSchema schemaObject = JsonSchemaFactory.getJsonSchema(schema);
        assertEquals("string", schemaObject.getTitle());
        assertTrue(schemaObject.getDescription().contains("Enum serialization schema"));
        assertTrue(schemaObject.getDescription().contains("Used by Jackson for serialization"));
        assertTrue(schemaObject.getDescription().contains("Does not validate JSON on deserialization"));
        assertTrue(schemaObject.getDescription().contains("Can be extended by adding annotations"));

        JsonSchema enumSchema = schemaObject.get("properties").get("value").getSchema();
        assertEquals("string", enumSchema.getTitle());
        assertEquals("array", enumSchema.getType());
        assertTrue(enumSchema.getDescription().contains("Schema for Enum values"));
        assertTrue(enumSchema.getDescription().contains("Values can be arrays or objects"));
        assertTrue(enumSchema.getDescription().contains("Values are serialized as strings"));
    }

    private static class MyEnumDeserializer extends StdScalarDeserializer<MyEnum> {
        @Override
        public MyEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return MyEnum.valueOf(p.getValueAsString());
        }
    }

    private static enum MyEnum {
        VALUE_1("VALUE_1_NAME"),
        VALUE_2("VALUE_2_NAME"),
        VALUE_3("VALUE_3_NAME"),
        VALUE_4("VALUE_4_NAME"),
        CUSTOM_VALUE("CUSTOM_VALUE_NAME");

        private final String name;

        MyEnum(String name) {
            this.name = name;
        }

        @JsonSerialize(using = EnumSerializer.class)
        public String getValue() {
            return name;
        }

        @JsonValue
        public String toString() {
            return name;
        }
    }
}