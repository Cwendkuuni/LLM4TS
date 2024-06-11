package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // setup
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);

        String[] expected = { "value" };
        
        // test
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        
        // verify
        assertEquals(expected.length, result.length);
        assertEquals(expected[0], result[0]);
        verify(jp, times(2)).nextToken();
        verify(jp).getText();
    }

    @Test
    public void testDeserialize_CustomElementDeserializer() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);

        // setup
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn(null);

        String[] expected = { null };
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        
        // test
        String[] result = deserializer.deserialize(jp, ctxt);
        
        // verify
        assertEquals(expected.length, result.length);
        assertEquals(expected[0], result[0]);
        verify(jp, times(2)).nextToken();
        verify(elementDeserializer).deserialize(jp, ctxt);
    }

    @Test
    public void testDeserialize_NonArray() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // setup
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        String[] expected = { "value" };
        
        // test
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        
        // verify
        assertEquals(expected.length, result.length);
        assertEquals(expected[0], result[0]);
        verify(jp).getCurrentToken();            
        verify(jp).getText();
    }
    
    @Test
    public void testDeserialize_NonArray_NullValue() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // setup
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        String[] expected = { null };
        
        // test
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        
        // verify
        assertEquals(expected.length, result.length);
        assertEquals(expected[0], result[0]);
        verify(jp).getCurrentToken();            
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        // setup
        when(typeDeserializer.deserializeTypedFromArray(jp, ctxt)).thenReturn(new Object());

        Object expected = new Object();
        
        // test
        Object result = StringArrayDeserializer.instance.deserializeWithType(jp, ctxt, typeDeserializer);
        
        // verify
        assertEquals(expected, result);
        verify(typeDeserializer).deserializeTypedFromArray(jp, ctxt);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // setup
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("");
        
        // test
        String[] result = StringArrayDeserializer.instance.handleNonArray(jp, ctxt);
        
        // verify
        assertNull(result);
        verify(jp).getCurrentToken();
        verify(jp).getText();
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_DisabledFeature() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // setup
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        
        // test
        StringArrayDeserializer.instance.handleNonArray(jp, ctxt);
        
        // verify
        verify(ctxt).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateContextual() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> elementDeserializer = mock(JsonDeserializer.class);

        // setup
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(elementDeserializer);
        when(ctxt.handleSecondaryContextualization(elementDeserializer, property)).thenReturn(elementDeserializer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // test
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        // verify
        assertEquals(elementDeserializer, result);
        verify(ctxt).findContextualValueDeserializer(ctxt.constructType(String.class), property);
        verify(ctxt).handleSecondaryContextualization(elementDeserializer, property);
    }

    private boolean isDefaultDeserializer(JsonDeserializer<?> deser) {
        return deser == null;
    }
}