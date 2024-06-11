package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = null;
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.DEFAULT_PROPERTY_NAME, builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testInitWithIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.NAME;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(null, null);
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.DEFAULT_PROPERTY_NAME, builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        builder.init(idType, null);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertNull(serializer);

        idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        serializer = builder.buildTypeSerializer(null, null, null);
        assertNull(serializer);

        idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        serializer = builder.buildTypeSerializer(null, null, null);
        assertNull(serializer);

        idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        serializer = builder.buildTypeSerializer(null, null, null);
        assertNull(serializer);

        idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
        builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        serializer = builder.buildTypeSerializer(null, null, null);
        assertNull(serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        builder.init(idType, null);
        builder.buildTypeSerializer(null, null, null);
    }
}