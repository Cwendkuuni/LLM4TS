package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNotNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test for JsonTypeInfo.Id.NONE
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        
        assertNull(builder.buildTypeSerializer(config, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializer() {
        // Test for JsonTypeInfo.Id.NONE
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        
        assertNull(builder.buildTypeDeserializer(config, baseType, subtypes));
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        
        assertEquals("type", builder._typeProperty);
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

}