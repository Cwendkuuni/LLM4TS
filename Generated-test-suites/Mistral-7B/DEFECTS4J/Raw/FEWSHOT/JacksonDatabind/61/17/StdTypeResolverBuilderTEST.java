package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);

        builder.init(JsonTypeInfo.Id.NAME, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testBuildTypeSerializer() {
        MapperConfig config = new MapperConfig();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = null;

        StdTypeResolverBuilder builder = noTypeInfoBuilder();
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.CLASS, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.NAME, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.NONE, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("typeProperty");
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = null;

        StdTypeResolverBuilder builder = noTypeInfoBuilder();
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.CLASS, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.NAME, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.NONE, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("typeProperty");
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.defaultImpl(String.class);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertFalse(builder.isTypeIdVisible());

        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getDefaultImpl());

        builder.defaultImpl(String.class);
        assertNotNull(builder.getDefaultImpl());

        builder.defaultImpl(NoClass.class);
        assertNotNull(builder.getDefaultImpl());
    }
}