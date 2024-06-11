package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class StdTypeResolverBuilderTest {

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        builder.typeIdVisibility(true);
        builder.defaultImpl(Object.class);

        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = config.constructType(Object.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        builder.typeIdVisibility(true);
        builder.defaultImpl(Object.class);

        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(Object.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsArrayTypeDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
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
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(Object.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeIdResolver typeIdResolver = builder.idResolver(config, baseType, subtypes, false, true);
        assertNotNull(typeIdResolver);
    }
}