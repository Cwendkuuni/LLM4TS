package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonValue;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    private ObjectMyClass obj1;
    private ObjectMyClass obj2;

    @BeforeClass
    public void setUp() throws InstantiationException, IllegalAccessException {
        ObjectMyClass.ClassWithJsonValue annotatedClass = new ObjectMyClass.ClassWithJsonValue();
        this.obj1 = annotatedClass.newInstance();
        this.obj2 = annotatedClass.newInstance();
    }

    @Test
    public void testJsonValueSerializer_HappyPath() throws IOException {
        // Given
        JsonFactory jsonFactory = new JsonFactory();
        JsonSerializer<Object> jsonSerializer = jsonFactory.findValueSerializer(String.class, null);
        JsonValueSerializer serializer = new JsonValueSerializer(
                ObjectMyClass.class.getDeclaredMethod("getValue"), jsonSerializer);

        // When
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        serializer.serialize(obj1, jsonGenerator, new SerializerProvider());

        // Then
        String jsonString = jsonGenerator.getOutputContent();
        String expectedJsonString = "\"someValue1\"";
        Assert.assertEquals(jsonString, expectedJsonString);
    }

    @Test
    public void testJsonValueSerializer_NullValue() throws IOException {
        // Given
        JsonValueSerializer serializer = new JsonValueSerializer(
                ObjectMyClass.class.getDeclaredMethod("getValue"), null);

        // When
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(obj1, jsonGenerator, new SerializerProvider());

        // Then
        String jsonString = jsonGenerator.getOutputContent();
        Assert.assertNull(jsonString);
    }

    @Test
    public void testJsonValueSerializer_ExceptionThrown() throws IOException {
        // Given
        class ExceptionThrowingClass {
            @JsonValue
            public String getValue() throws IOException {
                throw new IOException("Test exception");
            }
        }
        ExceptionThrowingClass exceptionThrowingObject = new ExceptionThrowingClass();
        JsonValueSerializer serializer = new JsonValueSerializer(
                ExceptionThrowingClass.class.getDeclaredMethod("getValue"), null);

        // When
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);

        // Then
        try {
            serializer.serialize(exceptionThrowingObject, jsonGenerator, new SerializerProvider());
            Assert.fail();
        } catch (JsonMappingException e) {
            String jsonString = stringWriter.toString();
            String expectedJsonString = "{\"@type\":\"io.json.JsonMappingException\",\"path\":\"/Value\",\"message\":\"Test exception\"}";
            Assert.assertEquals(jsonString, expectedJsonString);
        }
    }

    @Test
    public void testJsonValueSerializer_Enum() throws IOException {
        // Given
        JsonFactory jsonFactory = new JsonFactory();
        JsonValueSerializer serializer = new JsonValueSerializer(
                ObjectMyClassWithEnum.ClassWithJsonValue.class
                        .getDeclaredMethod("getValue"), null);

        // When
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        serializer.serialize(obj1, jsonGenerator, new SerializerProvider());

        // Then
        String jsonString = jsonGenerator.getOutputContent();
        String expectedJsonString = "\"SOME_VALUE\"";
        Assert.assertEquals(jsonString, expectedJsonString);
    }

    @Test
    public void testJsonValueSerializer_Contextualization() throws IOException {
        // Given
        JsonFactory jsonFactory = new JsonFactory();
        JsonSerializer<Object> jsonSerializer = jsonFactory.findValueSerializer(String.class, null);
        final BeanProperty beanProperty = new BeanProperty.Std(null, "value", String.class);

        // When
        JsonValueSerializer serializer1 = new JsonValueSerializer(
                ObjectMyClass.class.getDeclaredMethod("getValue1"), jsonSerializer, null, false);
        JsonValueSerializer serializer2 = serializer1.withResolved(beanProperty, null, null, false);

        // Then
        JsonSerializer<Object> ser = serializer2.createContextual(new SerializerProvider(), null);
        JsonSerializer<Object> expectedSerializer = jsonFactory.findValueSerializer(String.class, beanProperty);
        Assert.assertEquals(ser, expectedSerializer);
    }

    @Test
    public void testJsonValueSerializer_ContextualizationWithForceTypeInfo() throws IOException {
        // Given
        JsonFactory jsonFactory = new JsonFactory();
        JsonSerializer<Object> jsonSerializer = jsonFactory.findValueSerializer(String.class, null);
        final BeanProperty beanProperty = new BeanProperty.Std(null, "value", String.class);

        // When
        JsonValueSerializer serializer1 = new JsonValueSerializer(
                ObjectMyClass.class.getDeclaredMethod("getValue1"), jsonSerializer, null, true);
        JsonValueSerializer serializer2 = serializer1.withResolved(beanProperty, null, null, true);

        // Then
        JsonSerializer<Object> ser = serializer2.createContextual(new SerializerProvider(), null);
        JsonSerializer<Object> expectedSerializer = jsonFactory.findTypedValueSerializer(String.class, beanProperty);
        Assert.assertEquals(ser, expectedSerializer);
    }

    private static class ObjectMyClass {
        static class ClassWithJsonValue {
            @JsonValue
            public Object getValue() {
                return "someValue1";
            }
        }

        static class ClassWithJsonValueAndProperty {
            @JsonValue
            public Object getValue1() {
                return "someValue2";
            }

            @JsonProperty
            public Object getValue2() {
                return "someValue3";
            }
        }

        public static class ClassWithJsonValueAndEnum {
            @JsonValue
            public Enum getEnumValue() {
                return SOME_VALUE;
            }

            public enum Enum {
                SOME_VALUE
            }
        }
    }
}