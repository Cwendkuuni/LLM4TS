package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class StdTypeResolverBuilderTest {
    
    @Test
    public void testInit_ValidIdTypeAndIdResolver_ReturnsStdTypeResolverBuilderInstance() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        TypeResolverBuilder<StdTypeResolverBuilder> result = builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        // Assert
        assertEquals(builder, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInit_NullIdType_ThrowsIllegalArgumentException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        // Act
        builder.init(null, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
    }
    
    @Test
    public void testBuildTypeSerializer_IdTypeIsNone_ReturnsNull() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        // Act
        TypeSerializer result = builder.buildTypeSerializer(null, null, null);
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testBuildTypeSerializer_ValidIdTypeAndValidIncludeAs_ReturnsTypeSerializer() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(StdTypeResolverBuilderTest.class, "Test")
        );
        // Act
        TypeSerializer result = builder.buildTypeSerializer(null, null, subtypes);
        // Assert
        assertNotNull(result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializer_ValidIdTypeAndInvalidIncludeAs_ThrowsIllegalStateException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        builder.inclusion(null);
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(StdTypeResolverBuilderTest.class, "Test")
        );
        // Act
        builder.buildTypeSerializer(null, null, subtypes);
    }
    
    @Test
    public void testBuildTypeDeserializer_IdTypeIsNone_ReturnsNull() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        // Act
        TypeDeserializer result = builder.buildTypeDeserializer(null, null, null);
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testBuildTypeDeserializer_ValidIdTypeAndValidIncludeAs_ReturnsTypeDeserializer() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver(Gen.class, TypeFactory.defaultInstance()));
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        // Act
        TypeDeserializer result = builder.buildTypeDeserializer(null, TypeFactory.defaultInstance().constructType(Gen.class), null);
        // Assert
        assertNotNull(result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializer_ValidIdTypeAndInvalidIncludeAs_ThrowsIllegalStateException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver(Gen.class, TypeFactory.defaultInstance()));
        builder.inclusion(null);
        // Act
        builder.buildTypeDeserializer(null, TypeFactory.defaultInstance().constructType(Gen.class), null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIdResolver_NullIdType_ThrowsIllegalStateException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        // Act
        builder.idResolver(null, null, null, false, false);
    }
    
    @Test
    public void testIdResolver_CustomIdResolver_ReturnsCustomIdResolver() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver idResolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance());
        builder._customIdResolver = idResolver;
        // Act
        TypeIdResolver result = builder.idResolver(null, null, null, false, false);
        // Assert
        assertEquals(idResolver, result);
    }
    
    @Test
    public void testIdResolver_StandardIdTypeClass_ReturnsClassNameIdResolver() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        // Act
        TypeIdResolver result = builder.idResolver(null, null, null, false, false);
        // Assert
        assertTrue(result instanceof ClassNameIdResolver);
    }
    
    @Test
    public void testIdResolver_StandardIdTypeMinimalClass_ReturnsMinimalClassNameIdResolver() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        // Act
        TypeIdResolver result = builder.idResolver(null, null, null, false, false);
        // Assert
        assertTrue(result instanceof MinimalClassNameIdResolver);
    }
    
    @Test
    public void testIdResolver_StandardIdTypeName_ReturnsTypeNameIdResolver() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        // Act
        TypeIdResolver result = builder.idResolver(null, null, null, false, false);
        // Assert
        assertTrue(result instanceof TypeNameIdResolver);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolver_StandardIdTypeNone_ThrowsIllegalStateException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        // Act
        builder.idResolver(null, null, null, false, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolver_StandardIdTypeCustom_ThrowsIllegalStateException() {
        // Arrange
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CUSTOM, new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance()));
        // Act
        builder.idResolver(null, null, null, false, false);
    }

    private static class Gen {
        
    }
}