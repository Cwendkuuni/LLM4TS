package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    // Test for constructor and initialization method
    @Test
    public void testStdTypeResolverBuilderInitialization() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getTypeProperty());
        assertNull(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    // Test for noTypeInfoBuilder() method
    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getTypeProperty());
        assertNull(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    // Test for init() method
    @Test
    public void testInitMethod() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        assertNotNull(builder.getTypeProperty());
        assertEquals("class", builder.getTypeProperty());
    }

    // Test for buildTypeSerializer() method
    @Test
    public void testBuildTypeSerializer() throws JsonProcessingException {
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
    }

    // Test for buildTypeDeserializer() method
    @Test
    public void testBuildTypeDeserializer() throws JsonProcessingException {
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
    }

    // Test for inclusion() method
    @Test
    public void testInclusionMethod() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
    }

    // Test for typeProperty() method
    @Test
    public void testTypePropertyMethod() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("testProperty");
        assertEquals("testProperty", builder.getTypeProperty());
    }

    // Test for defaultImpl() method
    @Test
    public void testDefaultImplMethod() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    // Test for typeIdVisibility() method
    @Test
    public void testTypeIdVisibilityMethod() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    // Test for getDefaultImpl() method
    @Test
    public void testGetDefaultImplMethod() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    // Test for getTypeProperty() method
    @Test
    public void testGetTypePropertyMethod() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("testProperty");
        assertEquals("testProperty", builder.getTypeProperty());
    }

    // Test for isTypeIdVisible() method
    @Test
    public void testIsTypeIdVisibleMethod() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    // Test for idResolver() method with class type id
    @Test
    public void testIdResolverClassTypeId() {
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeIdResolver typeIdResolver = builder.idResolver(config, baseType, subtypes, false, false);
        assertNotNull(typeIdResolver);
    }

    // Test for idResolver() method with minimal class type id
    @Test
    public void testIdResolverMinimalClassTypeId() {
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);

        TypeIdResolver typeIdResolver = builder.idResolver(config, baseType, subtypes, false, false);
        assertNotNull(typeIdResolver);
    }

    // Test for idResolver() method with name type id
    @Test
    public void testIdResolverNameTypeId() {
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);

        TypeIdResolver typeIdResolver = builder.idResolver(config, baseType, subtypes, false, false);
        assertNotNull(typeIdResolver);
    }

    // Test for idResolver() method with custom type id (should throw an exception)
    @Test(expected = IllegalStateException.class)
    public void testIdResolverCustomTypeId() {
        MapperConfig<?> config = new MapperConfig.StandardConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CUSTOM, null);

        builder.idResolver(config, baseType, subtypes, false, false);
    }
}