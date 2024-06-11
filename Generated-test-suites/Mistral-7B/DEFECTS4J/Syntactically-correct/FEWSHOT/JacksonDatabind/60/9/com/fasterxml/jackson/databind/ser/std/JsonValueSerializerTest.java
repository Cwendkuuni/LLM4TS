package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Type;

public class JsonValueSerializerTest {

    @Test
    public void testSerialization() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
        ObjectNode node = mapper.createObjectNode();

        String value = "test";
        serializer.serializeWithType(value, new JsonGenerator(), mapper.getSerializerProvider(), mapper.getTypeSerializer());
        node.set("value", serializer.getSchema(mapper.getSerializerProvider(), mapper.constructType(String.class)));
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testSerializationWithContext() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);

        TestClass testClass = new TestClass();
        testClass.setValue("test");

        serializer = mapper.findValueSerializer(TestClass.class, "setValue", serializer, true);
        ObjectNode node = mapper.createObjectNode();

        serializer.serializeWithType(testClass, new JsonGenerator(), mapper.getSerializerProvider(), mapper.getTypeSerializer());
        node.set("value", serializer.getSchema(mapper.getSerializerProvider(), mapper.constructType(TestClass.class)));
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testSerializationWithDefaultSerializer() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String[].class, null);

        String[] values = new String[]{"test1", "test2"};
        serializer.serializeWithType(values, new JsonGenerator(), mapper.getSerializerProvider(), mapper.getTypeSerializer());
        ObjectNode node = mapper.createObjectNode();

        node.set("items", serializer.getSchema(mapper.getSerializerProvider(), mapper.constructType(String[].class)));
        JsonSchema schema = serializer.getSchema(mapper.getSerializerProvider(), mapper.constructType(String[].class));
        ObjectNode itemsSchema = schema.getSchemaProvider().findSchemaForType(mapper.getTypeFactory().constructType(String.class));
        node.set("items/type", itemsSchema.get("type").asText());

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testSerializationWithTypeId() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String[].class, null);

        String[] values = new String[]{"test1", "test2"};
        serializer = mapper.findValueSerializer(String[].class, null, serializer, true);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", serializer.toString());
        node.put("items", mapper.getTypeFactory().constructValueType(String[].class, Long.class));

        serializer.serializeWithType(values, new JsonGenerator(), mapper.getSerializerProvider(), mapper.getTypeSerializer());
        serializer.serializeWithType(values, new JsonGenerator(), mapper.getSerializerProvider(), node);

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testSerializationWithEnumType() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(MyEnum.class, null);

        MyEnum value = MyEnum.ENUM_B;
        serializer.serializeWithType(value, new JsonGenerator(), mapper.getSerializerProvider(), mapper.getTypeSerializer());
        ObjectNode node = mapper.createObjectNode();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), mapper.constructType(MyEnum.class));
        node.set("value", schema.get("items")
                .get(value.name().ordinal())
                .get("value"));

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testSerializationWithNonEnumClass() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(MyClass.class, null);

        MyClass obj = new MyClass();
        serializer.serializeWithType(obj, new JsonGenerator(), mapper.getSerializerProvider(), mapper.getTypeSerializer());
        ObjectNode node = mapper.createObjectNode();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), mapper.constructType(MyClass.class));
        node.set("value", schema.get("properties")
                .findValues("field1")
                .get(0)
                .get("type")
                .asText());

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testJsonFormatVisitor() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(MyClass.class, null);

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider().getSchemaVisitor(mapper.constructType(MyClass.class));
        serializer.acceptJsonFormatVisitor(visitor, mapper.constructType(MyClass.class));

        JsonSchema schema = visitor.getSchema();
        ObjectNode node = mapper.createObjectNode();
        node.set("$schema", schema.getSchemaVersion());
        node.set("title", schema.getTitle());
        node.set("description", schema.getDescription());
        node.set("type", schema.getType());
        node.set("properties", schema.getProperties());

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    @Test
    public void testJsonFormatVisitorEnum() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(MyEnum.class, null);

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider().getSchemaVisitor(mapper.constructType(MyEnum.class));
        serializer.acceptJsonFormatVisitor(visitor, mapper.constructType(MyEnum.class));

        JsonSchema schema = visitor.getSchema();
        ObjectNode node = mapper.createObjectNode();
        node.set("$schema", schema.getSchemaVersion());
        node.set("title", schema.getTitle());
        node.set("description", schema.getDescription());
        node.set("type", "string");
        node.set("enum", schema.getEnumValues());

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }

    static class TestClass {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    enum MyEnum {
        ENUM_A, ENUM_B, ENUM_C
    }

    static class MyClass {
        private String field1;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }
    }
}