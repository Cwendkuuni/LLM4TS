package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        String baseTypeName = deserializer.baseTypeName();

        assertEquals("java.lang.Object", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, "type", true, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        String propertyName = deserializer.getPropertyName();

        assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = new TypeIdResolver() {
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
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, idResolver, null, true, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        TypeIdResolver typeIdResolver = deserializer.getTypeIdResolver();

        assertEquals(idResolver, typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = JavaType.construct(Object.class);

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, defaultImpl) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        Class<?> defaultImplClass = deserializer.getDefaultImpl();

        assertEquals(Object.class, defaultImplClass);
    }

    @Test
    public void testBaseType() {
        JavaType baseType = JavaType.construct(Object.class);

        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, null, null, true, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        JavaType deserializerBaseType = deserializer.baseType();

        assertEquals(baseType, deserializerBaseType);
    }
}