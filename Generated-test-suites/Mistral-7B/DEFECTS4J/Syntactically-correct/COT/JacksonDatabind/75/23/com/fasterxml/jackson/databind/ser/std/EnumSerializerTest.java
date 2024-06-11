package com.example; // replace with your test package

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Test cases for EnumSerializer class.
 */
public class EnumSerializerTest {

    

    @Test
    public void testEnumSerializerBasicFunctionality() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        ObjectNode node = mapper.createObjectNode();
        JsonGenerator gen = mapper.getFactory().createGenerator(node);
        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        gen.close();
        String result = node.get("VALUE_1").asText();
        assertEquals(MyEnum.VALUE_1.name(), result);
    }

    @Test
    public void testEnumSerializerSerializeAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), true);

        ObjectNode node = mapper.createObjectNode();
        JsonGenerator gen = mapper.getFactory().createGenerator(node);
        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        gen.close();
        int result = node.get("VALUE_1").asInt();
        assertEquals(MyEnum.VALUE_1.ordinal(), result);
    }

    @Test
    public void testEnumSerializerSerializeUsingToString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        ObjectNode node = mapper.createObjectNode();
        JsonGenerator gen = mapper.getFactory().createGenerator(node);
        serializer.serialize(MyEnum.VALUE_1, gen, new SerializerProvider());
        gen.close();
        String result = node.get("VALUE_1").asText();
        assertEquals(MyEnum.VALUE_1.toString(), result);
    }

    @Test
    public void testEnumSerializerSchema() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        ObjectNode schemaNode = serializer.getSchema(new SerializerProvider(), null);
        ObjectNode enumNode = schemaNode.path("enum");
        Iterator<String> enumIterator = enumNode.iteratorelements();
        int index = 0;
        while (enumIterator.hasNext()) {
            String enumValue = enumIterator.next().asText();
            assertEquals(MyEnum.values()[index].name(), enumValue);
            index++;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnumSerializerInvalidSerializationShape() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.INVALID);
        EnumSerializer enumSerializer = new EnumSerializer(serializer, format);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testEnumSerializerNullEnumValue() throws Exception {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(MyEnum.class), null);

        ObjectNode node = mapper.createObjectNode();
        JsonGenerator gen = mapper.getFactory().createGenerator(node);
        try {
            serializer.serialize(null, gen, new SerializerProvider());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
        gen.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnumSerializerNullEnumValues() throws Exception {
        try {
            new EnumSerializer(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    

    enum MyEnum {
        VALUE_1,
        VALUE_2
    }
}