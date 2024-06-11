package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        builder.init(Id.CLASS, null);
        assertEquals(Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        builder._idType = Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        builder._typeProperty = "class";

        StdTypeResolverBuilder spyBuilder = spy(builder);
        // mocked behavior
        when(spyBuilder.idResolver(
                any(MapperConfig.class), any(JavaType.class),
                any(Collection.class), eq(true), eq(false)))
            .thenReturn(mock(TypeIdResolver.class));

        TypeSerializer serializer = spyBuilder.buildTypeSerializer(
                mock(SerializationConfig.class),
                mock(JavaType.class),
                mock(Collection.class));

        assertTrue(serializer instanceof AsArrayTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithNoIdType() {
        builder._idType = Id.NONE;

        assertNull(builder.buildTypeSerializer(
                mock(SerializationConfig.class),
                mock(JavaType.class),
                mock(Collection.class)));
    }

    @Test
    public void testBuildTypeSerializerWithInvalidIncludeAs() {
        builder._idType = Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;

        exception.expect(IllegalStateException.class);
        exception.expectMessage("Do not know how to construct standard type serializer for inclusion type: EXTERNAL_PROPERTY");

        builder.buildTypeSerializer(
                mock(SerializationConfig.class),
                mock(JavaType.class),
                mock(Collection.class));
    }

    @Test
    public void testBuildTypeDeserializer() {
        builder._idType = Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        builder._typeProperty = "class";

        StdTypeResolverBuilder spyBuilder = spy(builder);
        // mocked behavior
        when(spyBuilder.idResolver(
                any(MapperConfig.class), any(JavaType.class),
                any(Collection.class), eq(false), eq(true)))
            .thenReturn(mock(TypeIdResolver.class));

        TypeDeserializer deserializer = spyBuilder.buildTypeDeserializer(
                mock(DeserializationConfig.class),
                mock(JavaType.class),
                mock(Collection.class));

        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithNoIdType() {
        builder._idType = Id.NONE;

        assertNull(builder.buildTypeDeserializer(
                mock(DeserializationConfig.class),
                mock(JavaType.class),
                mock(Collection.class)));
    }

    @Test
    public void testBuildTypeDeserializerWithInvalidIncludeAs() {
        builder._idType = Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;

        exception.expect(IllegalStateException.class);
        exception.expectMessage("Do not know how to construct standard type serializer for inclusion type: EXTERNAL_PROPERTY");

        builder.buildTypeDeserializer(
                mock(DeserializationConfig.class),
                mock(JavaType.class),
                mock(Collection.class));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        builder.typeProperty("type");
        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testTypePropertyWithEmptyString() {
        builder.typeProperty("");
        assertEquals(Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        builder.defaultImpl(ArrayList.class);
        assertEquals(ArrayList.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        builder.typeIdVisibility(true);
        assertEquals(true, builder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        builder._defaultImpl = ArrayList.class;
        assertEquals(ArrayList.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        builder._typeProperty = "type";
        assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        builder._typeIdVisible = true;
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder spyBuilder = spy(builder);
        // mocked behavior
        when(spyBuilder.idResolver(
                any(MapperConfig.class), any(JavaType.class),
                any(Collection.class), eq(true), eq(false)))
            .thenReturn(mock(TypeIdResolver.class));

        assertNotNull(spyBuilder.idResolver(
                mock(MapperConfig.class),
                mock(JavaType.class),
                mock(Collection.class),
                true,
                false));
    }

    @Test(expected=IllegalStateException.class)
    public void testIdResolverWithNullCustomIdResolverAndNullIdType() {
        builder.idResolver(
                mock(MapperConfig.class),
                mock(JavaType.class),
                mock(Collection.class),
                true,
                false);
    }

    @Test
    public void testIdResolverWithClassIdType() {
        builder._idType = Id.CLASS;
        builder._customIdResolver = null;
        assertNotNull(builder.idResolver(
                mock(MapperConfig.class),
                mock(JavaType.class),
                mock(Collection.class),
                true,
                false));
    }

    @Test(expected=IllegalStateException.class)
    public void testIdResolverWithNoneIdType() {
        builder._idType = Id.NONE;
        builder._customIdResolver = null;
        assertNull(builder.idResolver(
                mock(MapperConfig.class),
                mock(JavaType.class),
                mock(Collection.class),
                true,
                false));
    }

    @Test(expected=IllegalStateException.class)
    public void testIdResolverWithCustomIdType() {
        builder._idType = Id.CUSTOM;
        builder._customIdResolver = null;
        assertNull(builder.idResolver(
                mock(MapperConfig.class),
                mock(JavaType.class),
                mock(Collection.class),
                true,
                false));
    }
}