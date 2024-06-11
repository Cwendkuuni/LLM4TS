package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder.As;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilderTest;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilderTest.TestClass1;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilderTest.TestClass2;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilderTest.TestClass3;

public class StdTypeResolverBuilderTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS.getDefaultPropertyName(), builder._typeProperty);

        builder.init(JsonTypeInfo.Id.NONE, null);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.NONE.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        builder.defaultImpl(TestClass1.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType baseType = objectMapper.constructType(StdTypeResolverBuilderTest.TestClass1.class);

        TypeSerializer typeSerializer = builder.buildTypeSerializer(objectMapper.getSerializationConfig(), baseType, null);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        builder.defaultImpl(TestClass1.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType baseType = objectMapper.constructType(StdTypeResolverBuilderTest.TestClass1.class);

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(objectMapper.getDeserializationConfig(), baseType, null);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsArrayTypeDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("");
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);

        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder._typeProperty);

        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(TestClass1.class);
        assertEquals(TestClass1.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(false);
        assertFalse(builder._typeIdVisible);

        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(TestClass1.class);
        assertEquals(TestClass1.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.typeProperty("type");
        assertEquals("type", builder.getTypeProperty());

        builder.typeProperty(null);
        assertEquals(JsonTypeInfo.Id.NAME.getDefaultPropertyName(), builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        MapperConfig<?> config = objectMapper.getDeserializationConfig();
        JavaType baseType = objectMapper.constructType(StdTypeResolverBuilderTest.TestClass1.class);
        Collection<NamedType> subtypes = new ArrayList<NamedType>();

        TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(idResolver);
        assertTrue(idResolver instanceof ClassNameIdResolver);
    }

    public static class TestClass1 {}

    public static class TestClass2 {}

    public static class TestClass3 {}

}