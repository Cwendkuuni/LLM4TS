package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class StdTypeResolverBuilderTest {
    
    private StdTypeResolverBuilder builder;

    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.class, new MapperConfig());
        
        StdTypeResolverBuilder result = builder.init(idType, idRes);
        
        assertEquals(idType, result._idType);
        assertEquals(idRes, result._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), result._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = Collections.singletonList(new NamedType());
        
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("typeIdPropName");
        builder.defaultImpl(BasicType.class);
        builder.typeIdVisibility(true);
        
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = Collections.singletonList(new NamedType());
        
        builder.init(JsonTypeInfo.Id.NONE, null);
        
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = Collections.singletonList(new NamedType());
        
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.typeProperty("typeIdPropName");
        builder.defaultImpl(BasicType.class);
        builder.typeIdVisibility(true);
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = Collections.singletonList(new NamedType());
        
        builder.init(JsonTypeInfo.Id.NONE, null);
        
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testInclusionWithNullIncludeAs() {
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        String typeIdPropName = "typeIdPropName";
        
        StdTypeResolverBuilder result = builder.typeProperty(typeIdPropName);
        
        assertEquals(typeIdPropName, result._typeProperty);
    }

    @Test
    public void testTypePropertyWithNull() {
        StdTypeResolverBuilder result = builder.typeProperty(null);
        
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), result._typeProperty);
    }

    @Test
    public void testTypePropertyWithEmptyString() {
        StdTypeResolverBuilder result = builder.typeProperty("");
        
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), result._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = BasicType.class;
        
        StdTypeResolverBuilder result = builder.defaultImpl(defaultImpl);
        
        assertEquals(defaultImpl, result._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        
        StdTypeResolverBuilder result = builder.typeIdVisibility(isVisible);
        
        assertEquals(isVisible, result._typeIdVisible);
    }
    
    private static class BasicType {
        
    }
}