package com.example.test; // Replace this package name with your test package name

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonValueSerializerTest {

    private static final JsonNodeFactory NODE_FACTORY = new JsonFactory().getNodeFactory();
    private ObjectNode testObject;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException {
        TestClass testClass = new TestClass();
        Method testMethod = TestClass.class.getMethod("getTestValue");
        Object testInstance = testClass.newInstance();
        jsonValueSerializer = new JsonValueSerializer(testMethod, new StdSerializer<>("", null));
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(jsonValueSerializer);
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        jsonValueSerializer.serialize(null, gen, new SerializerProvider());
        Assert.assertEquals("null", gen.getOutputFile().toString());
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        Object testValue = jsonValueSerializer.getValueMethod().invoke(new TestClass());
        jsonValueSerializer.serialize(testInstance, gen, new SerializerProvider());
        ObjectNode serializedObject = NODE_FACTORY.objectNode();
        ObjectNode result = gen.getOutputFileAsTree();
        Assert.assertEquals(serializedObject.toString(), result.toString());
        Assert.assertEquals(testValue.toString(), serializedObject.get("value").asText());
    }

    @Test
    public void testSerializeWithTypeInformation() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        Object testValue = jsonValueSerializer.getValueMethod().invoke(new TestClass());
        jsonValueSerializer.serializeWithType(testInstance, gen, new SerializerProvider(), new TypeSerializer());
        ObjectNode serializedObject = NODE_FACTORY.objectNode();
        ObjectNode result = gen.getOutputFileAsTree();
        String className = testValue.getClass().getName();
        String typeId = className.substring(className.lastIndexOf(".") + 1);
        ObjectNode typeNode = NODE_FACTORY.objectNode();
        ObjectNode typeIdNode = NODE_FACTORY.objectNode();
        typeIdNode.putPOJO("class", typeId);
        typeNode.set("@type", typeIdNode);
        serializedObject.set("value", testValue);
        serializedObject.set("type", typeNode);
        Assert.assertEquals(serializedObject.toString(), result.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testIOExceptionWhenInvokingValueMethod() throws IOException, InvocationTargetException {
        TestClass testClass = new TestClass();
        Method testMethod = TestClass.class.getMethod("getTestValue");
        Object testInstance = testClass.newInstance();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testMethod, new StdSerializer<>("", null));
        Object testValue = testMethod.invoke(testInstance);
        throw ((InvocationTargetException) testMethod.invoke(testInstance)).getCause();
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWhenValueSerializerIsNull() throws IOException {
        TestClass testClass = new TestClass();
        Method testMethod = TestClass.class.getMethod("getTestValue");
        Object testInstance = testClass.newInstance();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testMethod, null);
        jsonValueSerializer.serialize(testInstance, new JsonGenerator(new StringWriter()), new SerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWhenSerializedValueIsNull() throws IOException {
        TestClass testClass = new TestClass();
        Method testMethod = TestClass.class.getMethod("getTestValue");
        Object testInstance = testClass.newInstance();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testMethod, new StdSerializer<>("", null));
        jsonValueSerializer.getValueMethod().invoke(testInstance)
                = null;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        jsonValueSerializer.serialize(testInstance, gen, new SerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWhenValueSerializerThrowsException() throws IOException {
        TestClass testClass = new TestClass();
        Method testMethod = TestClass.class.getMethod("getTestValue");
        Object testInstance = testClass.newInstance();
        JsonSerializer<Object> valueSerializer = new JsonSerializer<>("", null) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                throw new IOException("Expected exception");
            }
        };
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testMethod, valueSerializer);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        jsonValueSerializer.serialize(testInstance, gen, new SerializerProvider());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        TestClass testClass = new TestClass();
        Method testMethod = TestClass.class.getMethod("getTestValue");
        Object testInstance = testClass.newInstance();
        JsonSerializer<Object> valueSerializer = new StdSerializer<>("", null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testMethod, valueSerializer);
        Assert.assertEquals(valueSerializer, jsonValueSerializer.createContextual(new SerializerProvider(), null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        TestClass testClass = new TestClass();
        Method testMethod = TestClass.class.getMethod("getTestValue");
        Object testInstance = testClass.newInstance();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testMethod, new StdSerializer<>("", null));
        ObjectNode schema = jsonValueSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper() {}, null);
        Assert.assertEquals(JsonNode.NULL, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() {
        TestEnum testEnum = new TestEnum("TestEnumValue1");
        Method testMethod = TestEnum.class.getMethod("getValue");
        Object testInstance = testEnum;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(testMethod, new StdSerializer<>("", null));
        JsonStringFormatVisitor stringVisitor = new JsonFactory().createJsonFormatVisitor(Feature.FAIL_ON_EMPTY_BEANS, null);
        Assert.assertNotNull(jsonValueSerializer._acceptJsonFormatVisitorForEnum(stringVisitor, null, TestEnum.class.getClass()));
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(int.class, null));
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(boolean.class, null));
        Assert.assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(double.class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Integer.class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.class, null));
        Assert.assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Double.class, null));
    }

    static class TestClass {
        @JsonSerialize
        public Object getTestValue() {
            return new Object();
        }
    }

    static class TestEnum {
        private final String value;

        TestEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}