package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

public class EnumSerializerTest {
    @Test
    public void testEnumSerializer() {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum1.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        assertEquals(enumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testSerialize() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum1.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        enumSerializer.serialize(TestEnum1.VALUE1, gen, serializers);
    }

    @Test
    public void testGetSchema() {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum1.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        JsonSerializer<?> serializer = enumSerializer;
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.get("enum").isArray());
        assertEquals("VALUE1", schema.get("enum").get(0).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum1.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);

        JsonFormatVisitorWrapper visitorWrapper = new ObjectMapper().getSerializationConfig().getDefaultJsonFormatVisitor().expectStringFormat(TestEnum1.class);
        JavaType typeHint = null;
        enumSerializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);

        JsonStringFormatVisitor visitor = visitorWrapper.expectStringFormat(typeHint);
        Set<String> enums = new LinkedHashSet<>();
        enums.add("VALUE1");
        enums.add("VALUE2");
        enums.add("VALUE3");
        enums.add("VALUE4");
        enums.add("VALUE5");
        enums.add("VALUE6");
        enums.add("VALUE7");
        enums.add("VALUE8");
        enums.add("VALUE9");
        enums.add("VALUE10");

        assertEquals(enums, visitor.getEnums());
    }
}