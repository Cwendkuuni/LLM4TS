package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        builder.typeIdVisibility(true);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
        AsArrayTypeSerializer serializer = (AsArrayTypeSerializer) typeSerializer;
        assertNull(serializer.getPropertyName());
        assertTrue(serializer.isTypeIdVisible());
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        builder.typeIdVisibility(true);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);
        AsPropertyTypeDeserializer deserializer = (AsPropertyTypeDeserializer) typeDeserializer;
        assertEquals("type", deserializer.getPropertyName());
        assertTrue(deserializer.isTypeIdVisible());
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("myType");
        assertEquals("myType", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.idResolver(null, null, null, true, true));
    }
}