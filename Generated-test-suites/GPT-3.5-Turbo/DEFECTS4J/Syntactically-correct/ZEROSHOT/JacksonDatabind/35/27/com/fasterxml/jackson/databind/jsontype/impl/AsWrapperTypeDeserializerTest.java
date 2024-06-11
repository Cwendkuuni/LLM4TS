package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;

    @Before
    public void setUp() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        verify(jp, times(2)).getCurrentToken();
        verify(jp, times(1)).nextToken();
        verify(jp, times(1)).getText();

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.getTypeId()).thenReturn(null);

        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        verify(jp, times(1)).getTypeId();

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.getTypeId()).thenReturn(null);

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        verify(jp, times(1)).getTypeId();

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(jp.getTypeId()).thenReturn(null);

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        verify(jp, times(1)).getTypeId();

        assertEquals(null, result);
    }
}