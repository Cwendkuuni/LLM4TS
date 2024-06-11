package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), 
            builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.ARRAY);
        builder.typeProperty("type");
        TypeSerializer serializer = builder.buildTypeSerializer(
            null, null, null);
        assertTrue(serializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        TypeSerializer serializer = builder.buildTypeSerializer(
            null, null, null);
        assertTrue(serializer instanceof AsPropertyTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.typeProperty("type");
        TypeSerializer serializer = builder.buildTypeSerializer(
            null, null, null);
        assertTrue(serializer instanceof AsWrapperTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        builder.typeProperty("type");
        TypeSerializer serializer = builder.buildTypeSerializer(
            null, null, null);
        assertTrue(serializer instanceof AsExternalTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithExistingProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.typeProperty("type");
        TypeSerializer serializer = builder.buildTypeSerializer(
            null, null, null);
        assertTrue(serializer instanceof AsExistingPropertyTypeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(null);
        builder.buildTypeSerializer(null, null, null);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.ARRAY);
        builder.typeProperty("type");
        TypeDeserializer deserializer = builder.buildTypeDeserializer(
            null, null, null);
        assertTrue(deserializer instanceof AsArrayTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        TypeDeserializer deserializer = builder.buildTypeDeserializer(
            null, null, null);
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.typeProperty("type");
        TypeDeserializer deserializer = builder.buildTypeDeserializer(
            null, null, null);
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        builder.typeProperty("type");
        TypeDeserializer deserializer = builder.buildTypeDeserializer(
            null, null, null);
        assertTrue(deserializer instanceof AsExternalTypeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithInvalidInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(null);
        builder.buildTypeDeserializer(null, null, null);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), 
            builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(int.class);
        assertEquals(int.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(float.class);
        assertEquals(float.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        MapperConfig config = null;
        TypeIdResolver resolver = builder.idResolver(config, baseType, 
            subtypes, true, false);
        assertNull(resolver);
    }

    @Test
    public void testIdResolverWithNameIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        MapperConfig config = null;
        builder.init(JsonTypeInfo.Id.NAME, null);
        TypeIdResolver resolver = builder.idResolver(config, baseType, 
            subtypes, true, false);
        assertTrue(resolver instanceof TypeNameIdResolver);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithNameIdTypeAndNoMatchingCase() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        MapperConfig config = null;
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.idResolver(config, baseType, subtypes, true, false);
    }

    @Test
    public void testIdResolverWithClassIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        MapperConfig config = null;
        builder.init(JsonTypeInfo.Id.CLASS, null);
        TypeIdResolver resolver = builder.idResolver(config, baseType, 
            subtypes, true, false);
        assertTrue(resolver instanceof ClassNameIdResolver);
    }

    @Test
    public void testIdResolverWithMinimalClassIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        MapperConfig config = null;
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        TypeIdResolver resolver = builder.idResolver(config, baseType, 
            subtypes, true, false);
        assertTrue(resolver instanceof MinimalClassNameIdResolver);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithCustomIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        MapperConfig config = null;
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        builder.idResolver(config, baseType, subtypes, true, false);
    }

}