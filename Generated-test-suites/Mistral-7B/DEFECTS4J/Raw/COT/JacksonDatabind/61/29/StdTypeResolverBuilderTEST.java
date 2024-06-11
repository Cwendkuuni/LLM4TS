package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

import org.junit.Test;

/**
 * Unit tests for {@link StdTypeResolverBuilder} class.
 */
public class StdTypeResolverBuilderTest {

    @Test
    public void testStdTypeResolverBuilder_DefaultConstruction() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder._idType);
        assertNull(builder._includeAs);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testStdTypeResolverBuilder_InitWithNullValues() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertNull(builder._idType);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testStdTypeResolverBuilder_InitWithValidValues() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.valueOf("java.lang.Integer"), null);
        builder.init(idType, idRes);
        assertEquals(Id.CLASS, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertNull(builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializerWithNullParams() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(result);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeSerializerWithValidParams() throws JsonMappingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = new MapperConfig();
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.valueOf("java.lang.Integer"), null);
        JavaType baseType = JavaType.valueOf("com.test.MyClass");
        Collection<NamedType> subtypes = new ArrayList<>();
        NamedType subtype1 = new NamedType(JavaType.valueOf("com.test.SubType1"), "subType1");
        subtypes.add(subtype1);
        NamedType subtype2 = new NamedType(JavaType.valueOf("com.test.SubType2"), "subtype2");
        subtypes.add(subtype2);

        builder.init(idType, idRes);
        TypeSerializer result = builder.buildTypeSerializer(config, baseType, subtypes);

        // TODO: Add assertions based on the structure and behavior of the TypeSerializer instance
    }

    @Test(expected = IllegalStateException.class)
    public void testStdTypeResolverBuilder_BuildTypeSerializerWithInvalidIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        builder.buildTypeSerializer(null, null, null, true, false);
        fail("Expected IllegalStateException");
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializerWithNullParams() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(result);
    }

    @Test
    public void testStdTypeResolverBuilder_BuildTypeDeserializerWithValidParams() throws JsonMappingException {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = new MapperConfig();
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new MinimalClassNameIdResolver(JavaType.valueOf("java.lang.Integer"), null);
        JavaType baseType = JavaType.valueOf("com.test.MyClass");
        Collection<NamedType> subtypes = new ArrayList<>();
        NamedType subtype1 = new NamedType(JavaType.valueOf("com.test.SubType1"), "subType1");
        subtypes.add(subtype1);
        NamedType subtype2 = new NamedType(JavaType.valueOf("com.test.SubType2"), "subtype2");
        subtypes.add(subtype2);

        builder.init(idType, idRes);
        TypeDeserializer result = builder.buildTypeDeserializer(config, baseType, subtypes);

        // TODO: Add assertions based on the structure and behavior of the TypeDeserializer instance
    }

    @Test(expected = IllegalStateException.class)
    public void testStdTypeResolverBuilder_BuildTypeDeserializerWithInvalidIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(Id.CLASS, null);
        builder.buildTypeDeserializer(null, null, null, false, true);
        fail("Expected IllegalStateException");
    }

    @Test
    public void testStdTypeResolverBuilder_GetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testStdTypeResolverBuilder_GetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("myTypeProperty");
        assertEquals("myTypeProperty", builder.getTypeProperty());
    }

    @Test
    public void testStdTypeResolverBuilder_IsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilder_IdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig config = new MapperConfig();
        Id idType = Id.CLASS;
        JavaType baseType = JavaType.valueOf("java.lang.Integer");
        Collection<NamedType> subtypes = new ArrayList<>();
        NamedType subtype = new NamedType(JavaType.valueOf("java.lang.Long"), "long");
        subtypes.add(subtype);
        TypeIdResolver result = builder.idResolver(config, baseType, subtypes, true, false);

        // TODO: Add assertions based on the structure and behavior of the TypeIdResolver instance
    }
}