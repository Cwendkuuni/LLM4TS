
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder createStdTypeResolverBuilder() {
        return new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        StdTypeResolverBuilder result = builder.init(JsonTypeInfo.Id.CLASS, null);

        assertNotNull(result);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializerForNoneIdType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NONE;
        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        assertNull(builder.buildTypeSerializer(config, baseType, subtypes));
    }

    @Test
    public void testBuildTypeSerializerForWrapperArrayType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Object.class;

        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
        AsArrayTypeSerializer typeSerializerImpl = (AsArrayTypeSerializer) typeSerializer;
        assertEquals(builder._includeAs, typeSerializerImpl._inclusion);
        assertNotNull(typeSerializerImpl._idResolver);
        assertNull(typeSerializerImpl._elementType);
    }

    @Test
    public void testBuildTypeSerializerForPropertyType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.PROPERTY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Object.class;

        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(typeSerializer instanceof AsPropertyTypeSerializer);
        AsPropertyTypeSerializer typeSerializerImpl = (AsPropertyTypeSerializer) typeSerializer;
        assertEquals(builder._includeAs, typeSerializerImpl._inclusion);
        assertNotNull(typeSerializerImpl._idResolver);
        assertNull(typeSerializerImpl._property);
        assertNull(typeSerializerImpl._typeInclusion);
    }

    @Test
    public void testBuildTypeSerializerForWrapperObjectType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Object.class;

        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(typeSerializer instanceof AsWrapperTypeSerializer);
        AsWrapperTypeSerializer typeSerializerImpl = (AsWrapperTypeSerializer) typeSerializer;
        assertEquals(builder._includeAs, typeSerializerImpl._inclusion);
        assertNotNull(typeSerializerImpl._idResolver);
        assertNull(typeSerializerImpl._property);
        assertNull(typeSerializerImpl._typeInclusion);
    }

    @Test
    public void testBuildTypeSerializerForExternalPropertyType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Object.class;

        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(typeSerializer instanceof AsExternalTypeSerializer);
        AsExternalTypeSerializer typeSerializerImpl = (AsExternalTypeSerializer) typeSerializer;
        assertEquals(builder._includeAs, typeSerializerImpl._inclusion);
        assertNotNull(typeSerializerImpl._idResolver);
        assertNull(typeSerializerImpl._property);
        assertNull(typeSerializerImpl._typeInclusion);
    }

    @Test
    public void testBuildTypeSerializerForExistingPropertyType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Object.class;

        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertTrue(typeSerializer instanceof AsExistingPropertyTypeSerializer);
        AsExistingPropertyTypeSerializer typeSerializerImpl = (AsExistingPropertyTypeSerializer) typeSerializer;
        assertEquals(builder._includeAs, typeSerializerImpl._inclusion);
        assertNotNull(typeSerializerImpl._idResolver);
        assertNull(typeSerializerImpl._property);
        assertNull(typeSerializerImpl._typeInclusion);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerForInvalidIncludeAs() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = null;

        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerForInvalidInclusionType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = null;

        SerializationConfig config = mock(SerializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerForNoneIdType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NONE;
        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        assertNull(builder.buildTypeDeserializer(config, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializerForWrapperObjectType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Object.class;

        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);
        AsWrapperTypeDeserializer typeDeserializerImpl = (AsWrapperTypeDeserializer) typeDeserializer;
        assertEquals(builder._includeAs, typeDeserializerImpl._inclusion);
        assertNotNull(typeDeserializerImpl._idResolver);
        assertNull(typeDeserializerImpl._typeProperty);
        assertNull(typeDeserializerImpl._typeInclusion);
    }

    @Test
    public void testBuildTypeDeserializerForExistingPropertyType() {
        StdTypeResolverBuilder builder = createStdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.CLASS;
        builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;
        builder._typeProperty = "type";
        builder._typeIdVisible = true;
        builder._defaultImpl = Object.class;

        DeserializationConfig config = mock(DeserializationConfig.class);
        JavaType baseType = mock(JavaType.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);
        AsPropertyTypeDeserializer typeDeserializerImpl = (AsPropertyTypeDeserializer) typeDeserializer;
        assertEquals(builder._includeAs, typeDeserializerImpl._inclusion);
        assertNotNull(typeDeserializerImpl._idResolver);
        assertNull(typeDeserializerImpl._typeProperty);
        assertNull(typeDeserializerImpl._typeInclusion);
    }
}
