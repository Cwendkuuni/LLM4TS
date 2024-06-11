package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testEmptyBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testBuilderWithSettings() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {});
        assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
        assertNotNull(builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testBuildTypeSerializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerWithEmptySubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeSerializer(new SerializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeSerializerWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
        assertNull(builder.buildTypeSerializer(new SerializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeSerializerWithNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerWithNullCollection() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeSerializer(new SerializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeSerializerWithPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertInstanceOf(AsPropertyTypeSerializer.class, typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperObjectInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertInstanceOf(AsWrapperTypeSerializer.class, typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithWrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertInstanceOf(AsArrayTypeSerializer.class, typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithExternalPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertInstanceOf(AsExternalTypeSerializer.class, typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithExistingPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertInstanceOf(AsExistingPropertyTypeSerializer.class, typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.defaultImpl(String.class);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertInstanceOf(AsPropertyTypeSerializer.class, typeSerializer);
        assertEquals(String.class, ((AsPropertyTypeSerializer) typeSerializer).getDefaultImpl());
    }

    @Test
    public void testBuildTypeSerializerWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeIdVisibility(true);
        TypeIdResolver customIdResolver = new TypeIdResolver() {
            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public JavaType resolvedType(DeserializationConfig config,
                    BeanDescription beanDesc, String id) throws JsonMappingException {
                return null;
            }
        };
        builder.setCustomIdResolver(customIdResolver);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), null, null);
        assertInstanceOf(AsPropertyTypeSerializer.class, typeSerializer);
        assertEquals(customIdResolver, ((AsPropertyTypeSerializer) typeSerializer).getIdResolver());
    }

    @Test
    public void testBuildTypeDeserializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithEmptySubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeDeserializer(new DeserializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
        assertNull(builder.buildTypeDeserializer(new DeserializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithNullCollection() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        assertNull(builder.buildTypeDeserializer(new DeserializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertInstanceOf(AsPropertyTypeDeserializer.class, typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperObjectInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertInstanceOf(AsWrapperTypeDeserializer.class, typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithWrapperArrayInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertInstanceOf(AsArrayTypeDeserializer.class, typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithExternalPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertInstanceOf(AsExternalTypeDeserializer.class, typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithExistingPropertyInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertInstanceOf(AsExistingPropertyTypeDeserializer.class, typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.defaultImpl(String.class);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertInstanceOf(AsPropertyTypeDeserializer.class, typeDeserializer);
        assertEquals(String.class, ((AsPropertyTypeDeserializer) typeDeserializer).getDefaultImpl());
    }

    @Test
    public void testBuildTypeDeserializerWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeIdVisibility(true);
        TypeIdResolver customIdResolver = new TypeIdResolver() {
            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public JavaType resolvedType(DeserializationConfig config,
                    BeanDescription beanDesc, String id) throws JsonMappingException {
                return null;
            }
        };
        builder.setCustomIdResolver(customIdResolver);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
        assertInstanceOf(AsPropertyTypeDeserializer.class, typeDeserializer);
        assertEquals(customIdResolver, ((AsPropertyTypeDeserializer) typeDeserializer).getIdResolver());
    }

    @Test
    public void testSetters() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.typeProperty("myTypeProperty");
        builder.defaultImpl(String.class);
        builder.typeIdVisibility(true);
        assertEquals("myTypeProperty", builder.getTypeProperty());
        assertEquals(String.class, builder.getDefaultImpl());
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public JavaType resolvedType(DeserializationConfig config,
                    BeanDescription beanDesc, String id) throws JsonMappingException {
                return null;
            }
        };
        builder.setCustomIdResolver(idResolver);
        assertEquals(idResolver, builder.getCustomIdResolver());
    }
}