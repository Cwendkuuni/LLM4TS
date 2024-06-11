package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"_class\":\"java.lang.String\",\"value\":\"foo\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("foo", result);

        json = "{\"_class\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}";
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolverForDeserialization();
        TypeDeserializer deserializerForSubType = mapper.getTypeFactory().constructValueType(MyClass.class).getValueType();
        deserializer = new AsWrapperTypeDeserializer(deserializerForSubType, null, null, false, MyClass.class);
        result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertInstanceOf(MyClass.class, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String[].class);

        String json = "[{\"_class\":\"java.lang.String\",\"value\":\"foo\"},{\"_class\":\"java.lang.String\",\"value\":\"bar\"}]";
        Object result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertArrayEquals(new String[]{"foo", "bar"}, (String[]) result);

        json = "[{\"_class\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}},{\"_class\":\"com.example.MyClass\",\"value\":{\"name\":\"Jane\",\"age\":31}}]";
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolverForDeserialization();
        TypeDeserializer deserializerForSubType = mapper.getTypeFactory().constructValueType(MyClass[].class).getValueType();
        deserializer = new AsWrapperTypeDeserializer(deserializerForSubType, null, null, false, MyClass[].class);
        result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertArrayEquals(new MyClass[]{new MyClass("John", 30), new MyClass("Jane", 31)}, (MyClass[]) result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "\"foo\"";
        Object result = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("foo", result);

        json = "\"{\"_class\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}\"";
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolverForDeserialization();
        TypeDeserializer deserializerForSubType = mapper.getTypeFactory().constructValueType(MyClass.class).getValueType();
        deserializer = new AsWrapperTypeDeserializer(deserializerForSubType, null, null, false, MyClass.class);
        result = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertInstanceOf(MyClass.class, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "null";
        Object result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertNull(result);

        json = "42";
        result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals(42, result);

        json = "{\"_class\":\"java.lang.String\",\"value\":\"foo\"}";
        result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("foo", result);

        TypeIdResolver typeIdResolver = mapper.getTypeIdResolverForDeserialization();
        TypeDeserializer deserializerForSubType = mapper.getTypeFactory().constructValueType(MyClass.class).getValueType();
        deserializer = new AsWrapperTypeDeserializer(deserializerForSubType, null, null, false, MyClass.class);
        json = "{\"_class\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}";
        result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertInstanceOf(MyClass.class, result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"@type\":\"java.lang.String\",\"value\":\"foo\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertEquals("foo", result);

        json = "{\"@type\":\"com.example.MyClass\",\"value\":{\"name\":\"John\",\"age\":30}}";
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolverForDeserialization();
        TypeDeserializer deserializerForSubType = mapper.getTypeFactory().constructValueType(MyClass.class).getValueType();
        deserializer = new AsWrapperTypeDeserializer(deserializerForSubType, null, null, false, MyClass.class);
        result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertInstanceOf(MyClass.class, result);
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeInformationWrapper() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"_class\":\"java.lang.String\"}";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidStartObject() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"_class\":\"java.lang.String\",\"value\":\"foo\"},\"bar\"";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingFieldName() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"_class\":\"java.lang.String\"},";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingEndObject() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"_class\":\"java.lang.String\",\"value\":\"foo\"},";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"value\":\"foo\"}";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeId() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, String.class);

        String json = "{\"_class\":\"java.lang.Integer\",\"value\":\"foo\"}";
        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

    @Test
    public void testMergeTypeId() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                javaType, mapper.getTypeIdResolver(), "_class", true, Object.class);

        String json = "{\"_class\":\"java.lang.String\",\"value\":\"foo\",\"otherProperty\":\"bar\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertInstanceOf(String.class, result);
        assertEquals("foo", result);

        json = "{\"_class\":\"java.lang.String\",\"value\":\"foo\",\"_class\":\"java.util.Map\"}";
        result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertInstanceOf(String.class, result);
        assertEquals("foo", result);
    }

    static class MyClass {
        private final String name;
        private final int age;

        public MyClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}