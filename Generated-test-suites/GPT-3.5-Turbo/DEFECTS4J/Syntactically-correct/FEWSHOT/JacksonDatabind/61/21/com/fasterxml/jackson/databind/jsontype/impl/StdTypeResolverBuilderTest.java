package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        
        builder.init(JsonTypeInfo.Id.CLASS, null);
        
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
        
        builder.init(JsonTypeInfo.Id.NAME, null);
        
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);
        
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        
        assertEquals(JsonTypeInfo.Id.CUSTOM, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CUSTOM.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsArrayTypeSerializer);
        
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsPropertyTypeSerializer);
        
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsWrapperTypeSerializer);
        
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsExternalTypeSerializer);
        
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsExistingPropertyTypeSerializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
        
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        builder.buildTypeSerializer(config, baseType, subtypes);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsArrayTypeDeserializer);
        
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
        
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
        
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsExternalTypeDeserializer);
        
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithInvalidInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
        
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        builder.buildTypeDeserializer(config, baseType, subtypes);
    }
    
    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeIdResolver resolver = builder.idResolver(config, baseType, subtypes, true, false);
        
        assertTrue(resolver instanceof ClassNameIdResolver);
        
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        
        assertTrue(resolver instanceof MinimalClassNameIdResolver);
        
        builder.init(JsonTypeInfo.Id.NAME, null);
        
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        
        assertTrue(resolver instanceof TypeNameIdResolver);
        
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        
        assertTrue(resolver == builder._customIdResolver);
    }
    
}