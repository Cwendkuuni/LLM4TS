package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testEmptyBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
        assertTrue(builder.isTypeIdVisible() == false);
    }

    @Test
    public void testInitBuilder() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(Object.class, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);

        assertEquals(idType, builder.getIdType());
        assertEquals(idRes, builder.getCustomIdResolver());
        assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertTrue(builder.isTypeIdVisible() == false);
    }

    @Test
    public void testBuildTypeSerializer() throws Exception {
        MapperConfig<Object> config = new MapperFactory().getMapperConfig();
        JavaType baseType = config.getTypeFactory().constructType(Object.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(String.class, config.getTypeFactory()));
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.NAME, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        assertNull(builder.buildTypeSerializer(config, baseType, subtypes));

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(serializer instanceof AsPropertyTypeSerializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(serializer instanceof AsWrapperTypeSerializer);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(serializer instanceof AsExternalTypeSerializer);

        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(serializer instanceof AsExistingPropertyTypeSerializer);

        builder.init(JsonTypeInfo.Id.NONE, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() throws Exception {
        MapperConfig<Object> config = new MapperFactory().getMapperConfig();
        JavaType baseType = config.getTypeFactory().constructType(Object.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(String.class, config.getTypeFactory()));
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.NAME, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(deserializer instanceof AsExternalTypeDeserializer);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(deserializer instanceof AsExistingPropertyTypeDeserializer);

        builder.init(JsonTypeInfo.Id.NONE, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);
    }

    @Test
    public void testIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, builder.getIncludeAs());

        builder.inclusion(null);
        assertNull(builder.getIncludeAs());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder.getTypeProperty());

        builder.typeProperty(null);
        assertNull(builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());

        builder.defaultImpl(null);
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }
}