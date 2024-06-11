package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test cases for {@link EnumSerializer} class.
 */
public class EnumSerializerTest {

    @Test
    public void testEnumSerializerConstruct() {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), null);
        assertNotNull(serializer);
        assertEquals(EnumSet.allOf(DayOfWeek.class).getClass(), serializer.handledType());
        assertEquals(EnumSet.allOf(DayOfWeek.class), serializer.getEnumValues());
        assertNull(serializer._serializeAsIndex);
    }

    @Test
    public void testEnumSerializerConstructWithSerializeAsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), true);
        assertNotNull(serializer);
        assertEquals(EnumSet.allOf(DayOfWeek.class).getClass(), serializer.handledType());
        assertEquals(EnumSet.allOf(DayOfWeek.class), serializer.getEnumValues());
        assertSame(Boolean.TRUE, serializer._serializeAsIndex);
    }

    @Test
    public void testEnumSerializerCreateContextual() throws JsonProcessingException {
        EnumSerializer serializer1 = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), null);
        SerializerProvider serializers = new StandardSerializerFactory().createSerializerInstances();
        JsonSerializer<?> serializer2 = serializer1.createContextual(serializers, null);
        assertEquals(serializer1.getClass(), serializer2.getClass());

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.INT);
        EnumSerializer serializer3 = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), null);
        JsonSerializer<?> serializer4 = serializer3.createContextual(serializers, null);
        assertNotSame(serializer1, serializer4);

        JsonFormat.Value formatWithShape = new JsonFormat.Value();
        formatWithShape.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<?> serializer5 = serializer3.createContextual(serializers, null);
        assertSame(serializer3, serializer5);

        JsonFormat.Value formatWithEnum = new JsonFormat.Value();
        formatWithEnum.setShape(JsonFormat.Shape.STRING);
        formatWithEnum.setEnumValues(new SerializableString[]{new SerializableString("MON"), new SerializableString("TUE")});
        JsonSerializer<?> serializer6 = serializer3.createContextual(serializers, null);
        assertSame(serializer3, serializer6);
    }

    @Test
    public void testSerializeAsIndex() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), null);
        ObjectWriter objectWriter = new JsonFactory().createGenerator(new StringWriter()).writer();
        ObjectNode node = objectWriter.writeValueAsObject(EnumSet.allOf(DayOfWeek.class));

        JsonGenerator gen = objectWriter.getCodec().treeToValues(node, Enum[].class, null);
        assertNull(serializer._serializeAsIndex(null));
        assertFalse(serializer._serializeAsIndex(new StandardSerializerFactory()));
        assertTrue(serializer._serializeAsIndex(new StandardSerializerFactory().createSerializerInstances()));

        JsonSerializer<?> serializerWithIndex = serializer.createContextual(new StandardSerializerFactory().createSerializerInstances(), null);
        assertTrue(serializerWithIndex._serializeAsIndex.booleanValue());

        JsonSerializer<?> serializerWithoutIndex = serializer.createContextual(new StandardSerializerFactory().createSerializerInstances(), null);
        assertNull(serializerWithoutIndex._serializeAsIndex);

        gen.writeValue(Arrays.asList(serializer, serializerWithIndex, serializerWithoutIndex).toArray(new JsonSerializer[0][]));
        ObjectReader objectReader = new JsonFactory().createParser(new StringReader(objectWriter.toString()));
        ObjectNode serializedNode = objectReader.readValueAsTree();
        ArrayNode serializedArray = (ArrayNode) serializedNode.get("serializer");
        JsonNode jsonNode1 = serializedArray.get(0);
        JsonNode jsonNode2 = serializedArray.get(1);
        JsonNode jsonNode3 = serializedArray.get(2);

        assertTrue(jsonNode1.isNull());
        assertTrue(jsonNode2.isTextual());
        assertTrue(jsonNode3.isTextual());

        JsonGenerator gen2 = objectWriter.writeValueAsArray(EnumSet.allOf(DayOfWeek.class).toArray(new DayOfWeek[0]));
        gen2.flush();
        String serializedString = objectWriter.toString();
        JsonNode jsonNode4 = objectMapper.readTree(serializedString);
        ArrayNode serializedArray2 = (ArrayNode) jsonNode4;
        JsonNode jsonNode5 = serializedArray2.get(0);
        JsonNode jsonNode6 = serializedArray2.get(1);
        JsonNode jsonNode7 = serializedArray2.get(2);

        assertTrue(jsonNode5.isNumber());
        assertTrue(jsonNode6.isTextual());
        assertTrue(jsonNode7.isTextual());
    }

    @Test
    public void testSerializeUsingToString() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), null);
        ObjectWriter objectWriter = new JsonFactory().createGenerator(new StringWriter());
        ObjectNode node = objectWriter.writeValueAsObject(EnumSet.allOf(DayOfWeek.class));

        JsonGenerator gen = objectWriter.getCodec().treeToValues(node, Enum[].class, null);
        assertFalse(serializer._serializeAsIndex(null));
        assertFalse(serializer._serializeAsIndex(new StandardSerializerFactory()));
        assertFalse(serializer._serializeAsIndex(new StandardSerializerFactory().createSerializerInstances()));

        JsonSerializer<?> serializerWithToString = serializer.createContextual(new StandardSerializerFactory().createSerializerInstances(), null);
        JsonFormat.Value formatWithToString = new JsonFormat.Value();
        formatWithToString.setShape(JsonFormat.Shape.STRING);
        formatWithToString.setSerializer(serializerWithToString);
        JsonSerializer<?> serializerWithoutToString = serializer.createContextual(new StandardSerializerFactory().createSerializerInstances(), null);

        gen.writeValue(Arrays.asList(serializer, serializerWithToString, serializerWithoutToString).toArray(new JsonSerializer[0][]));
        ObjectReader objectReader = new JsonFactory().createParser(new StringReader(objectWriter.toString()));
        ObjectNode serializedNode = objectReader.readValueAsTree();
        ArrayNode serializedArray = (ArrayNode) serializedNode.get("serializer");
        JsonNode jsonNode1 = serializedArray.get(0);
        JsonNode jsonNode2 = serializedArray.get(1);
        JsonNode jsonNode3 = serializedArray.get(2);

        assertTrue(jsonNode1.isNull());
        assertTrue(jsonNode2.isTextual());
        assertTrue(jsonNode3.isTextual());

        JsonGenerator gen2 = objectWriter.writeValueAsArray(EnumSet.allOf(DayOfWeek.class).toArray(new DayOfWeek[0]));
        gen2.setCodec(objectMapper);
        gen2.setSerializerProvider(new StandardSerializerFactory().createSerializerInstances());
        gen2.setSerializationConfig(serializers);
        gen2.writeValue(Arrays.asList(serializer, serializerWithToString, serializerWithoutToString).toArray(new JsonSerializer[0][]));
        gen2.flush();
        String serializedString = objectWriter.toString();
        JsonNode jsonNode4 = objectMapper.readTree(serializedString);
        ArrayNode serializedArray2 = (ArrayNode) jsonNode4;
        JsonNode jsonNode5 = serializedArray2.get(0);
        JsonNode jsonNode6 = serializedArray2.get(1);
        JsonNode jsonNode7 = serializedArray2.get(2);

        assertTrue(jsonNode5.isNull());
        assertTrue(jsonNode6.isTextual());
        assertTrue(jsonNode7.isTextual());
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), null);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode schemaNode = serializer.getSchema(objectMapper.getSerializationConfig(), Enum[].class);

        JsonNode schemaNumber = schemaNode.get("type");
        JsonNode schemaString = schemaNode.get("properties").get("enum").get(0);

        assertEquals("integer", schemaNumber.asText());
        assertEquals("string", schemaString.get("type").asText());
        assertEquals("[MON, TUE, WED, THU, FRI, SAT, SUN]", schemaString.get("items").asText());

        JsonNode schemaStringWithValues = schemaNode.get("properties").get("enum");
        JsonNode schemaStringWithValuesItem1 = schemaStringWithValues.get(0);
        JsonNode schemaStringWithValuesItem2 = schemaStringWithValues.get(1);

        assertEquals(" MON", schemaStringWithValuesItem1.get("value").asText());
        assertEquals(" TUE", schemaStringWithValuesItem2.get("value").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        EnumSerializer serializer = new EnumSerializer(EnumSet.allOf(DayOfWeek.class), null);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().findFormatVisitor("test", Enum[].class);
        visitor.expectStringFormat(Enum[].class);

        Set<String> expectedValues = new HashSet<>();
        expectedValues.add("MON");
        expectedValues.add("TUE");
        expectedValues.add("WED");
        expectedValues.add("THU");
        expectedValues.add("FRI");
        expectedValues.add("SAT");
        expectedValues.add("SUN");

        serializer.acceptJsonFormatVisitor(visitor, Enum[].class);

        Set<String> actualValues = visitor.getAcceptedValues();
        assertEquals(expectedValues, actualValues);
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SerializerProvider serializers = new StandardSerializerFactory().createSerializerInstances();

    private enum DayOfWeek {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }
}