package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertSame(idType, builder.getIdType());
        assertSame(idRes, builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, mock(TypeIdResolver.class));
    }

    @Test
    public void testBuildTypeSerializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new HashSet<>();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        TypeSerializer result = builder.buildTypeSerializer(null, baseType, subtypes);
        assertNull(result);

        TypeSerializer result2 = builder.buildTypeSerializer(null, baseType, subtypes);
        assertNull(result2);

        TypeSerializer result3 = builder.buildTypeSerializer(mock(SerializationConfig.class), baseType, subtypes);
        assertNull(result3);

        TypeSerializer result4 = builder.buildTypeSerializer(mock(SerializationConfig.class), baseType, subtypes);
        assertNull(result4);

        when(idRes.resolveId(baseType)).thenReturn("MyTypeId");
        when(mock(TypeFactory.class).constructType("java.lang.String")).thenReturn(JavaType.construct("java.lang.String"));
        when(mock(TypeFactory.class).constructSpecializedType(JavaType.construct("java.lang.Object"), JavaType.construct("java.lang.String"))).thenReturn(JavaType.construct("java.lang.String"));

        result = builder.buildTypeSerializer(mock(SerializationConfig.class), baseType, subtypes);
        assertNull(result);

        JavaType defaultImpl = JavaType.construct("com.example.MyType");
        builder.defaultImpl(defaultImpl);
        result = builder.buildTypeSerializer(mock(SerializationConfig.class), baseType, subtypes);
        assertSame(defaultImpl, result.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownType() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new HashSet<>();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        builder.buildTypeSerializer(mock(SerializationConfig.class), baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new HashSet<>();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        TypeDeserializer result = builder.buildTypeDeserializer(null, baseType, subtypes);
        assertNull(result);

        TypeDeserializer result2 = builder.buildTypeDeserializer(null, baseType, subtypes);
        assertNull(result2);

        TypeDeserializer result3 = builder.buildTypeDeserializer(mock(DeserializationConfig.class), baseType, subtypes);
        assertNull(result3);

        TypeDeserializer result4 = builder.buildTypeDeserializer(mock(DeserializationConfig.class), baseType, subtypes);
        assertNull(result4);

        when(idRes.resolveId(baseType)).thenReturn("MyTypeId");
        when(mock(TypeFactory.class).constructType("java.lang.String")).thenReturn(JavaType.construct("java.lang.String"));
        when(mock(TypeFactory.class).constructSpecializedType(JavaType.construct("java.lang.Object"), JavaType.construct("java.lang.String"))).thenReturn(JavaType.construct("java.lang.String"));

        result = builder.buildTypeDeserializer(mock(DeserializationConfig.class), baseType, subtypes);
        assertNull(result);

        JavaType defaultImpl = JavaType.construct("com.example.MyType");
        builder.defaultImpl(defaultImpl);
        result = builder.buildTypeDeserializer(mock(DeserializationConfig.class), baseType, subtypes);
        assertSame(defaultImpl, result.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerUnknownType() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new HashSet<>();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        builder.buildTypeDeserializer(mock(DeserializationConfig.class), baseType, subtypes);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("MyTypeProperty");
        assertEquals("MyTypeProperty", builder.getTypeProperty());
    }

    @Test
    public void testTypePropertyEmpty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("");
        assertEquals(JsonTypeInfo.Id.DEFAULT.getDefaultPropertyName(), builder.getTypeProperty());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
        assertSame(Object.class, builder.getDefaultImpl());

        builder.defaultImpl(String.class);
        assertSame(String.class, builder.getDefaultImpl());

        builder.defaultImpl(NoClass.class);
        assertSame(NoClass.class, builder.getDefaultImpl());

        builder.defaultImpl(Void.class);
        assertSame(Void.class, builder.getDefaultImpl());

        builder.defaultImpl(Integer.class);
        assertSame(Integer.class, builder.getDefaultImpl());
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder.getIncludeAs());

        builder.inclusion(null);
        assertNull(builder.getIncludeAs());
    }

    @Test
    public void testIdResolver() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertSame(idRes, builder.getCustomIdResolver());

        when(idRes.resolveId(mock(JavaType.class))).thenReturn("MyTypeId");
        builder.typeProperty("typeProperty");
        assertEquals("MyTypeId", builder.getTypeProperty());

        builder.defaultImpl(JavaType.construct("java.lang.String"));
        assertEquals("MyTypeId", builder.getIdType().getDefaultPropertyName());
    }
}