package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(resolverBuilder._idType);
        assertNotNull(resolverBuilder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_NullIdType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(resolverBuilder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializer_NoTypeId() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(resolverBuilder.buildTypeSerializer(null, null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializer_Exception() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CUSTOM, null);
        resolverBuilder.buildTypeSerializer(null, null, null);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(resolverBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer_NoTypeId() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(resolverBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializer_Exception() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CUSTOM, null);
        resolverBuilder.buildTypeDeserializer(null, null, null);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertNotNull(resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotNull(resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertNotNull(resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertNotNull(resolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusion_NullIncludeAs() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("type");
        assertNotNull(resolverBuilder._typeProperty);
    }

    @Test
    public void testTypeProperty_EmptyString() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("");
        assertNotNull(resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.defaultImpl(Object.class);
        assertNotNull(resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeIdVisibility(true);
        assertTrue(resolverBuilder._typeIdVisible);

        resolverBuilder.typeIdVisibility(false);
        assertFalse(resolverBuilder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        assertNotNull(resolverBuilder.idResolver(null, null, null, true, true));
    }
}