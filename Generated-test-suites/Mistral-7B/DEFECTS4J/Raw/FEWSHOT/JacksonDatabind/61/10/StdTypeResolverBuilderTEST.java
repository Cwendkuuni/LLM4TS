package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        MapperConfig<ObjectNode> config = mock(MapperConfig.class);
        TypeFactory typeFactory = mock(TypeFactory.class);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);

        JsonTypeInfo.Id idType = JsonTypeInfo.Id.MINIMAL_CLASS;
        builder.init(idType, null);
        assertSame(idType, builder._idType);
        assertNull(builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);

        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        builder.init(idType, idResolver);
        assertSame(idType, builder._idType);
        assertSame(idResolver, builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testBuildTypeSerializer() {
        MapperConfig<ObjectNode> config = mock(MapperConfig.class);
        TypeFactory typeFactory = mock(TypeFactory.class);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = Arrays.asList(mock(NamedType.class), mock(NamedType.class));

        TypeIdResolver idRes = mock(TypeIdResolver.class);

        when(config.getTypeFactory()).thenReturn(typeFactory);

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(result);

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, idRes);
        TypeSerializer resultWrapperArray = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(resultWrapperArray);
        assertInstanceOf(AsArrayTypeSerializer.class, resultWrapperArray);

        builder.init(JsonTypeInfo.Id.PROPERTY, idRes);
        TypeSerializer resultProperty = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(resultProperty);
        assertInstanceOf(AsPropertyTypeSerializer.class, resultProperty);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, idRes);
        TypeSerializer resultWrapperObject = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(resultWrapperObject);
        assertInstanceOf(AsWrapperTypeSerializer.class, resultWrapperObject);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, idRes);
        TypeSerializer resultExternalProperty = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(resultExternalProperty);
        assertInstanceOf(AsExternalTypeSerializer.class, resultExternalProperty);

        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, idRes);
        TypeSerializer resultExistingProperty = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(resultExistingProperty);
        assertInstanceOf(AsExistingPropertyTypeSerializer.class, resultExistingProperty);

        builder.init(JsonTypeInfo.Id.NONE, idRes);
        TypeSerializer resultNone = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(resultNone);
    }

    @Test
    public void testBuildTypeDeserializer() {
        MapperConfig<ObjectNode> config = mock(MapperConfig.class);
        TypeFactory typeFactory = mock(TypeFactory.class);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = Arrays.asList(mock(NamedType.class), mock(NamedType.class));

        TypeIdResolver idRes = mock(TypeIdResolver.class);

        when(config.getTypeFactory()).thenReturn(typeFactory);

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(result);

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, idRes);
        TypeDeserializer resultWrapperArray = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(resultWrapperArray);
        assertInstanceOf(AsArrayTypeDeserializer.class, resultWrapperArray);

        builder.init(JsonTypeInfo.Id.PROPERTY, idRes);
        TypeDeserializer resultProperty = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(resultProperty);
        assertInstanceOf(AsPropertyTypeDeserializer.class, resultProperty);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, idRes);
        TypeDeserializer resultWrapperObject = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(resultWrapperObject);
        assertInstanceOf(AsWrapperTypeDeserializer.class, resultWrapperObject);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, idRes);
        TypeDeserializer resultExternalProperty = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(resultExternalProperty);
        assertInstanceOf(AsExternalTypeDeserializer.class, resultExternalProperty);

        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, idRes);
        TypeDeserializer resultExistingProperty = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(resultExistingProperty);
        assertInstanceOf(AsExistingPropertyTypeDeserializer.class, resultExistingProperty);

        builder.init(JsonTypeInfo.Id.NONE, idRes);
        TypeDeserializer resultNone = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(resultNone);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typeIdPropName");
        assertEquals("typeIdPropName", builder.getTypeProperty());
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}