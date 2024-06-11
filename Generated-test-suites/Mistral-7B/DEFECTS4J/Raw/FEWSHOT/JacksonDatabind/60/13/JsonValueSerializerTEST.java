package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    @JsonSerialize(using = JsonValueSerializer.class)
    static class TestBean {
        private int value;

        public int getValue() {
            return value;
        }
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(Integer.class, null);
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, valueSerializer, false);
        ObjectNode node = mapper.createObjectNode();
        TestBean bean = new TestBean();
        bean.setValue(42);
        serializer.serialize(bean, node, new JsonSerializer.DefaultSerializerProvider(), null);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        assertEquals("42", result);
    }

    @Test
    public void testSerializationWithTypeInformation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(Integer.class, null);
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, valueSerializer, true);
        ObjectNode node = mapper.createObjectNode();
        TestBean bean = new TestBean();
        bean.setValue(42);
        serializer.serializeWithType(bean, null, new JsonSerializer.DefaultSerializerProvider(), mapper.getTypeSerializer());
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        assertEquals("{\"@type\":\"java.lang.Integer\"}", result);
        assertEquals(42, mapper.readValue(result, Integer.class));
    }

    @Test
    public void testSerializationWithCustomValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> customValueSerializer = mapper.findValueSerializer(Integer.class, null);
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, customValueSerializer, false);
        ObjectNode node = mapper.createObjectNode();
        TestBean bean = new TestBean();
        bean.setValue(42);
        serializer.serialize(bean, node, new JsonSerializer.DefaultSerializerProvider(), null);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        assertEquals("42", result);
        JsonSerializer<?> deserializedSerializer = mapper.findValueSerializer(Integer.class, null);
        JsonNode deserializedNode = mapper.readTree(result);
        JsonNode deserializedValue = deserializedSerializer.createContextual(mapper.getSerializationConfig(), null).deserialize(deserializedNode, JsonNode.class);
        assertEquals(42, deserializedValue.asInt());
    }

    @Test
    public void testSerializationWithContextualizer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> customValueSerializer = mapper.findValueSerializer(Integer.class, null);
        JsonValueSerializer serializer1 = new JsonValueSerializer(null, null, customValueSerializer, false);
        JsonValueSerializer serializer2 = serializer1.withResolved(null, null, null, false);
        ObjectNode node1 = mapper.createObjectNode();
        TestBean bean = new TestBean();
        bean.setValue(42);
        serializer1.serialize(bean, node1, new JsonSerializer.DefaultSerializerProvider(), null);
        String result1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node1);
        assertEquals("42", result1);
        JsonSerializer<?> deserializedSerializer1 = mapper.findValueSerializer(Integer.class, null);
        JsonNode deserializedNode1 = mapper.readTree(result1);
        JsonNode deserializedValue1 = deserializedSerializer1.createContextual(mapper.getSerializationConfig(), null).deserialize(deserializedNode1, JsonNode.class);
        assertEquals(42, deserializedValue1.asInt());

        ObjectNode node2 = mapper.createObjectNode();
        serializer2.serialize(bean, node2, new JsonSerializer.DefaultSerializerProvider(), null);
        String result2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node2);
        assertEquals(result1, result2);
        JsonNode deserializedNode2 = mapper.readTree(result2);
        JsonNode deserializedValue2 = deserializedSerializer1.createContextual(mapper.getSerializationConfig(), null).deserialize(deserializedNode2, JsonNode.class);
        assertEquals(42, deserializedValue2.asInt());
    }

    @Test
    public void testSerializationWithEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        @JsonSerialize(using = JsonValueSerializer.class)
        enum TestEnum {
            VALUE_1, VALUE_2
        }
        JsonValueSerializer serializer = mapper.findValueSerializer(TestEnum.class, null);
        ObjectNode node =
                mapper.createObjectNode();
        TestEnum enumValue = TestEnum.VALUE_1;
        serializer.serialize(enumValue, node, new JsonSerializer.DefaultSerializerProvider(), null);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        JsonSchema schema = serializer.getSchema(new JsonSerializer.DefaultSerializerProvider(), null);
        assertEquals("VALUE_1", result);
        assertEquals(JsonSchema.Type.STRING, schema.getSchemaType());
    }

    @Test
    public void testSerializationWithEnumWithCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        @JsonSerialize(using = JsonValueSerializer.class)
        enum TestEnum {
            VALUE_1, VALUE_2
        }
        JsonSerializer<TestEnum> customSerializer = mapper.findValueSerializer(TestEnum.class, null);
        JsonValueSerializer serializer = mapper.findValueSerializer(TestEnum.class, null);
        ObjectNode node = mapper.createObjectNode();
        TestEnum enumValue = TestEnum.VALUE_1;
        serializer.acceptJsonFormatVisitor(mapper.getSerializationConfig().getFactory().createJsonFormatVisitorWrapper(null, null), null);
        serializer.serialize(enumValue, node, new JsonSerializer.DefaultSerializerProvider(), null);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        JsonSchema schema = serializer.getSchema(new JsonSerializer.DefaultSerializerProvider(), null);
        assertEquals("VALUE_1", result);
        assertEquals(JsonSchema.Type.STRING, schema.getSchemaType());
        JsonNode node2 = mapper.readTree(result);
        TestEnum deserializedValue = customSerializer.createContextual(mapper.getSerializationConfig(), null).deserialize(node2, TestEnum.class);
        assertEquals(TestEnum.VALUE_1, deserializedValue);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        @JsonSerialize(using = JsonValueSerializer.class)
        enum TestEnum {
            VALUE_1, VALUE_2
        }
        JsonValueSerializer serializer = mapper.findValueSerializer(TestEnum.class, null);
        JsonFormatVisitorWrapper wrapper = mapper.getSerializationConfig().getFactory().createJsonFormatVisitorWrapper(null, null);
        wrapper.expectStringFormat(JsonType.construct(TestEnum.class));
        serializer._acceptJsonFormatVisitorForEnum(wrapper, JsonType.construct(TestEnum.class.getName()), TestEnum.class);
        JsonSchema schema = serializer.getSchema(new JsonSerializer.DefaultSerializerProvider(), null);
        JsonNode node = mapper.createObjectNode();
        TestEnum enumValue = TestEnum.VALUE_1;
        serializer.serialize(enumValue, node, new JsonSerializer.DefaultSerializerProvider(), null);
        JsonNode expectedNode = mapper.createObjectNode();
        expectedNode.put("type", "string");
        expectedNode.put("enumValues", mapper.createValueFactory().valueNodeArray(new String[]{"VALUE_1", "VALUE_2"}));
        assertEquals(expectedNode, schema.getSchemaNode());
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Integer.TYPE, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.TYPE, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Double.TYPE, null));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Integer.class, null));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.class, null));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Double.class, null));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Integer.TYPE, new JsonSerializer<>(new Object() {})));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.TYPE, new JsonSerializer<>(new Object() {})));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(Double.TYPE, new JsonSerializer<>(new Object() {})));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, new JsonSerializer<>(new Object() {})));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Integer.class, new JsonSerializer<>(new Object() {})));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.class, new JsonSerializer<>(new Object() {})));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Double.class, new JsonSerializer<>(new Object() {})));
    }
}