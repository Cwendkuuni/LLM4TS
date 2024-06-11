package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

@RunWith(MockitoJUnitRunner.class)
public class AsWrapperTypeDeserializerTest {

    @Mock
    private JsonParser jp;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private TypeDeserializer deserializer;

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsWrapperTypeDeserializer wrapper = new AsWrapperTypeDeserializer(null, null, null, true, null);
        Object result = new Object();
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn(result);
        Object obj = wrapper.deserializeTypedFromObject(jp, ctxt);
        assertEquals(result, obj);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        AsWrapperTypeDeserializer wrapper = new AsWrapperTypeDeserializer(null, null, null, true, null);
        Object result = new Object();
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn(result);
        Object obj = wrapper.deserializeTypedFromArray(jp, ctxt);
        assertEquals(result, obj);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        AsWrapperTypeDeserializer wrapper = new AsWrapperTypeDeserializer(null, null, null, true, null);
        Object result = new Object();
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn(result);
        Object obj = wrapper.deserializeTypedFromScalar(jp, ctxt);
        assertEquals(result, obj);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsWrapperTypeDeserializer wrapper = new AsWrapperTypeDeserializer(null, null, null, true, null);
        Object result = new Object();
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn(result);
        Object obj = wrapper.deserializeTypedFromAny(jp, ctxt);
        assertEquals(result, obj);
    }

    @Test
    public void testDeserialize() throws IOException {
        AsWrapperTypeDeserializer wrapper = new AsWrapperTypeDeserializer(null, null, null, true, null);
        TokenBuffer tb = new TokenBuffer(null, false);
        tb.writeStartObject();
        tb.writeFieldName("typeId");
        tb.writeString("typeIdValue");
        tb.writeFieldName("value");
        tb.writeInt(10);
        tb.writeEndObject();
        JsonParser p = tb.asParser(jp);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING,
                JsonToken.FIELD_NAME, JsonToken.VALUE_NUMBER_INT, JsonToken.END_OBJECT);
        when(jp.getText()).thenReturn("typeIdValue");
        when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(10);
        Object obj = wrapper.deserialize(p, ctxt);
        assertEquals(10, obj);
    }
    
}