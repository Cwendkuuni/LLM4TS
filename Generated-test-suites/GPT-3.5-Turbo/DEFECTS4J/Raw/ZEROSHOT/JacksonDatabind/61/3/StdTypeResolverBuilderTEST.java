package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            builder.init(JsonTypeInfo.Id.CLASS, null);
            assertNull(builder._customIdResolver);
            assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = null; // TODO: set a value for baseType
        Collection<NamedType> subtypes = null; // TODO: set a value for subtypes
        try {
            builder.buildTypeSerializer(config, baseType, subtypes);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = null; // TODO: set a value for baseType
        Collection<NamedType> subtypes = null; // TODO: set a value for subtypes
        try {
            builder.buildTypeDeserializer(config, baseType, subtypes);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.inclusion(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
            assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.typeProperty(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            builder.typeProperty("");
            assertEquals(builder._idType.getDefaultPropertyName(), builder._typeProperty);
            builder.typeProperty("test");
            assertEquals("test", builder._typeProperty);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = null; // TODO: set a value for defaultImpl
        try {
            builder.defaultImpl(defaultImpl);
            assertEquals(defaultImpl, builder._defaultImpl);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true; // TODO: set a value for isVisible
        try {
            builder.typeIdVisibility(isVisible);
            assertEquals(isVisible, builder._typeIdVisible);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = null; // TODO: set a value for defaultImpl
        try {
            builder.defaultImpl(defaultImpl);
            assertEquals(defaultImpl, builder.getDefaultImpl());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeIdPropName = null; // TODO: set a value for typeIdPropName
        try {
            builder.typeProperty(typeIdPropName);
            assertEquals(typeIdPropName, builder.getTypeProperty());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true; // TODO: set a value for isVisible
        try {
            builder.typeIdVisibility(isVisible);
            assertEquals(isVisible, builder.isTypeIdVisible());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = null; // TODO: set a value for config
        JavaType baseType = null; // TODO: set a value for baseType
        Collection<NamedType> subtypes = null; // TODO: set a value for subtypes
        boolean forSer = true; // TODO: set a value for forSer
        boolean forDeser = true; // TODO: set a value for forDeser
        try {
            builder.idResolver(config, baseType, subtypes, forSer, forDeser);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}