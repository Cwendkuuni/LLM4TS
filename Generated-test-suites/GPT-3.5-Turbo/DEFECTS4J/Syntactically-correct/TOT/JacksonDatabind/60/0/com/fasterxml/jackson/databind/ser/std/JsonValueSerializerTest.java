package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.BeanSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    private static final String TEST_STRING = "testString";
    private static final int TEST_INT = 10;
    private static final boolean TEST_BOOL = true;
    private static final double TEST_DOUBLE = 10.5;

    // Test class for JsonValueSerializer
    private static class TestClass {

        private String name;

        public TestClass(String name) {
            this.name = name;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String getName() {
            return name;
        }

    }

    // Test serialize with default JsonSerializer
    @Test
    public void testSerializeWithDefaultSerializer() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(getAnnotatedMethod(TestClass.class));
        TestClass testObject = new TestClass(TEST_STRING);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();

        Assert.assertEquals("\"" + TEST_STRING + "\"", serialize(serializer, testObject, serializerProvider));
    }

    // Test serialize with custom JsonSerializer
    @Test
    public void testSerializeWithCustomSerializer() throws IOException {
        JsonSerializer<Object> customSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString().toUpperCase());
            }
        };
        JsonValueSerializer serializer = new JsonValueSerializer(getAnnotatedMethod(TestClass.class), customSerializer);
        TestClass testObject = new TestClass(TEST_STRING);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();

        Assert.assertEquals("\"" + TEST_STRING.toUpperCase() + "\"", serialize(serializer, testObject, serializerProvider));
    }

    // Test serialize with null value
    @Test
    public void testSerializeWithNullValue() throws IOException {
        JsonValueSerializer serializer = new JsonValueSerializer(getAnnotatedMethod(TestClass.class));
        TestClass testObject = new TestClass(null);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();

        Assert.assertEquals("null", serialize(serializer, testObject, serializerProvider));
    }

    // Helper method to serialize using the given serializer
    private String serialize(JsonValueSerializer serializer, Object value, SerializerProvider serializerProvider) throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(writer);

        try {
            serializer.serialize(value, generator, serializerProvider);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }

        generator.flush();

        return writer.toString();
    }

    // Helper method to retrieve the annotated method from a class
    private AnnotatedMethod getAnnotatedMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(com.fasterxml.jackson.annotation.JsonValue.class)) {
                return new AnnotatedMethod(null, method, null, null);
            }
        }
        return null;
    }

}