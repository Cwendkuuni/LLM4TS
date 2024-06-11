package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EnumSerializerTest {

    private ObjectMapper objectMapper;
    private EnumSerializer enumSerializer;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
    }

    @Test
    public void testSerializeEnumAsIndex() throws JsonProcessingException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
        jsonGenerator.setCodec(objectMapper);
        enumSerializer.serialize(MyEnum.VALUE1, jsonGenerator, null);
        jsonGenerator.flush();

        StringWriter stringWriter = new StringWriter();
        jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        jsonGenerator.setCodec(objectMapper);
        enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), true);
        enumSerializer.serialize(MyEnum.VALUE1, jsonGenerator, null);
        jsonGenerator.flush();

        String result1 = stringWriter.toString();
        String result2 = result1.replaceAll("\\d+", "");
        assertEquals(result2, "'VALUE1'");
        assertEquals(Integer.parseInt(result1), MyEnum.VALUE1.ordinal());
    }

    @Test
    public void testSerializeEnumAsString() throws JsonProcessingException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
        jsonGenerator.setCodec(objectMapper);
        enumSerializer.serialize(MyEnum.VALUE1, jsonGenerator, null);
        jsonGenerator.flush();

        StringWriter stringWriter = new StringWriter();
        jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        jsonGenerator.setCodec(objectMapper);
        enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), false);
        enumSerializer.serialize(MyEnum.VALUE1, jsonGenerator, null);
        jsonGenerator.flush();

        String result = stringWriter.toString();
        assertEquals("VALUE1", result);
    }

    @Test
    public void testSerializeEnumWithCustomValues() throws JsonProcessingException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new StringWriter());
        jsonGenerator.setCodec(objectMapper);

        ObjectNode schemaNode = objectMapper.readValue(getClass().getResource("/schema.json"), ObjectNode.class);
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class, schemaNode), null);
        enumSerializer.serialize(MyEnum.CUSTOM_VALUE, jsonGenerator, null);
        jsonGenerator.flush();

        StringWriter stringWriter = new StringWriter();
        jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class, schemaNode), true);
        enumSerializer.serialize(MyEnum.CUSTOM_VALUE, jsonGenerator, null);
        jsonGenerator.flush();

        String result1 = stringWriter.toString();
        String result2 = result1.replaceAll("\\d+", "");
        assertEquals(result2, "'CUSTOM_VALUE'");
        assertEquals(Integer.parseInt(result1), MyEnum.CUSTOM_VALUE.ordinal());
    }

    @Test
    public void testCreateContextual() {
        JsonSerializer<?> serializer = enumSerializer.createContextual(null, null);
        assertNotNull(serializer);
        assertTrue(serializer instanceof EnumSerializer);

        EnumSerializer newEnumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), false);
        serializer = newEnumSerializer.createContextual(null, null);
        assertNotNull(serializer);
        assertTrue(serializer instanceof EnumSerializer);
        assertFalse(serializer.equals(newEnumSerializer));

        EnumSerializer newEnumSerializer2 = new EnumSerializer(EnumValues.allValues(MyEnum.class), true);
        serializer = newEnumSerializer.createContextual(null, null);
        assertNotNull(serializer);
        assertTrue(serializer instanceof EnumSerializer);
        assertFalse(serializer.equals(newEnumSerializer));

        serializer = newEnumSerializer2.createContextual(null, null);
        assertNotNull(serializer);
        assertTrue(serializer instanceof EnumSerializer);
        assertTrue(serializer.equals(newEnumSerializer2));
    }

    @Test
    public void testGetSchema() throws JsonMappingException, JsonProcessingException {
        ObjectNode schemaNode = enumSerializer.getSchema(null, null);
        assertNotNull(schemaNode);
        assertFalse(schemaNode.isEmpty());
        assertTrue(schemaNode.get("type").asText().equals("object"));
        assertTrue(schemaNode.has("properties"));
        assertTrue(schemaNode.get("properties").isContainerNode());

        ObjectNode propertiesNode = schemaNode.get("properties");
        assertTrue(propertiesNode.get("enum").isArray());
        assertTrue(propertiesNode.get("enum").isContainerNode());

        Iterator<Object> enumIterator = propertiesNode.get("enum").elements();
        int count = 0;
        while (enumIterator.hasNext()) {
            ObjectNode enumValueNode = (ObjectNode) enumIterator.next();
            String enumValue = enumValueNode.get("value").asText();
            assertTrue(EnumSet.allOf(MyEnum.class).contains(Enum.valueOf(MyEnum.class, enumValue)));
            count++;
        }

        assertEquals(MyEnum.values().length, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSchemaWithInvalidShape() throws JsonMappingException, JsonProcessingException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.allValues(MyEnum.class), null);
        ObjectNode schemaNode = enumSerializer.getSchema(null, null);
        JsonFormat.Shape shape = JsonFormat.Shape.NEGATIVE_INFINITY;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(shape);
        enumSerializer = new EnumSerializer(enumSerializer, format);
        schemaNode = enumSerializer.getSchema(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        JsonStringFormatVisitor visitor = objectMapper.getFactory().constructStringFormatVisitor(format, null);

        ObjectNode schemaNode = enumSerializer.getSchema(null, null);
        enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class, schemaNode), null);
        enumSerializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode enumNodes = visitor.acceptArray();
        assertEquals(MyEnum.values().length, enumNodes.size());
        for (int i = 0; i < MyEnum.values().length; i++) {
            JsonNode enumNode = enumNodes.get(i);
            assertEquals(MyEnum.values()[i].toString(), enumNode.get("value").asText());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitorWithEnabledToStringSerialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        JsonStringFormatVisitor visitor = objectMapper.getFactory().constructStringFormatVisitor(format, null);

        ObjectNode schemaNode = enumSerializer.getSchema(null, null);
        enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class, schemaNode), null);
        enumSerializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode enumNodes = visitor.acceptArray();
        for (int i = 0; i < MyEnum.values().length; i++) {
            JsonNode enumNode = enumNodes.get(i);
            assertEquals(MyEnum.values()[i].toString(), enumNode.get("value").asText());
        }
    }

    @Test
    public void testAcceptJsonFormatVisitorWithDisabledToStringSerialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        JsonStringFormatVisitor visitor = objectMapper.getFactory().constructStringFormatVisitor(format, null);

        ObjectNode schemaNode = enumSerializer.getSchema(null, null);
        enumSerializer = new EnumSerializer(EnumValues.constructFromValues(MyEnum.class, schemaNode), null);
        enumSerializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode enumNodes = visitor.acceptArray();
        for (int i = 0; i < MyEnum.values().length; i++) {
            JsonNode enumNode = enumNodes.get(i);
            SerializableString serializableString = EnumValues.constructSerializableString(MyEnum.values()[i].name());
            assertEquals(serializableString.getValue(), enumNode.get("value").asText());
        }
    }

    enum MyEnum {
        VALUE1,
        VALUE2,
        CUSTOM_VALUE;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        public String customValue() {
            return "CustomValue_" + this.name();
        }
    }
}