package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetterlessPropertyTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @After
    public void teardown() throws Exception {
        makeAccessible(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS.getClass());
    }

    @Test
    public void testSetterlessPropertyBasic() throws IOException {
        // Create a test class with a setterless property
        static class TestClass {
            @JsonSetter(value = "items")
            private List<String> list;
        }

        // Serialize a JSON object with a list property, which will be deserialized using SetterlessProperty
        ObjectNode json = mapper.createObjectNode();
        json.put("items", Arrays.asList("one", "two"));

        // Deserialize the JSON object into a TestClass instance
        TestClass testClass = mapper.readValue(json.toString(), TestClass.class);

        // Check if the list property was correctly deserialized and modified
        Assert.assertEquals(Arrays.asList("one", "two"), testClass.list);
    }

    @Test
    public void testSetterlessPropertyEmpty() throws IOException {
        // Create a test class with a setterless property
        static class TestClass {
            @JsonSetter(value = "items")
            private List<String> list;
        }

        // Serialize an empty JSON object with a list property, which will be deserialized using SetterlessProperty
        ObjectNode json = mapper.createObjectNode();

        // Deserialize the JSON object into a TestClass instance
        TestClass testClass = mapper.readValue(json.toString(), TestClass.class);

        // Check if the list property was correctly deserialized and modified
        Assert.assertNotNull(testClass.list);
        Assert.assertTrue(testClass.list.isEmpty());
    }

    @Test
    public void testSetterlessPropertyNull() throws IOException {
        // Create a test class with a setterless property
        static class TestClass {
            @JsonSetter(value = "items")
            private List<String> list;
        }

        // Serialize a JSON object with a null list property, which will be deserialized using SetterlessProperty
        ObjectNode json = mapper.createObjectNode();
        json.put("items", null);

        // Deserialize the JSON object into a TestClass instance
        TestClass testClass = mapper.readValue(json.toString(), TestClass.class);

        // Check if the list property was correctly deserialized and modified
        Assert.assertNull(testClass.list);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessPropertySetMethodCall() throws IOException {
        // Create a test class with a setterless property
        static class TestClass {
            @JsonSetter(value = "items")
            private List<String> list;

            public void setList(List<String> list) {
                // This should never be called
                throw new UnsupportedOperationException();
            }
        }

        // Serialize a JSON object with a list property, which will be deserialized using SetterlessProperty
        ObjectNode json = mapper.createObjectNode();
        json.put("items", Arrays.asList("one", "two"));

        // Deserialize the JSON object into a TestClass instance
        TestClass testClass = mapper.readValue(json.toString(), TestClass.class);

        // Attempt to call the setter method, which should not be supported
        testClass.setList(new ArrayList<>());
    }

    @Test(expected = JsonMappingException.class)
    public void testSetterlessPropertyInvalidType() throws IOException {
        // Create a test class with a setterless property
        static class TestClass {
            @JsonSetter(value = "items")
            private Map<String, Integer> map;

            public static final SetterlessProperty property = new SetterlessProperty(
                    new BeanPropertyDefinition(
                            TestClass.class.getDeclaredField("map"),
                            Map.class,
                            null,
                            new Annotations(),
                            new AnnotatedMethod(TestClass.class.getDeclaredMethod("setMap", Map.class))),
                    null,
                    null,
                    null);
        }

        // Serialize a JSON object with a map property, which will be deserialized using SetterlessProperty, but the property is not settable since it's a Map<String, Integer>
        ObjectNode json = mapper.createObjectNode();
        json.put("items", Arrays.asList(1, 2));

        // Attempt to deserialize the JSON object into a TestClass instance
        TestClass testClass = null;
        try {
            testClass = mapper.readValue(json.toString(), TestClass.class);
        } catch (IOException e) {
            // The exception is expected
        }

        // Check if the deserialization failed due to the unsupported map type
        Assert.assertNull(testClass);
    }
}