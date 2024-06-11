package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, mock(TypeIdResolver.class));
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._defaultImpl);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, mock(TypeIdResolver.class));
    }

    @Test
    public void testBuildTypeSerializerWithNoTypeInfo() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerWithNoTypeInfo() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertSame(builder, builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY));
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
        assertSame(builder, builder.typeProperty("myTypeProperty"));
        assertEquals("myTypeProperty", builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNullTypeIdPropName() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertSame(builder, builder.typeProperty(null));
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._defaultImpl);
        assertSame(builder, builder.defaultImpl(JavaType.class));
        assertEquals(JavaType.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertFalse(builder._typeIdVisible);
        assertSame(builder, builder.typeIdVisibility(true));
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testIdResolverWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        TypeIdResolver customIdResolver = mock(TypeIdResolver.class);
        assertSame(customIdResolver, builder.idResolver(null, null, null, false, false));
    }

    @Test
    public void testIdResolverWithClassIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        MapperConfig<?> config = mock(MapperConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = null;
        boolean forSer = false;
        boolean forDeser = false;

        assertEquals(ClassNameIdResolver.class, builder.idResolver(config, baseType, subtypes, forSer, forDeser).getClass());
    }

    @Test
    public void testIdResolverWithMinimalClassIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);

        MapperConfig<?> config = mock(MapperConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = null;
        boolean forSer = false;
        boolean forDeser = false;

        assertEquals(MinimalClassNameIdResolver.class, builder.idResolver(config, baseType, subtypes, forSer, forDeser).getClass());
    }

    @Test
    public void testIdResolverWithNameIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);

        MapperConfig<?> config = mock(MapperConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = null;
        boolean forSer = false;
        boolean forDeser = false;

        assertEquals(TypeNameIdResolver.class, builder.idResolver(config, baseType, subtypes, forSer, forDeser).getClass());
    }

    @Test
    public void testIdResolverWithCustomIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CUSTOM, null);

        MapperConfig<?> config = mock(MapperConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = null;
        boolean forSer = false;
        boolean forDeser = false;

        try {
            builder.idResolver(config, baseType, subtypes, forSer, forDeser);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}