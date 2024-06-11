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
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertNotNull(serializer);
        assertNull(serializer.getTypeIdResolver());
    }

    @Test
    public void testTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertNotNull(deserializer);
        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.typeProperty("myTypeProperty");
        assertEquals("myTypeProperty", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }
}