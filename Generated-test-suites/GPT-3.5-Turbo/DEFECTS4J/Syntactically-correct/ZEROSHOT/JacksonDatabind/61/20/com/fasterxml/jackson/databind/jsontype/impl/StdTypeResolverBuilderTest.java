package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder stdTypeResolverBuilder;

    @Before
    public void setUp() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, stdTypeResolverBuilder._idType);
        assertNull(stdTypeResolverBuilder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        stdTypeResolverBuilder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeSerializer typeSerializer = stdTypeResolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeDeserializer typeDeserializer = stdTypeResolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(typeDeserializer);
    }

    @Test
    public void testInclusion() {
        assertEquals(stdTypeResolverBuilder, stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT));
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, stdTypeResolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        stdTypeResolverBuilder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        String typeIdPropName = "type";
        assertEquals(stdTypeResolverBuilder, stdTypeResolverBuilder.typeProperty(typeIdPropName));
        assertEquals(typeIdPropName, stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithDefault() {
        String typeIdPropName = null;
        assertEquals(stdTypeResolverBuilder, stdTypeResolverBuilder.typeProperty(typeIdPropName));
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = String.class;
        assertEquals(stdTypeResolverBuilder, stdTypeResolverBuilder.defaultImpl(defaultImpl));
        assertEquals(defaultImpl, stdTypeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        assertEquals(stdTypeResolverBuilder, stdTypeResolverBuilder.typeIdVisibility(isVisible));
        assertEquals(isVisible, stdTypeResolverBuilder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(stdTypeResolverBuilder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), stdTypeResolverBuilder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        assertFalse(stdTypeResolverBuilder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        boolean forSer = true;
        boolean forDeser = true;
        TypeIdResolver typeIdResolver = stdTypeResolverBuilder.idResolver(config, baseType, subtypes, forSer, forDeser);
        assertNull(typeIdResolver);
    }
}