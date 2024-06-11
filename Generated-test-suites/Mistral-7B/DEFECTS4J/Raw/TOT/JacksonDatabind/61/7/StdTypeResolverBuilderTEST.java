package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

public class StdTypeResolverBuilderTest {

    @SuppressWarnings("unchecked")
    private static <T> T createTestObject(Class<T> type) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                "{\"typeIdProperty\":\"foo\", \"typeIdValue\":\"MyType\"}", type);
    }

    @SuppressWarnings("unchecked")
    private static <T> T createTestObjectWithCustomIdResolver(Class<T> type,
            TypeIdResolver customIdResolver) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeIdResolver(customIdResolver);
        return mapper.readValue(
                "{\"typeIdProperty\":\"bar\", \"typeIdValue\":\"MyType\"}", type);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderNoTypeInfo() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        TypeSerializer ts = mapper.getTypeSerializer(String.class);
        assertNull(ts);

        TypeDeserializer td = mapper.getTypeDeserializer(String.class);
        assertNull(td);
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderCustomIdResolver() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.typeProperty(typeIdProperty);
        TypeIdResolver customIdResolver = new TypeNameIdResolver(null, null);
        builder.customIdResolver(customIdResolver);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        // Test serialization
        MyClass obj = new MyClass();
        String json = mapper.writeValueAsString(obj);
        assertEquals("{\"typeIdProperty\":\"MyType\"}", json);

        // Test deserialization
        MyClass deser = mapper.readValue(json, MyClass.class);
        assertEquals(obj.toString(), deser.toString());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderPropertyInclusion() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeProperty(typeIdProperty);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        MyClass obj = new MyClass();
        String json = mapper.writeValueAsString(obj);
        assertEquals("{\"typeIdProperty\":\"MyType\"}", json);

        MyClass deser = mapper.readValue(json, MyClass.class);
        assertEquals(obj.toString(), deser.toString());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderWrapperObjectInclusion() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        builder.typeProperty(typeIdProperty);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        MyClass obj = new MyClass();
        String json = mapper.writeValueAsString(obj);
        assertEquals("{\"@type\":\"MyType\"}", json);

        MyClass deser = mapper.readValue(json, MyClass.class);
        assertEquals(obj.toString(), deser.toString());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderWrapperArrayInclusion() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        builder.typeProperty(typeIdProperty);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        MyClass[] arr = new MyClass[1];
        arr[0] = new MyClass();
        String json = mapper.writeValueAsString(arr);
        assertEquals("[{\"@type\":\"MyType\"}]", json);

        MyClass[] deser = mapper.readValue(json, MyClass[].class);
        assertEquals(arr.length, deser.length);
        for (MyClass each : deser) {
            assertEquals(arr[0].toString(), each.toString());
        }
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderExistingPropertyInclusion() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        builder.typeProperty(typeIdProperty);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        MyClassWithTypeId obj = new MyClassWithTypeId();
        obj.setTypeId(typeIdValue);
        String json = mapper.writeValueAsString(obj);
        assertEquals("{\"typeIdProperty\":\"MyType\"}", json);

        MyClassWithTypeId deser = mapper.readValue(json, MyClassWithTypeId.class);
        assertEquals(obj.getTypeId(), deser.getTypeId());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderExternalPropertyInclusion() throws Exception {
        String typeIdProperty = "@type";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        builder.typeProperty(typeIdProperty);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        MyClassWithTypeId obj = new MyClassWithTypeId();
        obj.setTypeId(typeIdValue);
        String json = mapper.writeValueAsString(obj);
        assertEquals("{\"@type\":\"MyType\"}", json);

        MyClassWithTypeId deser = mapper.readValue(json, MyClassWithTypeId.class);
        assertEquals(obj.getTypeId(), deser.getTypeId());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderCustomTypeIdResolver() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.typeProperty(typeIdProperty);

        TypeIdResolver customIdResolver = new CustomTypeIdResolver();
        builder.customIdResolver(customIdResolver);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        MyClass obj = new MyClass();
        String json = mapper.writeValueAsString(obj);
        assertEquals("{\"typeIdProperty\":\"custom:MyType\"}", json);

        MyClass deser = mapper.readValue(json, MyClass.class);
        assertEquals(obj.toString(), deser.toString());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderMissingTypeId() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeProperty(typeIdProperty);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        MyClass obj = new MyClass();
        String json = mapper.writeValueAsString(obj);
        String json2 = json.replace("MyType", "WrongType");
        MyClass deser = mapper.readValue(json, MyClass.class);
        MyClass deser2 = mapper.readValue(json2, MyClass.class);
        assertEquals(deser.toString(), deser2.toString());
        assertEquals(deser.toString(), obj.toString());
    }

    @org.junit.Test
    public void testStdTypeResolverBuilderMissingTypeIdDeserialization() throws Exception {
        String typeIdProperty = "typeIdProperty";
        String typeIdValue = "MyType";

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeProperty(typeIdProperty);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeResolverBuilder(builder);

        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setTypeResolverBuilder(builder);

        MyClass obj = new MyClass();
        String json = mapper.writeValueAsString(obj);
        MyClass deser = mapper.readValue(json, MyClass.class);

        // test null idType
        StdTypeResolverBuilder builder2 = new StdTypeResolverBuilder();
        builder2.init(null, null);
        mapper2.setTypeResolverBuilder(builder2);
        try {
            mapper2.readValue(json, MyClass.class);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test null idRes
        StdTypeResolverBuilder builder3 = new StdTypeResolverBuilder();
        builder3.init(JsonTypeInfo.Id.PROPERTY, null);
        mapper2.setTypeResolverBuilder(builder3);
        try {
            mapper2.readValue(json, MyClass.class);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test empty collection
        StdTypeResolverBuilder builder4 = new StdTypeResolverBuilder();
        builder4.init(JsonTypeInfo.Id.PROPERTY, Collections.emptyList());
        mapper2.setTypeResolverBuilder(builder4);
        try {
            mapper2.readValue(json, MyClass.class);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test non-matching types
        StdTypeResolverBuilder builder5 = new StdTypeResolverBuilder();
        builder5.init(JsonTypeInfo.Id.PROPERTY, Arrays.asList(new NamedType(String.class, "StringType")));
        mapper2.setTypeResolverBuilder(builder5);
        try {
            mapper2.readValue(json, MyClass.class);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test matching type
        StdTypeResolverBuilder builder6 = new StdTypeResolverBuilder();
        builder6.init(JsonTypeInfo.Id.PROPERTY, Arrays.asList(new NamedType(MyClass.class, "TypeName")));
        mapper2.setTypeResolverBuilder(builder6);
        MyClass deser2 = mapper2.readValue(json, MyClass.class);
        assertEquals(deser.toString(), deser2.toString());
    }

    private static class MyClass {
        @Override
        public String toString() {
            return "MyClass";
        }
    }

    private static class MyClassWithTypeId {
        private String typeId;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public String toString() {
            return "MyClassWithTypeId [typeId=" + typeId + "]";
        }
    }

    private static class CustomTypeIdResolver extends TypeIdResolverBase {
        @Override
        public TypeId idFromValue(Object value, JavaType type) {
            return new TypeId(TypeIdResolverBase.DEFAULT_PREFIX, "custom:" + type.getName());
        }
    }

}