package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.EnumValues;

/**
 * Test cases for {@link EnumSerializer}.
 */
@JacksonStdImpl
public class EnumSerializerTest {

    private static final Enum<?>[] ENUM_VALUES = Enum.values(CustomEnum.class);

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        EnumSerializer enumSerializer = EnumSerializer.construct(CustomEnum.class, null, null, null);

        for (Enum<?> enumValue : ENUM_VALUES) {
            rootNode.put("value", enumSerializer.serialize(enumValue, null, serializers));
        }

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println("Serialized JSON: " + json);
        assertEquals("{\"value\":[\"VALUE_1\",\"VALUE_2\",\"VALUE_3\"]}", json);
    }

    @Test
    public void testSerializeWithIndex() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapperWithEnumIndex = objectMapper.copy();
        objectMapperWithEnumIndex.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);

        ObjectNode rootNode = objectMapper.createObjectNode();

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        EnumSerializer enumSerializer = EnumSerializer.construct(CustomEnum.class, null, null, null);

        for (Enum<?> enumValue : ENUM_VALUES) {
            rootNode.put("value", enumSerializer.serialize(enumValue, null, serializers));
        }

        String jsonWithOrdinals = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println("Serialized JSON with ordinals: " + jsonWithOrdinals);
        assertEquals("{\"value\":[0,1,2]}", jsonWithOrdinals);

        String jsonWithEnumIndex = objectMapperWithEnumIndex.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println("Serialized JSON with enum index: " + jsonWithEnumIndex);
        assertEquals("{\"value\":[\"VALUE_1\",\"VALUE_2\",\"VALUE_3\"]}", jsonWithEnumIndex);
    }

    @Test
    public void testGetSchema() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        EnumSerializer enumSerializer = EnumSerializer.construct(CustomEnum.class, null, null, null);

        JsonNode schema = enumSerializer.getSchema(serializers, EnumSerializer.class);
        System.out.println("Schema: " + schema.toString());
        assertEquals("{\"type\":\"object\",\"properties\":{\"value\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"enum\":[\"VALUE_1\",\"VALUE_2\",\"VALUE_3\"]}}}}", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonFormatVisitorWrapper jsonFormatVisitor = objectMapper.schemaFactory().getJsonFormatVisitor(ObjectMapper.DefaultTyping.DYNATYPING, CustomEnum[].class);

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        EnumSerializer enumSerializer = EnumSerializer.construct(CustomEnum.class, null, null, null);

        enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, EnumSerializer.class);

        JsonSchema schema = jsonFormatVisitor.getSchema();
        System.out.println("Schema: " + schema.toString());
        assertEquals("{\"type\":\"array\",\"items\":{\"type\":\"string\",\"enum\":[\"VALUE_1\",\"VALUE_2\",\"VALUE_3\"]}}", schema.toString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        EnumSerializer enumSerializer = EnumSerializer.construct(CustomEnum.class, null, null, null);

        JsonProperty property = new JsonProperty("test");
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);

        JsonSerializer<Enum<?>> contextualSerializer = enumSerializer.createContextual(serializers, property);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("test", contextualSerializer.serialize(Enum.valueOf(CustomEnum.class, "VALUE_1"), null, serializers));

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println("Serialized JSON: " + json);
        assertEquals("{\"test\":\"VALUE_1\"}", json);
    }
}

enum CustomEnum {
    VALUE_1,
    VALUE_2,
    VALUE_3
}