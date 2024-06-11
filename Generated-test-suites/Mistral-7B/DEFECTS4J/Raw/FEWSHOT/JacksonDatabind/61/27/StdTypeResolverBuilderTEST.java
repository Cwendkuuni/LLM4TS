package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("", builder._typeProperty);
        assertFalse(builder._typeIdVisible);
        assertNull(builder._defaultImpl);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder._idType);

        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);

        builder.init(JsonTypeInfo.Id.CUSTOM, new CustomTypeIdResolver());
        assertEquals(JsonTypeInfo.Id.CUSTOM, builder._idType);
        assertNotNull(builder._customIdResolver);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        // Test with null baseType and subtypes
        assertNull(builder.buildTypeSerializer(null, null, null));

        // Test with empty baseType and subtypes
        JavaType baseType = JavaTypeFactory.instance.constructSimpleType(String.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        assertNull(builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes));

        // Test with non-NONE _idType
        builder.init(JsonTypeInfo.Id.CLASS, null);
        TypeSerializer serializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        // Test with null baseType and subtypes
        assertNull(builder.buildTypeDeserializer(null, null, null));

        // Test with empty baseType and subtypes
        JavaType baseType = JavaTypeFactory.instance.constructSimpleType(String.class);
        Collection<NamedType> subtypes = new ArrayList<>();
        assertNull(builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes));

        // Test with non-NONE _idType
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typeIdPropName");
        assertEquals("typeIdPropName", builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }
}

class CustomTypeIdResolver implements TypeIdResolver {
    // Implement the TypeIdResolver interface here
}