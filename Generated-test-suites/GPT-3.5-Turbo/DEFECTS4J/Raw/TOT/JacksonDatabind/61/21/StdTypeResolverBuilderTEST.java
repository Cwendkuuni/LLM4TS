package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testInitWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        builder.init(JsonTypeInfo.Id.CUSTOM, idResolver);
        assertEquals(JsonTypeInfo.Id.CUSTOM, builder._idType);
        assertEquals(idResolver, builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CUSTOM.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        assertNull(builder.buildTypeSerializer(config, baseType, subtypes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypePropertyWithNullIdPropertyName() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);
        assertEquals(builder._idType.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        assertNull(builder.buildTypeDeserializer(config, baseType, subtypes));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithInvalidIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeIdentifierWithInvalidIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    // Add more test cases for each method

}