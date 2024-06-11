package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setUp() {
        builder = new StdTypeResolverBuilder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        builder.init(null, null);
    }

    @Test
    public void testInit() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNull() {
        builder.inclusion(null);
    }

    @Test
    public void testInclusion() {
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotNull(builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testTypePropertyDefault() {
        builder.typeProperty(null);
        assertNotNull(builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testBuildTypeSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.constructType(String.class);
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeIdResolver idResolver = resolverBuilder.idResolver(mapper.getDeserializationConfig(), baseType, subtypes, true, true);
        assertNotNull(resolverBuilder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.constructType(String.class);
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeIdResolver idResolver = resolverBuilder.idResolver(mapper.getDeserializationConfig(), baseType, subtypes, false, true);
        assertNotNull(resolverBuilder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes));
    }
}