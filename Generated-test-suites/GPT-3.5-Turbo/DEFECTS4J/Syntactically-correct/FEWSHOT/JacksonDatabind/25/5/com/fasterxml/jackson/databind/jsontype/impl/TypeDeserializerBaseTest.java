package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
    @Test
    public void testForProperty() {
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty prop = null;
        TypeDeserializerBase result = typeDeserializerBase.forProperty(prop);
        assertEquals(baseType, result.baseType);
        assertEquals(idRes, result._idResolver);
        assertEquals(typePropertyName, result._typePropertyName);
        assertEquals(typeIdVisible, result._typeIdVisible);
        assertEquals(defaultImpl, result._defaultImpl.getRawClass());
        assertNull(result._property);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(null, null, "", true, null);
        TypeDeserializerBase.JsonTypeInfoType result = typeDeserializerBase.getTypeInclusion();
        assertEquals(TypeDeserializerBase.JsonTypeInfoType.NONE, result);
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(null, null, "", true, null);
        String result = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.Object", result);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(null, null, "", true, null);
        String result = typeDeserializerBase.getPropertyName();
        assertEquals("", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(null, null, "", true, null);
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(null, null, "", true, null);
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNull(result);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(null, null, "", true, null);
        String result = typeDeserializerBase.toString();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl; base-type:null; id-resolver: null]", result);
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {
        private static final long serialVersionUID = 1L;

        protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.NONE;
        }

        public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }

        public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }

        public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }

        public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }

        public Object deserializeTypedFromAny(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
            return null;
        }

        public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
            return null;
        }

        public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
            return null;
        }

        public Object deserializeTypedFromScalar(JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser) throws IOException {
            return null;
        }

        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }

        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws IOException {
            throw JsonMappingException.from(ctxt, "Unrecognized type id: " + typeId);
        }
    }
}