package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeIdResolver;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link StdTypeResolverBuilder}.
 */
public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setUp() {
        builder = new StdTypeResolverBuilder();
    }

    /**
     * Test instantiation.
     */
    @Test
    public void testInstantiation() {
        assertNotNull(builder);
    }

    /**
     * Test null initialization.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInitialization() {
        builder.init(null, null);
    }

    /**
     * Test initialization with null typeId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeId() {
        builder.init(JsonTypeInfo.Id.class, null);
    }

    /**
     * Test initialization with null idResolver.
     */
    @Test
    public void testNullIdResolver() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder._customIdResolver);
    }

    /**
     * Test initialization with valid arguments.
     */
    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new StdTypeIdResolver();
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
    }

    /**
     * Test null typeIdProperty when building type deserializer.
     */
    @Test
    public void testNullTypeIdPropertyDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    /**
     * Test empty typeIdProperty when building type deserializer.
     */
    @Test
    public void testEmptyTypeIdPropertyDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("");
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    /**
     * Test valid typeIdProperty when building type deserializer.
     */
    @Test
    public void testValidTypeIdPropertyDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("typeIdProperty");
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
    }

    /**
     * Test null defaultImpl when building type deserializer.
     */
    @Test
    public void testNullDefaultImplDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    /**
     * Test valid defaultImpl when building type deserializer.
     */
    @Test
    public void testValidDefaultImplDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(String.class);
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfiguration();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Collections.emptyList();
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    /**
     * Test null forSer in buildTypeSerializer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullForSerSerializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeSerializer(null, null, null);
    }

    /**
     * Test valid arguments in buildTypeSerializer.
     */
    @Test
    public void testValidArgumentsBuilderSerializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfiguration();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "subtype"));
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
    }

    /**
     * Test null forDeser in buildTypeDeserializer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullForDeserDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeDeserializer(null, null, null);
    }

    /**
     * Test valid arguments in buildTypeDeserializer.
     */
    @Test
    public void testValidArgumentsBuilderDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfiguration();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "subtype"));
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    /**
     * Test null inclusion in buildTypeDeserializer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInclusionDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeDeserializer(null, null, null);
    }

    /**
     * Test invalid inclusion in buildTypeDeserializer.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidInclusionDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.values()[10]);
        builder.buildTypeDeserializer(null, null, null);
    }

    /**
     * Test valid inclusion in buildTypeDeserializer.
     */
    @Test
    public void testValidInclusionDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        ObjectMapper mapper = new ObjectMapper();
        MapperConfig<?> config = mapper.getConfiguration();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "subtype"));
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    /**
     * Test null typeIdResolver in buildTypeSerializer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeIdResolverSerializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeSerializer(null, null, null, true, false);
    }

    /**
     * Test null idResolver in buildTypeDeserializer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullIdResolverDeserializer() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeDeserializer(null, null, null, false, true);
    }
}

class StdTypeIdResolver extends TypeIdResolverBase {
    @Override
    public TypeId idFromValue(Object value) {
        return new SimpleTypeId(ClassIdType.Name.STRING_CLASS_ID, null);
    }
}

class StdTypeResolverBuilderTest {
    private static class NoClass {}
}