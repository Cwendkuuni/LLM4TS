package com.example;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

@Test
public class JsonValueSerializerTest {

    private static final Object TEST_OBJECT = new TestClass("TestValue");

    @BeforeMethod
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
    }

    @Test
    public void testSerializingWithJsonValueAnnotation() throws IOException, JsonProcessingException {
        JsonSerializer<Object> serializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                TestClass.class.getMethod("getValue"), serializer, false);

        ObjectWriter writer = mapper.writerWithRootValue(TEST_OBJECT);
        String json = writer.writeValueAsString(jsonValueSerializer);

        Assert.assertEquals(json, "\"TestValue\"");
    }

    @Test
    public void testSerializingWithJsonValueAnnotationAndCustomSerializer() throws IOException, JsonProcessingException {
        JsonSerializer<Object> stringSerializer = mapper.findValueSerializer(String.class, null);
        JsonSerializer<Object> intSerializer = mapper.findValueSerializer(int.class, null);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                TestClass.class.getMethod("getValue"), intSerializer, false);

        ObjectWriter writer = mapper.writerWithRootValue(TEST_Object);
        String json = writer.writeValueAsString(jsonValueSerializer);

        Assert.assertEquals(json, "42");
    }

    @Test
    public void testSerializingWithJsonValueAnnotationAndCustomSerializerWithTypeInformation() throws IOException, JsonProcessingException {
        JsonSerializer<Object> stringSerializer = mapper.findValueSerializer(String.class, null);
        JsonSerializer<Object> intSerializer = mapper.findValueSerializer(int.class, null);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                TestClass.class.getMethod("getValue"), intSerializer, true);

        ObjectWriter writer = mapper.writerWithRootValue(TEST_Object);
        String json = writer.writeValueAsString(jsonValueSerializer);

        Assert.assertEquals(json, "42");
        Assert.assertEquals(jsonValueSerializer.handledType().getRawClass(), int.class);
    }

    @Test
    public void testSerializingWithJsonValueAnnotationAndEnum() throws IOException, JsonMappingException, IllegalAccessException, InvocationTargetException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithRootValue(TEST_Object);

        JsonValueSerializer serializer = mapper.findValueSerializer(TestEnum.class, null);

        String json = writer.writeValueAsString(serializer);

        JsonSchema schema = mapper.schemaFor(serializer.getClass());
        JsonNode schemaNode = schema.getSchemaNode();

        Assert.assertEquals(json, "\"RED\"");
        Assert.assertEquals(schemaNode.get("enumValues").asText(), "[\"RED\", \"GREEN\", \"BLUE\"]");
    }

    @Test
    public void testContextualSerializationWithJsonValueAnnotation() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithRootValue(TEST_Object);

        JsonSerializer<Object> intSerializer = mapper.findValueSerializer(int.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                TestClass.class.getMethod("getValue"), intSerializer, false);

        String json = writer.writeValueAsString(jsonValueSerializer);

        Assert.assertEquals(json, "42");
    }

    @Test
    public void testContextualSerializationWithJsonValueAnnotationWithTypeInformation() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithRootValue(TEST_Object);

        JsonSerializer<Object> intSerializer = mapper.findValueSerializer(int.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                TestClass.class.getMethod("getValue"), intSerializer, true);

        String json = writer.writeValueAsString(jsonValueSerializer);

        Assert.assertEquals(json, "42");
        Assert.assertEquals(jsonValueSerializer.handledType().getRawClass(), int.class);
    }

    @Test
    public void testDeserializing() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": 42}";
        TestClass testClass = mapper.readValue(json, TestClass.class);

        Assert.assertEquals(testClass.getValue(), 42);
    }

    @Test
    public void testDeserializingWithNullValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": null}";
        TestClass testClass = mapper.readValue(json, TestClass.class);

        Assert.assertNull(testClass.getValue());
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializingWithInvalidValue() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": [1, 2, 3]}";
        mapper.readValue(json, TestClass.class);
    }

    @Test
    public void testDeserializingWithJsonValueAnnotationAndCustomDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": 42}";
        TestClass testClass = mapper.readValue(json, TestClass.class);

        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) testClass;
        JsonSerializer<Object> intSerializer = jsonValueSerializer.getValueSerializer();

        Assert.assertEquals(intSerializer.getClass(), IntSerializer.class);
    }

    @Test
    public void testDeserializingWithJsonValueAnnotationAndCustomDeserializerWithTypeInformation() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"value\": 42}";
        TestClass testClass = mapper.readValue(json, TestClass.class);

        JsonValueSerializer jsonValueSerializer = (JsonValueSerializer) testClass;
        JsonSerializer<Object> intSerializer = jsonValueSerializer.getValueSerializer();

        Assert.assertEquals(intSerializer.getClass(), IntSerializer.class);
        Assert.assertEquals(jsonValueSerializer.handledType().getRawClass(), int.class);
    }

    @Test
    public void testSchemaGeneration() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(
                TestClass.class.getMethod("getValue"), null, false);

        JsonSchema schema = mapper.schemaFor(serializer.getClass());
        JsonNode schemaNode = schema.getSchemaNode();

        Assert.assertEquals(schemaNode.get("title").asText(), "JsonValueSerializer");
        Assert.assertEquals(schemaNode.get("type").asText(), "object");
        Assert.assertEquals(schemaNode.get("properties")
                .get("value").get("type").asText(), "number");
    }

    @Test
    public void testSchemaGenerationWithTypeInformation() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = new JsonValueSerializer(
                TestClass.class.getMethod("getValue"), null, true);

        JsonSchema schema = mapper.schemaFor(serializer.getClass());
        JsonNode schemaNode = schema.getSchemaNode();

        Assert.assertEquals(schemaNode.get("title").asText(), "JsonValueSerializer");
        Assert.assertEquals(schemaNode.get("type").asText(), "object");
        Assert.assertEquals(schemaNode.get("properties")
                .get("value").get("type").asText(), "integer");
    }

    private static class TestClass {
        private int value;

        TestClass(String name) {
            this.value = Integer.parseInt(name);
        }

        int getValue() {
            return value;
        }

        @JsonValue
        String valueMethod() {
            return String.valueOf(value);
        }

        static class IntSerializer extends JsonSerializer<Integer> {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value);
            }
        }
    }

    private static enum TestEnum {
        RED,
        GREEN,
        BLUE
    }
}