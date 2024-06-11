package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;


public class AsPropertyTypeDeserializerTest {

    // Mock objects
    private JavaType btMock = mock(JavaType.class);
    private TypeIdResolver idResMock = mock(TypeIdResolver.class);
    private JavaType defaultImplMock = mock(JavaType.class);

    @Test
    public void testAsPropertyTypeDeserializer_withDefaultInclusion() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock);

        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testAsPropertyTypeDeserializer_withCustomInclusion() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock, As.WRAPPER_ARRAY);

        assertEquals(As.WRAPPER_ARRAY, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject_withTypeId() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        TokenBuffer tbMock = mock(TokenBuffer.class);
        Object resultMock = mock(Object.class);

        when(jpMock.canReadTypeId()).thenReturn(true);
        when(jpMock.getTypeId()).thenReturn("typeId");
        when(deserializer._deserializeWithNativeTypeId(jpMock, ctxtMock, "typeId")).thenReturn(resultMock);

        assertEquals(resultMock, deserializer.deserializeTypedFromObject(jpMock, ctxtMock));

        verify(jpMock).canReadTypeId();
        verify(jpMock).getTypeId();
        verify(deserializer).deserializeTypedFromObject(jpMock, ctxtMock);
        verify(deserializer)._deserializeWithNativeTypeId(jpMock, ctxtMock, "typeId");
    }

    @Test
    public void testDeserializeTypedFromObject_withStartObjectToken() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        TokenBuffer tbMock = mock(TokenBuffer.class);
        Object resultMock = mock(Object.class);

        when(jpMock.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jpMock.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jpMock.getCurrentName()).thenReturn("typeProperty");
        when(jpMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jpMock.getText()).thenReturn("typeId");
        when(deserializer._deserializeTypedForId(jpMock, ctxtMock, tbMock)).thenReturn(resultMock);

        assertEquals(resultMock, deserializer.deserializeTypedFromObject(jpMock, ctxtMock));

        verify(jpMock).getCurrentToken();
        verify(jpMock).nextToken();
        verify(jpMock).getCurrentName();
        verify(jpMock).nextToken();
        verify(jpMock).getText();
        verify(deserializer).deserializeTypedFromObject(jpMock, ctxtMock);
        verify(deserializer)._deserializeTypedForId(jpMock, ctxtMock, tbMock);
    }

    @Test
    public void testDeserializeTypedFromObject_withUnknownTokenAndDefaultImpl() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        TokenBuffer tbMock = mock(TokenBuffer.class);
        Object resultMock = mock(Object.class);

        when(jpMock.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jpMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jpMock.getCurrentName()).thenReturn("unknownProperty");
        when(defaultImplMock.getRawClass()).thenReturn(Object.class);
        when(deserializer._findDefaultImplDeserializer(ctxtMock)).thenReturn(new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return resultMock;
            }
        });

        assertEquals(resultMock, deserializer.deserializeTypedFromObject(jpMock, ctxtMock));

        verify(jpMock).getCurrentToken();
        verify(jpMock).nextToken();
        verify(jpMock).getCurrentName();
        verify(defaultImplMock).getRawClass();
        verify(deserializer)._findDefaultImplDeserializer(ctxtMock);
    }

    @Test
    public void testDeserializeTypedFromObject_withDefaultImplAndNaturalValue() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        TokenBuffer tbMock = mock(TokenBuffer.class);
        Object resultMock = mock(Object.class);

        when(jpMock.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jpMock.readValueAs(any(Class.class))).thenReturn(resultMock);

        assertEquals(resultMock, deserializer.deserializeTypedFromObject(jpMock, ctxtMock));

        verify(jpMock).getCurrentToken();
        verify(jpMock).readValueAs(Object.class);
    }

    @Test
    public void testDeserializeTypedFromObject_withArrayToken() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        TokenBuffer tbMock = mock(TokenBuffer.class);
        Object resultMock = mock(Object.class);

        when(jpMock.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        when(deserializer.deserializeTypedFromAny(jpMock, ctxtMock)).thenReturn(resultMock);

        assertEquals(resultMock, deserializer.deserializeTypedFromObject(jpMock, ctxtMock));

        verify(jpMock).getCurrentToken();
        verify(deserializer).deserializeTypedFromObject(jpMock, ctxtMock);
        verify(deserializer).deserializeTypedFromAny(jpMock, ctxtMock);
    }

    @Test
    public void testDeserializeTypedFromAny_withArrayToken() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(btMock, idResMock, "typeProperty", true, defaultImplMock);
        JsonParser jpMock = mock(JsonParser.class);
        DeserializationContext ctxtMock = mock(DeserializationContext.class);
        TokenBuffer tbMock = mock(TokenBuffer.class);
        Object resultMock = mock(Object.class);

        when(jpMock.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        when(deserializer.deserializeTypedFromArray(jpMock, ctxtMock)).thenReturn(resultMock);

        assertEquals(resultMock, deserializer.deserializeTypedFromAny(jpMock, ctxtMock));

        verify(jpMock).getCurrentToken();
        verify(deserializer).deserializeTypedFromArray(jpMock, ctxtMock);
    }

}