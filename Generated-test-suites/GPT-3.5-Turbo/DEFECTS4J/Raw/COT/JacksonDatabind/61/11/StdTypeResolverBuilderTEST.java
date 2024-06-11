
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collection;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        Assert.assertNotNull(builder._idType);
        Assert.assertNull(builder._customIdResolver);
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        Assert.assertNotNull(builder._idType);
        Assert.assertNull(builder._customIdResolver);
        Assert.assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder._idType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, null);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        Assert.assertNotNull(builder._includeAs);
        Assert.assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        Assert.assertNotNull(builder._includeAs);
        Assert.assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("type");
        Assert.assertNotNull(builder._typeProperty);
        Assert.assertEquals("type", builder._typeProperty);

        builder.typeProperty("");
        Assert.assertNotNull(builder._typeProperty);
        Assert.assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
        Assert.assertNotNull(builder._defaultImpl);
        Assert.assertEquals(Object.class, builder._defaultImpl);

        builder.defaultImpl(null);
        Assert.assertNull(builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder._typeIdVisible);

        builder.typeIdVisibility(false);
        Assert.assertFalse(builder._typeIdVisible);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        builder.typeIdVisibility(true);

        TestTypeSerializer typeSerializer = (TestTypeSerializer) builder.buildTypeSerializer(null, null, null);
        Assert.assertNotNull(typeSerializer);
        Assert.assertEquals(builder._typeProperty, typeSerializer.getPropertyName());
        Assert.assertEquals(builder._typeIdVisible, typeSerializer.getTypeIdVisible());
        Assert.assertEquals(builder._defaultImpl, typeSerializer.getDefaultImpl());
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        builder.typeIdVisibility(true);

        TestTypeDeserializer typeDeserializer = (TestTypeDeserializer) builder.buildTypeDeserializer(null, null, null);
        Assert.assertNotNull(typeDeserializer);
        Assert.assertEquals(builder._typeProperty, typeDeserializer.getPropertyName());
        Assert.assertEquals(builder._typeIdVisible, typeDeserializer.getTypeIdVisible());
        Assert.assertEquals(builder._defaultImpl, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
        builder.defaultImpl(Object.class);
        builder.typeIdVisibility(true);

        TestIdResolver idResolver = (TestIdResolver) builder.idResolver(null, null, null, true, true);
        Assert.assertNotNull(idResolver);
    }
    
    private static class TestTypeDeserializer extends TypeDeserializer {
        public TestTypeDeserializer(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        public String getPropertyName() {
            return _typePropertyName;
        }

        public boolean getTypeIdVisible() {
            return _typeIdVisible;
        }

        public JavaType getDefaultImpl() {
            return _defaultImpl;
        }
    }

    private static class TestTypeSerializer extends TypeSerializer {
        public TestTypeSerializer(TypeIdResolver idRes, Class<?> defImpl) {
            super(idRes, defImpl);
        }

        public String getPropertyName() {
            return _typePropertyName;
        }

        public boolean getTypeIdVisible() {
            return _typeIdVisible;
        }

        public Class<?> getDefaultImpl() {
            return _defaultImpl;
        }
    }
    
    private static class TestIdResolver implements TypeIdResolver {

        @Override
        public void init(JavaType baseType) {

        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
    }

}
