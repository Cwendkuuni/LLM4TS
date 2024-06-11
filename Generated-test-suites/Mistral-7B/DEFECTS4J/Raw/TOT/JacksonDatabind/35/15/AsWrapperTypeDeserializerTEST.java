package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class AsWrapperTypeDeserializerTest extends TestBase {

    private static final String NESTED_CLASS = "NestedClass";

    @Test
    public void testDeserializeWrappedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("typeId", "MyClass");
        jsonMap.put("MyClass", new MyClass("Hello World"));
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        MyClass expected = new MyClass("Hello World");

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(MyClass.class, As.WRAPPER_OBJECT);
        Object result = deserializer.deserializeTypedFromAny(parser, context());

        assertEquals(result, expected);
    }

    @Test
    public void testDeserializeWrappedObjectMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("MyClass", new MyClass("Hello World"));
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        MyClass expected = new MyClass("Hello World");

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(MyClass.class, As.WRAPPER_OBJECT);

        assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromAny(parser, context()));
    }

    @Test
    public void testDeserializeWrappedObjectInvalidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("typeId", "InvalidTypeId");
        jsonMap.put("MyClass", new MyClass("Hello World"));
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        MyClass expected = new MyClass("Hello World");

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(MyClass.class, As.WRAPPER_OBJECT);

        assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromAny(parser, context()));
    }

    @Test
    public void testDeserializeWrappedObjectEmptyTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("", new MyClass("Hello World"));
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        MyClass expected = new MyClass("Hello World");

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(MyClass.class, As.WRAPPER_OBJECT);

        assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromAny(parser, context()));
    }

    @Test
    public void testDeserializeWrappedObjectNestedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("typeId", "NestedClass");
        jsonMap.put("NestedClass", new NestedClass("Nested"));

        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        NestedClass expected = new NestedClass("Nested");

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(NestedClass.class, As.WRAPPER_OBJECT);
        Object result = deserializer.deserializeTypedFromAny(parser, context());

        assertEquals(result, expected);
    }

    @Test
    public void testDeserializeWrappedObjectMissingNestedTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("typeId", "NestedClass");
        jsonMap.put("NestedClass", "MissingTypeId");

        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        NestedClass expected = new NestedClass("Nested");

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(NestedClass.class, As.WRAPPER_OBJECT);

        assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromAny(parser, context()));
    }

    @Test
    public void testDeserializeWrappedObjectInvalidNestedTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("typeId", "NestedClass");
        jsonMap.put("NestedClass", "InvalidTypeId");

        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
        NestedClass expected = new NestedClass("Nested");

        JsonParser parser = mapper.getFactory().createParser(jsonString);
        TypeDeserializer deserializer = mapper.getTypeDeserializerInstance(NestedClass.class, As.WRAPPER_OBJECT);

        assertThrows(JsonMappingException.class, () -> deserializer.deserializeTypedFromAny(parser, context()));
    }

    private static class MyClass {
        private final String value;

        public MyClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class NestedClass {
        private final String value;

        public NestedClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}