package com.fasterxml.jackson.databind.jsontype.impl;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_NullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
    }

    @Test
    public void testBuildTypeSerializer() {
        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        builder.typeIdVisibility(true);

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        builder.typeIdVisibility(true);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusion_NullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
    }

    @Test
    public void testTypeProperty_Null() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = builder.getDefaultImpl();
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = builder.getTypeProperty();
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean typeIdVisible = builder.isTypeIdVisible();
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;
        boolean forSer = true;
        boolean forDeser = true;

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);

        TypeIdResolver idResolver = builder.idResolver(config, baseType, subtypes, forSer, forDeser);
    }
}