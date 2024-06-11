package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testConstructor() {
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        assertNull(stdTypeResolverBuilder._idType);
        assertNull(stdTypeResolverBuilder._includeAs);
        assertNull(stdTypeResolverBuilder._typeProperty);
        assertFalse(stdTypeResolverBuilder._typeIdVisible);
        assertNull(stdTypeResolverBuilder._defaultImpl);
        assertNull(stdTypeResolverBuilder._customIdResolver);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder stdTypeResolverBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(stdTypeResolverBuilder._idType);
        assertNull(stdTypeResolverBuilder._includeAs);
        assertNull(stdTypeResolverBuilder._typeProperty);
        assertFalse(stdTypeResolverBuilder._typeIdVisible);
        assertNull(stdTypeResolverBuilder._defaultImpl);
        assertNull(stdTypeResolverBuilder._customIdResolver);
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new TypeIdResolver() {}; // Dummy implementation
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.init(idType, idRes);
        assertEquals(idType, stdTypeResolverBuilder._idType);
        assertEquals(idRes, stdTypeResolverBuilder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
        assertFalse(stdTypeResolverBuilder._typeIdVisible);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        // TODO: Create a mock MapperConfig, JavaType and Collection for testing
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithNullIdType() {
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder._idType = null;
        try {
            stdTypeResolverBuilder.buildTypeSerializer(null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("idType can not be null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testBuildTypeDeserializer() {
        // TODO: Create a mock DeserializationConfig, JavaType and Collection for testing
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithNullIdType() {
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder._idType = null;
        try {
            stdTypeResolverBuilder.buildTypeDeserializer(null, null, null);
        } catch (IllegalStateException e) {
            assertEquals("Can not build, 'init()' not yet called", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
        stdTypeResolverBuilder.inclusion(includeAs);
        assertEquals(includeAs, stdTypeResolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNullIncludeAs() {
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        String typeIdPropName = "typeIdPropName";
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = Object.class;
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, stdTypeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.typeIdVisibility(isVisible);
        assertEquals(isVisible, stdTypeResolverBuilder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = Object.class;
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, stdTypeResolverBuilder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        String typeIdPropName = "typeIdPropName";
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, stdTypeResolverBuilder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        boolean isVisible = true;
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.typeIdVisibility(isVisible);
        assertEquals(isVisible, stdTypeResolverBuilder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        // TODO: Create a mock MapperConfig, JavaType and Collection for testing
        TypeIdResolver idResolver = new TypeIdResolver() {}; // Dummy implementation
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder._customIdResolver = idResolver;
        assertEquals(idResolver, stdTypeResolverBuilder.idResolver(null, null, null, true, false));
    }
}