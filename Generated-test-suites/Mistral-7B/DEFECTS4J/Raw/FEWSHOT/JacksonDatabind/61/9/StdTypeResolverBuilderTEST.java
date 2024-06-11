package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testInitWithCustomIdResolver() {
        TypeIdResolver customIdResolver = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, customIdResolver);
        assertNotNull(builder._idType);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertEquals(customIdResolver, builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        new StdTypeResolverBuilder().init(null, null);
    }

    @Test
    public void testBuildTypeSerializerNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerWithClass() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), JavaType.of(String.class), null);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), JavaType.of(String[].class), null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.PROPERTY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), JavaType.of(String.class), null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), JavaType.of(Map.class), null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), JavaType.of(String.class), null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithExistingProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), JavaType.of(String.class), null);
        assertNotNull(serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
        builder.buildTypeSerializer(new MapperConfig(), JavaType.of(String.class), null);
    }

    @Test
    public void testBuildTypeDeserializerNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithClass() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.of(String.class), null);
        assertNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.of(String[].class), null);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.PROPERTY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.of(String.class), null);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.of(Map.class), null);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.of(String.class), null);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithExistingProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.of(String.class), null);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerUnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
        builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.of(String.class), null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("myCustomTypeProperty");
        assertEquals("myCustomTypeProperty", builder.getTypeProperty());
    }

    @Test
    public void testTypePropertyNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);
        assertEquals("", builder.getTypeProperty());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testTypeIdVisibilityFalse() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String[].class);
        assertEquals(String[].class, builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(null);
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplClass() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplInterface() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Iterator.class);
        assertEquals(Iterator.class, builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplVoid() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Void.class);
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplNoClass() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(NoClass.class);
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeNull() {
        new StdTypeResolverBuilder().inclusion(null);
    }
}