package com.fasterxml.jackson.databind.deser.std;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext ctxt;
    
    @Before
    public void setup() {
        deserializer = new StringCollectionDeserializer();
        parser = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
    }
    
    @After
    public void tearDown() {
        deserializer = null;
        parser = null;
        ctxt = null;
    }
    
    @Test
    public void testDeserialize_WithValidJsonInput_ShouldReturnListWithValues() throws Exception {
        when(parser.nextTextValue()).thenReturn("Value1", "Value2", null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        
        List<String> expected = Arrays.asList("Value1", "Value2");
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_WithNullParser_ShouldThrowException() throws Exception {
        try {
            deserializer.deserialize(null, ctxt);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testDeserialize_WithInvalidJsonInput_ShouldThrowException() throws Exception {
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        
        try {
            deserializer.deserialize(parser, ctxt);
            Assert.fail("Expected MismatchedInputException to be thrown");
        } catch (MismatchedInputException e) {
            // expected
        }
    }
    
    @Test
    public void testDeserialize_WithCustomDeserializer_ShouldInvokeCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        
        when(parser.nextTextValue()).thenReturn("Value1", "Value2", null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        
        deserializer = new StringCollectionDeserializer(customDeserializer);
        
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        
        List<String> expected = Arrays.asList("CustomValue1", "CustomValue2");
        Assert.assertEquals(expected, result);
        
        verify(customDeserializer, times(2)).deserialize(parser, ctxt);
    }
    
    @Test
    public void testDeserialize_WithNullValueAndSkipNullsFalse_ShouldAddNullValueToResult() throws Exception {
        when(parser.nextTextValue()).thenReturn(null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        
        deserializer = new StringCollectionDeserializer(false);
        
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        
        List<String> expected = Arrays.asList(null);
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDeserialize_WithNullValueAndSkipNullsTrue_ShouldNotAddNullValueToResult() throws Exception {
        when(parser.nextTextValue()).thenReturn(null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        
        deserializer = new StringCollectionDeserializer(true);
        
        Collection<String> result = deserializer.deserialize(parser, ctxt);
        
        List<String> expected = new ArrayList<>();
        Assert.assertEquals(expected, result);
    }
}