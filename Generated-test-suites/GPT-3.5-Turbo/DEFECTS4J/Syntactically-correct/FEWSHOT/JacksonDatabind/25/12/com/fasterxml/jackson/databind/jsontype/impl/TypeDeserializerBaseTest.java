package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        JavaType baseType = ClassUtil.typeFrom(null, Object.class);
        TypeIdResolver idResolver = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());

        baseType = ClassUtil.typeFrom(null, String.class);
        typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        JavaType baseType = ClassUtil.typeFrom(null, Object.class);
        TypeIdResolver idResolver = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("type", typeDeserializerBase.getPropertyName());

        typePropertyName = "class";
        typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("class", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        JavaType baseType = ClassUtil.typeFrom(null, Object.class);
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertSame(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType baseType = ClassUtil.typeFrom(null, Object.class);
        TypeIdResolver idResolver = null;
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(String.class, typeDeserializerBase.getDefaultImpl());

        defaultImpl = Integer.class;
        typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(Integer.class, typeDeserializerBase.getDefaultImpl());
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {

        protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
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

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
            return null;
        }
    }
}