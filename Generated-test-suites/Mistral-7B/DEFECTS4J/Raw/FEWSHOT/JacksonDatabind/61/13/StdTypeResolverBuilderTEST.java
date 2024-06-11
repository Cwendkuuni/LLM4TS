package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertNull(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);

        builder.init(Id.CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);

        builder.init(Id.NAME, new TypeIdResolver() {
            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public JavaType resolvedType(MapperConfig<?> config, JavaType baseType, Object value) {
                return null;
            }
        });
        assertNotNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNotNull(builder._customIdResolver);
    }

    @Test
    public void testBuildTypeSerializer() throws JsonMappingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();

        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> namedTypes = null;

        builder.init(Id.CLASS, null)
                .typeIdVisibility(true)
                .defaultImpl(String.class)
                .inclusion(As.PROPERTY)
                .typeProperty("type");

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, javaType, namedTypes);
        assertNotNull(typeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.init(Id.CLASS, null);
        builder.buildTypeSerializer(null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithNullSubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();

        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);

        builder.init(Id.CLASS, null)
                .typeIdVisibility(true)
                .defaultImpl(String.class)
                .inclusion(As.PROPERTY)
                .typeProperty("type");

        builder.buildTypeSerializer(config, javaType, null);
    }

    @Test
    public void testBuildTypeSerializerWithNoIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();

        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> namedTypes = null;

        builder.init(null, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, javaType, namedTypes);
        assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() throws JsonMappingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();

        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> namedTypes = null;

        builder.init(Id.CLASS, null)
                .typeIdVisibility(true)
                .defaultImpl(String.class)
                .inclusion(As.PROPERTY)
                .typeProperty("type");

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, javaType, namedTypes);
        assertNotNull(typeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();

        builder.init(Id.CLASS, null);
        builder.buildTypeDeserializer(null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithNullSubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfig();

        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);

        builder.init(Id.CLASS, null)
                .typeIdVisibility(true)
                .defaultImpl(String.class)
                .inclusion(As.PROPERTY)
                .typeProperty("type");

        builder.buildTypeDeserializer(config, javaType, null);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.typeProperty("type");
        assertEquals("type", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }
}