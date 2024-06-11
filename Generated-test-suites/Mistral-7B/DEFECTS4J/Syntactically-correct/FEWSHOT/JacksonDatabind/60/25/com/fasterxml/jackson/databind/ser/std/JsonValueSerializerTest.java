package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.AnnotatedMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for {@link JsonValueSerializer}
 */
public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer() throws Exception {
        ObjectValueFactory valueFactory = new ObjectMapper().getValueFactory();

        // Test with annotated method
        JsonValue jsonValueAnnotatedMethod = new JsonValue("getAnnotatedValue");
        Object testClass = new TestJsonValueAnnotatedMethod();
        Object value = jsonValueAnnotatedMethod.getValue(testClass);
        JsonSerializer<Object> jsonSerializer = new JsonValueSerializer(jsonValueAnnotatedMethod, null);
        JsonSerializer<Object> ser = new JsonValueSerializer(jsonSerializer, null, null, false);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        ser.serialize(value, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        String expectedOutput = "\"testAnnotatedValue\"";
        String actualOutput = jsonGenerator.getOutputSaveStream().getBytes("UTF-8")
                .toString().replaceAll("\\s+", "");
        assertEquals(expectedOutput, actualOutput);

        // Test with annotated class
        JsonValue jsonValueAnnotatedClass = new JsonValue("getClassValue");
        Object testClassAnnotated = new TestJsonValueAnnotatedClass();
        Object valueAnnotatedClass = jsonValueAnnotatedClass.getValue(testClassAnnotated);
        JsonSerializer<Object> jsonSerializerAnnotatedClass = new JsonValueSerializer(null, null, jsonValueAnnotatedClass, false);
        JsonSerializer<Object> serAnnotatedClass = new JsonValueSerializer(jsonSerializerAnnotatedClass, null, null, false);

        JsonGenerator jsonGeneratorAnnotatedClass = new JsonFactory().createGenerator(System.out);
        serAnnotatedClass.serialize(valueAnnotatedClass, jsonGeneratorAnnotatedClass, new SerializerProvider());
        jsonGeneratorAnnotatedClass.flush();
        jsonGeneratorAnnotatedClass.close();

        String expectedOutputAnnotatedClass = "\"testAnnotatedClassValue\"";
        String actualOutputAnnotatedClass = jsonGeneratorAnnotatedClass.getOutputSaveStream().getBytes("UTF-8")
                .toString().replaceAll("\\s+", "");
        assertEquals(expectedOutputAnnotatedClass, actualOutputAnnotatedClass);

        // Test with no annotation
        Object testClassNoAnnotation = new TestJsonValueNoAnnotation();
        Object valueNoAnnotation = valueFactory.valueFromObject(testClassNoAnnotation);
        JsonSerializer<Object> jsonSerializerNoAnnotation = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeString(value.toString());
            }
        };
        JsonSerializer<Object> serNoAnnotation = new JsonValueSerializer(null, null, null, false);

        JsonGenerator jsonGeneratorNoAnnotation = new JsonFactory().createGenerator(System.out);
        serNoAnnotation.serialize(valueNoAnnotation, jsonGeneratorNoAnnotation, new SerializerProvider());
        jsonGeneratorNoAnnotation.flush();
        jsonGeneratorNoAnnotation.close();

        String expectedOutputNoAnnotation = "\"testNoAnnotationValue\"";
        String actualOutputNoAnnotation = jsonGeneratorNoAnnotation.getOutputSaveStream().getBytes("UTF-8")
                .toString().replaceAll("\\s+", "");
        assertEquals(expectedOutputNoAnnotation, actualOutputNoAnnotation);
    }

    @Test
    public void testJsonValueSerializerSchema() throws Exception {
        ObjectValueFactory valueFactory = new ObjectMapper().getValueFactory();

        JsonValue jsonValueAnnotatedMethod = new JsonValue("getAnnotatedValue");
        Object testClassAnnotated = new TestJsonValueAnnotatedMethod();
        Object valueAnnotatedMethod = jsonValueAnnotatedMethod.getValue(testClassAnnotated);
        JsonSerializer<Object> jsonSerializerAnnotatedMethod = new JsonValueSerializer(null, null, jsonValueAnnotatedMethod, false);

        JsonSerializer<Object> serAnnotatedMethod = new JsonValueSerializer(jsonSerializerAnnotatedMethod, null, null, false) {
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                visitor.expectStringFormat(typeHint);
            }
        };

        SchemaAware schemaAware = new JsonSchema();
        Object schema = schemaAware.getSchema(new SerializerProvider(), null);
        JsonNode jsonNode = serAnnotatedMethod.getSchema(new SerializerProvider(), valueAnnotatedMethod.getClass());

        assertEquals(schema.toString(), jsonNode.toString());
    }

    @Test
    public void testJsonValueSerializerContextualization() throws Exception {
        ObjectValueFactory valueFactory = new ObjectMapper().getValueFactory();

        JsonValue jsonValueAnnotatedMethod = new JsonValue("getAnnotatedValue");
        Object testClassAnnotated = new TestJsonValueAnnotatedMethod();
        Object valueAnnotatedMethod = jsonValueAnnotatedMethod.getValue(testClassAnnotated);
        JsonSerializer<Object> jsonSerializerAnnotatedMethod = new JsonValueSerializer(jsonValueAnnotatedMethod, null);

        JsonSerializer<Object> serAnnotatedMethod = new JsonValueSerializer(jsonSerializerAnnotatedMethod, null, null, false) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
                    throws JsonMappingException {
                return new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                            throws IOException {
                        gen.writeString("testValueFromCreateContextual");
                    }
                };
            }
        };

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Object> ser = new JsonValueSerializer(serAnnotatedMethod, null, null, false);

        ser.serialize(valueAnnotatedMethod, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        String expectedOutput = "testValueFromCreateContextual";
        String actualOutput = jsonGenerator.getOutputSaveStream().getBytes("UTF-8")
                .toString().replaceAll("\\s+", "");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJsonValueSerializerHandledType() throws Exception {
        ObjectValueFactory valueFactory = new ObjectMapper().getValueFactory();

        JsonValue jsonValueAnnotatedMethod = new JsonValue("getAnnotatedValue");
        Object testClassAnnotated = new TestJsonValueAnnotatedMethod();
        Object valueAnnotatedMethod = jsonValueAnnotatedMethod.getValue(testClassAnnotated);
        JsonSerializer<Object> jsonSerializerAnnotatedMethod = new JsonValueSerializer(jsonValueAnnotatedMethod, null);

        JsonSerializer<Object> serAnnotatedMethod = new JsonValueSerializer(jsonSerializerAnnotatedMethod, null, null, false) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
                    throws JsonMappingException {
                return new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                            throws IOException {
                        gen.writeString("testValueFromCreateContextual");
                    }
                };
            }
        };

        JsonSerializer<Object> ser = new JsonValueSerializer(serAnnotatedMethod, null, null, false);
        Object testClass = new TestJsonValueHandledType();
        Object valueHandledType = jsonValueAnnotatedMethod.getValue(testClass);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        ser.serialize(valueHandledType, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        String expectedOutput = "testValueFromCreateContextual";
        String actualOutput = jsonGenerator.getOutputSaveStream().getBytes("UTF-8")
                .toString().replaceAll("\\s+", "");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJsonValueSerializerNullValue() throws Exception {
        ObjectValueFactory valueFactory = new ObjectMapper().getValueFactory();

        JsonValue jsonValueAnnotatedMethod = new JsonValue("getAnnotatedValue");
        Object testClassAnnotated = new TestJsonValueAnnotatedMethod();
        JsonSerializer<Object> jsonSerializerAnnotatedMethod = new JsonValueSerializer(jsonValueAnnotatedMethod, null);

        JsonSerializer<Object> serAnnotatedMethod = new JsonValueSerializer(jsonSerializerAnnotatedMethod, null, null, false) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                if (value == null) {
                    provider.defaultSerializeNull(gen);
                    return;
                }
                gen.writeString("testValueFromSerialize");
            }
        };

        JsonSerializer<Object> ser = new JsonValueSerializer(serAnnotatedMethod, null, null, false);
        Object testClass = new TestJsonValueNullValue();
        Object valueNullValue = jsonValueAnnotatedMethod.getValue(testClass);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        ser.serialize(valueNullValue, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        String expectedOutput = "null";
        String actualOutput = jsonGenerator.getOutputSaveStream().getBytes("UTF-8")
                .toString().replaceAll("\\s+", "");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJsonValueSerializerException() throws Exception {
        ObjectValueFactory valueFactory = new ObjectMapper().getValueFactory();

        JsonValue jsonValueAnnotatedMethod = new JsonValue("getAnnotatedValue");
        Object testClassAnnotated = new TestJsonValueAnnotatedMethod();
        Object valueAnnotatedMethod = jsonValueAnnotatedMethod.getValue(testClassAnnotated);
        JsonSerializer<Object> jsonSerializerAnnotatedMethod = new JsonValueSerializer(jsonValueAnnotatedMethod, null);

        JsonSerializer<Object> serAnnotatedMethod = new JsonValueSerializer(jsonSerializerAnnotatedMethod, null, null, false) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                throw new IOException("Test IOException");
            }
        };

        JsonSerializer<Object> ser = new JsonValueSerializer(serAnnotatedMethod, null, null, false);

        try {
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
            ser.serialize(valueAnnotatedMethod, jsonGenerator, new SerializerProvider());
            fail("Expected IOException but got no exception");
        } catch (IOException e) {
            assertEquals("Test IOException", e.getMessage());
        }
    }

    @Test
    public void testJsonValueSerializerWithType() throws Exception {
        ObjectValueFactory valueFactory = new ObjectMapper().getValueFactory();

        JsonValue jsonValueAnnotatedMethod = new JsonValue("getAnnotatedValue");
        Object testClassAnnotated = new TestJsonValueAnnotatedMethod();
        Object valueAnnotatedMethod = jsonValueAnnotatedMethod.getValue(testClassAnnotated);
        JsonSerializer<Object> jsonSerializerAnnotatedMethod = new JsonValueSerializer(jsonValueAnnotatedMethod, null);

        JsonSerializer<Object> serAnnotatedMethod = new JsonValueSerializer(jsonSerializerAnnotatedMethod, null, null, false) {
            @Override
            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                    TypeSerializer typeSerializer) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        JsonSerializer<Object> ser = new JsonValueSerializer(serAnnotatedMethod, null, null, false);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readValue("[]", TestJsonValueSerializerWithType.class);
            fail("Expected IOException but got JSON Node");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test class for JSON value serializer with annotated method
     */
    static class TestJsonValueAnnotatedMethod {
        @JsonValue
        public String getAnnotatedValue() {
            return "testAnnotatedValue";
        }
    }

    /**
     * Test class for JSON value serializer with annotated class
     */
    static class TestJsonValueAnnotatedClass {
        @JsonValue
        public String getClassValue() {
            return "testAnnotatedClassValue";
        }
    }

    /**
     * Test class for JSON value serializer with no annotation
     */
    static class TestJsonValueNoAnnotation {
        public String toString() {
            return "testNoAnnotationValue";
        }
    }

    /**
     * Test class for JSON value serializer with null value
     */
    static class TestJsonValueNullValue {
        // Empty class
    }

    /**
     * Test class for JSON value serializer with type information
     */
    static class TestJsonValueSerializerWithType {
        public String getAnnotatedValue() {
            return "testValue";
        }

        @JsonSerialize(using = TestJsonSerializerWithType.class)
        public static class NestedClass {
            public String getValue() {
                return "testNestedValue";
            }
        }

        public NestedClass getNested() {
            return new NestedClass();
        }
    }

    /**
     * Test serializer for JSON value serializer with type information
     */
    static class TestJsonSerializerWithType extends StdSerializer<Object> {
        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                TypeSerializer typeSerializer) throws IOException {
            typeSerializer.writeTypePrefixForScalar(value, gen);
            gen.writeString("testValueFromSerializerWithType");
            typeSerializer.writeTypeSuffixForScalar(value, gen);
        }
    }
}