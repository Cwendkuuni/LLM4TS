package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl();
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl();
        assertEquals("_typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl();
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl();
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    static class TypeDeserializerBaseImpl extends TypeDeserializerBase {

        TypeDeserializerBaseImpl() {
            super(JavaType.unknownType(), new TypeIdResolverImpl(), "_typePropertyName", true, JavaType.unknownType());
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        static class TypeIdResolverImpl implements TypeIdResolver {

            @Override
            public void init(JavaType baseType) {}

            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> type) {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext ctxt, String id) {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
        }
    }
}