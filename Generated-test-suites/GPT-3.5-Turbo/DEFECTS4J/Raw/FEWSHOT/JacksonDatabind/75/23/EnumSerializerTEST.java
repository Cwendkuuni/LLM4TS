package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;

    @Before
    public void setup() {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        enumSerializer = new EnumSerializer(enumValues, Boolean.TRUE);
    }

    @Test
    public void testGetEnumValues() {
        EnumValues result = enumSerializer.getEnumValues();
        assertTrue(result.enums().contains(TestEnum.VALUE1));
        assertTrue(result.enums().contains(TestEnum.VALUE2));
        assertFalse(result.enums().contains(TestEnum.INVALID));
        assertEquals(TestEnum.VALUE1.name(), result.serializedValueFor(TestEnum.VALUE1).toString());
    }

    @Test
    public void testSerializeAsIndex() {
        SerializerProvider provider = null;
        assertTrue(enumSerializer._serializeAsIndex(provider));
    }

    @Test
    public void testSerializeAsIndexWithFormat() {
        SerializerProvider provider = null;
        JsonFormat.Value format = null;
        boolean result = enumSerializer._isShapeWrittenUsingIndex(TestEnum.class, format, true).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        enumSerializer.serialize(TestEnum.VALUE1, gen, serializers);
    }

    @Test
    public void testCreateContextual() throws IOException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonSerializer<?> result = enumSerializer.createContextual(serializers, property);
        assertEquals(enumSerializer, result);
    }

    @Test
    public void testGetSchema() throws IOException {
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonNode result = enumSerializer.getSchema(provider, typeHint);
        assertTrue(result instanceof ObjectNode);
        ObjectNode objectNode = (ObjectNode) result;
        assertEquals("string", objectNode.get("type").asText());
        assertTrue(objectNode.get("type").isTextual());
        assertTrue(objectNode.get("required").asBoolean());
        assertTrue(objectNode.get("properties").isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaWithShape() throws IOException {
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        enumSerializer.getSchema(provider, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    public static enum TestEnum {
        VALUE1,
        VALUE2,
        INVALID;
    }
}