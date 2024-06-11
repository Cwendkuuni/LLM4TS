package com.fasterxml.jackson.databind.tests.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TestBase;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AsWrapperTypeDeserializerTest extends TestBase {

    @Test
    public void testDeserializeWithValidWrapperObject() throws Exception {
        String json = "{\"type\":\"com.example.MyClass\", \"data\":{\"id\":1, \"name\":\"Test\"}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        MyClass obj = mapper.treeToValue(rootNode, deserializer);
        assertEquals(obj.id, 1);
        assertEquals(obj.name, "Test");
    }

    @Test
    public void testDeserializeWithMissingTypeId() throws Exception {
        String json = "{\"data\":{\"id\":1, \"name\":\"Test\"}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        try {
            MyClass obj = mapper.treeToValue(rootNode, deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithInvalidTypeId() throws Exception {
        String json = "{\"type\":\"com.example.InvalidClass\", \"data\":{\"id\":1, \"name\":\"Test\"}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        try {
            MyClass obj = mapper.treeToValue(rootNode, deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithMissingData() throws Exception {
        String json = "{\"type\":\"com.example.MyClass\"}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        try {
            MyClass obj = mapper.treeToValue(rootNode, deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithEmptyData() throws Exception {
        String json = "{\"type\":\"com.example.MyClass\", \"data\":{}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        MyClass obj = mapper.treeToValue(rootNode, deserializer);
        assertNull(obj);
    }

    @Test
    public void testDeserializeWithNullData() throws Exception {
        String json = "{\"type\":\"com.example.MyClass\", \"data\":null}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        MyClass obj = mapper.treeToValue(rootNode, deserializer);
        assertNull(obj);
    }

    @Test
    public void testDeserializeWithEmptyTypeId() throws Exception {
        String json = "{\"type\":\"\", \"data\":{\"id\":1, \"name\":\"Test\"}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        try {
            MyClass obj = mapper.treeToValue(rootNode, deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithInvalidTypeIdFormat() throws Exception {
        String json = "{\"type\":\"com.example.MyClass with suffix\", \"data\":{\"id\":1, \"name\":\"Test\"}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        try {
            MyClass obj = mapper.treeToValue(rootNode, deserializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithExtraFields() throws Exception {
        String json = "{\"type\":\"com.example.MyClass\", \"data\":{\"id\":1, \"name\":\"Test\"}, \"extra\":\"Field\"}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        MyClass obj = mapper.treeToValue(rootNode, deserializer);
        assertEquals(obj.id, 1);
        assertEquals(obj.name, "Test");
        assertNull(obj.extra);
    }

    @Test
    public void testDeserializeWithNestedWrapperObject() throws Exception {
        String json = "{\"type\":\"com.example.MyClass\", \"data\":{\"type\":\"com.example.NestedClass\", \"data\":{\"value\":\"Test\"}}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                mapper.constructType(MyClass.class), mapper.getTypeIdResolver(), "_type", false, MyDefaultClass.class);
        MyClass obj = mapper.treeToValue(rootNode, deserializer);
        assertEquals(obj.id, 0); // default value
        assertEquals(obj.name, null); // default value
        NestedClass nested = (NestedClass) obj.data;
        assertEquals(nested.value, "Test");
    }

    // Add more test cases as needed...

    static class MyClass {
        int id;
        String name;

        MyClass() {}

        MyClass(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class MyDefaultClass {
        // Default class for handling cases where no specific type is found
    }

    static class NestedClass {
        String value;

        NestedClass() {}

        NestedClass(String value) {
            this.value = value;
        }
    }
}