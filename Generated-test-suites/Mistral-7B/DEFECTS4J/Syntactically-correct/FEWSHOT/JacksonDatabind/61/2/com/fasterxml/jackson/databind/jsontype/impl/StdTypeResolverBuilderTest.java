package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testEmptyBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeProperty());
        assertNull(builder.isTypeIdVisible());
        assertNull(builder._customIdResolver);
        assertNull(builder._idType);
        assertNull(builder._includeAs);
    }

    @Test
    public void testInitBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testInitBuilderWithIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {});
        assertNotNull(builder._idType);
        assertNotNull(builder._customIdResolver);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.defaultImpl(String.class);
        serializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.defaultImpl(String.class);
        deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertNull(builder._includeAs);

        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder._includeAs);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.typeProperty("foo");
        assertNotNull(builder._typeProperty);
        assertNull(builder._includeAs);
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.defaultImpl(String.class);
        assertNotNull(builder._defaultImpl);
        assertNull(builder._includeAs);
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeIdVisible);
        assertNotNull(builder._typeProperty);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.typeIdVisibility(true);
        assertNull(builder._typeIdVisible);
        assertNull(builder._includeAs);
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._defaultImpl);

        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.typeIdVisibility(false);
        assertNotNull(builder._typeIdVisible);
        assertNull(builder._includeAs);
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._defaultImpl);
    }
}