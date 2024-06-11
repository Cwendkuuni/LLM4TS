package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

public class StdTypeResolverBuilderTest {
    
    private StdTypeResolverBuilder typeResolverBuilder;
    
    @Before
    public void setup() {
        typeResolverBuilder = new StdTypeResolverBuilder();
    }
    
    @Test
    public void testInit() {
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        
        assertNotNull(result);
        assertEquals(JsonTypeInfo.Id.CLASS, typeResolverBuilder._idType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        typeResolverBuilder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = config.constructType(Collection.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        
        TypeIdResolver idResolver = typeResolverBuilder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(idResolver);
        
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, idResolver);
        assertNotNull(result);
        
        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(typeSerializer);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(Collection.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();
        
        TypeIdResolver idResolver = typeResolverBuilder.idResolver(config, baseType, subtypes, false, true);
        assertNotNull(idResolver);
        
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.init(JsonTypeInfo.Id.CLASS, idResolver);
        assertNotNull(result);
        
        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(typeDeserializer);
    }
    
    @Test
    public void testInclusion() {
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        
        assertNotNull(result);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, typeResolverBuilder._includeAs);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        typeResolverBuilder.inclusion(null);
    }
    
    @Test
    public void testTypeProperty() {
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.typeProperty("type");
        
        assertNotNull(result);
        assertEquals("type", typeResolverBuilder._typeProperty);
    }
    
    @Test
    public void testTypePropertyWithNull() {
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.typeProperty(null);
        
        assertNotNull(result);
        assertNotNull(typeResolverBuilder._typeProperty);
        assertEquals(typeResolverBuilder._idType.getDefaultPropertyName(), typeResolverBuilder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.defaultImpl(List.class);
        
        assertNotNull(result);
        assertEquals(List.class, typeResolverBuilder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        TypeResolverBuilder<StdTypeResolverBuilder> result = typeResolverBuilder.typeIdVisibility(true);
        
        assertNotNull(result);
        assertTrue(typeResolverBuilder._typeIdVisible);
    }
    
    @Test
    public void testGetDefaultImpl() {
        typeResolverBuilder.defaultImpl(List.class);
        
        Class<?> result = typeResolverBuilder.getDefaultImpl();
        
        assertNotNull(result);
        assertEquals(List.class, result);
    }
    
    @Test
    public void testGetTypeProperty() {
        typeResolverBuilder.typeProperty("type");
        
        String result = typeResolverBuilder.getTypeProperty();
        
        assertNotNull(result);
        assertEquals("type", result);
    }
    
    @Test
    public void testIsTypeIdVisible() {
        typeResolverBuilder.typeIdVisibility(true);
        
        boolean result = typeResolverBuilder.isTypeIdVisible();
        
        assertTrue(result);
    }
}