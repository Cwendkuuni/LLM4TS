package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.ser.impl.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Test cases for {@link JsonValueSerializer} class.
 */
public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    private static <T> JsonSerializer<T> createSerializer(Class<T> type, Method getterMethod,
                                                          JsonSerializer<Object> valueSerializer) {
        try {
            ObjectMap mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerForType(type);
            return (JsonSerializer<T>) reader.findValueSerializer(type, null);
        } catch (JsonMappingException e) {
            throw new RuntimeException("Failed to create serializer for type " + type, e);
        }
    }

    @Test
    public void testSimpleSerialization() throws Exception {
        Class testClass = MyTestClass.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(String.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(Object.class);
        MyTestClass testInstance = new MyTestClass();

        String result = writer.writeValueAsString(testInstance);
        assertEquals("\"test\"", result);
    }

    @Test
    public void testSerializationWithTypeInformation() throws Exception {
        Class testClass = MyTestClassWithType.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(String.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer, true);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(Object.class);
        MyTestClassWithType testInstance = new MyTestClassWithType();

        String result = writer.writeValueAsString(testInstance);
        assertEquals("\"test\"", result);
        assertEquals("String test", mapper.readerForValueId(result).getValueClass().getName());
    }

    @Test
    public void testSerializationForEnum() throws Exception {
        Class testClass = MyEnumTestClass.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(MyEnum.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(Object.class);
        MyEnumTestClass testInstance = new MyEnumTestClass(MyEnum.FOO);

        String result = writer.writeValueAsString(testInstance);
        assertEquals("\"FOO\"", result);
    }

    @Test
    public void testSerializationForContextualizing() throws Exception {
        Class testClass = MyContextTestClass.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(String.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(Object.class);
        MyContextTestClass testInstance = new MyContextTestClass();
        testInstance.setValueSerializer(createSerializer(Integer.class, testInstance.getValueMethod(), null));

        String result = writer.writeValueAsString(testInstance);
        assertEquals("42", result);
    }

    @Test
    public void testSerializationForNaturalTypes() throws Exception {
        Class testClass = MyNaturalTestClass.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(Integer.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(Object.class);
        MyNaturalTestClass testInstance = new MyNaturalTestClass();
        testInstance.setValue(42);

        String result = writer.writeValueAsString(testInstance);
        assertEquals("42", result);
    }

    @Test
    public void testDeserialization() throws Exception {
        Class testClass = MyTestClass.class;
        Method setValueMethod = testClass.getMethod("setValue", String.class);

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(testClass);

        String json = "\"test\"";
        MyTestClass testInstance = reader.readValue(JsonNode.class, testClass);
        assertEquals("test", testInstance.getValue());

        testInstance.setValue("modified");
        String modifiedJson = writer.writeValueAsString(testInstance);
        MyTestClass modifiedTestInstance = reader.readValue(modifiedJson, testClass);
        assertEquals("modified", modifiedTestInstance.getValue());
    }

    @Test
    public void testDeserializationWithTypeInformation() throws Exception {
        Class testClass = MyTestClassWithType.class;
        Method setValueMethod = testClass.getMethod("setValue", String.class);

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(testClass);

        String json = "\"test\"";
        MyTestClassWithType testInstance = reader.readValue(JsonNode.class, testClass);
        assertEquals("test", testInstance.getValue());
        assertEquals("String test", testInstance.getType());

        testInstance.setValue("modified");
        String modifiedJson = writer.writeValueAsString(testInstance);
        MyTestClassWithType modifiedTestInstance = reader.readValue(modifiedJson, testClass);
        assertEquals("modified", modifiedTestInstance.getValue());
        assertEquals("String modified", modifiedTestInstance.getType());
    }

    @Test
    public void testDeserializationForEnum() throws Exception {
        Class testClass = MyEnumTestClass.class;
        Method setValueMethod = testClass.getMethod("setValue", String.class);

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(testClass);

        String json = "\"FOO\"";
        MyEnumTestClass testInstance = reader.readValue(JsonNode.class, testClass);
        assertEquals(MyEnum.FOO, testInstance.getValue());

        testInstance.setValue("BAR");
        String barJson = writer.writeValueAsString(testInstance);
        MyEnumTestClass barTestInstance = reader.readValue(barJson, testClass);
        assertEquals(MyEnum.BAR, barTestInstance.getValue());
    }

    @Test
    public void testDeserializationForContextualizing() throws Exception {
        Class testClass = MyContextTestClass.class;
        Method setValueMethod = testClass.getMethod("setValue", Integer.class);

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(testClass);

        String json = "42";
        MyContextTestClass testInstance = reader.readValue(JsonNode.class, testClass);
        assertEquals(42, testInstance.getValue());

        testInstance.setValue(5);
        String fiveJson = writer.writeValueAsString(testInstance);
        MyContextTestClass fiveTestInstance = reader.readValue(fiveJson, testClass);
        assertEquals(5, fiveTestInstance.getValue());
    }

    @Test
    public void testDeserializationForNaturalTypes() throws Exception {
        Class testClass = MyNaturalTestClass.class;
        Method setValueMethod = testClass.getMethod("setValue", int.class);

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(testClass);

        String json = "42";
        MyNaturalTestClass testInstance = reader.readValue(JsonNode.class, testClass);
        assertEquals(42, testInstance.getValue());

        testInstance.setValue(5);
        String fiveJson = writer.writeValueAsString(testInstance);
        MyNaturalTestClass fiveTestInstance = reader.readValue(fiveJson, testClass);
        assertEquals(5, fiveTestInstance.getValue());
    }

    @Test
    public void testSchemaGeneration() throws Exception {
        Class testClass = MyTestClass.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(String.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer);
        JsonSchema schema = serializer.getSchema(new ObjectMapper(), null);

        assertEquals(JsonSchema.Type.STRING, schema.getSchemaType());
        assertEquals("String test", schema.getDescription());
    }

    @Test
    public void testSchemaGenerationForEnum() throws Exception {
        Class testClass = MyEnumTestClass.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(MyEnum.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer);
        JsonSchema schema = serializer.getSchema(new ObjectMapper(), null);

        JsonSchema enumSchema = schema.getEnumValues();
        assertEquals(MyEnum.FOO.name(), enumSchema.getValue("FOO").getDescription());
        assertEquals(MyEnum.BAR.name(), enumSchema.getValue("BAR").getDescription());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Class testClass = MyFormatTestClass.class;
        Method getValueMethod = testClass.getMethod("getValue");
        JsonSerializer<Object> valueSerializer = createSerializer(String.class, getValueMethod, null);

        JsonValueSerializer serializer = new JsonValueSerializer(getValueMethod, valueSerializer);

        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider().findSchemaVisitor(JsonFormatVisitorWrapper.class);

        serializer.acceptJsonFormatVisitor(visitor, null);

        JsonSchema schema = visitor.getSchema();
        assertEquals(JsonSchema.Type.STRING, schema.getSchemaType());
        assertEquals("String test", schema.getDescription());
    }

    private static class MyTestClass {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class MyTestClassWithType {
        private String value;
        private String type;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private static enum MyEnum {
        FOO, BAR
    }

    private static class MyEnumTestClass {
        private MyEnum value;

        public MyEnum getValue() {
            return value;
        }

        public void setValue(MyEnum value) {
            this.value = value;
        }
    }

    private static class MyContextTestClass {
        private JsonSerializer<Object> valueSerializer;

        public JsonSerializer<Object> getValueSerializer() {
            return valueSerializer;
        }

        public void setValueSerializer(JsonSerializer<Object> valueSerializer) {
            this.valueSerializer = valueSerializer;
        }

        private Method valueMethod;

        public Object getValue() throws Exception {
            return valueMethod.invoke(this);
        }

        public void setValueMethod(Method valueMethod) {
            this.valueMethod = valueMethod;
        }
    }

    private static class MyNaturalTestClass {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @JsonValue
    private static class MyFormatTestClass {
        public String getValue() {
            return "test";
        }
    }
}