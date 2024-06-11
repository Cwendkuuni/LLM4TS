package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializerBase;
    private JavaType baseType;
    private TypeIdResolver idRes;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;

    @Before
    public void setup() {
        baseType = mock(JavaType.class);
        idRes = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = mock(JavaType.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
            
        };
    }

    @Test
    public void testBaseTypeName() {
        String expected = baseType.getRawClass().getName();
        String actual = typeDeserializerBase.baseTypeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        String expected = typePropertyName;
        String actual = typeDeserializerBase.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver expected = idRes;
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> expected = ClassUtil.rawClass(defaultImpl);
        Class<?> actual = typeDeserializerBase.getDefaultImpl();
        assertEquals(expected, actual);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDeserializeWithNativeTypeId_WithNullTypeId() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        assertNull(actual);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDeserializeWithNativeTypeId_WithNonNullTypeId() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "typeId";
        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNull(actual);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        assertNull(actual);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String extraDesc = "extraDesc";
        JavaType actual = typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);
        assertNull(actual);
    }

    @Test
    public void testToString() {
        String expected = "[" + typeDeserializerBase.getClass().getName() + "; base-type:" + baseType
                + "; id-resolver: " + idRes + "]";
        String actual = typeDeserializerBase.toString();
        assertEquals(expected, actual);
    }
}