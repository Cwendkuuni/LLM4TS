package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testEnumSerializerConstruction() {
        Class<MyEnum> enumClass = MyEnum.class;
        EnumSerializer serializer = EnumSerializer.construct(enumClass, null, null, null);
        assertNotNull(serializer);
        assertEquals(enumClass, serializer.handledType());
    }

    @Test
    public void testEnumSerializerCreateContextual() throws JsonMappingException {
        Class<MyEnum> enumClass = MyEnum.class;
        SerializerProvider serializers = new SerializerProvider();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(serializers, enumClass), null);
        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormat.Shape shape = Shape.STRING;
        format.setShape(shape);
        serializer = serializer.createContextual(serializers, null);
        assertNotNull(serializer);
        assertSame(serializer.getClass(), EnumSerializer.class);
        assertEquals(shape, serializer._serializeAsIndex(serializers) ? Shape.INT : shape);
    }

    @Test
    public void testSerializeEnum() throws IOException {
        Class<MyEnum> enumClass = MyEnum.class;
        SerializerProvider serializers = new SerializerProvider();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(serializers, enumClass), null);
        ObjectWriter writer = new ObjectMapper().writer();
        Enum<?> enumValue = MyEnum.VALUE_3;
        JsonGenerator gen = writer.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(enumValue, gen, serializers);
        gen.close();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutput();
        String jsonString = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        assertEquals("\"VALUE_3\"", jsonString);
    }

    @Test
    public void testSerializeEnumAsIndex() throws IOException {
        Class<MyEnum> enumClass = MyEnum.class;
        SerializerProvider serializers = new SerializerProvider();
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(serializers, enumClass), true);
        ObjectWriter writer = new ObjectMapper().writer();
        Enum<?> enumValue = MyEnum.VALUE_3;
        JsonGenerator gen = writer.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(enumValue, gen, serializers);
        gen.close();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) gen.getOutput();
        String jsonString = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        assertEquals("3", jsonString);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        Class<MyEnum> enumClass = MyEnum.class;
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = mapper.findValueSerializer(enumClass);
        JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), null);
        ObjectNode schemaObject = (ObjectNode) schema;
        assertEquals("string", schemaObject.get("type").valueAsText());
        ArrayNode enumNode = schemaObject.get("properties").get("enum");
        assertEquals(3, enumNode.size());
        assertEquals("VALUE_1", enumNode.get(0).valueAsText());
        assertEquals("VALUE_2", enumNode.get(1).valueAsText());
        assertEquals("VALUE_3", enumNode.get(2).valueAsText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Class<MyEnum> enumClass = MyEnum.class;
        ObjectMapper mapper = new ObjectMapper();
        EnumSerializer serializer = mapper.findValueSerializer(enumClass);
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(SerializableString.class, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        ArrayNode enumNode = visitor.getArrayNode();
        assertEquals(3, enumNode.size());
        assertEquals("VALUE_1", enumNode.get(0).valueAsText());
        assertEquals("VALUE_2", enumNode.get(1).valueAsText());
        assertEquals("VALUE_3", enumNode.get(2).valueAsText());
    }

    @JsonFormat(shape = Shape.STRING)
    enum MyEnum {
        VALUE_1, VALUE_2, VALUE_3
    }
}