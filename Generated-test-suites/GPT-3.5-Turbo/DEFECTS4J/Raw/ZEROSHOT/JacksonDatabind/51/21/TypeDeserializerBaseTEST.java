package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(Object.class);
        typeDeserializerBase = new TypeDeserializerBaseTestImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        Assert.assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testGetTypeName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        Assert.assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = typeDeserializerBase.getTypeIdResolver();
        Assert.assertNotNull(resolver);
        Assert.assertTrue(resolver instanceof TypeIdResolverImpl);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        Assert.assertNotNull(defaultImpl);
        Assert.assertEquals(Object.class, defaultImpl);
    }

    private class TypeIdResolverImpl implements TypeIdResolver {

        private JavaType baseType;

        @Override
        public void init(JavaType baseType) {
            this.baseType = baseType;
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
        public JavaType typeFromId(DatabindContext context, String id) throws IOException {
            return baseType;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
    }

    private class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {

        public TypeDeserializerBaseTestImpl(JavaType baseType, TypeIdResolver idRes,
                                             String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }
}