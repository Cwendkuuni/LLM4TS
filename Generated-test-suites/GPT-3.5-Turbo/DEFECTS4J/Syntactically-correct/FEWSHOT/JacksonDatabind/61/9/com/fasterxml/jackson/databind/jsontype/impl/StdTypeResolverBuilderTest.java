package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class StdTypeResolverBuilderTest {
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NONE;
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertNull(typeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeAsType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NAME;
        builder._includeAs = null;
        builder.buildTypeSerializer(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(ArrayList.class);
        assertEquals(ArrayList.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._typeProperty = "type";
        assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._typeIdVisible = true;
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, true, true);
        assertNull(idResolver);
    }
}