package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        // Test initialization of StdTypeResolverBuilder
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        Assert.assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        Assert.assertNull(builder._customIdResolver);
        Assert.assertEquals("class", builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        // Test initialization with null idType
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        // Test buildTypeSerializer with idType NONE
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        TypeSerializer serializer = builder.buildTypeSerializer(null,null,null);

        Assert.assertNull(serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidIncludeAs() {
        // Test buildTypeSerializer with invalid includeAs
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.buildTypeSerializer(null,null,null);
    }

    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        // Test buildTypeDeserializer with idType NONE
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null,null,null);

        Assert.assertNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithInvalidIncludeAs() {
        // Test buildTypeDeserializer with invalid includeAs
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.buildTypeDeserializer(null,null,null);
    }

    @Test
    public void testInclusion() {
        // Test inclusion method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);

        Assert.assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        // Test inclusion method with null includeAs
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        // Test typeProperty method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typeId");

        Assert.assertEquals("typeId", builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNullTypeIdPropName() {
        // Test typeProperty method with null typeIdPropName
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);

        Assert.assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        // Test defaultImpl method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);

        Assert.assertEquals(Object.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        // Test typeIdVisibility method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);

        Assert.assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        // Test getDefaultImpl method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);

        Assert.assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        // Test getTypeProperty method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");

        Assert.assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        // Test isTypeIdVisible method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(false);

        Assert.assertFalse(builder.isTypeIdVisible());
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithNullIdType() {
        // Test idResolver with null idType
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.idResolver(null,null,null,false,false);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithInvalidIdType() {
        // Test idResolver with invalid idType
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.idResolver(null,null,null,false,false);
    }

    @Test
    public void testIdResolverWithClassIdType() {
        // Test idResolver with idType CLASS
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        TypeIdResolver resolver = builder.idResolver(null,null,null,true,false);

        Assert.assertEquals(ClassNameIdResolver.class, resolver.getClass());
    }

    @Test
    public void testIdResolverWithMinimalClassIdType() {
        // Test idResolver with idType MINIMAL_CLASS
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        TypeIdResolver resolver = builder.idResolver(null,null,null,true,false);

        Assert.assertEquals(MinimalClassNameIdResolver.class, resolver.getClass());
    }

    @Test
    public void testIdResolverWithNameIdType() {
        // Test idResolver with idType NAME
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        TypeIdResolver resolver = builder.idResolver(null,null,null,true,false);

        Assert.assertEquals(TypeNameIdResolver.class, resolver.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithInvalidIdTypeForSerializer() {
        // Test idResolver with invalid idType for serializer
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null); // idType NONE is not allowed
        builder.idResolver(null,null,null,true,false);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithInvalidIdTypeForDeserializer() {
        // Test idResolver with invalid idType for deserializer
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null); // idType NONE is not allowed
        builder.idResolver(null,null,null,false,true);
    }

}