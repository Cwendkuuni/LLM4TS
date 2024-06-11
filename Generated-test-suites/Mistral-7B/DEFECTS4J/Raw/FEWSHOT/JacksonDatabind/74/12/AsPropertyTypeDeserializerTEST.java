package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.annotation.As.PROPERTY;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private JavaType createTestType() {
        return mock(JavaType.class);
    }

    private TypeIdResolver createTypeIdResolver() {
        return mock(TypeIdResolver.class);
    }

    private AsPropertyTypeDeserializer createTestSubject(JavaType bt, TypeIdResolver idRes, String typePropertyName,
            boolean typeIdVisible, JavaType defaultImpl) {
        return new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, PROPERTY);
    }

    private AsPropertyTypeDeserializer createTestSubject(JavaType bt, TypeIdResolver idRes, String typePropertyName,
            boolean typeIdVisible, JavaType defaultImpl, As inclusion) {
        return new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
    }

    private Object deserializeWithNativeTypeId(JsonParser p, DeserializationContext ctxt, Object typeId) throws IOException {
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        when(idRes.idFromEmptyContext(p)).thenReturn(typeId);
        AsPropertyTypeDeserializer testSubject = createTestSubject(mock(JavaType.class), idRes, "", false, null);
        return testSubject.deserializeTypedFromObject(p, ctxt);
    }

    private Object deserializeWithoutNativeTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
        AsPropertyTypeDeserializer testSubject = createTestSubject(mock(JavaType.class), mock(TypeIdResolver.class),
                "", false, null);
        return testSubject.deserializeTypedFromObject(p, ctxt);
    }

    private Object deserializeWithPropertyName(JsonParser p, DeserializationContext ctxt) throws IOException {
        AsPropertyTypeDeserializer testSubject = createTestSubject(mock(JavaType.class), mock(TypeIdResolver.class),
                "typePropertyName", false, null);
        return testSubject.deserializeTypedFromObject(p, ctxt);
    }

    private Object deserializeWithDefaultImpl(JsonParser p, DeserializationContext ctxt, TokenBuffer tb) throws IOException {
        AsPropertyTypeDeserializer testSubject = createTestSubject(mock(JavaType.class), mock(TypeIdResolver.class),
                "", false, mock(JavaType.class), As.PROPERTY);
        return testSubject.deserializeTypedUsingDefaultImpl(p, ctxt, tb);
    }

    private Object deserializeWithNativeTypeIdAndPropertyName(JsonParser p, DeserializationContext ctxt, Object typeId) throws IOException {
        AsPropertyTypeDeserializer testSubject = createTestSubject(mock(JavaType.class), mock(TypeIdResolver.class),
                "typePropertyName", false, null);
        return testSubject._deserializeWithNativeTypeId(p, ctxt, typeId);
    }

    @Test
    public void testDeserializeTypedFromObject_withNativeTypeId() throws IOException {
        String typeId = "typeId";
        Object result = deserializeWithNativeTypeId(mock(JsonParser.class), mock(DeserializationContext.class), typeId);
        // verify result
        assertInstanceOf(Object.class, result);
        // TODO: add more assertions here as necessary
    }

    @Test
    public void testDeserializeTypedFromObject_withoutNativeTypeId() throws IOException {
        Object result = deserializeWithoutNativeTypeId(mock(JsonParser.class), mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_withPropertyName() throws IOException {
        Object result = deserializeWithPropertyName(mock(JsonParser.class), mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_withDefaultImpl() throws IOException {
        Object result = deserializeWithDefaultImpl(mock(JsonParser.class), mock(DeserializationContext.class), null);
        // verify result
        assertInstanceOf(Object.class, result);
        // TODO: add more assertions here as necessary
    }

    @Test
    public void testDeserializeTypedFromObject_withNativeTypeIdAndPropertyName() throws IOException {
        String typeId = "typeId";
        Object result = deserializeWithNativeTypeIdAndPropertyName(mock(JsonParser.class), mock(DeserializationContext.class), typeId);
        // verify result
        assertInstanceOf(Object.class, result);
        // TODO: add more assertions here as necessary
    }
}