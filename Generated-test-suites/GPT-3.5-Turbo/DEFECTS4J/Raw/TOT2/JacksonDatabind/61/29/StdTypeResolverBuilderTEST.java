package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver());
        
        assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
        assertNotNull(resolverBuilder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), resolverBuilder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.MINIMAL_CLASS, new MinimalClassNameIdResolver());
        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        
        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, resolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("myType");
        
        assertEquals("myType", resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.defaultImpl(String.class);
        
        assertEquals(String.class, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeIdVisibility(true);
        
        assertTrue(resolverBuilder._typeIdVisible);
    }

}