package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        // Test init method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNotNull(builder._customIdResolver);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        // Test noTypeInfoBuilder method
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test buildTypeSerializer method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        // TODO: Add test case for each switch case
    }

    @Test
    public void testTypeProperty() {
        // Test typeProperty method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "type";
        builder.typeProperty(typeProperty);
        
        assertEquals(typeProperty, builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        // Test defaultImpl method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        
        assertEquals(defaultImpl, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        // Test typeIdVisibility method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true;
        builder.typeIdVisibility(isVisible);
        
        assertEquals(isVisible, builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        // Test getDefaultImpl method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        // Test getTypeProperty method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "type";
        builder.typeProperty(typeProperty);
        
        assertEquals(typeProperty, builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        // Test isTypeIdVisible method
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true;
        builder.typeIdVisibility(isVisible);
        
        assertTrue(builder.isTypeIdVisible());
    }
}