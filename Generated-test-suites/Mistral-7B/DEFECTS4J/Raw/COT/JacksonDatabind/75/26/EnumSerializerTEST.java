package com.example; // Replace with your test package

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

    

    @Test
    public void testEnumSerializerConstructor() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
        assertNotNull(serializer);
    }

    @Test
    public void testCreateContextualSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, objectMapper.getConfiguration(), null, null);
        assertInstanceOf(EnumSerializer.class, serializer);
    }

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, objectMapper.getConfiguration(), null, null);
        ObjectWriter writer = objectMapper.writer().with(SerializationFeature.WRITE_ENUMS_USING_INDEX);

        MyEnum enumValue = MyEnum.VAL1;
        String serializedValue = writer.writeValueAsString(enumValue);
        int index = Integer.parseInt(serializedValue);

        assertEquals(index, serializer.serialize(enumValue).nextToken().asInt());
    }

    @Test
    public void testSerializeToString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer();
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, objectMapper.getConfiguration(), null, null);

        MyEnum enumValue = MyEnum.VAL1;
        String serializedValue = writer.writeValueAsString(enumValue);

        assertEquals(enumValue.toString(), serializer.serialize(enumValue).getValueAsString());
    }

    @Test
    public void testSerializeCustomValues() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer();
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, objectMapper.getConfiguration(), null, null);

        MyEnum enumValue = MyEnum.VAL2;
        ObjectNode serializedValue = (ObjectNode) writer.writeValueAsTree(enumValue);
        String expectedValue = "CUSTOM_VALUE_2";

        assertEquals(expectedValue, serializedValue.get("value").asText());
        assertEquals(enumValue.toString(), serializer.serialize(enumValue).getValueAsString());
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeUnknownEnum() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = EnumSerializer.construct(MyUnknownEnum.class, objectMapper.getConfiguration(), null, null);

        MyUnknownEnum unknownEnum = MyUnknownEnum.UNKNOWN;
        serializer.serialize(unknownEnum, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualSerializerWithNullProperty() {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = serializer.createContextual(null, null);
        fail("Should not be able to create contextual serializer with null property");
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualSerializerWithInvalidType() {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = serializer.createContextual(objectMapper.getSerializationConfig(), new Foo());
        fail("Should not be able to create contextual serializer with invalid type");
    }

    @Test
    public void testGetEnumValues() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, objectMapper.getConfiguration(), null, null);

        JsonNode schemaNode = serializer.getSchema(objectMapper.getSerializationConfig(), MyEnum.class);
        ArrayNode enumNode = (ArrayNode) schemaNode.get("enum");

        assertEquals(MyEnum.VAL1.getName(), enumNode.get(0).asText());
        assertEquals(MyEnum.VAL2.getName(), enumNode.get(1).asText());
        assertEquals(MyEnum.VAL3.getName(), enumNode.get(2).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EnumSerializer serializer = EnumSerializer.construct(MyEnum.class, objectMapper.getConfiguration(), null, null);

        JsonNode schemaNode = serializer.acceptJsonFormatVisitor(null, MyEnum.class);
        ArrayNode enumNode = (ArrayNode) schemaNode.get("enum");

        assertEquals(MyEnum.VAL1.toString(), enumNode.get(0).asText());
        assertEquals(MyEnum.VAL2.toString(), enumNode.get(1).asText());
        assertEquals(MyEnum.VAL3.toString(), enumNode.get(2).asText());
    }

    

    enum MyEnum {
        VAL1,
        VAL2("CUSTOM_VALUE_2"),
        VAL3;

        private final String _serializedValue;

        MyEnum() {
            this(null);
        }

        MyEnum(String serializedValue) {
            this._serializedValue = serializedValue;
        }

        public String getName() {
            return name();
        }

        public String toString() {
            return _serializedValue != null ? _serializedValue : name();
        }
    }

    enum MyUnknownEnum {
        UNKNOWN;
    }

    class Foo {
    }
}