package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder initBuilder = builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, initBuilder._idType);
        assertNull(initBuilder._customIdResolver);
        assertEquals("", initBuilder._typeProperty);
        assertNull(initBuilder._typeIdVisible);
        assertNull(initBuilder._defaultImpl);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testBuildTypeSerializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder initBuilder = builder.init(idType, null);

        JavaType baseType = Object.class.getType();
        Collection<NamedType> subtypes = null;

        TypeSerializer typeSerializer = initBuilder.buildTypeSerializer(new MapperConfig<>(), baseType, subtypes);
        assertNull(typeSerializer);

        idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        initBuilder = builder.init(idType, null);
        typeSerializer = initBuilder.buildTypeSerializer(new MapperConfig<>(), baseType, subtypes);
        assertNull(typeSerializer);

        idType = JsonTypeInfo.Id.PROPERTY;
        initBuilder = builder.init(idType, null);
        typeSerializer = initBuilder.buildTypeSerializer(new MapperConfig<>(), baseType, subtypes);
        assertNull(typeSerializer);

        idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        initBuilder = builder.init(idType, null);
        typeSerializer = initBuilder.buildTypeSerializer(new MapperConfig<>(), baseType, subtypes);
        assertNull(typeSerializer);

        idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        initBuilder = builder.init(idType, null);
        typeSerializer = initBuilder.buildTypeSerializer(new MapperConfig<>(), baseType, subtypes);
        assertNull(typeSerializer);

        idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
        initBuilder = builder.init(idType, null);
        typeSerializer = initBuilder.buildTypeSerializer(new MapperConfig<>(), baseType, subtypes);
        assertNull(typeSerializer);

        idType = JsonTypeInfo.Id.CUSTOM;
        initBuilder = builder.init(idType, null);
        assertNull(initBuilder._idType);
        assertNull(initBuilder._customIdResolver);
        assertEquals("", initBuilder._typeProperty);
        assertNull(initBuilder._typeIdVisible);
        assertNull(initBuilder._defaultImpl);
    }

    @Test
    public void testBuildTypeDeserializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder initBuilder = builder.init(idType, null);

        JavaType baseType = Object.class.getType();
        Collection<NamedType> subtypes = null;

        TypeDeserializer typeDeserializer = initBuilder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(typeDeserializer);

        idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        initBuilder = builder.init(idType, null);
        typeDeserializer = initBuilder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(typeDeserializer);

        idType = JsonTypeInfo.Id.PROPERTY;
        initBuilder = builder.init(idType, null);
        typeDeserializer = initBuilder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(typeDeserializer);

        idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        initBuilder = builder.init(idType, null);
        typeDeserializer = initBuilder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(typeDeserializer);

        idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        initBuilder = builder.init(idType, null);
        typeDeserializer = initBuilder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(typeDeserializer);

        idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
        initBuilder = builder.init(idType, null);
        typeDeserializer = initBuilder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNull(typeDeserializer);

        idType = JsonTypeInfo.Id.CUSTOM;
        initBuilder = builder.init(idType, null);
        assertNull(initBuilder._idType);
        assertNull(initBuilder._customIdResolver);
        assertEquals("", initBuilder._typeProperty);
        assertNull(initBuilder._typeIdVisible);
        assertNull(initBuilder._defaultImpl);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        StdTypeResolverBuilder inclusionBuilder = builder.inclusion(includeAs);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, inclusionBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "typeProperty";
        StdTypeResolverBuilder typePropertyBuilder = builder.typeProperty(typeProperty);
        assertEquals("typeProperty", typePropertyBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        StdTypeResolverBuilder defaultImplBuilder = builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, defaultImplBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean typeIdVisible = true;
        StdTypeResolverBuilder typeIdVisibilityBuilder = builder.typeIdVisibility(typeIdVisible);
        assertEquals(typeIdVisible, typeIdVisibilityBuilder._typeIdVisible);
    }
}