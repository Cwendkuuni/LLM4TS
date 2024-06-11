package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("$class", builder._typeProperty);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("$type");
        
        List<NamedType> subtypes = new ArrayList<>();
        subtypes.add(new NamedType(String.class, "string"));
        subtypes.add(new NamedType(Integer.class, "integer"));
        
        TypeSerializer serializer = builder.buildTypeSerializer(null, null, subtypes);
        assertEquals(AsArrayTypeSerializer.class, serializer.getClass());
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.typeProperty("@class");
        
        List<NamedType> subtypes = new ArrayList<>();
        subtypes.add(new NamedType(String.class, "string"));
        subtypes.add(new NamedType(Integer.class, "integer"));
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, subtypes);
        assertEquals(AsPropertyTypeDeserializer.class, deserializer.getClass());
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.defaultImpl(CharSequence.class);
        assertEquals(CharSequence.class, builder._defaultImpl);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.typeIdVisibility(true);
        assertEquals(true, builder._typeIdVisible);
    }
}