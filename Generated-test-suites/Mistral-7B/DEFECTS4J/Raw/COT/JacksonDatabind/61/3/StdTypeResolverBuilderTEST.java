package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

/**
 * Test cases for StdTypeResolverBuilder class.
 */
public class StdTypeResolverBuilderTest {

    @Test
    public void testStdTypeResolverBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.getDefaultImpl());
        assertEquals("", builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilderInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Object.class;
        String typeProperty = "type";
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(defaultImpl);
        builder.typeProperty(typeProperty);
        builder.typeIdVisibility(true);
        assertEquals(defaultImpl, builder.getDefaultImpl());
        assertEquals(typeProperty, builder.getTypeProperty());
        assertTrue(builder.isTypeIdVisible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStdTypeResolverBuilderInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testStdTypeResolverBuilderNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testBuildTypeSerializerNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerClassIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = Object.class;
        Collection<NamedType> subtypes = null;
        TypeSerializer result = builder.buildTypeSerializer((SerializationConfig) config, baseType, subtypes);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = Object.class;
        Collection<NamedType> subtypes = null;
        builder.buildTypeSerializer((SerializationConfig) config, baseType, subtypes);
        builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        builder.buildTypeSerializer((SerializationConfig) config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeSerializerWrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = Object[].class;
        Collection<NamedType> subtypes = null;
        TypeSerializer result = builder.buildTypeSerializer((SerializationConfig) config, baseType, subtypes);
        assertNotNull(result);
    }

    @Test
    public void testBuildTypeSerializerPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = String.class;
        Collection<NamedType> subtypes = null;
        TypeSerializer result = builder.buildTypeSerializer((SerializationConfig) config, baseType, subtypes);
        assertNotNull(result);
    }

    // Add other test cases for buildTypeSerializer method with different inclusion types

    @Test
    public void testBuildTypeDeserializerNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerClassIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = Object.class;
        Collection<NamedType> subtypes = null;
        TypeDeserializer result = builder.buildTypeDeserializer((DeserializationConfig) config, baseType, subtypes);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerUnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = Object.class;
        Collection<NamedType> subtypes = null;
        builder.buildTypeDeserializer((DeserializationConfig) config, baseType, subtypes);
        builder.include(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        builder.buildTypeDeserializer((DeserializationConfig) config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerWrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = Object[].class;
        Collection<NamedType> subtypes = null;
        TypeDeserializer result = builder.buildTypeDeserializer((DeserializationConfig) config, baseType, subtypes);
        assertNotNull(result);
    }

    @Test
    public void testBuildTypeDeserializerPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Object config = new Object();
        JavaType baseType = String.class;
        Collection<NamedType> subtypes = null;
        TypeDeserializer result = builder.buildTypeDeserializer((DeserializationConfig) config, baseType, subtypes);
        assertNotNull(result);
    }

    // Add other test cases for buildTypeDeserializer method with different inclusion types
}