package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        mapper = null;
    }

    @Test
    public void testDefaultInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getCustomIdResolver());

        TypeResolverBuilder<StdTypeResolverBuilder> builder2 = mapper.getTypeFactory()
                .constructType(StdTypeResolverBuilder.class);
        assertNotNull(builder2);

        StdTypeResolverBuilder builder3 = builder2.build();
        assertNull(builder3.getIdType());
        assertNull(builder3.getIncludeAs());
        assertNull(builder3.getTypeProperty());
        assertNull(builder3.getDefaultImpl());
        assertNull(builder3.getTypeIdVisible());
        assertNull(builder3.getCustomIdResolver());
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idResolver = new TypeNameIdResolver(mapper.getConfig(), null, null, true, true);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, idResolver);

        assertEquals(idType, builder.getIdType());
        assertSame(idResolver, builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());

        builder = new StdTypeResolverBuilder()
                .init(idType, null);
        assertEquals(idType, builder.getIdType());
        assertNull(builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testBuildTypeSerializer() {
        // Test with default settings
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNull(serializer);

        // Test with specific settings
        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .typeProperty("typeProperty")
                .defaultImpl(String.class);
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNotNull(serializer);

        // Test error cases
        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.NONE, null);
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNull(serializer);

        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNull(serializer);

        // Test toString method
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNotNull(serializer.toString());
    }

    @Test
    public void testBuildTypeDeserializer() {
        // Test with default settings
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNull(deserializer);

        // Test with specific settings
        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .typeProperty("typeProperty")
                .defaultImpl(String.class);
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNotNull(deserializer);

        // Test error cases
        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.NONE, null);
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNull(deserializer);

        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNull(deserializer);

        // Test toString method
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(),
                JavaType.of(String.class), Arrays.asList(new NamedType(Integer.class, "int")));
        assertNotNull(deserializer.toString());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("typeProperty");

        assertEquals("typeProperty", builder.getTypeProperty());

        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .typeProperty("typeProperty2");

        assertEquals("typeProperty2", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(String.class);

        assertEquals(String.class, builder.getDefaultImpl());

        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .defaultImpl(Integer.class);

        assertEquals(Integer.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);

        assertTrue(builder.isTypeIdVisible());

        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null)
                .typeIdVisibility(false);

        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);

        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());

        builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());
    }
}