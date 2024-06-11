package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private MapperFactory mapperFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mapperFactory = new MapperFactory();
        mapperFactory.setSerializationConfig(new SerializationConfig());
        mapperFactory.setDeserializationConfig(new DeserializationConfig());
        objectMapper = mapperFactory.getMapper();
    }

    @Test
    public void testStdTypeResolverBuilder_Default() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeResolver<?> typeResolver = builder.build();
        Assert.assertNotNull(typeResolver);

        JavaType javaType = objectMapper.getTypeFactory()
                .constructRawType(StdTypeResolverBuilder.class);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), javaType, null);
        Assert.assertNull(typeSerializer);

        JavaType baseType = objectMapper.getTypeFactory()
                .constructType(String.class);
        Collection<NamedType> subtypes = null;
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        Assert.assertNull(typeDeserializer);
    }

    @Test
    public void testStdTypeResolverBuilder_Init() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        TypeResolver<?> typeResolver = builder.build();
        Assert.assertNotNull(typeResolver);

        JavaType javaType = objectMapper.getTypeFactory()
                .constructRawType(StdTypeResolverBuilder.class);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), javaType, null);
        Assert.assertNull(typeSerializer);

        JavaType baseType = objectMapper.getTypeFactory()
                .constructType(String.class);
        Collection<NamedType> subtypes = null;
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        Assert.assertNull(typeDeserializer);

        String typeProperty = "_typeProperty";
        builder.typeProperty(typeProperty);
        TypeDeserializer typeDeserializer2 = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        Assert.assertNotNull(typeDeserializer2);
        Assert.assertEquals(typeProperty, typeDeserializer2.getTypeIdPropertyName());
    }

    @Test
    public void testStdTypeResolverBuilder_IncludeAs_WrapperArray() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), javaType, null);
        Assert.assertNotNull(typeSerializer);
        Assert.assertTrue(typeSerializer instanceof AsArrayTypeSerializer);

        JavaType baseType = objectMapper.getTypeFactory()
                .constructType(Integer[].class);
        Collection<NamedType> subtypes = null;
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        Assert.assertNotNull(typeDeserializer);
        Assert.assertTrue(typeDeserializer instanceof AsArrayTypeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testStdTypeResolverBuilder_IncludeAs_Unknown() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        builder.inclusion(JsonTypeInfo.As.UNKNOWN);
    }
}