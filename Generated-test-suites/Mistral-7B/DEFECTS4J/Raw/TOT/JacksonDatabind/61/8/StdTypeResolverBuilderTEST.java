package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeResolver<?> resolver = builder.build();
        Assert.assertNull(resolver);
    }

    @Test
    public void testEmptyBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().noTypeInfoBuilder();
        TypeResolver<?> resolver = builder.build();
        Assert.assertNull(resolver);
    }

    @Test
    public void testBasicBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        TypeResolver<?> resolver = builder.build();
        Assert.assertNotNull(resolver);
    }

    @Test
    public void testSerialization() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), javaType, null);
        String json = objectMapper.writeValueAsString(new Object() {});
        Assert.assertEquals("{}", json);
    }

    @Test
    public void testSerializationWithCustomIdResolver() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.typeProperty("type");
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        TypeIdResolver idResolver = new TypeNameIdResolver(objectMapper.getSerializationConfig(), javaType, null, true, false);
        builder.customIdResolver(idResolver);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), javaType, null);
        String json = objectMapper.writeValueAsString(new Object() {});
        Assert.assertEquals("{}", json);
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JavaType javaType = new TypeReference<String[]>() {}.getType();
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), javaType, null);
        String json = "[]";
        Object deserialized = objectMapper.readValue(json, javaType.getType());
        Assert.assertArrayEquals(new String[0], (String[]) deserialized);
    }

    @Test
    public void testDeserializationWithCustomIdResolver() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.typeProperty("type");
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JavaType javaType = new TypeReference<String[]>() {}.getType();
        TypeIdResolver idResolver = new TypeNameIdResolver(objectMapper.getDeserializationConfig(), javaType, null, false, true);
        builder.customIdResolver(idResolver);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), javaType, null);
        String json = "[]";
        Object deserialized = objectMapper.readValue(json, javaType.getType());
        Assert.assertArrayEquals(new String[0], (String[]) deserialized);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializationWithInvalidInclusionType() throws JsonProcessingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        MapperFactory mapperFactory = new MapperFactory();
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JavaType javaType = new TypeReference<String[]>() {}.getType();
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), javaType, null);
    }
}