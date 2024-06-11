package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class DatabindContextTest {

    @Test
    public void testDatabindContext() throws Exception {
        // Initialize MapperConfig
        MapperConfig config = new MapperConfig(new AnnotationIntrospector());

        // Create DatabindContext instance
        DatabindContext context = new BaseDatabindContext() {
            @Override
            public MapperConfig<?> getConfig() {
                return config;
            }

            @Override
            public AnnotationIntrospector getAnnotationIntrospector() {
                return config.getAnnotationIntrospector();
            }

            @Override
            public boolean isEnabled(MapperFeature feature) {
                return config.isEnabled(feature);
            }

            @Override
            public boolean canOverrideAccessModifiers() {
                return config.canOverrideAccessModifiers();
            }

            @Override
            public Class<?> getActiveView() {
                return null;
            }

            @Override
            public Locale getLocale() {
                return Locale.getDefault();
            }

            @Override
            public TimeZone getTimeZone() {
                return TimeZone.getDefault();
            }

            @Override
            public JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType) {
                return null;
            }

            @Override
            public Object getAttribute(Object key) {
                return null;
            }

            @Override
            public DatabindContext setAttribute(Object key, Object value) {
                return this;
            }

            @Override
            public JavaType constructType(Type type) {
                return TypeFactory.defaultInstance().constructType(type);
            }

            @Override
            public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
                return null;
            }

            @Override
            public JavaType resolveSubType(JavaType baseType, String subClass) {
                return null;
            }

            @Override
            public <T> T reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
                throw new JsonMappingException(msg + " for type: " + type);
            }

            @Override
            public <T> T reportBadDefinition(Class<?> type, String msg) throws JsonMappingException {
                return reportBadDefinition(TypeFactory.defaultInstance().constructType(type), msg);
            }
        };

        // Test generic config access
        MapperConfig mConfig = context.getConfig();
        AnnotationIntrospector aIntrospector = context.getAnnotationIntrospector();
        Assert.assertNotNull(mConfig);
        Assert.assertNotNull(aIntrospector);

        // Test access to specific config settings
        Assert.assertTrue(context.isEnabled(MapperFeature.DEFAULT_INTROSPECTION));
        Assert.assertTrue(context.canOverrideAccessModifiers());

        // Test access to generic attributes
        context.setAttribute("test", "testValue");
        Object testAttribute = context.getAttribute("test");
        Assert.assertEquals("testValue", testAttribute);

        // Test type instantiation/resolution
        Type refType = new TypeReference<List<Foo>>(){}.getType();
        JavaType javaType = context.constructType(refType);
        Assert.assertNotNull(javaType);

        JavaType baseType = context.getTypeFactory().constructType(Foo.class);
        JavaType specializedType = context.constructSpecializedType(baseType, Bar.class);
        Assert.assertNotNull(specializedType);

        JavaType resolvedType = context.resolveSubType(baseType, "com.fasterxml.jackson.databind.test.Baz");
        Assert.assertNotNull(resolvedType);

        // Test error reporting
        JavaType invalidType = context.getTypeFactory().constructType("com.fasterxml.jackson.databind.test.InvalidType");
        try {
            context.reportBadDefinition(invalidType, "Invalid type definition: " + _desc(invalidType.getClassName()));
        } catch (JsonMappingException e) {
            Assert.assertEquals("Invalid type definition: [com.fasterxml.jackson.databind.test.InvalidType]", e.getMessage());
        }
    }

    // Test classes used in test
    @JsonAutoDetect
    static class Foo {
        @JsonProperty
        private int id;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Bar.class, name = "bar")
    })
    static class Bar extends Foo {
        @JsonProperty
        private String name;
    }

    static class InvalidType {
        // empty class for test purpose
    }
}