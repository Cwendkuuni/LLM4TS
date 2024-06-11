package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertNull(resolverBuilder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        assertNull(resolverBuilder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, resolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("type");
        assertEquals("type", resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.defaultImpl(String.class);
        assertEquals(String.class, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeIdVisibility(true);
        assertTrue(resolverBuilder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        assertNotNull(resolverBuilder.idResolver(null, null, null, true, true));
    }
}