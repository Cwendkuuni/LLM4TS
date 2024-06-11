package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultConstruction() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testNoTypeInfoBuilderConstruction() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new TypeIdResolver() {
            // Empty implementation as we are not using the idRes in this test
        };
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIdTypeInInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test case for JsonTypeInfo.Id.NONE
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.NONE;
        assertNull(builder.buildTypeSerializer(null, null, null));

        // Test case for other JsonTypeInfo.Id values
        builder = new StdTypeResolverBuilder();
        idType = JsonTypeInfo.Id.CLASS;
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        // Test case for JsonTypeInfo.Id.NONE
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.NONE;
        assertNull(builder.buildTypeDeserializer(null, null, null));

        // Test case for other JsonTypeInfo.Id values
        builder = new StdTypeResolverBuilder();
        idType = JsonTypeInfo.Id.CLASS;
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
        builder.inclusion(includeAs);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIncludeInImclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "typeIdPropName";
        builder.typeProperty(typeProperty);
        assertEquals("typeIdPropName", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Object.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = new MapperConfig<>();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(idRes);
    }
}