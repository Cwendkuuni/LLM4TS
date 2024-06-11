package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.As;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName,
                typeIdVisible, defaultImpl);

        assertNotNull(deserializer);
        assertEquals(bt, deserializer.baseType());
        assertEquals(idRes, deserializer._idResolver);
        assertEquals(typePropertyName, deserializer._typePropertyName);
        assertEquals(typeIdVisible, deserializer._typeIdVisible);
        assertEquals(defaultImpl, deserializer._defaultImpl);
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        assertNull(result);
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        assertNull(result);
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        assertNull(result);
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        assertNull(result);
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void test_deserialize() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class);

        deserializer._deserialize(p, ctxt);

        verify(deserializer, times(1))._deserialize(p, ctxt);
        verify(deserializer, times(1))._findDeserializer(ctxt, "");
    }

    @Test(expected=com.fasterxml.jackson.databind.JsonMappingException.class)
    public void test_deserialize_wrongTokenException() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class);

        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer._deserialize(p, ctxt);
    }

    @Test
    public void test_deserialize_withTypeId() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class);

        when(p.canReadTypeId()).thenReturn(true);

        deserializer._deserialize(p, ctxt);

        verify(deserializer, times(1))._deserializeWithNativeTypeId(p, ctxt, null);
        verify(deserializer, never())._findDeserializer(ctxt, "");
    }

    @Test(expected=com.fasterxml.jackson.databind.JsonMappingException.class)
    public void test_deserialize_withTypeId_wrongTokenException() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        AsWrapperTypeDeserializer deserializer = mock(AsWrapperTypeDeserializer.class);

        when(p.canReadTypeId()).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer._deserialize(p, ctxt);
    }

}