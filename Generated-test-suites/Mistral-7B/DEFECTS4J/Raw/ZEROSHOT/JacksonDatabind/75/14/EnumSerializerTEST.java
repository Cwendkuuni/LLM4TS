package com.example.jackson.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnumSerializerTest {

    private ObjectMapper mapper;
    private EnumSerializer enumSerializer;
    private EnumValues enumValues;

    @BeforeAll
    public void setup() throws JsonProcessingException {
        mapper = new ObjectMapper();
        MyEnum[] myEnumValues = MyEnum.values();
        enumValues = EnumValues.constructFromEnum(MyEnum.class, myEnumValues);
        enumSerializer = new EnumSerializer(enumValues, true);
    }

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        enumSerializer.serialize(MyEnum.VALUE_1, gen, null);
        assertEquals(0, gen.getOutputBuffer().readInt());

        gen.reset();
        enumSerializer.serialize(MyEnum.VALUE_2, gen, null);
        assertEquals(1, gen.getOutputBuffer().readInt());
    }

    @Test
    public void testSerializeAsString() throws JsonProcessingException {
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        gen.setPrePrecedence(true); // needed to write string values before numbers
        enumSerializer.serialize(MyEnum.VALUE_1, gen, null);
        assertEquals("VALUE_1", gen.getOutputBuffer().toString());

        gen.reset();
        enumSerializer.serialize(MyEnum.VALUE_2, gen, null);
        assertEquals("VALUE_2", gen.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        Schema schema = mapper.getTypeFactory().constructSchema(ObjectNode.class);
        JsonNode schemaNode = enumSerializer.getSchema(mapper.getSerializationConfig(), Object.class);

        assertInstanceOf(ObjectNode.class, schemaNode);
        ObjectNode objectNode = (ObjectNode) schemaNode;

        assertFalse(objectNode.get("type").isTextual());
        assertTrue(objectNode.get("type").isContainerNode());
        assertNotNull(objectNode.get("enum"));

        ArrayNode enumNode = (ArrayNode) objectNode.get("enum");
        assertEquals(2, enumNode.size());

        String value1 = enumNode.get(0).asText();
        String value2 = enumNode.get(1).asText();

        assertEquals("VALUE_1", value1);
        assertEquals("VALUE_2", value2);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode enumNode = mapper.createObjectNode();

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(null, Object.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, Object.class);

        ObjectNode serializerNode = (ObjectNode) visitor.asPrefixTree().findValue("com.fasterxml.jackson.databind.ser.std.EnumSerializer");
        assertInstanceOf(ObjectNode.class, serializerNode);

        ArrayNode enumValuesNode = (ArrayNode) serializerNode.get("enumValues");
        assertEquals(2, enumValuesNode.size());

        String value1 = enumValuesNode.get(0).asText();
        String value2 = enumValuesNode.get(1).asText();

        assertEquals("VALUE_1", value1);
        assertEquals("VALUE_2", value2);

        visitor = mapper.schemaFactory().getJsonFormatVisitor(JsonFormat.Value.builder(Shape.STRING, null).build(), Object.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, Object.class);

        serializerNode = (ObjectNode) visitor.asPrefixTree().findValue("com.fasterxml.jackson.databind.ser.std.EnumSerializer");
        assertInstanceOf(ObjectNode.class, serializerNode);

        enumNode = (ObjectNode) serializerNode.get("serializers");
        assertTrue(enumNode.isContainerNode());

        ObjectNode serializerNode2 = (ObjectNode) enumNode.get(0);
        assertEquals("VALUE_1", serializerNode2.get("value").asText());
        assertEquals("java.lang.String", serializerNode2.get("valueClass").asText());
        assertEquals("string", serializerNode2.get("type").asText());

        serializerNode2 = (ObjectNode) enumNode.get(1);
        assertEquals("VALUE_2", serializerNode2.get("value").asText());
        assertEquals("java.lang.String", serializerNode2.get("valueClass").asText());
        assertEquals("string", serializerNode2.get("type").asText());

        rootNode.set("enumSerializer", enumNode);
        ObjectNode jsonNode = mapper.writeValueAsObject(rootNode);

        assertEquals("{\"enumSerializer\":{\"serializers\":[{\"value\":\"VALUE_1\",\"valueClass\":\"java.lang.String\",\"type\":\"string\"},{\"value\":\"VALUE_2\",\"valueClass\":\"java.lang.String\",\"type\":\"string\"}]}}", jsonNode.toString());
    }
}

enum MyEnum {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    VALUE_1,
    @JsonFormat(shape = JsonFormat.Shape.STRING, value = "VALUE_2_STRING")
    VALUE_2;

    public static final EnumSerializer SERIALIZER = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), false);
}