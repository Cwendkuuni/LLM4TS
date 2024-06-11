package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        assertEquals(JsonTypeInfo.Id.CLASS, typeResolverBuilder._idType);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(null, null, null);
        assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(null, null, null);
        assertNull(typeDeserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, typeResolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.typeProperty("typeId");
        assertEquals("typeId", typeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.defaultImpl(Object.class);
        assertEquals(Object.class, typeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder typeResolverBuilder = new StdTypeResolverBuilder();
        typeResolverBuilder.typeIdVisibility(true);
        assertTrue(typeResolverBuilder._typeIdVisible);
    }
}