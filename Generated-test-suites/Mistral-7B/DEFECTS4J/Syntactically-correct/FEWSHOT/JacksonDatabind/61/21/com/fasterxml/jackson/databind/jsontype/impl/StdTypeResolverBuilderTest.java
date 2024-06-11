package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setUp() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit_NullIdType() {
        try {
            builder.init(null, null);
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInit_NullIdRes() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull("Expected null", builder._customIdResolver);
    }

    @Test
    public void testInit_ValidArgs() {
        builder.init(JsonTypeInfo.Id.CLASS, new IdResolverAnnotatedClass());
        assertSame("Expected IdResolverAnnotatedClass", builder._customIdResolver, new IdResolverAnnotatedClass());
    }

    @Test
    public void testBuildTypeSerializer_NullConfig() {
        assertNull("Expected null", builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializer_NullBaseType() {
        assertNull("Expected null", builder.buildTypeSerializer(new MapperConfig(), null, null));
    }

    @Test
    public void testBuildTypeSerializer_NullSubtypes() {
        assertNull("Expected null", builder.buildTypeSerializer(new MapperConfig(), null, null));
    }

    @Test
    public void testBuildTypeSerializer_EmptySubtypes() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull("Expected null", builder.buildTypeSerializer(new MapperConfig(), null, new ArrayList<>()));
    }

    @Test
    public void testBuildTypeSerializer_ValidArgs() throws JsonMappingException {
        MapperConfig config = new MapperConfig();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        subtypes.add(new SimpleType("List", config.getTypeFactory().constructType(Integer[].class)));
        TypeIdResolver idRes = new TypeNameIdResolver(config, baseType, subtypes, true, false);

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull("Expected non-null", serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializer_UnknownIncludeType() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeSerializer(new MapperConfig(), null, null);
    }

    @Test
    public void testBuildTypeDeserializer_NullConfig() {
        assertNull("Expected null", builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer_NullBaseType() {
        assertNull("Expected null", builder.buildTypeDeserializer(new DeserializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeDeserializer_NullSubtypes() {
        assertNull("Expected null", builder.buildTypeDeserializer(new DeserializationConfig(), null, null));
    }

    @Test
    public void testBuildTypeDeserializer_EmptySubtypes() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull("Expected null", builder.buildTypeDeserializer(new DeserializationConfig(), null, new ArrayList<>()));
    }

    @Test
    public void testBuildTypeDeserializer_ValidArgs() throws JsonMappingException, JsonParseException {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = config.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        subtypes.add(new SimpleType("List", config.getTypeFactory().constructType(Integer[].class)));
        TypeIdResolver idRes = new TypeNameIdResolver(config, baseType, subtypes, false, true);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull("Expected non-null", deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializer_UnknownIncludeType() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.buildTypeDeserializer(new DeserializationConfig(), null, null);
    }

    @Test
    public void testTypeProperty_NullArg() {
        builder.typeProperty(null);
        assertEquals("Expected default property name", JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testTypeProperty_EmptyArg() {
        builder.typeProperty("");
        assertEquals("Expected default property name", JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testTypeProperty_ValidArg() {
        builder.typeProperty("myProp");
        assertEquals("Expected 'myProp'", "myProp", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl_NullArg() {
        builder.defaultImpl(null);
        assertNull("Expected null", builder._defaultImpl);
    }

    @Test
    public void testDefaultImpl_ValidArg() {
        builder.defaultImpl(Integer.class);
        assertEquals("Expected Integer.class", Integer.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility_NullArg() {
        builder.typeIdVisibility(false);
        assertFalse("Expected false", builder._typeIdVisible);
    }

    @Test
    public void testTypeIdVisibility_ValidArg() {
        builder.typeIdVisibility(true);
        assertTrue("Expected true", builder._typeIdVisible);
    }
}

class IdResolverAnnotatedClass {}