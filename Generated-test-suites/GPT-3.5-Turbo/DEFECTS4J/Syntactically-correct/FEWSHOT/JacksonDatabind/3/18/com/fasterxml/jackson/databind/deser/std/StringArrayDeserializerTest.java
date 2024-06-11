package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

@JacksonStdImpl
public class StringArrayDeserializerTest {
    private StringArrayDeserializer deserializer;

    @Before
    public void setup() {
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        
        Object[] chunk = new Object[5];
        when(buffer.resetAndStart()).thenReturn(chunk);
        
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value1").thenReturn("value2");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        
        verify(ctxt).returnObjectBuffer(buffer);
    }
    
    @Test
    public void testDeserializeCustom() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        
        Object[] chunk = new Object[5];
        when(buffer.resetAndStart()).thenReturn(chunk);
        
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("value1").thenReturn("value2");
        
        deserializer = new StringArrayDeserializer(elementDeserializer);
        
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        
        verify(ctxt).returnObjectBuffer(buffer);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        when(typeDeserializer.deserializeTypedFromArray(jp, ctxt)).thenReturn("typedResult");
        
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertEquals("typedResult", result);
    }
    
    @Test(expected = IOException.class)
    public void testDeserializeNonArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("");
        
        assertNull(deserializer.handleNonArray(jp, ctxt));
    }
    
    @Test
    public void testContextual() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(deser);
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertEquals(deserializer, result);
    }

    private boolean isDefaultDeserializer(JsonDeserializer<?> deser) {
        return false;
    }
}