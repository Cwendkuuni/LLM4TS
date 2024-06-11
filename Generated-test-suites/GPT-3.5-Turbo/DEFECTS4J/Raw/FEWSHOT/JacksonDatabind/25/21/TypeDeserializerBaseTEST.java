package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        assertNull(deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        assertNull(deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        assertNull(deserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", deserializer.toString());
    }

    @Test
    public void test_findDeserializer() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt,
                    String typeId) throws IOException {
                return null;
            }
        };
        assertNull(deserializer._findDeserializer(null, ""));
    }

    @Test
    public void test_findDefaultImplDeserializer() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        assertNull(deserializer._findDefaultImplDeserializer(null));
    }

    @Test
    public void test_handleUnknownTypeId() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase() {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return null;
            }
        };
        try {
            deserializer._handleUnknownTypeId(null, "", null, null);
            fail("Should have thrown an exception");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }
}