package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setUp() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        StdTypeResolverBuilder initBuilder = builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, initBuilder._idType);
        assertNull(initBuilder._customIdResolver);
        assertEquals("", initBuilder._typeProperty);
        assertFalse(initBuilder._typeIdVisible);
        assertNull(initBuilder._defaultImpl);
    }

    @Test
    public void testInitWithCustomIdResolver() {
        TypeIdResolver customIdResolver = mock(TypeIdResolver.class);
        StdTypeResolverBuilder initBuilder = builder.init(JsonTypeInfo.Id.CLASS, customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS, initBuilder._idType);
        assertEquals(customIdResolver, initBuilder._customIdResolver);
        assertEquals("", initBuilder._typeProperty);
        assertFalse(initBuilder._typeIdVisible);
        assertNull(initBuilder._defaultImpl);
    }

    @Test
    public void testBuildTypeSerializerNullIdType() {
        assertNull(builder.buildTypeSerializer(mock(SerializationConfig.class), mock(JavaType.class), mock(Collection.class)));
    }

    @Test
    public void testBuildTypeSerializerWrapperArray() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        builder.init(idType, null);
        AsArrayTypeSerializer typeSerializer = (AsArrayTypeSerializer) builder.buildTypeSerializer(mock(SerializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerProperty() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        builder.init(idType, null);
        AsPropertyTypeSerializer typeSerializer = (AsPropertyTypeSerializer) builder.buildTypeSerializer(mock(SerializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWrapperObject() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        builder.init(idType, null);
        AsWrapperTypeSerializer typeSerializer = (AsWrapperTypeSerializer) builder.buildTypeSerializer(mock(SerializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerExternalProperty() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        builder.init(idType, null);
        AsExternalTypeSerializer typeSerializer = (AsExternalTypeSerializer) builder.buildTypeSerializer(mock(SerializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerExistingProperty() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
        builder.init(idType, null);
        AsExistingPropertyTypeSerializer typeSerializer = (AsExistingPropertyTypeSerializer) builder.buildTypeSerializer(mock(SerializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerInvalidIncludeAs() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.INVALID_INCLUDE_AS;
        builder.init(idType, null);
        builder.buildTypeSerializer(mock(SerializationConfig.class), mock(JavaType.class), mock(Collection.class));
    }

    @Test
    public void testBuildTypeDeserializerNullIdType() {
        assertNull(builder.buildTypeDeserializer(mock(DeserializationConfig.class), mock(JavaType.class), mock(Collection.class)));
    }

    @Test
    public void testBuildTypeDeserializerWrapperArray() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        builder.init(idType, null);
        AsArrayTypeDeserializer typeDeserializer = (AsArrayTypeDeserializer) builder.buildTypeDeserializer(mock(DeserializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerProperty() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        builder.init(idType, null);
        AsPropertyTypeDeserializer typeDeserializer = (AsPropertyTypeDeserializer) builder.buildTypeDeserializer(mock(DeserializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWrapperObject() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        builder.init(idType, null);
        AsWrapperTypeDeserializer typeDeserializer = (AsWrapperTypeDeserializer) builder.buildTypeDeserializer(mock(DeserializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerExternalProperty() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        builder.init(idType, null);
        AsExternalTypeDeserializer typeDeserializer = (AsExternalTypeDeserializer) builder.buildTypeDeserializer(mock(DeserializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerExistingProperty() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
        builder.init(idType, null);
        AsExistingPropertyTypeDeserializer typeDeserializer = (AsExistingPropertyTypeDeserializer) builder.buildTypeDeserializer(mock(DeserializationConfig.class), mock(JavaType.class), mock(Collection.class));
        assertNotNull(typeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerInvalidIncludeAs() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.INVALID_INCLUDE_AS;
        builder.init(idType, null);
        builder.buildTypeDeserializer(mock(DeserializationConfig.class), mock(JavaType.class), mock(Collection.class));
    }

    @Test
    public void testTypeProperty() {
        String typeProperty = "typeProperty";
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = Object.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        boolean typeIdVisible = true;
        builder.typeIdVisibility(typeIdVisible);
        assertEquals(typeIdVisible, builder.isTypeIdVisible());
    }
}