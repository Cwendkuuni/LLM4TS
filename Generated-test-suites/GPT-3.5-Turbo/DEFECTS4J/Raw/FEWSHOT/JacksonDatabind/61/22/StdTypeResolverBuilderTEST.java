
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInitShouldThrowExceptionIfIdTypeIsNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testInitShouldSetIdTypeAndCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, new CustomTypeIdResolver());
        
        assertEquals(JsonTypeInfo.Id.NAME, builder._idType);
        assertEquals(CustomTypeIdResolver.class, builder._customIdResolver.getClass());
    }

    @Test
    public void testBuildTypeSerializerShouldReturnNullIfIdTypeIsNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NONE;

        TypeSerializer result = builder.buildTypeSerializer(null, null, null);

        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerShouldThrowExceptionForUnknownIncludeAsType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder._idType = JsonTypeInfo.Id.NAME;
        builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;

        builder.buildTypeSerializer(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoTypeInfoBuilderShouldThrowExceptionIfIdTypeIsNull() {
        StdTypeResolverBuilder.noTypeInfoBuilder();
    }

    @Test
    public void testNoTypeInfoBuilderShouldInitializeIdTypeAsNone() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();

        assertEquals(JsonTypeInfo.Id.NONE, builder._idType);
    }

    @Test
    public void testDefaultImplShouldSetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Integer.class);

        assertEquals(Integer.class, builder._defaultImpl);
    }

    @Test
    public void testTypePropertyShouldSetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");

        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testTypePropertyShouldSetDefaultTypePropertyIfNull() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);

        assertEquals("type", builder._typeProperty);
    }

    @Test
    public void testTypeIdVisibilityShouldSetTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);

        assertEquals(true, builder._typeIdVisible);
    }

    private static class CustomTypeIdResolver implements TypeIdResolver {

        @Override
        public void init(JavaType baseType) {}

        @Override
        public String idFromValue(Object value) { return null; }

        @Override
        public String idFromValueAndType(Object value, Class<?> valueType) { return null; }

        @Override
        public String idFromBaseType() { return null; }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) { return null; }

        @Override
        public JsonTypeInfo.Id getMechanism() { return null; }
    }
}
