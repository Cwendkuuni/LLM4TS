package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.test.BaseMapTest;

import org.junit.Test;

/**
 * Test class for StdTypeResolverBuilder
 */
public class StdTypeResolverBuilderTest extends BaseMapTest {

    // Test methods for public constructors

    @Test
    public void testStdTypeResolverBuilderDefaultConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testStdTypeResolverBuilderNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder._customIdResolver);
    }

    // Test methods for init()

    @Test
    public void testStdTypeResolverBuilderInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.init(null, null));
    }

    @Test
    public void testStdTypeResolverBuilderInitNullIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testStdTypeResolverBuilderInitValidParameters() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        builder.init(idType, idResolver);
        assertEquals(idType, builder.getIdType());
        assertEquals(idResolver, builder._customIdResolver);
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    // Test methods for buildTypeSerializer()

    @Test
    public void testBuildTypeSerializerNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertThrows(NullPointerException.class, () -> builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = new SerializationConfig();
        Collection<NamedType> subtypes = new ArrayList<>();
        assertThrows(NullPointerException.class, () -> builder.buildTypeSerializer(config, null, subtypes));
    }

    @Test
    public void testBuildTypeSerializerNullSubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = createType();
        assertThrows(NullPointerException.class, () -> builder.buildTypeSerializer(config, baseType, null));
    }

    @Test
    public void testBuildTypeSerializerIdTypeNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = createType();
        Collection<NamedType> subtypes = new ArrayList<>();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerValidParameters() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = createType();
        Collection<NamedType> subtypes = new ArrayList<>();
        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        // Check typeSerializer instance and properties
        assertTrue(typeSerializer instanceof TypeSerializer);
        assertNotNull(typeSerializer.getTypeIdResolver());
        // Further testing would require creation of real TypeSerializer subclasses
        // for each inclusion type and proper verification of their behavior,
        // which is beyond the scope of this test case.
    }

    // Test methods for buildTypeDeserializer()

    @Test
    public void testBuildTypeDeserializerNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertThrows(NullPointerException.class, () -> builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = new DeserializationConfig();
        Collection<NamedType> subtypes = new ArrayList<>();
        assertThrows(NullPointerException.class, () -> builder.buildTypeDeserializer(config, null, subtypes));
    }

    @Test
    public void testBuildTypeDeserializerNullSubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = createType();
        assertThrows(NullPointerException.class, () -> builder.buildTypeDeserializer(config, baseType, null));
    }

    @Test
    public void testBuildTypeDeserializerIdTypeNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = createType();
        Collection<NamedType> subtypes = new ArrayList<>();
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerValidParameters() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = createType();
        Collection<NamedType> subtypes = new ArrayList<>();
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        // Check typeDeserializer instance and properties
        assertTrue(typeDeserializer instanceof TypeDeserializer);
        assertNotNull(typeDeserializer.getTypeIdResolver());
        // Further testing would require creation of real TypeDeserializer
        // subclasses for each inclusion type and proper verification of their behavior,
        // which is beyond the scope of this test case.
    }

    // Test methods for setters

    @Test
    public void testSettersNullValues() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertThrows(NullPointerException.class, () -> builder.inclusion(null));
        assertThrows(NullPointerException.class, () -> builder.typeProperty(null));
        assertThrows(NullPointerException.class, () -> builder.defaultImpl(null));
        assertThrows(IllegalArgumentException.class, () -> builder.typeIdVisibility(123));
    }

    @Test
    public void testSettersValidParameters() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        String typeProperty = "typeProperty";
        Class<?> defaultImpl = Object.class;
        boolean typeIdVisible = true;
        builder.inclusion(includeAs);
        assertEquals(includeAs, builder.getIncludeAs());
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder.getTypeProperty());
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
        builder.typeIdVisibility(typeIdVisible);
        assertEquals(typeIdVisible, builder.isTypeIdVisible());
    }

    // Test methods for getters

    @Test
    public void testGettersDefaultValues() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder._customIdResolver);
    }

    @Test
    public void testGettersSetValues() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        String typeProperty = "typeProperty";
        Class<?> defaultImpl = Object.class;
        boolean typeIdVisible = true;
        builder.init(idType, null);
        assertEquals(idType, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder._customIdResolver);
        builder.inclusion(includeAs);
        assertEquals(includeAs, builder.getIncludeAs());
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder.getTypeProperty());
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
        builder.typeIdVisibility(typeIdVisible);
        assertEquals(typeIdVisible, builder.isTypeIdVisible());
    }

    // Helper methods

    private JavaType createType() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructType(String.class);
    }
}