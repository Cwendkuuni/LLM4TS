package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StdTypeResolverBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testStdTypeResolverBuilder() throws IOException {
        StdTypeResolverBuilder typeResolverBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();

        // Test default configuration
        TypeResolverTypeDescriptor<String> stringType = new TypeResolverBase() {
            @Override
            public JavaType getType(SerializerProvider serializers, TypeResolverBuilder builder) throws IOException {
                return JavaType.constructUnresolved(String.class, this.getClass());
            }
        }.getType(objectMapper.getSerializerProvider(), typeResolverBuilder);

        TypeSerializer stringTypeSerializer = typeResolverBuilder.buildTypeSerializer(objectMapper.getSerializationConfig(), stringType, Collections.emptyList());
        Assert.assertNull(stringTypeSerializer);

        // Test custom configuration
        JsonTypeInfo customJsonTypeInfo = new JsonTypeInfo.Builder()
                .useWrapperOrder(true)
                .id(JsonTypeInfo.Id.CLASS)
                .build();
        StdTypeResolverBuilder customTypeResolverBuilder = new StdTypeResolverBuilder()
                .init(customJsonTypeInfo, null);

        TypeResolverTypeDescriptor<MyClass> myClassType = new TypeResolverBase<MyClass>() {
            @Override
            public JavaType getType(SerializerProvider serializers, TypeResolverBuilder builder) throws IOException {
                return JavaType.constructUnresolved(MyClass.class, this.getClass());
            }
        }.getType(objectMapper.getSerializerProvider(), customTypeResolverBuilder);

        TypeSerializer myClassTypeSerializer = customTypeResolverBuilder.buildTypeSerializer(objectMapper.getSerializationConfig(), myClassType, Arrays.asList(new NamedType(String.class, null, null, null, null, null)));
        Assert.assertNotNull(myClassTypeSerializer);

        ObjectNode json = objectMapper.writeValueAsTree(new MyClass());
        JsonNode deserializedNode = objectMapper.readTree(json);
        Assert.assertEquals(JsonNode.ValueType.POJO, deserializedNode.getValueType());
        MyClass deserializedObject = objectMapper.readValue(json.traverse(), MyClass.class);
        Assert.assertEquals(new MyClass(), deserializedObject);

        // Test errors
        StdTypeResolverBuilder errorTypeResolverBuilder = new StdTypeResolverBuilder()
                .init(null, null);
        try {
            TypeSerializer errorStringTypeSerializer = errorTypeResolverBuilder.buildTypeSerializer(objectMapper.getSerializationConfig(), stringType, Collections.emptyList());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StdTypeResolverBuilder errorCustomTypeResolverBuilder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.NONE, null);
        try {
            TypeSerializer errorMyClassTypeSerializer = errorCustomTypeResolverBuilder.buildTypeSerializer(objectMapper.getSerializationConfig(), myClassType, Arrays.asList(new NamedType(String.class, null, null, null, null, null)));
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        StdTypeResolverBuilder errorUnknownTypeResolverBuilder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CUSTOM, null);
        try {
            TypeSerializer errorMyClassTypeSerializer = errorUnknownTypeResolverBuilder.buildTypeSerializer(objectMapper.getSerializationConfig(), myClassType, Arrays.asList(new NamedType(String.class, null, null, null, null, null)));
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    static class MyClass {
        // Empty class
    }
}