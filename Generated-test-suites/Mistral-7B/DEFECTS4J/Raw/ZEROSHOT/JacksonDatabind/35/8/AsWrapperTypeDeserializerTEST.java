package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

    public AsWrapperTypeDeserializerTest() {
        super(new ObjectMapper());
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"type\":\"com.TestClass\", \"data\":{\"name\":\"John\", \"age\":30}}";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
        assertEquals(TestClass.class, result.getClass());
        assertEquals("John", ((TestClass) result).getName());
        assertEquals(30, ((TestClass) result).getAge());
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"type\":\"com.TestClass\", \"data\":{\"name\":\"Mary\", \"age\":25}}]";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass[].class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(json), context);
        assertArrayEquals(new TestClass[] {new TestClass("Mary", 25)}, (TestClass[]) result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        String json = "\"com.TestClass#1\"";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser(json), context);
        assertEquals(TestClass.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json = "{\"data\":{\"name\":\"John\", \"age\":30}}";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromAny(mapper.getFactory().createParser(json), context);
        assertEquals(TestClass.class, result.getClass());
        assertEquals("John", ((TestClass) result).getName());
        assertEquals(30, ((TestClass) result).getAge());
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonWithoutStartObject() throws Exception {
        String json = "{\"data\":{\"name\":\"John\", \"age\":30}}"; // missing "type" key
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonWithoutFieldName() throws Exception {
        String json = "{ \"type\":\"com.TestClass\", }"; // missing "data" key
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonWithoutEndObject() throws Exception {
        String json = "{\"type\":\"com.TestClass\", \"data\":{\"name\":\"John\", \"age\":30}";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonWithWrongTypeId() throws Exception {
        String json = "{\"type\":\"com.TestClass2\", \"data\":{\"name\":\"John\", \"age\":30}}";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonWithNullTypeId() throws Exception {
        String json = "{\"data\":{\"name\":\"John\", \"age\":30}}";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"@type\":\"com.TestClass\", \"data\":{\"name\":\"John\", \"age\":30}}";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "@type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
        assertEquals(TestClass.class, result.getClass());
        assertEquals("John", ((TestClass) result).getName());
        assertEquals(30, ((TestClass) result).getAge());
    }

    @Test
    public void testDeserializeWithMergedTypeId() throws Exception {
        String json = "{\"type\":\"com.TestClass\", \"@type\":\"com.TestClassWithAnnotation\", \"data\":{\"name\":\"John\", \"age\":30}}";
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = TestClassImpl.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(json), context);
        assertEquals(TestClassWithAnnotation.class, result.getClass());
        assertEquals("John", ((TestClassWithAnnotation) result).getName());
        assertEquals(30, ((TestClassWithAnnotation) result).getAge());
    }

    static class TestClass {
        private String name;
        private int age;

        public TestClass(String name, int age) {
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

    static class TestClassImpl {}

    static class TestClassWithAnnotation {
        @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.WRAPPER_OBJECT)
        private String name;
        private int age;

        @JsonProperty("@type")
        private String typeId;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getTypeId() {
            return typeId;
        }
    }
}