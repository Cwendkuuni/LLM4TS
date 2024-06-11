package com.example; // replace with your package name

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static com.fasterxml.jackson.core.Version.VERSION_2_12;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SerializationConfig config) throws Exception {
                config.setVersion(VERSION_2_12);
            }
        });
    }

    @Test
    public void testSerializationWithValueMethodAnnotation() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                Object.class.getMethod("getValueMethod"), null);

        // When
        String result = objectMapper.writeValueAsString(new MyClass(42));

        // Then
        assertEquals("42", result);
    }

    @Test
    public void testSerializationWithCustomValueSerializer() throws IOException {
        // Given
        JsonValueSerializer<MyClass> serializer = new JsonValueSerializer<MyClass>(
                MyClass.class.getMethod("getValueMethod"),
                new MyCustomSerializer());

        // When
        String result = objectMapper.writeValueAsString(new MyClass(42));

        // Then
        assertEquals("custom_serialization", result);
    }

    @Test
    public void testSerializationWithEnumValueMethodAnnotation() throws Exception {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                MyEnum.class.getMethod("getValueMethod"), null);

        // When
        String result = objectMapper.writeValueAsString(MyEnum.VALUE1);

        // Then
        assertEquals("VALUE_ONE", result);
    }

    @Test
    public void testSerializationWithNaturalType() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                Integer.class.getMethod("intValue"), null);

        // When
        String result = objectMapper.writeValueAsString(42);

        // Then
        assertEquals("42", result);
    }

    @Test
    public void testSerializationWithStringType() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                String.class.getMethod("getValue"), null);

        // When
        String result = objectMapper.writeValueAsString("Hello, World!");

        // Then
        assertEquals("\"Hello, World!\"", result);
    }

    @Test
    public void testSerializationWithBooleanType() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                Boolean.class.getMethod("booleanValue"), null);

        // When
        String result = objectMapper.writeValueAsString(true);

        // Then
        assertEquals("true", result);
    }

    @Test
    public void testSerializationWithDoubleType() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                Double.class.getMethod("doubleValue"), null);

        // When
        String result = objectMapper.writeValueAsString(3.14);

        // Then
        assertEquals("3.14", result);
    }

    @Test
    public void testSerializationWithNullValue() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                Object.class.getMethod("getValueMethod"), null);

        // When
        String result = objectMapper.writeValueAsString(null);

        // Then
        assertEquals("null", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithException() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                Throwable.class.getMethod("getMessage"), null);

        // When & Then
        objectMapper.writeValueAsString(new IOException("Test error"));
    }

    @Test
    public void testSerializationWithCustomContextualSerializer() throws IOException {
        // Given
        JsonValueSerializer<MyClass> serializer = new JsonValueSerializer<MyClass>(
                MyClass.class.getMethod("getValueMethod"),
                new MyCustomContextualSerializer());

        // When
        String result = objectMapper.writeValueAsString(new MyClass(42));

        // Then
        assertEquals("custom_contextual_serialization", result);
    }

    @Test
    public void testSerializationWithTypeSerializer() throws IOException {
        // Given
        final String expectedType = "MyType";
        JsonValueSerializer serializer = new JsonValueSerializer<MyClass>(
                MyClass.class.getMethod("getValueMethod"),
                new MyClassSerializer(), true);

        // When
        String result = objectMapper.writeValueAsString(new MyClass(42));

        // Then
        String[] parts = result.split(":");
        assertEquals(2, parts.length);
        assertEquals(expectedType, parts[0]);
        assertEquals("42", parts[1]);
    }

    @Test
    public void testJsonFormatVisitor() throws JsonMappingException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                MyClass.class.getMethod("getValueMethod"), null);

        // When
        JsonNode schema = objectMapper.schema(serializer.getClass());

        // Then
        JsonSchema schemaNode = schema.getDefaultValue();
        assertEquals("MyClass", schemaNode.getDescription());
        assertEquals(JsonSchema.Type.OBJECT, schemaNode.getType());
        assertEquals(1, schemaNode.getProperties().size());
        JsonSchema propertySchema = schemaNode.getProperties().get(0);
        assertEquals("value", propertySchema.getName());
        assertEquals(JsonSchema.Type.STRING, propertySchema.getType());
    }

    private class MyClass {
        private int value;

        public MyClass(int value) {
            this.value = value;
        }

        public int getValueMethod() {
            return value;
        }
    }

    private class MyCustomSerializer extends JsonSerializer<MyClass> {
        @Override
        public void serialize(MyClass value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString("custom_serialization");
        }
    }

    private class MyCustomContextualSerializer extends JsonSerializer<MyClass> implements ContextualSerializer {
        @Override
        public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property)
                throws JsonMappingException {
            return new MyCustomSerializer();
        }
    }

    private class MyClassSerializer extends JsonSerializer<MyClass> {
        @Override
        public void serializeWithType(MyClass value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
                throws IOException {
            typeSer.writeTypePrefixForScalar(value, gen);
            typeSer.writeTypeSuffixForScalar(value, gen);
            gen.writeString("MyType");
        }
    }

    private enum MyEnum {
        VALUE1,
        VALUE2;

        public String getValueMethod() {
            return name();
        }
    }
}