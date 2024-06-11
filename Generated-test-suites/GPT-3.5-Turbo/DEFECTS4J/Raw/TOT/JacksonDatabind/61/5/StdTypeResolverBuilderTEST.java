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
    }
    
    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);

        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertNull(serializer);
    }
    
    @Test
    public void testBuildTypeSerializerWithWrapperArrayType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsArrayTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerWithPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);

        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsPropertyTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerWithWrapperObjectType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);

        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsWrapperTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerWithExternalPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);

        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsExternalTypeSerializer);
    }
    
    @Test
    public void testBuildTypeSerializerWithExistingPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);

        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        
        assertTrue(serializer instanceof AsExistingPropertyTypeSerializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);

        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        builder.buildTypeSerializer(config, baseType, subtypes);
    }
    
    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertNull(deserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerWithWrapperArrayType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsArrayTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerWithPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerWithWrapperObjectType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
    }
    
    @Test
    public void testBuildTypeDeserializerWithExternalPropertyType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertTrue(deserializer instanceof AsExternalTypeDeserializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithInvalidType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(null);

        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JavaType baseType = new JavaType(null, null, null, null, null);
        Collection<NamedType> subtypes = new ArrayList<>();

        builder.buildTypeDeserializer(config, baseType, subtypes);
    }
    
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);

        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder._includeAs);
    }
    
    @Test(expected = IllegalArgumentException.class)
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
    public void testTypePropertyWithNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);

        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
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