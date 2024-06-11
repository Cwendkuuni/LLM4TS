package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.inference.StdTypeResolverBuilder;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {
    StdTypeResolverBuilder stdTypeResolverBuilder;

    @Before
    public void setUp() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        assertNotNull(stdTypeResolverBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        stdTypeResolverBuilder.init(null, new ClassNameIdResolver(null, null));
    }

    @Test
    public void testBuildTypeSerializerWithIdTypeNone() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        TypeSerializer typeSerializer = stdTypeResolverBuilder.buildTypeSerializer(null, null, null);
        assertNull(typeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeAs() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        stdTypeResolverBuilder.buildTypeSerializer(null, null, null);
    }

    @Test
    public void testBuildTypeDeserializerWithIdTypeNone() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        TypeDeserializer typeDeserializer = stdTypeResolverBuilder.buildTypeDeserializer(null, null, null);
        assertNull(typeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnknownIncludeAs() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        stdTypeResolverBuilder.buildTypeDeserializer(null, null, null);
    }

    @Test
    public void testInclusion() {
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, stdTypeResolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        stdTypeResolverBuilder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        stdTypeResolverBuilder.typeProperty("type");
        assertEquals("type", stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithEmptyString() {
        stdTypeResolverBuilder.typeProperty("");
        assertEquals("__type", stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNullString() {
        stdTypeResolverBuilder.typeProperty(null);
        assertEquals("__type", stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        stdTypeResolverBuilder.defaultImpl(Object.class);
        assertEquals(Object.class, stdTypeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        stdTypeResolverBuilder.typeIdVisibility(true);
        assertTrue(stdTypeResolverBuilder._typeIdVisible);
    }

    @Test
    public void testIdResolverWithCustomIdResolver() {
        TypeIdResolver customIdResolver = new ClassNameIdResolver(null, null);
        stdTypeResolverBuilder._customIdResolver = customIdResolver;

        assertNull(stdTypeResolverBuilder.idResolver(null, null, null, false, false));
    }

    @Test
    public void testIdResolverWithCustomIdResolverAndNullIdType() {
        TypeIdResolver customIdResolver = new ClassNameIdResolver(null, null);
        stdTypeResolverBuilder._customIdResolver = customIdResolver;

        try {
            stdTypeResolverBuilder.idResolver(null, null, null, true, false);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalStateException e) {
            assertEquals("Can not build, 'init()' not yet called", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdResolverWithNullSubsetOfClassesForIdTypeClass() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        stdTypeResolverBuilder.idResolver(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, null, null, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdResolverWithNullSubtypeResolverForIdTypeMinimalClass() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.MINIMAL_CLASS, new MinimalClassNameIdResolver(null, null));
        stdTypeResolverBuilder.idResolver(ObjectMapper.DefaultTyping.NON_FINAL, null, null, true, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIdResolverWithNullSubtypeResolverForIdTypeName() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        stdTypeResolverBuilder.idResolver(ObjectMapper.DefaultTyping.NON_FINAL, null, null, true, false);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithUnknownIdType() {
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        stdTypeResolverBuilder.idResolver(ObjectMapper.DefaultTyping.NON_FINAL, null, null, true, false);
    }
}