package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeIdentifier;
import com.fasterxml.jackson.databind.type.TypeResolver;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test case for StdTypeResolverBuilder class.
 */
public class StdTypeResolverBuilderTest {

    @Test
    public void testEmptyBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeIdVisible());
        assertNull(builder.getCustomIdResolver());
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
    public void testInitBuilder() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertSame(idType, builder.getIdType());
        assertSame(idRes, builder.getCustomIdResolver());
        assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        MapperConfig<?> config = mock(MapperConfig.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        String typeProperty = "typeProperty";
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        when(idRes.getId(any(JavaType.class), any(JavaType.class), anyInt(), anyBoolean()))
                .thenAnswer(new Answer<TypeIdentifier>() {
                    @Override
                    public TypeIdentifier answer(InvocationOnMock invocation) {
                        return new TypeIdentifier(typeProperty, null, null);
                    }
                });
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        // TODO: Test serialization
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithWrongIncludeAs() {
        MapperConfig<?> config = mock(MapperConfig.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        String typeProperty = "typeProperty";
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        when(idRes.getId(any(JavaType.class), any(JavaType.class), anyInt(), anyBoolean()))
                .thenAnswer(new Answer<TypeIdentifier>() {
                    @Override
                    public TypeIdentifier answer(InvocationOnMock invocation) {
                        return new TypeIdentifier(typeProperty, null, null);
                    }
                });
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        // TODO: Test deserialization
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithWrongIncludeAs() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
        builder.inclusion(includeAs);
        assertSame(includeAs, builder.getIncludeAs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "typeProperty";
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Object.class;
        builder.defaultImpl(defaultImpl);
        assertSame(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Object.class;
        builder.defaultImpl(defaultImpl);
        assertSame(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "typeProperty";
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}