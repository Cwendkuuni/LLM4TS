package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        Assert.assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        Assert.assertNull(builder._customIdResolver);
        Assert.assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        Assert.assertNull(typeSerializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        typeSerializer = builder.buildTypeSerializer(null, null, null);
        Assert.assertNotNull(typeSerializer);
        Assert.assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        Assert.assertNull(typeDeserializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        Assert.assertNotNull(typeDeserializer);
        Assert.assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typeName");
        Assert.assertEquals("typeName", builder._typeProperty);

        builder.typeProperty("");
        Assert.assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);

        builder.typeProperty(null);
        Assert.assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(this.getClass());
        Assert.assertEquals(this.getClass(), builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(this.getClass());
        Assert.assertEquals(this.getClass(), builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typeName");
        Assert.assertEquals("typeName", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeIdResolver typeIdResolver = builder.idResolver(config, baseType, subtypes, false, true);
        Assert.assertNull(typeIdResolver);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        typeIdResolver = builder.idResolver(config, baseType, subtypes, false, true);
        Assert.assertNotNull(typeIdResolver);
        Assert.assertTrue(typeIdResolver instanceof MinimalClassNameIdResolver);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithInvalidIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        builder.idResolver(null, null, null, false, false);
    }
}