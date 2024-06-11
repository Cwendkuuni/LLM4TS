package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase deserializer;

    @Test
    public void testConstructor() {
        JavaType baseType = new JavaType();
        TypeIdResolver idRes = new TypeIdResolverImpl();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = new JavaType();
        deserializer = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = new BeanPropertyImpl();
        TypeDeserializerBase newDeserializer = deserializer.forProperty(prop);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = deserializer.getTypeInclusion();
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = deserializer.baseTypeName();
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = deserializer.getPropertyName();
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = deserializer.getTypeIdResolver();
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = deserializer.getDefaultImpl();
    }

    @Test
    public void testToString() {
        String string = deserializer.toString();
    }

    @Test
    public void test_findDeserializer() {
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "type";
        JsonDeserializer<Object> deser = deserializer._findDeserializer(ctxt, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() {
        DeserializationContext ctxt = new DeserializationContext();
        JsonDeserializer<Object> deser = deserializer._findDefaultImplDeserializer(ctxt);
    }

    @Test
    public void test_deserializeWithNativeTypeId() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object result = deserializer._deserializeWithNativeTypeId(jp, ctxt);
    }

    @Test
    public void test_handleUnknownTypeId() {
        DeserializationContext ctxt = new DeserializationContext();
        String typeId = "type";
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        JavaType baseType = new JavaType();
        JavaType result = deserializer._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
    }

    private class JavaType { }

    private class TypeIdResolverImpl implements TypeIdResolver {

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
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public String idFromBaseType() {
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

    private class BeanPropertyImpl implements BeanProperty {

    }

}