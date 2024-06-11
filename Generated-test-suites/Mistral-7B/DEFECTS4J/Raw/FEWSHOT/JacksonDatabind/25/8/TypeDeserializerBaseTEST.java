package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null);
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, String.class);
        assertEquals(String.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(null, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerNonDefaultTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                if ("listType".equals(typeId)) {
                    return new ListTypeDeserializer();
                }
                return super._findDeserializer(ctxt, typeId);
            }
        };
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(null, "listType");
        assertNotNull(deserializer);
        assertEquals(ListTypeDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDeserializerDefaultTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return new NullifyingDeserializer();
            }
        };
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(null, null);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.class, deserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null);
        typeDeserializerBase._findDeserializer(null, "unknownTypeId");
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null);
        assertNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null);
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(Object.class, null, null, false, null);
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.startsWith("[TypeDeserializerBase"));
        assertTrue(toString.endsWith("]"));
    }

    private static class BaseTypeDeserializerBase extends TypeDeserializerBase {
        BaseTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        BaseTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
    }

    private static class ListTypeDeserializer extends StdDeserializer<List<?>> {
        ListTypeDeserializer() {
            this(null);
        }

        ListTypeDeserializer(Class<?> vc) {
            this(null);
        }

        ListTypeDeserializer(Class<?> vc, JsonDeserializer<?> contentDeser) {
            this(null);
            this._valueClass = vc;
            this._contentDeserializer = contentDeser;
        }

        @Override
        public List<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.isExpectedStartArrayToken()) {
                List<Object> value = new ArrayList<>();
                while (p.nextToken() != null) {
                    value.add(ctxt.readValue(_valueClass));
                }
                p.nextToken(JsonToken.END_ARRAY);
                return value;
            } else {
                throw ctxt.mappingException("Not starting array as expected");
            }
        }
    }
}