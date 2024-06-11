package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import org.junit.*;

import static org.junit.Assert.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class JsonValueSerializerTest {

    @JsonValue
    static class TestClass {
        @JsonSerialize
        public String getValue() {
            return "value";
        }
    }

    @JsonValue
    static class EnumClass {
        @JsonValue
        public String getValue() {
            return name();
        }
    }

    @JsonSerialize(using = JsonValueSerializer.class)
    static class TestClassWithValueSerializer {
    }

    @JsonProperty("value")
    static class NestedClass {
        private int value;

        @JsonValue
        public int getValue() {
            return value;
        }

        @JsonCreator
        public NestedClass(@JsonProperty("value") int value) {
            this.value = value;
        }
    }

    @Test
    public void testSerializer_ValueMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(TestClass.class, null);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", new TestClass());

        String result = mapper.writer().writeValueAsString(node);
        assertEquals("{\"value\":\"value\"}", result);
    }

    @Test
    public void testSerializer_ValueMethod_Enum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer<EnumClass> enumSerializer = mapper.findValueSerializer(EnumClass.class, null);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", EnumClass.values()[0]);

        String result = mapper.writer().writeValueAsString(node);
        assertEquals("{\"value\":\"FOO\"}", result);
    }

    @Test
    public void testSerializer_ValueMethod_Nested() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(NestedClass.class, null);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", new NestedClass(5));

        String result = mapper.writer().writeValueAsString(node);
        assertEquals("{\"value\":5}", result);
    }

    @Test
    public void testSerializer_ValueMethod_Nested_MappingError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer<NestedClass> serializer = mapper.findValueSerializer(NestedClass.class, null);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", new Object());

        assertThrows(MappingException.class, () -> mapper.writer().writeValueAsString(node));
    }

    @Test
    public void testSerializer_ValueMethod_Nested_ValueSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer<NestedClass> serializer = mapper.findValueSerializer(NestedClass.class, null);
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(int.class, null);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", new NestedClass(5));

        String result = mapper.writer()
                .withValueSerializer(int.class, intSerializer)
                .writeValueAsString(node);

        assertEquals("{\"value\":5}", result);
    }

    @Test
    public void testSerializer_ValueMethod_Nested_ContextualSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("value", new TestClassWithValueSerializer());

        JsonSerializer<Object> ser = mapper.findValueSerializer(TestClassWithValueSerializer.class, null);
        ser = ser.withResolved(new BeanProperty.Std("value", TestClassWithValueSerializer.class), null, null, false);

        String result = mapper.writer().writeValueAsString(node);
        assertEquals("{\"value\":\"value\"}", result);
    }

    @Test
    public void testSerializer_ValueMethod_Nested_ContextualSchemaAware() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("value", new TestClassWithValueSerializer());

        JsonSerializer<Object> ser = mapper.findValueSerializer(TestClassWithValueSerializer.class, null);
        ser = ser.withResolved(new BeanProperty.Std("value", TestClassWithValueSerializer.class), null, null, true);

        SchemaAware schemaAware = (SchemaAware) ser;
        JsonSchema schema = schemaAware.getSchema(mapper.getSerializerProvider(), null);
        assertEquals(1, schema.getSchemaList().size());
        JsonNode schemaNode = schema.getSchemaList().get(0);

        assertEquals("string", schemaNode.get("type").asText());
        assertEquals("value", schemaNode.get("properties").get("value").get("title").asText());
        assertEquals("value", schemaNode.get("properties").get("value").get("description").asText());
    }

    @Test
    public void testSerializer_ValueMethod_Nested_Enum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("value", EnumClass.values()[0]);

        JsonSerializer<Object> ser = mapper.findValueSerializer(EnumClass.class, null);
        ser = ser.withResolved(new BeanProperty.Std("value", EnumClass.class), null, null, true);

        SchemaAware schemaAware = (SchemaAware) ser;
        JsonSchema schema = schemaAware.getSchema(mapper.getSerializerProvider(), null);
        assertEquals(1, schema.getSchemaList().size());
        JsonNode schemaNode = schema.getSchemaList().get(0);

        assertEquals("string", schemaNode.get("type").asText());
        assertEquals("enum", schemaNode.get("type").get("items").asText());
        assertEquals("FOO", schemaNode.get("properties").get("value").get("description").asText());
    }
}