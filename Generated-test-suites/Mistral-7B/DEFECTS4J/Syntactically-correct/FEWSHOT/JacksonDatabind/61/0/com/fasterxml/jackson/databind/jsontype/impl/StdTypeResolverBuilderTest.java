package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(null, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testBuildTypeSerializerWithNullBaseTypeAndSubtypes() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        assertEquals(null, builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerWithValidBaseTypeAndSubtypesAndPropertyInclusion() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(null, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeSerializer result = builder.buildTypeSerializer(null, baseType, subtypes);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithValidBaseTypeAndSubtypesAndWrongInclusion() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(null, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        builder.buildTypeSerializer(null, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerWithNullBaseTypeAndSubtypes() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        assertEquals(null, builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithValidBaseTypeAndSubtypesAndPropertyInclusion() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(null, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeDeserializer result = builder.buildTypeDeserializer(null, baseType, subtypes);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithValidBaseTypeAndSubtypesAndWrongInclusion() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(null, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        builder.buildTypeDeserializer(null, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        builder.inclusion(includeAs);
        assertEquals(includeAs, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
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
    public void testDefaultImplNull() {
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
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeIdPropName = "typeId";
        builder.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}