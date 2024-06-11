package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder typeResolverBuilder;

    @Before
    public void setup() {
        typeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit_ValidIdType_ValidTypeIdResolver() {
        // Arrange
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver typeIdResolver = new ClassNameIdResolver(JavaType.builder().build(), null);

        // Act
        StdTypeResolverBuilder result = typeResolverBuilder.init(idType, typeIdResolver);

        // Assert
        assertEquals(idType, result._idType);
        assertEquals(typeIdResolver, result._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), result._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_NullIdType_ThrowsIllegalArgumentException() {
        // Arrange
        JsonTypeInfo.Id idType = null;
        TypeIdResolver typeIdResolver = new ClassNameIdResolver(JavaType.builder().build(), null);

        // Act
        typeResolverBuilder.init(idType, typeIdResolver);

        // Assert
        // Expecting an IllegalArgumentException to be thrown
    }

    @Test
    public void testBuildTypeSerializer_IdTypeNone_ReturnsNull() {
        // Arrange
        SerializationConfig config = new SerializationConfig(null, null, null);
        JavaType baseType = JavaType.builder().build();
        Collection<NamedType> subtypes = null;
        typeResolverBuilder._idType = JsonTypeInfo.Id.NONE;

        // Act
        TypeSerializer result = typeResolverBuilder.buildTypeSerializer(config, baseType, subtypes);

        // Assert
        assertNull(result);
    }

    // Add more test methods for each public method in StdTypeResolverBuilder class

}