package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
    }

    @Test
    public void testDeserializeTypedFromObject_WithTypeId() throws IOException {
        when(jsonParser.canReadTypeId()).thenReturn(true);
        when(jsonParser.getTypeId()).thenReturn("typeId");

        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        verify(jsonParser, times(1)).canReadTypeId();
        verify(jsonParser, times(1)).getTypeId();
        verify(deserializationContext, never()).wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString());
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithoutTypeId() throws IOException {
        when(jsonParser.canReadTypeId()).thenReturn(true);
        when(jsonParser.getTypeId()).thenReturn(null);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_NULL, JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        verify(jsonParser, times(1)).canReadTypeId();
        verify(jsonParser, times(1)).getTypeId();
        verify(jsonParser, times(4)).nextToken();
        verify(deserializationContext, never()).wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString());
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        Object result = deserializer.deserializeTypedFromArray(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        Object result = deserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        assertNull(result);
    }
}