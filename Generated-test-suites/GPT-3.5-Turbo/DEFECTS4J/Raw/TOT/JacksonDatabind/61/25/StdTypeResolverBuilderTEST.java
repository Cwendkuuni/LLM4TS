package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import java.util.Collection;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        // Test case for init method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);

        // Test when idType is null
        try {
            builder.init(null, null);
            fail("Expected exception IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("idType can not be null", e.getMessage());
        }
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test case for buildTypeSerializer method

        // Test typical use case with PROPERTY includeAs type
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        builder.defaultImpl(String.class);
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(serializer instanceof AsPropertyTypeSerializer);
        AsPropertyTypeSerializer propertySerializer = (AsPropertyTypeSerializer) serializer;
        assertEquals("type", propertySerializer.getTypePropertyName());

        // Test typical use case with WRAPPER_OBJECT includeAs type
        builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        serializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(serializer instanceof AsWrapperTypeSerializer);
        AsWrapperTypeSerializer wrapperSerializer = (AsWrapperTypeSerializer) serializer;

        // Test when idType is NONE
        builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        serializer = builder.buildTypeSerializer(null, null, null);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        // Test case for buildTypeDeserializer method

        // Test typical use case with WRAPPER_ARRAY includeAs type
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeIdVisibility(true);
        builder.defaultImpl(Integer.class);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(deserializer instanceof AsArrayTypeDeserializer);
        AsArrayTypeDeserializer arrayDeserializer = (AsArrayTypeDeserializer) deserializer;
        assertTrue(arrayDeserializer.getIdVisible());

        // Test typical use case with PROPERTY includeAs type
        builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.defaultImpl(Double.class);
        deserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
        AsPropertyTypeDeserializer propertyDeserializer = (AsPropertyTypeDeserializer) deserializer;
        assertEquals(JsonTypeInfo.As.PROPERTY, propertyDeserializer.getTypeInclusion().getContentInclusion());

        // Test when idType is NONE
        builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        deserializer = builder.buildTypeDeserializer(null, null, null);
        assertNull(deserializer);
    }

    @Test
    public void testInclusion() {
        // Test case for inclusion method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        StdTypeResolverBuilder result = builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertSame(builder, result);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder._includeAs);

        // Test when includeAs is null
        try {
            builder.inclusion(null);
            fail("Expected exception IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("includeAs can not be null", e.getMessage());
        }
    }

    @Test
    public void testTypeProperty() {
        // Test case for typeProperty method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        StdTypeResolverBuilder result = builder.typeProperty("type");
        assertSame(builder, result);
        assertEquals("type", builder._typeProperty);

        // Test when typeIdPropName is null
        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
        
        // Test empty typeIdPropName
        builder.typeProperty("");
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        // Test case for defaultImpl method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        StdTypeResolverBuilder result = builder.defaultImpl(Integer.class);
        assertSame(builder, result);
        assertEquals(Integer.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        // Test case for typeIdVisibility method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        StdTypeResolverBuilder result = builder.typeIdVisibility(true);
        assertSame(builder, result);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        // Test case for getDefaultImpl method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(Double.class);
        Class<?> defaultImpl = builder.getDefaultImpl();
        assertEquals(Double.class, defaultImpl);
    }

    @Test
    public void testGetTypeProperty() {
        // Test case for getTypeProperty method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.typeProperty("class");
        String typeProperty = builder.getTypeProperty();
        assertEquals("class", typeProperty);
    }

    @Test
    public void testIsTypeIdVisible() {
        // Test case for isTypeIdVisible method

        // Test typical use case
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}