package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        TypeIdResolver idRes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(null, idRes);
    }

    @Test
    public void testBuildTypeSerializerNullIdType() {
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(null, builder.buildTypeSerializer(config, baseType, subtypes));
    }

    @Test
    public void testBuildTypeSerializerClassInclusion() {
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = JavaType.construct(Object.class);
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownInclusionType() {
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = JavaType.construct(Object.class);
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerNullIdType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(null, builder.buildTypeDeserializer(config, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializerClassInclusion() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = JavaType.construct(Object.class);
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerUnknownInclusionType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = JavaType.construct(Object.class);
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typeIdPropName");
        assertEquals("typeIdPropName", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImplNullClass() {
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
    public void testTypeIdVisibilityFalse() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testTypeIdVisibilityTrue() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}