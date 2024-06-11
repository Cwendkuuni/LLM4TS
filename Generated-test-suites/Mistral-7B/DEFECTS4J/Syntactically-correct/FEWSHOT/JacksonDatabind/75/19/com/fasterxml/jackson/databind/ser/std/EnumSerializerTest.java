package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
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

    @Test
    public void testSerialization() throws IOException {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
        ObjectNode node = testSerializationHelper(serializer, MyEnum.VALUE1);
        assertEquals("VALUE1", node.get("value").asText());

        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null).withFormat(format);
        node = testSerializationHelper(serializer, MyEnum.VALUE1);
        assertEquals("VALUE1", node.get("value").asText());

        format.shape(JsonFormat.Shape.INT);
        serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), true).withFormat(format);
        node = testSerializationHelper(serializer, MyEnum.VALUE1);
        assertEquals("0", node.get("value").asText());
    }

    @Test
    public void testSerializationWithCustomValues() throws IOException {
        EnumSerializer serializer = new EnumSerializer(CustomEnumValues.INSTANCE, null);
        ObjectNode node = testSerializationHelper(serializer, CustomEnum.FOO);
        assertEquals("customFOO", node.get("value").asText());

        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        serializer = new EnumSerializer(CustomEnumValues.INSTANCE, null).withFormat(format);
        node = testSerializationHelper(serializer, CustomEnum.FOO);
        assertEquals("customFOO", node.get("value").asText());

        format.shape(JsonFormat.Shape.INT);
        serializer = new EnumSerializer(CustomEnumValues.INSTANCE, true).withFormat(format);
        node = testSerializationHelper(serializer, CustomEnum.FOO);
        assertEquals("0", node.get("value").asText());
    }

    @Test
    public void testDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node1 = mapper.readTree("{\"value\": 0}");
        Enum<?> enum1 = mapper.readValue(node1, Enum.class);
        assertSame(MyEnum.VALUE1, enum1);

        JsonNode node2 = mapper.readTree("{\"value\": \"VALUE2\"}");
        Enum<?> enum2 = mapper.readValue(node2, Enum.class);
        assertSame(MyEnum.VALUE2, enum2);

        JsonNode node3 = mapper.readTree("{\"value\": \"CustomVALUE3\"}");
        Enum<?> enum3 = mapper.readValue(node3, Enum.class);
        assertSame(CustomEnum.CUSTOMVALUE3, enum3);
    }

    @Test
    public void testContextualSerialization() throws IOException {
        EnumSerializer serializer1 = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
        assertNull(serializer1.createContextual(null, null));

        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.INT);
        EnumSerializer serializer2 = new EnumSerializer(EnumValues.allValues(MyEnum.class), true).withFormat(format);
        JsonSerializer<?> serializer3 = serializer2.createContextual(null, null);
        assertNotNull(serializer3);
        assertTrue(serializer3 instanceof EnumSerializer);
        assertEquals(JsonFormat.Shape.INT, ((EnumSerializer) serializer3)._serializeAsIndex);

        format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        EnumSerializer serializer4 = new EnumSerializer(EnumValues.allValues(MyEnum.class), null).withFormat(format);
        JsonSerializer<?> serializer5 = serializer4.createContextual(null, null);
        assertNotNull(serializer5);
        assertTrue(serializer5 instanceof EnumSerializer);
        assertNull(serializer5._serializeAsIndex);
    }

    @Test
    public void testContextualSerializationWithCustomValues() throws IOException {
        EnumSerializer serializer1 = new EnumSerializer(CustomEnumValues.INSTANCE, null);
        assertNull(serializer1.createContextual(null, null));

        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.INT);
        EnumSerializer serializer2 = new EnumSerializer(CustomEnumValues.INSTANCE, true).withFormat(format);
        JsonSerializer<?> serializer3 = serializer2.createContextual(null, null);
        assertNotNull(serializer3);
        assertTrue(serializer3 instanceof EnumSerializer);
        assertEquals(CustomEnumValues.INSTANCE, serializer3.getEnumValues());
        assertEquals(JsonFormat.Shape.INT, serializer3._serializeAsIndex);

        format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        EnumSerializer serializer4 = new EnumSerializer(CustomEnumValues.INSTANCE, null).withFormat(format);
        JsonSerializer<?> serializer5 = serializer4.createContextual(null, null);
        assertNotNull(serializer5);
        assertTrue(serializer5 instanceof EnumSerializer);
        assertEquals(CustomEnumValues.INSTANCE, serializer5.getEnumValues());
        assertNull(serializer5._serializeAsIndex);
    }

    @Test
    public void testGetSchema() {
        EnumSerializer serializer1 = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
        SchemaNode schema1 = serializer1.getSchema(null, null);
        assertEquals("string", schema1.get("type").asText());
        assertEquals(true, schema1.get("describedBy").isEmpty());

        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.INT);
        EnumSerializer serializer2 = new EnumSerializer(EnumValues.allValues(MyEnum.class), true).withFormat(format);
        SchemaNode schema2 = serializer2.getSchema(null, null);
        assertEquals("integer", schema2.get("type").asText());
        assertEquals(true, schema2.get("describedBy").isEmpty());

        format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        EnumSerializer serializer3 = new EnumSerializer(EnumValues.allValues(MyEnum.class), null).withFormat(format);
        SchemaNode schema3 = serializer3.getSchema(null, null);
        assertEquals("string", schema3.get("type").asText());
        JsonNode enumNode = schema3.get("properties").get("enum");
        assertEquals(2, enumNode.size());
        assertEquals("VALUE1", enumNode.get(0).asText());
        assertEquals("VALUE2", enumNode.get(1).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
        JsonFormatVisitorWrapper visitor = null;
        try {
            serializer.acceptJsonFormatVisitor(visitor, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        visitor = createVisitorWithIntFormat();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonFormat.Shape.INT, visitor.getShape());

        visitor = createVisitorWithStringFormat();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(JsonFormat.Shape.STRING, visitor.getShape());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithCustomValues() {
        EnumSerializer serializer = new EnumSerializer(CustomEnumValues.INSTANCE, null);
        JsonFormatVisitorWrapper visitor = null;
        try {
            serializer.acceptJsonFormatVisitor(visitor, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        visitor = createVisitorWithIntFormat();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(CustomEnumValues.INSTANCE, visitor.getEnumValues());
        assertEquals(JsonFormat.Shape.INT, visitor.getShape());

        visitor = createVisitorWithStringFormat();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals(CustomEnumValues.INSTANCE, visitor.getEnumValues());
        assertEquals(JsonFormat.Shape.STRING, visitor.getShape());
    }

    private ObjectNode testSerializationHelper(EnumSerializer serializer, Enum<?> enumValue)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.writeValueAsTree(serializer.serialize(enumValue, null, null));
        ObjectNode objectNode = (ObjectNode) node;
        return objectNode;
    }

    private JsonFormatVisitorWrapper createVisitorWithIntFormat() {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.INT);
        return format.acceptJsonStringFormatVisitor(null);
    }

    private JsonFormatVisitorWrapper createVisitorWithStringFormat() {
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        return format.acceptJsonStringFormatVisitor(null);
    }

    private static class CustomEnum {
        public static enum CustomEnum {
            CUSTOMVALUE1,
            CUSTOMVALUE2,
            CUSTOMVALUE3;

            private static final CustomEnumValues INSTANCE = new CustomEnumValues(CustomEnum.class);
        }

        @JsonFormat(shape = Shape.STRING)
        public String value() {
            return name().toLowerCase();
        }
    }

    private static class CustomEnumValues extends EnumValues {
        public static final CustomEnumValues INSTANCE = new CustomEnumValues();

        private CustomEnumValues() {
            super(CustomEnum.class);
        }

        @Override
        public SerializableString serializedValueFor(Enum<?> e) {
            return new SimpleSerializableString(e.name().toLowerCase());
        }
    }

    private static class MyEnum {
        public static enum Value1 {
            VALUE1,
            VALUE2;
        }
    }
}