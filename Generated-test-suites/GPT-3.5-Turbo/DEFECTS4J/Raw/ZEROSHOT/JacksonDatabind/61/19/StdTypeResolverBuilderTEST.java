package com.fasterxml.jackson.databind.jsontype.impl;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(typeSerializer instanceof AsPropertyTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(typeSerializer instanceof AsWrapperTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(typeSerializer instanceof AsExternalTypeSerializer);

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertTrue(typeSerializer instanceof AsExistingPropertyTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(typeDeserializer instanceof AsArrayTypeDeserializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertTrue(typeDeserializer instanceof AsExternalTypeDeserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);

        try {
            builder.inclusion(null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);

        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);
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
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = builder.getDefaultImpl();
        assertNull(defaultImpl);

        builder.defaultImpl(String.class);
        defaultImpl = builder.getDefaultImpl();
        assertEquals(String.class, defaultImpl);
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = builder.getTypeProperty();
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), typeProperty);

        builder.typeProperty("type");
        typeProperty = builder.getTypeProperty();
        assertEquals("type", typeProperty);
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertFalse(builder.isTypeIdVisible());

        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}