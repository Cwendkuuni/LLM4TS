package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        assertEquals(idType, builder.getIdType());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getCustomIdResolver());

        idType = JsonTypeInfo.Id.NAME;
        builder.init(idType, null);
        assertEquals(idType, builder.getIdType());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getCustomIdResolver());

        TypeIdResolver idResolver = new TypeIdResolver() {};
        builder.init(idType, idResolver);
        assertEquals(idType, builder.getIdType());
        assertSame(idResolver, builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
    }

    @Test
    public void testBuildTypeSerializer() {
        MapperConfig<?> config = new MapperConfig<>();
        JavaType baseType = config.getTypeFactory().constructType(int.class);
        Collection<NamedType> subtypes = Collections.singletonList(new NamedType("Test", String.class));

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.NONE, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = config.getTypeFactory().constructType(int.class);
        Collection<NamedType> subtypes = Collections.singletonList(new NamedType("Test", String.class));

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.NONE, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.getIdType());
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("testProperty");
        assertEquals("testProperty", builder.getTypeProperty());
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.getTypeProperty());
        builder.typeProperty("testProperty");
        assertEquals("testProperty", builder.getTypeProperty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder.getDefaultImpl());
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.getDefaultImpl());
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder.getDefaultImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(null);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertFalse(builder.isTypeIdVisible());
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(null);
    }
}