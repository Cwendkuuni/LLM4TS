package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class StdTypeResolverBuilderTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.DEFAULT_USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);
    }

    @Test
    public void testNewInstance() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInit() throws JsonMappingException {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        Class<?> defaultImpl = String.class;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        assertEquals(idType, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        JsonTypeInfo.Id idType2 = JsonTypeInfo.Id.MINIMAL_CLASS;
        TypeIdResolver idRes = new TypeIdResolver() {
            @Override
            public TypeId idFromValueAndType(Object value, JavaType type) {
                return null;
            }

            @Override
            public JavaType idFromValue(Object value) {
                return null;
            }

            @Override
            public JavaType idFromValueAndType(Object value, JavaType type, boolean allowEmpty) {
                return null;
            }

            @Override
            public JavaType idFromValue(Object value, JavaType valueType, JavaType containerType, boolean allowEmpty) {
                return null;
            }
        };
        builder.init(idType2, idRes);
        assertEquals(idType2, builder.getIdType());
        assertEquals(idRes, builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testBuildTypeSerializer() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> subtypes = new java.util.ArrayList<>();
        subtypes.add(new NamedType("Foo", String.class));
        TypeIdResolver idRes = mapper.getTypeFactory().constructValueTypeIdResolver(baseType, subtypes);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.PROPERTY, idRes);
        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownInclusionType() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> subtypes = new java.util.ArrayList<>();
        TypeIdResolver idRes = mapper.getTypeFactory().constructValueTypeIdResolver(baseType, subtypes);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.FOO, idRes);
        builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializer() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> subtypes = new java.util.ArrayList<>();
        TypeIdResolver idRes = mapper.getTypeFactory().constructValueTypeIdResolver(baseType, subtypes);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.PROPERTY, idRes);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerUnknownInclusionType() throws Exception {
        JavaType baseType = mapper.getTypeFactory().constructType(Object.class);
        Collection<NamedType> subtypes = new java.util.ArrayList<>();
        TypeIdResolver idRes = mapper.getTypeFactory().constructValueTypeIdResolver(baseType, subtypes);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.FOO, idRes);
        builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("foo");
        assertEquals("foo", builder.getTypeProperty());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testInclude() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
    }
}