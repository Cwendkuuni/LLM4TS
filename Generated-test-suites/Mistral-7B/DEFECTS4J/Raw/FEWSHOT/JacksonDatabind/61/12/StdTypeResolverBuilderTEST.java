package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testBuilderWithInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idResolver = new MinimalClassNameIdResolver(
                JavaType.construct(String.class), new TypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, idResolver);
        assertEquals(idType, builder.getIdType());
        assertEquals(idResolver, builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testTypeSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.DEFAULT_USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);

        JavaType baseType = JavaType.construct(String.class);
        Class<?> defaultImpl = Object.class;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.WRAPPER_OBJECT, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .typeProperty("type")
                .defaultImpl(defaultImpl);

        TypeSerializer serializer = builder.buildTypeSerializer(
                mapper.getSerializationConfig(), baseType, null);
        assertNotNull(serializer);
    }

    @Test
    public void testTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.DEFAULT_USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);

        JavaType baseType = JavaType.construct(String.class);
        Class<?> defaultImpl = Object.class;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.WRAPPER_OBJECT, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .typeProperty("type")
                .defaultImpl(defaultImpl);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(
                mapper.getDeserializationConfig(), baseType, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeSerializerWithUnknownIncludeAs() {
        ObjectMapper mapper = new ObjectMapper();

        JavaType baseType = JavaType.construct(String.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.UNKNOWN_INCLUDE_AS);

        try {
            builder.buildTypeSerializer(
                    mapper.getSerializationConfig(), baseType, null);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type serializer for inclusion type: UNKNOWN_INCLUDE_AS", e.getMessage());
        }
    }

    @Test
    public void testBuildTypeDeserializerWithUnknownIncludeAs() {
        ObjectMapper mapper = new ObjectMapper();

        JavaType baseType = JavaType.construct(String.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.UNKNOWN_INCLUDE_AS);

        try {
            builder.buildTypeDeserializer(
                    mapper.getDeserializationConfig(), baseType, null);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type deserializer for inclusion type: UNKNOWN_INCLUDE_AS", e.getMessage());
        }
    }
}