package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MapperConfig config = new MapperConfig();
        objectMapper = new ObjectMapper(config);
    }

    @Test
    public void testStdTypeResolverBuilder_DefaultConstruction() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_InitWithNullValues() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_InitWithValidValues() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(Object.class, null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, idRes);
        assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
        assertEquals(idRes, builder.getCustomIdResolver());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer_NoneType() {
        StdTypeResolverBuilder builder = noTypeInfoBuilder();
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(),
                JavaType.forRawClass(String[].class), null);
        assertNull(serializer);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer_PropertyInclusion() {
        String typeProperty = "type";
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.PROPERTY, null)
                .typeProperty(typeProperty);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(),
                JavaType.forRawClass(String[].class), null);
        assertEquals(AsPropertyTypeSerializer.class.getName(), serializer.getClass().getName());
        assertEquals(typeProperty, serializer.getPropertyName());
        assertFalse(((AsPropertyTypeSerializer) serializer).isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer_WrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.WRAPPER_ARRAY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(),
                JavaType.forRawClass(String[].class), null);
        assertEquals(AsArrayTypeSerializer.class.getName(), serializer.getClass().getName());
        assertNull(serializer.getPropertyName());
        assertFalse(((AsArrayTypeSerializer) serializer).isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer_WrapperObjectInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.WRAPPER_OBJECT, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertEquals(AsWrapperTypeSerializer.class.getName(), serializer.getClass().getName());
        assertNull(serializer.getPropertyName());
        assertFalse(((AsWrapperTypeSerializer) serializer).isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer_ExternalPropertyInclusion() {
        String typeProperty = "type";
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.EXTERNAL_PROPERTY, null)
                .typeProperty(typeProperty);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertEquals(AsExternalTypeSerializer.class.getName(), serializer.getClass().getName());
        assertEquals(typeProperty, serializer.getPropertyName());
        assertFalse(((AsExternalTypeSerializer) serializer).isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializer_ExistingPropertyInclusion() {
        String typeProperty = "type";
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.EXISTING_PROPERTY, null)
                .typeProperty(typeProperty);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertEquals(AsExistingPropertyTypeSerializer.class.getName(), serializer.getClass().getName());
        assertEquals(typeProperty, serializer.getPropertyName());
        assertFalse(((AsExistingPropertyTypeSerializer) serializer).isTypeIdVisible());
    }

    @Test(expected = IllegalStateException.class)
    public void testStdTypeResolverBuilder_BuildTypeSerializer_UnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.FOO, null);
        builder.buildTypeSerializer(new SerializationConfig(), JavaType.forRawClass(String.class), null);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_NoneType() {
        StdTypeResolverBuilder builder = noTypeInfoBuilder();
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertNull(deserializer);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_PropertyInclusion() {
        String typeProperty = "type";
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.PROPERTY, null)
                .typeProperty(typeProperty)
                .typeIdVisibility(true);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertEquals(AsPropertyTypeDeserializer.class.getName(), deserializer.getClass().getName());
        assertEquals(typeProperty, deserializer.getPropertyName());
        assertTrue(((AsPropertyTypeDeserializer) deserializer).isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_WrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.WRAPPER_ARRAY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(),
                JavaType.forRawClass(String[].class), null);
        assertEquals(AsArrayTypeDeserializer.class.getName(), deserializer.getClass().getName());
        assertNull(deserializer.getPropertyName());
        assertFalse(((AsArrayTypeDeserializer) deserializer).isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_WrapperObjectInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.WRAPPER_OBJECT, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertEquals(AsWrapperTypeDeserializer.class.getName(), deserializer.getClass().getName());
        assertNull(deserializer.getPropertyName());
        assertFalse(((AsWrapperTypeDeserializer) deserializer).isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_ExternalPropertyInclusion() {
        String typeProperty = "type";
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.EXTERNAL_PROPERTY, null)
                .typeProperty(typeProperty)
                .typeIdVisibility(true);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertEquals(AsExternalTypeDeserializer.class.getName(), deserializer.getClass().getName());
        assertEquals(typeProperty, deserializer.getPropertyName());
        assertTrue(((AsExternalTypeDeserializer) deserializer).isTypeIdVisible());
    }

    @Test(expected = IllegalStateException.class)
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_UnknownInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.FOO, null);
        builder.buildTypeDeserializer(new DeserializationConfig(), JavaType.forRawClass(String.class), null);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializer_DefaultImpl() {
        String typeProperty = "type";
        Class<?> defaultImpl = String[].class;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.As.PROPERTY, null)
                .typeProperty(typeProperty)
                .defaultImpl(defaultImpl)
                .typeIdVisibility(true);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(),
                JavaType.forRawClass(String.class), null);
        assertEquals(AsPropertyTypeDeserializer.class.getName(), deserializer.getClass().getName());
        assertEquals(typeProperty, deserializer.getPropertyName());
        assertTrue(((AsPropertyTypeDeserializer) deserializer).isTypeIdVisible());
        assertEquals(defaultImpl, ((AsPropertyTypeDeserializer) deserializer).getDefaultImpl());
    }
}