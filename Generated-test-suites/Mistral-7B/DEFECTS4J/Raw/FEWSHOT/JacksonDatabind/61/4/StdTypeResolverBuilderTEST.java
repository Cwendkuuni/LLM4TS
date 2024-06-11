package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.ClassIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeResolverBuilder.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.jsontype.impl.TypeResolverBuilder.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        Id idType = Id.CLASS;
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.getIdType()).thenReturn(idType);
        builder.init(idType, idResolver);

        assertNotNull(builder._idType);
        assertNotNull(builder._customIdResolver);
        assertEquals(idType, builder._idType);
        assertEquals(idResolver, builder._customIdResolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, mock(TypeIdResolver.class));
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testBuildTypeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "foo"),
                new NamedType(String.class, "bar"));

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "foo"),
                new NamedType(String.class, "bar"));

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(As.PROPERTY);
        assertEquals(As.PROPERTY, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludeNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("foo");
        assertEquals("foo", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Object.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "foo"),
                new NamedType(String.class, "bar"));

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(idResolver);

        ClassIdResolver classIdResolver = (ClassIdResolver) idResolver;
        assertEquals(Id.CLASS, classIdResolver.getIdType());

        MinimalClassNameIdResolver minimalClassNameIdResolver = (MinimalClassNameIdResolver) idResolver;
        assertNull(minimalClassNameIdResolver.getIdType());

        TypeNameIdResolver typeNameIdResolver = (TypeNameIdResolver) idResolver;
        assertEquals(config, typeNameIdResolver.getConfig());
        assertEquals(baseType, typeNameIdResolver.getBaseType());
        assertEquals(subtypes, typeNameIdResolver.getSubtypes());
        assertEquals(true, typeNameIdResolver.isForSerialization());
        assertEquals(false, typeNameIdResolver.isForDeserialization());
    }
}