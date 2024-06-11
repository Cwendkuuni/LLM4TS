package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }
        });
        assertNotNull(builder.getIdType());
        assertNotNull(builder.getCustomIdResolver());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeSerializer(null, null, null));

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.buildTypeSerializer(null, null, null));

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }
        });
        assertNull(builder.buildTypeSerializer(null, null, null));

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, Class<?> suggestedType) {
                return new TypeIdImpl(String.class.getName(), null);
            }
        });
        assertNotNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.buildTypeDeserializer(null, null, null));

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }
        });
        assertNull(builder.buildTypeDeserializer(null, null, null));

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, Class<?> suggestedType) {
                return new TypeIdImpl(String.class.getName(), null);
            }
        });
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertNotNull(builder.getIncludeAs());
        assertEqualTo(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder.getIncludeAs());
        assertEqualTo(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotNull(builder.getIncludeAs());
        assertEqualTo(JsonTypeInfo.As.WRAPPER_OBJECT, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertNotNull(builder.getIncludeAs());
        assertEqualTo(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder.getIncludeAs());

        builder.inclusion(null);
        assertNull(builder.getIncludeAs());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.getTypeProperty());

        builder.typeProperty("foo");
        assertNotNull(builder.getTypeProperty());
        assertEqualTo("foo", builder.getTypeProperty());

        builder.typeProperty(null);
        assertNull(builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.getDefaultImpl());

        builder.defaultImpl(String.class);
        assertNotNull(builder.getDefaultImpl());
        assertEqualTo(String.class, builder.getDefaultImpl());

        builder.defaultImpl(null);
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertFalse(builder.isTypeIdVisible());

        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }
        });
        assertNotNull(builder.getIdType());
        assertNotNull(builder.getCustomIdResolver());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());

        builder.init(JsonTypeInfo.Id.CLASS, new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, Class<?> suggestedType) {
                return new TypeIdImpl("foo", null);
            }
        });
        assertNotNull(builder.getIdType());
        assertNotNull(builder.getCustomIdResolver());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
    }

    private void assertEqualTo(JsonTypeInfo.As expected, JsonTypeInfo.As actual) {
        assertEquals(expected.toString(), actual.toString());
    }
}