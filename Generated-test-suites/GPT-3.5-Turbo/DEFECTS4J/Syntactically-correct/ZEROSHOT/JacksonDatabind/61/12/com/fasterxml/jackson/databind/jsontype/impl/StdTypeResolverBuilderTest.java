package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(result, builder);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("name", builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        TypeIdResolver idRes = new ClassNameIdResolver(null, null);
        builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        TypeSerializer result = builder.buildTypeSerializer(null, null, null);
        assertNotNull(result);
        assertTrue(result instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        TypeIdResolver idRes = new ClassNameIdResolver(null, null);
        builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        TypeDeserializer result = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(result);
        assertTrue(result instanceof AsWrapperTypeDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeProperty("type");
        assertEquals(result, builder);
        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNullOrEmptyValue() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result1 = builder.typeProperty(null);
        assertEquals(result1, builder);
        assertEquals("name", builder._typeProperty);

        StdTypeResolverBuilder result2 = builder.typeProperty("");
        assertEquals(result2, builder);
        assertEquals("name", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.defaultImpl(String.class);
        assertEquals(result, builder);
        assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeIdVisibility(true);
        assertEquals(result, builder);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._defaultImpl = String.class;
        assertEquals(String.class, builder.getDefaultImpl());
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
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        boolean forSer = true;
        boolean forDeser = false;
        TypeIdResolver result = builder.idResolver(config, baseType, subtypes, forSer, forDeser);
        assertNull(result);
    }
}