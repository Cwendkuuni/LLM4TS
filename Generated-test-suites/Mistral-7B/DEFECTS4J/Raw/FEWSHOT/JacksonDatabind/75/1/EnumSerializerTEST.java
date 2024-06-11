package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test cases for {@link EnumSerializer} class.
 */
public class EnumSerializerTest {

    @Test
    public void testSerializationAndDeserialization() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymous(MyEnum.class), false);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithContextualSerializer(MyEnum.class, serializer);

        // Serialize to JSON using name
        MyEnum enumValue = MyEnum.VALUE_1;
        String json = writer.writeValueAsString(enumValue);
        assertNotNull(json);
        assertEquals("\"VALUE_1\"", json);

        // Deserialize from JSON using name
        MyEnum deserializedEnum = mapper.readValue(json, MyEnum.class);
        assertNotNull(deserializedEnum);
        assertEquals(enumValue, deserializedEnum);

        // Serialize to JSON using index
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setSerializationFeature(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        JsonGenerator gen = serializers.getFactory().createGenerator(System.out, null);
        serializer.serialize(enumValue, gen, serializers);
        gen.flush();
        assertEquals("1", mapper.readTree(System.out.toString()).asText());

        // Deserialize from JSON using index
        ObjectNode node = mapper.readTree("1");
        MyEnum deserializedEnumFromIndex = mapper.treeToValue(node, MyEnum.class);
        assertNotNull(deserializedEnumFromIndex);
        assertEquals(enumValue, deserializedEnumFromIndex);
    }

    @Test
    public void testSerializationAndDeserializationWithToString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymous(MyEnum.class), true);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithContextualSerializer(MyEnum.class, serializer);

        // Serialize to JSON using toString
        MyEnum enumValue = MyEnum.VALUE_1;
        String json = writer.writeValueAsString(enumValue);
        assertNotNull(json);
        assertEquals("VALUE_1", json);

        // Deserialize from JSON using toString
        MyEnum deserializedEnum = mapper.readValue(json, MyEnum.class);
        assertNotNull(deserializedEnum);
        assertEquals(enumValue, deserializedEnum);
    }

    @Test
    public void testSerializationAndDeserializationWithJsonFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);

        MyEnumEnum enumValue = MyEnumEnum.VALUE_1;
        @JsonFormat(shape = JsonFormat.Shape.INT)
        MyEnumEnum deserializedEnum = mapper.readValue("1", MyEnumEnum.class);
        assertNotNull(deserializedEnum);
        assertEquals(enumValue, deserializedEnum);
    }

    @Test
    public void testCreateContextual() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymous(MyEnum.class), false);
        SerializerProvider serializers = new SerializerProvider();
        assertEquals(serializer, serializer.createContextual(serializers, null));

        // Test with JsonFormat
        MyEnumEnum deserializedEnum = new MyEnumEnum("VALUE_2");
        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.INT);
        assertEquals(serializer, serializer.createContextual(serializers, new BeanProperty()));
        assertEquals(MyEnumEnum.VALUE_2, mapper.readValue("1", MyEnumEnum.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsShapeWrittenUsingIndexWithInvalidShape() {
        EnumSerializer. _isShapeWrittenUsingIndex(MyEnum.class, null, false);
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymous(MyEnum.class), false);
        ObjectNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertNotNull(schemaNode);
        assertEquals("string", schemaNode.get("type").asText());
        assertEquals("\"VALUE_1\"", schemaNode.path("enum").get(0).asText());
        assertEquals("\"VALUE_2\"", schemaNode.path("enum").get(1).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymous(MyEnum.class), false);
        ObjectNode schemaNode = mapper.treeToObject(serializer.acceptJsonFormatVisitor(null, null));
        assertNotNull(schemaNode);
        assertEquals("string", schemaNode.get("type").asText());
        assertEquals("\"VALUE_1\"", schemaNode.path("enum").get(0).asText());
        assertEquals("\"VALUE_2\"", schemaNode.path("enum").get(1).asText());
    }

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.anonymous(MyEnum.class), false);
        SerializerProvider serializers = new SerializerProvider();
        assertFalse(serializer._serializeAsIndex(serializers));
        assertTrue(serializer._serializeAsIndex(serializers, new JsonFormat.Value(null, JsonFormat.Shape.INT)));
    }

    @Test
    public void testIsShapeWrittenUsingIndex() throws Exception {
        assertTrue(EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, null, false));
        assertFalse(EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, null, true));

        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.INT);
        assertTrue(EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, format, false));
        assertFalse(EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, format, true));

        JsonFormat formatWithProperty = new JsonFormat();
        formatWithProperty.setShape(JsonFormat.Shape.INT);
        formatWithProperty.setSerializationInclusion(JsonFormat.Serialization.INCLUDE_FOR_ALL_NULLS);
        assertTrue(EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, formatWithProperty, false));
        assertFalse(EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, formatWithProperty, true));
    }

    enum MyEnum {
        VALUE_1,
        VALUE_2
    }

    enum MyEnumEnum {
        VALUE_1,
        VALUE_2;

        private final String name;

        MyEnumEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}