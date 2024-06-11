package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNotNull(builder);
        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(null, builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.buildTypeSerializer(null, null, null);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperArrayIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertEquals(AsArrayTypeSerializer.class, typeSerializer.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithPropertyIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertEquals(AsPropertyTypeSerializer.class, typeSerializer.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithWrapperObjectIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertEquals(AsWrapperTypeSerializer.class, typeSerializer.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithExternalPropertyIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertEquals(AsExternalTypeSerializer.class, typeSerializer.getClass());
    }

    @Test
    public void testBuildTypeSerializerWithExistingPropertyIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, null, null);
        assertNotNull(typeSerializer);
        assertEquals(AsExistingPropertyTypeSerializer.class, typeSerializer.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnknownIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        builder.buildTypeDeserializer(null, null, null);
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperArrayIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertEquals(AsArrayTypeDeserializer.class, typeDeserializer.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithPropertyIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertEquals(AsPropertyTypeDeserializer.class, typeDeserializer.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperObjectIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertEquals(AsWrapperTypeDeserializer.class, typeDeserializer.getClass());
    }

    @Test
    public void testBuildTypeDeserializerWithExternalPropertyIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(null, null, null);
        assertNotNull(typeDeserializer);
        assertEquals(AsExternalTypeDeserializer.class, typeDeserializer.getClass());
    }

    @Test
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertNotNull(result);
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, builder._includeAs);
    }

    @Test
    public void testTypePropertyWithNullProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeProperty(null);
        assertNotNull(result);
        assertEquals(builder._idType.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithEmptyProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeProperty("");
        assertNotNull(result);
        assertEquals(builder._idType.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeProperty("typeId");
        assertNotNull(result);
        assertEquals("typeId", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.defaultImpl(Object.class);
        assertNotNull(result);
        assertEquals(Object.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.typeIdVisibility(true);
        assertNotNull(result);
        assertEquals(true, builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._defaultImpl = Object.class;
        assertEquals(Object.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._typeProperty = "typeId";
        assertEquals("typeId", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._typeIdVisible = true;
        assertEquals(true, builder.isTypeIdVisible());
    }
}