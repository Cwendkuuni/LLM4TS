package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.beanutils.BeanDeserializer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<String> elementDeserializer;

    @Before
    public void setUp() throws Exception {
        deserializer = new StringArrayDeserializer();
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        elementDeserializer = mock(JsonDeserializer.class);
        deserializer._elementDeserializer = elementDeserializer;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize() throws Exception {
        String[] expected = {"foo", "bar"};
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo", "bar");
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("foo", "bar");
        
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);
        
        assertArrayEquals(expected, result);
        verify(elementDeserializer, times(2)).deserialize(jsonParser, deserializationContext);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_CustomElementDeserializer() throws Exception {
        String[] expected = {"foo", "bar"};
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo", "bar");
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("foo", "bar");
        
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = customDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertArrayEquals(expected, result);
        verify(elementDeserializer, times(2)).deserialize(jsonParser, deserializationContext);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_WithNulls() throws Exception {
        String[] expected = {"foo", null, "bar"};
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo", "bar");
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("foo", null, "bar");
        
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);
        
        assertArrayEquals(expected, result);
        verify(elementDeserializer, times(3)).deserialize(jsonParser, deserializationContext);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_CustomNonStringValue() throws Exception {
        String[] expected = {"1", "2"};
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("1", "2");
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("1", "2");
        
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = customDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertArrayEquals(expected, result);
        verify(elementDeserializer, times(2)).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserialize_NonArray() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);
        
        assertNull(result);
    }

    @Test
    public void testDeserialize_NonArray_AcceptSingleValueAsArray() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(any())).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");
        
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);
        
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_NonArray_NonStringValue() throws Exception {
        String[] expected = {"foo"};
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(any())).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("foo");
        
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = customDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertArrayEquals(expected, result);
        verify(elementDeserializer, never()).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserialize_NonArray_WithEmptyStringAsNull() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(any())).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");
        
        String[] result = deserializer.deserialize(jsonParser, deserializationContext);
        
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object expected = new Object();
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(expected);
        
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertEquals(expected, result);
    }
}