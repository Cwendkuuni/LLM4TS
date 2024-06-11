package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.init(null, null);
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);
        
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("name", builder._typeProperty);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
        
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
        
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInclusion_Null() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.inclusion(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTypeProperty_Null() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.typeProperty(null);
    }
    
    @Test
    public void testTypeProperty_Empty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.typeProperty("");
        assertEquals("class", builder._typeProperty);
    }
    
    @Test
    public void testTypeProperty_Valid() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.defaultImpl(null);
        assertNull(builder._defaultImpl);
        
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.typeIdVisibility(true);
        assertEquals(true, builder._typeIdVisible);
        
        builder.typeIdVisibility(false);
        assertEquals(false, builder._typeIdVisible);
    }

}