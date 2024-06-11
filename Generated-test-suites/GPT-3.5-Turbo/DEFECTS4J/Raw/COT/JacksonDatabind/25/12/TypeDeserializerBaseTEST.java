
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseSubclass(null, null);
        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseSubclass(null, null);
        assertNull(deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolverStub idResolver = new TypeIdResolverStub();
        TypeDeserializerBase deserializer = new TypeDeserializerBaseSubclass(null, idResolver);
        assertEquals(idResolver, deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseSubclass(null, null);
        assertNull(deserializer.getDefaultImpl());

        deserializer = new TypeDeserializerBaseSubclass(null, null, Integer.class);
        assertEquals(Integer.class, deserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TypeDeserializerBaseSubclass(null, null);
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]",
                deserializer.toString());
    }

    private static class TypeDeserializerBaseSubclass extends TypeDeserializerBase {
        public TypeDeserializerBaseSubclass(JavaType baseType, TypeIdResolver idRes) {
            super(baseType, idRes, null, false, null);
        }

        public TypeDeserializerBaseSubclass(JavaType baseType, TypeIdResolver idRes, Class<?> defaultImpl) {
            super(baseType, idRes, null, false, defaultImpl);
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

    private static class TypeIdResolverStub implements TypeIdResolver {

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
        public JavaType typeFromId(DatabindContext context, String id, BeanProperty property) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public Id getMechanism() {
            return null;
        }
    }

}
