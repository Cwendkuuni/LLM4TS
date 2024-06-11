package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder stdTypeResolverBuilder;

    @Before
    public void setup() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(builder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        stdTypeResolverBuilder.init(null, null);
    }

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        assertSame(stdTypeResolverBuilder, builder);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(typeSerializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsPropertyTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsWrapperTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsExternalTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsExistingPropertyTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(typeDeserializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsArrayTypeDeserializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsExternalTypeDeserializer);
    }

    @Test
    public void testInclusionWithNullIncludeAs() {
        try {
            stdTypeResolverBuilder.inclusion(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("includeAs can not be null", e.getMessage());
        }
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertSame(stdTypeResolverBuilder, builder);
    }

    @Test
    public void testTypePropertyWithNullTypeIdPropName() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.typeProperty(null);
        assertSame(stdTypeResolverBuilder, builder);
        assertEquals("defaultTypeName", stdTypeResolverBuilder.getTypeProperty());
    }

    @Test
    public void testTypePropertyWithEmptyTypeIdPropName() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.typeProperty("");
        assertSame(stdTypeResolverBuilder, builder);
        assertEquals("defaultTypeName", stdTypeResolverBuilder.getTypeProperty());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.typeProperty("customTypeName");
        assertSame(stdTypeResolverBuilder, builder);
        assertEquals("customTypeName", stdTypeResolverBuilder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.defaultImpl(String.class);
        assertSame(stdTypeResolverBuilder, builder);
        assertEquals(String.class, stdTypeResolverBuilder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.typeIdVisibility(true);
        assertSame(stdTypeResolverBuilder, builder);
        assertTrue(stdTypeResolverBuilder.isTypeIdVisible());
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithNullIdType() {
        stdTypeResolverBuilder.idResolver(null, null, null, false, false);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, false, false);
        assertNotNull(idResolver);
        assertTrue(idResolver instanceof TypeNameIdResolver);

        builder.init(JsonTypeInfo.Id.CLASS, null);
        idResolver = builder.idResolver(config, baseType, subtypes, false, false);
        assertNotNull(idResolver);
        assertTrue(idResolver instanceof ClassNameIdResolver);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        idResolver = builder.idResolver(config, baseType, subtypes, false, false);
        assertNotNull(idResolver);
        assertTrue(idResolver instanceof MinimalClassNameIdResolver);
    }
}