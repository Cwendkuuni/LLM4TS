package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.AsExistingPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }
    
    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(serializer instanceof AsArrayTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NONE;
        builder._includeAs = JsonTypeInfo.As.PROPERTY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);
    }
    
    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = null;
        
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerWithDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.PROPERTY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Integer.class;
        
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        StdTypeResolverBuilder result = builder.inclusion(includeAs);
        assertSame(builder, result);
        assertSame(includeAs, builder._includeAs);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }
    
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeIdPropName = "type";
        StdTypeResolverBuilder result = builder.typeProperty(typeIdPropName);
        assertSame(builder, result);
        assertSame(typeIdPropName, builder._typeProperty);
    }
    
    @Test
    public void testTypePropertyWithEmptyString() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeIdPropName = "";
        StdTypeResolverBuilder result = builder.typeProperty(typeIdPropName);
        assertSame(builder, result);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }
    
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Integer.class;
        StdTypeResolverBuilder result = builder.defaultImpl(defaultImpl);
        assertSame(builder, result);
        assertSame(defaultImpl, builder._defaultImpl);
    }
    
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true;
        StdTypeResolverBuilder result = builder.typeIdVisibility(isVisible);
        assertSame(builder, result);
        assertSame(isVisible, builder._typeIdVisible);
    }
    
    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        BuilderMock config = new BuilderMock();
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        boolean forSer = false;
        boolean forDeser = false;
        
        TypeIdResolver resolver = builder.idResolver(config, baseType, subtypes, forSer, forDeser);
        assertTrue(resolver instanceof TypeNameIdResolver);
    }
    
    class BuilderMock extends SerializationConfig {
        
    }
}