package com.example.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

public class JsonValueSerializerTest {

    private ObjectTestObject testObject;

    @BeforeMethod
    public void setUp() {
        testObject = new ObjectTestObject();
    }

    @Test
    public void testSerializationWithValueMethod() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(testObject.getClass().getDeclaredMethod("getStringValue"), String.class), serializer);

        String expected = "test";
        String actual = mapper.writeValueAsString(testObject);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSerializationWithBeanProperty() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new StandardAnnotationIntrospector());

        ObjectMapper.SerializedForm serializedForm = mapper.findSerializedFormForProperty(ObjectTestObject.class, "stringProperty");
        Method method = serializedForm.getMember().getDeclaringClass().getDeclaredMethod("getStringProperty");

        JsonSerializer<Object> serializer = mapper.findValueSerializer(String.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(method, String.class), serializer);

        String expected = "test";
        String actual = mapper.writeValueAsString(testObject);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testContextualization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectTestObject testObject = new ObjectTestObject();

        // Test serialization with default type (String)
        String defaultSerialization = mapper.writeValueAsString(testObject);
        Assert.assertEquals(defaultSerialization, "test");

        // Test serialization with custom type (Integer)
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.findValueSerializerProvider().setNullValueSerializer(new StandardSerializer(Integer.class));
        JsonSerializer<Integer> intSerializer = customMapper.findValueSerializer(Integer.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(testObject.getClass().getDeclaredMethod("getIntValue"), Integer.class), intSerializer);

        JsonSerializer<?> serializedSerializer = mapper.findContextualValueSerializer(JsonValueSerializer.class, testObject, ObjectTestObject.class);
        Assert.assertEquals(serializedSerializer, jsonValueSerializer);

        String customSerialization = customMapper.writeValueAsString(testObject);
        Assert.assertEquals(customSerialization, "1");
    }

    @Test
    public void testSerializationWithType() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        ObjectTestObject testObject = new ObjectTestObject();
        String expected = "test:String";
        String actual = mapper.writeValueAsString(testObject);
        Assert.assertEquals(actual, expected);

        Object testObjectWithType = mapper.readValue(actual, ObjectTestObject.class);
        String actualDeserialized = mapper.writeValueAsString(testObjectWithType);
        Assert.assertEquals(expected, actualDeserialized);

        JsonSerializer<Object> serializer = mapper.findValueSerializer(ObjectTestObject.class, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(testObject.getClass().getDeclaredMethod("getStringValue"), String.class), serializer);

        actual = mapper.writeValueAsString(testObjectWithType, typeSerializer);
        Assert.assertEquals(expected, actual);

        Object deserialized = mapper.readValue(actual, ObjectTestObject.class, typeSerializer);
        Assert.assertEquals(testObjectWithType, deserialized);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectTestObject testObject = new ObjectTestObject();

        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider().findSchemaVisitor(ObjectTestObject.class);
        visitor.expectAnyFormat(JsonType.STRING);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(testObject.getClass().getDeclaredMethod("getStringValue"), String.class), null);
        jsonValueSerializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode expected = mapper.schemaFactory().getSchemaProvider().findSchemaDefinition(ObjectTestObject.class);
        JsonNode actual = visitor.getSchema();
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(int[].class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(List.class, null));
    }

    static class ObjectTestObject {
        private String stringProperty;

        public String getStringValue() {
            return stringProperty;
        }

        public void setStringValue(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public int getIntValue() {
            return 1;
        }

        public String getStringProperty() {
            return stringProperty;
        }
    }
}

class StandardAnnotationIntrospector extends StdAnnotationIntrospector {
    @Override
    public AnnotatedMethod findGetter(BeanProperty property) {
        Method method = super.findGetter(property);
        if (method == null) {
            throw new RuntimeException("Could not find getter for property: " + property.getName());
        }
        return new AnnotatedMethod(method, property.getType());
    }
}