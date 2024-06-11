package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnumSerializerTest {

    @Test
    public void testSerialize_EnumAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(
            config, (Class<Enum<?>>) enumClass), true);
        Enum testEnum = EnumTest.TEST_ENUM1;
        String expected = "0";

        String result = enumSerializer.serialize(testEnum, gen, serializers).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_EnumUsingToString() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(
            config, (Class<Enum<?>>) enumClass), false);
        Enum testEnum = EnumTest.TEST_ENUM2;
        String expected = "\"TEST_ENUM2\"";

        String result = enumSerializer.serialize(testEnum, gen, serializers).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testGetSchema_EnumAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(
            config, (Class<Enum<?>>) enumClass), true);
        ObjectNode expected = createSchemaNode("integer", true);

        ObjectNode result = enumSerializer.getSchema(provider, typeHint);

        assertEquals(expected, result);
    }

    @Test
    public void testGetSchema_EnumAsEnumName() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(
            config, (Class<Enum<?>>) enumClass), false);
        ObjectNode expected = createSchemaNode("string", true);
        ArrayNode enumNode = expected.putArray("enum");
        enumNode.add("TEST_ENUM1");
        enumNode.add("TEST_ENUM2");
        enumNode.add("TEST_ENUM3");

        ObjectNode result = enumSerializer.getSchema(provider, typeHint);

        assertEquals(expected, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_EnumAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(
            config, (Class<Enum<?>>) enumClass), true);
        JsonFormatVisitorWrapper visitor;
        JavaType typeHint;

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Check if visitor received the expected format for number type
        assertEquals(JsonParser.NumberType.INT, visitor.getFormatChecked());
    }

    @Test
    public void testAcceptJsonFormatVisitor_EnumAsEnumName() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(
            config, (Class<Enum<?>>) enumClass), false);
        JsonFormatVisitorWrapper visitor;
        JavaType typeHint;
        ArrayNode enumNode = createSchemaNode();
        enumNode.add("TEST_ENUM1");
        enumNode.add("TEST_ENUM2");
        enumNode.add("TEST_ENUM3");

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Check if visitor received the expected enum values
        assertEquals(enumNode, visitor.getEnumValuesChecked());
    }
}