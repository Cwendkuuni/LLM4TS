package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        // Test initialization of StdTypeResolverBuilder
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test building TypeSerializer based on configurations
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        assertNotNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer() {
        // Test building TypeDeserializer based on configurations
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);

        assertNotNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInclusion() {
        // Test setting inclusion type
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);

        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        // Test setting custom type property name
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("customType");

        assertEquals("customType", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        // Test setting default implementation class
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);

        assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        // Test setting type id visibility
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);

        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        // Test getting type id resolver based on configurations
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        assertNotNull(builder.idResolver(null, null, null, true, false));
    }

}