package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeDeserializerBaseTest {
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, "type", false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertEquals("type", deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeNameResolver() {
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
            public JavaType typeFromId(DatabindContext context, String id) throws IOException {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, idResolver, null, false, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertEquals(idResolver, deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return String.class;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, defaultImpl) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };

        assertEquals(String.class, deserializer.getDefaultImpl());
    }
}