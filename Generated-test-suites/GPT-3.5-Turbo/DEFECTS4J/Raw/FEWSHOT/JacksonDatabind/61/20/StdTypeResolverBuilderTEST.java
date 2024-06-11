package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;

import java.util.ArrayList;
import java.util.Collection;

public class StdTypeResolverBuilderTest {
    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);

        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        assertEquals(JsonTypeInfo.Id.CUSTOM, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("custom", builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.ARRAY);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        builder.typeIdVisibility(true);

        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<StdTypeResolverBuilder.NamedType> subtypes = new ArrayList<>();

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.ARRAY);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        builder.typeIdVisibility(true);

        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<StdTypeResolverBuilder.NamedType> subtypes = new ArrayList<>();

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.ARRAY);
        assertEquals(JsonTypeInfo.As.ARRAY, builder._includeAs);

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder._includeAs);

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);

        builder.typeProperty(null);
        assertEquals("class", builder._typeProperty);

        builder.typeProperty("");
        assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertEquals(true, builder._typeIdVisible);

        builder.typeIdVisibility(false);
        assertEquals(false, builder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<StdTypeResolverBuilder.NamedType> subtypes = new ArrayList<>();

        TypeIdResolver resolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertEquals(ClassNameIdResolver.class, resolver.getClass());

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertEquals(MinimalClassNameIdResolver.class, resolver.getClass());

        builder.init(JsonTypeInfo.Id.NAME, null);
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertEquals(TypeNameIdResolver.class, resolver.getClass());

        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        resolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertEquals(ClassNameIdResolver.class, resolver.getClass());
    }

    static class NamedType {
        public Class<?> _class;
        public String _name;

        public NamedType(Class<?> cls, String name) {
            _class = cls;
            _name = name;
        }
    }
}