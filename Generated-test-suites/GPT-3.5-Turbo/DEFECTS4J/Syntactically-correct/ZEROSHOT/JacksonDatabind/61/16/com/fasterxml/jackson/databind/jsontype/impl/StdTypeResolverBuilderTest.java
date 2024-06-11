package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Assert;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            Assert.fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("idType can not be null", e.getMessage());
        }
        builder.init(JsonTypeInfo.Id.NAME, null);
        Assert.assertNotNull(builder._idType);
        Assert.assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        Assert.assertNull(builder._customIdResolver);
        Assert.assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NONE;
        Assert.assertNull(builder.buildTypeSerializer(null, null, null));

        // TODO: Test other cases
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NONE;
        Assert.assertNull(builder.buildTypeDeserializer(null, null, null));

        // TODO: Test other cases
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.inclusion(null);
            Assert.fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("includeAs can not be null", e.getMessage());
        }

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        Assert.assertNotNull(builder._includeAs);
        Assert.assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Assert.assertEquals("class", builder._typeProperty);

        builder.typeProperty(null);
        Assert.assertEquals("class", builder._typeProperty);

        builder.typeProperty("");
        Assert.assertEquals("class", builder._typeProperty);

        builder.typeProperty("type");
        Assert.assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Assert.assertNull(builder._defaultImpl);

        builder.defaultImpl(String.class);
        Assert.assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Assert.assertFalse(builder._typeIdVisible);

        builder.typeIdVisibility(true);
        Assert.assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Assert.assertNull(builder.getDefaultImpl());

        builder.defaultImpl(String.class);
        Assert.assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Assert.assertEquals("class", builder.getTypeProperty());

        builder.typeProperty("type");
        Assert.assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Assert.assertFalse(builder.isTypeIdVisible());

        builder.typeIdVisibility(true);
        Assert.assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        boolean forSer = false;
        boolean forDeser = false;

        try {
            builder.idResolver(config, baseType, subtypes, forSer, forDeser);
            Assert.fail("Exception not thrown");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Can not build, 'init()' not yet called", e.getMessage());
        }

        // TODO: Test other cases
    }
}