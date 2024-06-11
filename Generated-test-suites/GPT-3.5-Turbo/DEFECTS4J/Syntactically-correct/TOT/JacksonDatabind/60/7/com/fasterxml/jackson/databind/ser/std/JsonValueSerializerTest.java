package com.fasterxml.jackson.databind.ser.std;

import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerialize_withNullValue_shouldSerializeNull() throws Exception {
        // Arrange
        TestClass testObject = new TestClass(null);
        String expectedJson = "{\"value\":null}";

        // Act
        String actualJson = mapper.writeValueAsString(testObject);

        // Assert
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_withNonNullValue_shouldSerializeValue() throws Exception {
        // Arrange
        TestClass testObject = new TestClass(123);
        String expectedJson = "{\"value\":123}";

        // Act
        String actualJson = mapper.writeValueAsString(testObject);

        // Assert
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_withObjectMethod_shouldCallMethodAndSerializeResult() throws Exception {
        // Arrange
        TestClass testObject = new TestClass(new CustomObject());
        String expectedJson = "{\"value\":\"customValue\"}";

        // Act
        String actualJson = mapper.writeValueAsString(testObject);

        // Assert
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_withBooleanMethod_shouldCallMethodAndSerializeResult() throws Exception {
        // Arrange
        TestClass testObject = new TestClass(true);
        String expectedJson = "{\"value\":\"true\"}";

        // Act
        String actualJson = mapper.writeValueAsString(testObject);

        // Assert
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerialize_withIntegerMethod_shouldCallMethodAndSerializeResult() throws Exception {
        // Arrange
        TestClass testObject = new TestClass(123);
        String expectedJson = "{\"value\":\"123\"}";

        // Act
        String actualJson = mapper.writeValueAsString(testObject);

        // Assert
        Assert.assertEquals(expectedJson, actualJson);
    }

    // additional test cases...

    private static class TestClass {
        @JsonValue
        private final Object value;

        public TestClass(Object value) {
            this.value = value;
        }
    }

    private static class CustomObject {
        public String getValue() {
            return "customValue";
        }
    }
}