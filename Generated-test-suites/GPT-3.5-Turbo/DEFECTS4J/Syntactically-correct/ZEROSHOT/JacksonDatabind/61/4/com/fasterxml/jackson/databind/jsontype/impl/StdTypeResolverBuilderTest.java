package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(builder);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
    }
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(serializer instanceof AsWrapperTypeSerializer);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, builder._includeAs);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
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
    public void testTypePropertyWithEmptyString() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("");
        assertNull(builder._typeProperty);
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
        builder.defaultImpl(Integer.class);
        assertEquals(Integer.class, builder.getDefaultImpl());
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
        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }
}