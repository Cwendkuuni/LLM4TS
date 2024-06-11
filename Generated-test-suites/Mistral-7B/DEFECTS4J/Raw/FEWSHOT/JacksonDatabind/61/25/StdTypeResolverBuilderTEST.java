package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = null;
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
        assertFalse(builder._typeIdVisible);
    }

    @Test
    public void testBuildTypeSerializer_withNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
       assertEquals(null, builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializer_withEmptySubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertEquals(null, builder.buildTypeSerializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeSerializer_withTypeIdNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertEquals(null, builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializer_withPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, baseType, subtypes);
        assertNotNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_withWrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.WRAPPER_ARRAY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeSerializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeSerializer_withWrapperObjectInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.WRAPPER_OBJECT, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeSerializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeSerializer_withExternalPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.EXTERNAL_PROPERTY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeSerializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeSerializer_withExistingPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.EXISTING_PROPERTY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeSerializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer_withNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(null, builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer_withEmptySubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertEquals(null, builder.buildTypeDeserializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer_withTypeIdNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertEquals(null, builder.buildTypeDeserializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer_withPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.PROPERTY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, baseType, subtypes);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_withWrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.WRAPPER_ARRAY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeDeserializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer_withWrapperObjectInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.WRAPPER_OBJECT, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeDeserializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer_withExternalPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.EXTERNAL_PROPERTY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeDeserializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer_withExistingPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.As.EXISTING_PROPERTY, null);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        assertNotNull(builder.buildTypeDeserializer(null, baseType, subtypes));
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeIdPropName = "typeId";
        builder.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultImplWithNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(null);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testTypeIdVisibilityFalse() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(false);
        assertFalse(builder._typeIdVisible);
    }
}