package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;
    
    @Before
    public void setUp() {
        deserializer = new StringArrayDeserializer();
    }
    
    @Test
    public void testDeserialize_withNullArray_returnsNull() {
        String[] result = deserializer.deserialize(null, null);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_withEmptyArray_returnsEmptyArray() {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_withValidArray_returnsArray() {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value1");
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value1", result[0]);
    }
    
    @Test
    public void testDeserialize_withNullElement_returnsArrayWithNull() {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
    }
    
    @Test(expected = IOException.class)
    public void testDeserialize_withInvalidStartToken_throwsIOException() {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.isExpectedStartArrayToken()).thenReturn(false);
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testDeserializeWithType_returnsDeserializedObject() {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jp, ctxt)).thenReturn("value1");
        
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertNotNull(result);
        assertEquals("value1", result);
    }
}