package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        // create a mock TypeIdResolver
        TypeIdResolver typeIdResolver = new TypeIdResolver() {
            @Override
            public void init(JavaType arg0) {}

            @Override
            public String idFromBaseType() {
                return null;
            }

            @Override
            public String idFromValue(Object arg0) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object arg0, Class<?> arg1) {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext arg0, String arg1) {
                return null;
            }
            
        };
        // create a mock JavaType
        JavaType baseType = new JavaType(TypeDeserializerBase.class);
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, typeIdResolver, "typePropertyName", true, ClassUtil.defaultClass());
    }

    @Test
    public void testGetTypeName() {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetProperty() {
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertTrue(typeDeserializerBase.getTypeIdResolver() instanceof TypeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertEquals(NullifyingDeserializer.instance, typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        typeDeserializerBase._handleUnknownTypeId(ctxt, "typeId", typeDeserializerBase.getTypeIdResolver(), typeDeserializerBase._baseType);
    }

    private class TypeDeserializerBaseMock extends TypeDeserializerBase {
        private static final long serialVersionUID = 1L;

        protected TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible,
                Class<?> defaultImpl) {
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
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                TypeIdResolver idResolver, JavaType baseType) throws IOException {
            throw new com.fasterxml.jackson.databind.JsonMappingException("");
        }
    }
}