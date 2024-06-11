package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        // Test with valid idType and idResolver
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNotNull(builder._customIdResolver);

        // Test with null idType
        try {
            builder.init(null, new ClassNameIdResolver());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("idType can not be null", e.getMessage());
        }
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test with idType.NONE
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.buildTypeSerializer(null, null, null));

        // Test with idType.PROPERTY and includeAs.PROPERTY
        builder.init(JsonTypeInfo.Id.PROPERTY, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        // TODO: Add test case for buildTypeSerializer
    }

    @Test
    public void testBuildTypeDeserializer() {
        // Test with idType.NONE
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.buildTypeDeserializer(null, null, null));

        // Test with idType.PROPERTY and includeAs.PROPERTY
        builder.init(JsonTypeInfo.Id.PROPERTY, new ClassNameIdResolver());
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        // TODO: Add test case for buildTypeDeserializer
    }

    @Test
    public void testInclusion() {
        // Test with valid includeAs
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);

        // Test with null includeAs
        try {
            builder.inclusion(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("includeAs can not be null", e.getMessage());
        }
    }

    @Test
    public void testTypeProperty() {
        // Test with valid typeIdPropName
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("clazz");
        assertEquals("clazz", builder._typeProperty);

        // Test with null typeIdPropName
        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
        // TODO: Add test case for typeProperty with empty typeIdPropName
    }

    @Test
    public void testDefaultImpl() {
        // Test with valid defaultImpl
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);

        // Test with null defaultImpl
        builder.defaultImpl(null);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        // Test with true isVisible
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);

        // Test with false isVisible
        builder.typeIdVisibility(false);
        assertFalse(builder._typeIdVisible);
    }
}