package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInitNotNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Class<?> defaultImpl = builder._defaultImpl;
        String typeProperty = builder._typeProperty;
        builder.buildTypeSerializer(null, null, null);
        assertNotNull(builder._idType);
        assertNotNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertEquals(defaultImpl, builder._defaultImpl);
        assertEquals(typeProperty, builder._typeProperty);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Class<?> defaultImpl = builder._defaultImpl;
        String typeProperty = builder._typeProperty;
        builder.buildTypeDeserializer(null, null, null);
        assertNotNull(builder._idType);
        assertNotNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertEquals(defaultImpl, builder._defaultImpl);
        assertEquals(typeProperty, builder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertNotNull(builder._includeAs);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertNotNull(builder._typeProperty);
        assertEquals("type", builder._typeProperty);
    }
    
    @Test
    public void testTypePropertyEmpty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("");
        assertNotNull(builder._typeProperty);
        assertEquals("type", builder._typeProperty);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertNotNull(builder._defaultImpl);
        assertEquals(String.class, builder._defaultImpl);
    }
}