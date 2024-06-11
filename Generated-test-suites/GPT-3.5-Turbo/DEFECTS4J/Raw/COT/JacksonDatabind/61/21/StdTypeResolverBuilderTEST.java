package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.Assert;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class StdTypeResolverBuilderTest {
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver resolver = new ClassNameIdResolver(null, null);
        StdTypeResolverBuilder result = builder.init(JsonTypeInfo.Id.CLASS, resolver);
        Assert.assertEquals(result, builder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver resolver = new ClassNameIdResolver(null, null);
        builder.init(null, resolver);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeSerializer serializer = builder.buildTypeSerializer(null, baseType, subtypes);
        Assert.assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, baseType, subtypes);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        Assert.assertEquals(result, builder);
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
        Assert.assertEquals(result, builder);
    }

    @Test
    public void testTypePropertyWithEmptyString() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeProperty("");
        Assert.assertEquals(result, builder);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.defaultImpl(String.class);
        Assert.assertEquals(result, builder);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeIdVisibility(true);
        Assert.assertEquals(result, builder);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        Assert.assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        Assert.assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder.isTypeIdVisible());
    }
}