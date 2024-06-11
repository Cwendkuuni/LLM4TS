package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testInitWithIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, new CustomTypeIdResolver());
        assertNotNull(builder._idType);
        assertNotNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testBuildTypeSerializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerWithEmptySubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeSerializer(null, JavaType.valueOf("java.lang.String"), null));
    }

    @Test
    public void testBuildTypeSerializerWithClassTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig<>(null), JavaType.valueOf("java.lang.String"), null);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithClassNameTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig<>(null), JavaType.valueOf("java.lang.String"), null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithNameTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NAME, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig<>(null), JavaType.valueOf("java.lang.String"), null);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerWithCustomTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CUSTOM, null);
        try {
            builder.buildTypeSerializer(new MapperConfig<>(null), JavaType.valueOf("java.lang.String"), null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type serializer for inclusion type: CUSTOM", e.getMessage());
        }
    }

    @Test
    public void testBuildTypeSerializerWithWrongIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        try {
            builder.buildTypeSerializer(new MapperConfig<>(null), JavaType.valueOf("java.lang.String"), null).getClass();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type serializer for inclusion type: WRONG_INCLUDE_AS", e.getMessage());
        }
    }

    @Test
    public void testBuildTypeDeserializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithEmptySubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeDeserializer(null, JavaType.valueOf("java.lang.String"), null));
    }

    @Test
    public void testBuildTypeDeserializerWithClassTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.valueOf("java.lang.String"), null);
        assertNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithClassNameTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.valueOf("java.lang.String"), null);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithNameTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NAME, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.valueOf("java.lang.String"), null);
        assertNotNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithCustomTypeId() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CUSTOM, null);
        try {
            builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.valueOf("java.lang.String"), null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type deserializer for inclusion type: CUSTOM", e.getMessage());
        }
    }

    @Test
    public void testBuildTypeDeserializerWithWrongIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
        try {
            builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.valueOf("java.lang.String"), null).getClass();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type deserializer for inclusion type: WRONG_INCLUDE_AS", e.getMessage());
        }
    }
}