
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    // Test init method
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);
    }

    // Test buildTypeSerializer method with Id.NONE
    @Test
    public void testBuildTypeSerializerWithIdNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    // Test buildTypeSerializer method with Id.WRAPPER_ARRAY
    @Test
    public void testBuildTypeSerializerWithIdWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsArrayTypeSerializer);
    }

    // Test buildTypeSerializer method with Id.PROPERTY
    @Test
    public void testBuildTypeSerializerWithIdProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsPropertyTypeSerializer);
    }

    // Test buildTypeSerializer method with Id.WRAPPER_OBJECT
    @Test
    public void testBuildTypeSerializerWithIdWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsWrapperTypeSerializer);
    }

    // Test buildTypeSerializer method with Id.EXTERNAL_PROPERTY
    @Test
    public void testBuildTypeSerializerWithIdExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsExternalTypeSerializer);
    }

    // Test buildTypeSerializer method with Id.EXISTING_PROPERTY
    @Test
    public void testBuildTypeSerializerWithIdExistingProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        assertNotNull(builder.buildTypeSerializer(null, null, null));
        assertTrue(builder.buildTypeSerializer(null, null, null) instanceof AsExistingPropertyTypeSerializer);
    }

    // Test buildTypeDeserializer method with Id.NONE
    @Test
    public void testBuildTypeDeserializerWithIdNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    // Test buildTypeDeserializer method with Id.WRAPPER_ARRAY
    @Test
    public void testBuildTypeDeserializerWithIdWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsArrayTypeDeserializer);
    }

    // Test buildTypeDeserializer method with Id.PROPERTY
    @Test
    public void testBuildTypeDeserializerWithIdProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsPropertyTypeDeserializer);
    }

    // Test buildTypeDeserializer method with Id.WRAPPER_OBJECT
    @Test
    public void testBuildTypeDeserializerWithIdWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsWrapperTypeDeserializer);
    }

    // Test buildTypeDeserializer method with Id.EXTERNAL_PROPERTY
    @Test
    public void testBuildTypeDeserializerWithIdExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
        assertTrue(builder.buildTypeDeserializer(null, null, null) instanceof AsExternalTypeDeserializer);
    }

    // Test inclusion method
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    // Test typeProperty method
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("myTypeProperty");
        assertEquals("myTypeProperty", builder._typeProperty);
    }

    // Test defaultImpl method
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder._defaultImpl);
    }

    // Test typeIdVisibility method
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    // Test getDefaultImpl method
    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    // Test getTypeProperty method
    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("myTypeProperty");
        assertEquals("myTypeProperty", builder.getTypeProperty());
    }

    // Test isTypeIdVisible method
    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}
