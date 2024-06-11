package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("java.lang.Object", deserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, "type", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("type", deserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = new TypeIdResolver() {
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
            public String getDescForKnownTypeIds() {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, resolver, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals(resolver, deserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType(null, null) {
            @Override
            public JavaType withContentType(JavaType contentType) {
                return null;
            }

            @Override
            public JavaType withTypeHandler(Object h) {
                return null;
            }

            @Override
            protected String _buildCanonicalName(int cnt) {
                return null;
            }

            @Override
            public JavaType narrowBy(Class<?> subclass) {
                return null;
            }

            @Override
            public boolean isArrayType() {
                return false;
            }

            @Override
            public boolean isInterface() {
                return false;
            }

            @Override
            public boolean isReferenceType() {
                return false;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals(Object.class, deserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]",
                deserializer.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertThrows(NullPointerException.class, () -> deserializer._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithType() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertThrows(NullPointerException.class, () -> deserializer._deserializeWithNativeTypeId(jp, ctxt, null));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertThrows(JsonMappingException.class, () -> deserializer._handleUnknownTypeId(ctxt, null, null, null));
    }
}