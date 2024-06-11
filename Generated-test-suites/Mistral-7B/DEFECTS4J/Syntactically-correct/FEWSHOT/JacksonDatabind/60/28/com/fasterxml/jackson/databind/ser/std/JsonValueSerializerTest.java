package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        assertNotNull(serializer);

        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getEnumValue"), JsonValueSerializerTest.class);
        AnnotatedClass<?> annotatedClass = new AnnotatedClass<>(JsonValueSerializerTest.class, JsonValueSerializerTest.class.getClassLoader());
        BeanProperty property = new BeanProperty("property", annotatedClass, method);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};

        JsonValueSerializer contextualSerializer = serializer.withResolved(property, valueSerializer, false);
        assertEquals(contextualSerializer, serializer.createContextual(new JsonSerializerProvider(), property));
    }

    @Test
    public void testJsonValueSerializer_WithExplicitSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getEnumValue"), JsonValueSerializerTest.class);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};

        JsonValueSerializer contextualSerializer = serializer.withResolved(null, valueSerializer, false);
        assertEquals(valueSerializer, contextualSerializer._valueSerializer);
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsNull() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getNullValue"), JsonValueSerializerTest.class);

        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        serializer.serialize(bean, gen, provider);
        assertEquals("null", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsBoolean() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("isBooleanValue"), JsonValueSerializerTest.class);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(Boolean.class) {};

        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        serializer.serialize(bean, gen, provider);
        assertEquals("true", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsEnum() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getEnumValue"), JsonValueSerializerTest.class);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};

        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        serializer.serialize(bean, gen, provider);
        assertEquals("RED", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsString() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getStringValue"), JsonValueSerializerTest.class);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};

        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        serializer.serialize(bean, gen, provider);
        assertEquals("Hello, World!", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodThrowsException() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("throwExceptionValue"), JsonValueSerializerTest.class);

        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        try {
            serializer.serialize(bean, gen, provider);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Error in creating JsonSerializer instance for method com.fasterxml.jackson.databind.ser.std.JsonValueSerializerTest.throwExceptionValue(): java.lang.ArrayIndexOutOfBoundsException: 2", e.getMessage());
        }
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsNullAnnotated() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);
        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getNullAnnotatedValue"), JsonValueSerializerTest.class);

        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        serializer.serialize(bean, gen, provider);
        assertEquals("null", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsNullAnnotated_WithoutExplicitSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getNullAnnotatedValue"), JsonValueSerializerTest.class);
        BeanProperty property = new BeanProperty("property", new AnnotatedClass<>(JsonValueSerializerTest.class, JsonValueSerializerTest.class.getClassLoader()), method);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};

        JsonValueSerializer contextualSerializer = serializer.withResolved(property, valueSerializer, false);
        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        contextualSerializer.serialize(bean, gen, provider);
        assertEquals("null", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsEnumAnnotated() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getEnumAnnotatedValue"), JsonValueSerializerTest.class);
        BeanProperty property = new BeanProperty("property", new AnnotatedClass<>(JsonValueSerializerTest.class, JsonValueSerializerTest.class.getClassLoader()), method);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(Color.class) {};

        JsonValueSerializer contextualSerializer = serializer.withResolved(property, valueSerializer, false);
        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        contextualSerializer.serialize(bean, gen, provider);
        assertEquals("BLUE", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsEnumAnnotated_WithoutExplicitSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getEnumAnnotatedValue"), JsonValueSerializerTest.class);
        BeanProperty property = new BeanProperty("property", new AnnotatedClass<>(JsonValueSerializerTest.class, JsonValueSerializerTest.class.getClassLoader()), method);

        JsonValueSerializer contextualSerializer = serializer.createContextual(new JsonSerializerProvider(), property);
        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        contextualSerializer.serialize(bean, gen, provider);
        assertEquals("BLUE", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsStringAnnotated() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getStringAnnotatedValue"), JsonValueSerializerTest.class);
        BeanProperty property = new BeanProperty("property", new AnnotatedClass<>(JsonValueSerializerTest.class, JsonValueSerializerTest.class.getClassLoader()), method);
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(String.class) {};

        JsonValueSerializer contextualSerializer = serializer.withResolved(property, valueSerializer, false);
        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        contextualSerializer.serialize(bean, gen, provider);
        assertEquals("Hello, World!", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodReturnsStringAnnotated_WithoutExplicitSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getStringAnnotatedValue"), JsonValueSerializerTest.class);
        BeanProperty property = new BeanProperty("property", new AnnotatedClass<>(JsonValueSerializerTest.class, JsonValueSerializerTest.class.getClassLoader()), method);

        JsonValueSerializer contextualSerializer = serializer.createContextual(new JsonSerializerProvider(), property);
        Object bean = new JsonValueSerializerTest();
        JsonGenerator gen = new TestJsonGenerator();
        SerializerProvider provider = new TestSerializerProvider();

        contextualSerializer.serialize(bean, gen, provider);
        assertEquals("Hello, World!", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonValueSerializer_WithValueMethodThrowsExceptionAnnotated() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        AnnotatedMethod method = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("throwExceptionAnnotatedValue"), JsonValueSerializerTest.class);
        BeanProperty property = new BeanProperty("property", new AnnotatedClass<>(JsonValueSerializerTest.class, JsonValueSerializerTest.class.getClassLoader()), method);

        try {
            JsonValueSerializer contextualSerializer = serializer.createContextual(new JsonSerializerProvider(), property);
            Object bean = new JsonValueSerializerTest();
            JsonGenerator gen = new TestJsonGenerator();
            SerializerProvider provider = new TestSerializerProvider();

            contextualSerializer.serialize(bean, gen, provider);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Error in creating JsonSerializer instance for method com.fasterxml.jackson.databind.ser.std.JsonValueSerializerTest.throwExceptionAnnotatedValue(): java.lang.ArrayIndexOutOfBoundsException: 2", e.getMessage());
        }
    }

    private static class TestJsonGenerator extends JsonGenerator {
        private StringBuilder outputBuffer = new StringBuilder();

        @Override
        public void writeString(String value) throws IOException {
            outputBuffer.append(value);
        }

        @Override
        public void close() throws IOException {
            super.close();
            assertEquals(outputBuffer.toString(), gen.getOutputBuffer().toString());
            outputBuffer.setLength(0);
        }
    }

    private static class TestSerializerProvider extends SerializerProvider {
        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> valueClass, BeanProperty property) {
            if (valueClass == String.class) {
                return new StdSerializer<Object>(String.class) {};
            }
            return new StdSerializer<Object>(valueClass) {};
        }

        @Override
        public JsonSerializer<?> findTypedValueSerializer(Class<?> valueClass, BeanProperty property, TypeSerializer typeSerializer) {
            if (valueClass == String.class) {
                return new StdSerializer<Object>(String.class) {};
            }
            return new StdSerializer<Object>(valueClass) {};
        }

        @Override
        public JsonSerializer<?> findSerializerForProperty(BeanProperty property) {
            if (property.getName().equals("property")) {
                return new StdSerializer<Object>(String.class) {};
            }
            return null;
        }

        @Override
        public ObjectMapper getFactory() {
            return null;
        }
    }

    private Object getEnumValue() {
        return Color.RED;
    }

    private boolean isBooleanValue() {
        return true;
    }

    private Color getEnumAnnotatedValue() {
        return Color.BLUE;
    }

    @JsonSerialize(using = StringSerializer.class)
    private String getStringValue() {
        return "Hello, World!";
    }

    @JsonValue
    private Color getEnumAnnotatedValue2() {
        return Color.GREEN;
    }

    @JsonValue
    private String getStringAnnotatedValue() {
        return "Hello, World!";
    }

    private void throwExceptionValue() throws ArrayIndexOutOfBoundsException {
        throw new ArrayIndexOutOfBoundsException("Index: 2, Length: 2");
    }

    @JsonValue
    private String getNullAnnotatedValue() {
        return null;
    }

    private String getNullValue() {
        return null;
    }

    @JsonValue
    private Color getEnumAnnotatedValue3() {
        return Color.YELLOW;
    }

    @JsonValue
    private String getStringAnnotatedValue2() {
        return "Hello";
    }

    @JsonValue
    private void throwExceptionAnnotatedValue() {
        throw new RuntimeException("Test Exception");
    }

    private void doNothing() {
    }
}

enum Color {
    RED, GREEN, BLUE, YELLOW
}

class StringSerializer extends StdSerializer<String> {
}