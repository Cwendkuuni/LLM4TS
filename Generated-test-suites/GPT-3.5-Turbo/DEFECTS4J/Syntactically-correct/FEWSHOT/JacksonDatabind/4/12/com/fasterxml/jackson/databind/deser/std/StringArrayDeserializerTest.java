package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer stringArrayDeserializer;
    
    @Mock
    private JsonDeserializer<String> elementDeserializer;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private ObjectBuffer objectBuffer;
    
    @Mock
    private BeanProperty beanProperty;
    
    @Mock
    private TypeDeserializer typeDeserializer;
    
    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
        stringArrayDeserializer._elementDeserializer = elementDeserializer;
    }
    
    @Test
    public void testDeserialize() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo");
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        when(objectBuffer.resetAndStart()).thenReturn(new Object[1]);
        when(objectBuffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[] {"foo"});
        assertEquals(new String[] {"foo"}, stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
        verify(jsonParser, times(2)).nextToken();
        verify(jsonParser).getText();
        verify(deserializationContext).leaseObjectBuffer();
        verify(objectBuffer).resetAndStart();
        verify(objectBuffer).completeAndClearBuffer(any(), anyInt(), any());
        verify(deserializationContext).returnObjectBuffer(objectBuffer);
    }
    
    @Test
    public void testDeserializeCustomWithNullValue() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(elementDeserializer.getNullValue()).thenReturn(null);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        when(objectBuffer.resetAndStart()).thenReturn(new Object[1]);
        when(objectBuffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[] {null});
        assertEquals(new String[] {null}, stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
        verify(jsonParser, times(2)).nextToken();
        verify(elementDeserializer).getNullValue();
        verify(deserializationContext).leaseObjectBuffer();
        verify(objectBuffer).resetAndStart();
        verify(objectBuffer).completeAndClearBuffer(any(), anyInt(), any());
        verify(deserializationContext).returnObjectBuffer(objectBuffer);
    }
    
    @Test
    public void testDeserializeCustomWithNonNullValue() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("foo");
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        when(objectBuffer.resetAndStart()).thenReturn(new Object[1]);
        when(objectBuffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[] {"foo"});
        assertEquals(new String[] {"foo"}, stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
        verify(jsonParser, times(2)).nextToken();
        verify(elementDeserializer).deserialize(jsonParser, deserializationContext);
        verify(deserializationContext).leaseObjectBuffer();
        verify(objectBuffer).resetAndStart();
        verify(objectBuffer).completeAndClearBuffer(any(), anyInt(), any());
        verify(deserializationContext).returnObjectBuffer(objectBuffer);
    }
    
    @Test
    public void testDeserializeEmpty() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        assertNull(stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
        verify(jsonParser).isExpectedStartArrayToken();
        verify(deserializationContext).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(jsonParser).getCurrentToken();
    }
    
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayWithException() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    }
    
    @Test
    public void testHandleNonArrayWithEmptyStringAsNull() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.getText()).thenReturn("");
        assertNull(stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
        verify(jsonParser).isExpectedStartArrayToken();
        verify(deserializationContext).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(jsonParser).getCurrentToken();
        verify(deserializationContext).isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        verify(jsonParser).getText();
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        assertNull(stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
        verify(typeDeserializer).deserializeTypedFromArray(jsonParser, deserializationContext);
    }
    
    @Test
    public void testCreateContextualWithConvertingContentDeserializer() throws Exception {
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(false);
        assertNull(stringArrayDeserializer.createContextual(deserializationContext, beanProperty));
        verify(deserializationContext).constructType(String.class);
        verify(deserializationContext).handleSecondaryContextualization(elementDeserializer, beanProperty);
        verifyZeroInteractions(beanProperty);
    }
    
    @Test
    public void testCreateContextualWithDefaultDeserializer() throws Exception {
        when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(true);
        assertNull(stringArrayDeserializer.createContextual(deserializationContext, beanProperty));
        verify(deserializationContext).constructType(String.class);
        verify(deserializationContext, never()).handleSecondaryContextualization(elementDeserializer, beanProperty);
        verifyZeroInteractions(beanProperty);
    }
}