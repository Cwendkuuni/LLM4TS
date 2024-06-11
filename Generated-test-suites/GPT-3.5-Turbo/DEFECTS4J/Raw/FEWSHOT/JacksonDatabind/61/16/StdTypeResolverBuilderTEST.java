package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;
    private MapperConfig<?> config;
    private JavaType baseType;
    private Collection<NamedType> subtypes;

    @Before
    public void setUp() {
        builder = new StdTypeResolverBuilder();
        config = mock(MapperConfig.class);
        baseType = mock(JavaType.class);
        subtypes = new HashSet<NamedType>();
    }

    @Test
    public void testInit() {
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        builder.init(JsonTypeInfo.Id.CLASS, idRes);

        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        builder._idType = JsonTypeInfo.Id.NONE;

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);

        assertNull(result);
    }

    @Test
    public void testBuildTypeSerializerWithTypeArray() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);

        assertEquals(AsArrayTypeSerializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithProperty() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.PROPERTY;

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);

        assertEquals(AsPropertyTypeSerializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithWrapperObject() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);

        assertEquals(AsWrapperTypeSerializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithExternalProperty() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);

        assertEquals(AsExternalTypeSerializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithExistingProperty() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;

        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);

        assertEquals(AsExistingPropertyTypeSerializer.class, result.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnsupportedIncludeAs() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = null;

        builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        builder._idType = JsonTypeInfo.Id.NONE;

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);

        assertNull(result);
    }

    @Test
    public void testBuildTypeDeserializerWithTypeArray() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);

        assertEquals(AsArrayTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithProperty() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.PROPERTY;

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);

        assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperObject() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);

        assertEquals(AsWrapperTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithExternalProperty() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);

        assertEquals(AsExternalTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithExistingProperty() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;

        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);

        assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnsupportedIncludeAs() {
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = null;

        builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);

        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        String typeIdPropName = "typeId";
        builder.typeProperty(typeIdPropName);

        assertEquals(typeIdPropName, builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNullTypeProperty() {
        String typeIdPropName = null;
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.NAME;
        builder.typeProperty(typeIdPropName);

        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);

        assertEquals(defaultImpl, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        builder.typeIdVisibility(isVisible);

        assertEquals(isVisible, builder._typeIdVisible);
    }
}