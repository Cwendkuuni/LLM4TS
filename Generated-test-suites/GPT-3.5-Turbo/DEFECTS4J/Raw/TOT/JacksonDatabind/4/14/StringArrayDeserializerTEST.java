package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import static org.mockito.Mockito.*;

public class StringArrayDeserializerTest {
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private ObjectBuffer objectBuffer;
    
    @Mock
    private JsonToken jsonToken;
    
    @Mock
    private DeserializationFeature deserializationFeature;
    
    @Mock
    private BeanProperty beanProperty;

    @Test
    public void testDeserialize() throws Exception {
        MockitoAnnotations.initMocks(this);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        stringArrayDeserializer._elementDeserializer = mock(JsonDeserializer.class);
        
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(jsonToken).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("test");
        
        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        verify(jsonParser, atLeast(2)).nextToken();
        verify(jsonParser, times(1)).getText();
    }
    
    @Test
    public void testDeserializeCustom() throws Exception {
        MockitoAnnotations.initMocks(this);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        stringArrayDeserializer._elementDeserializer = mock(JsonDeserializer.class);
        
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(jsonToken).thenReturn(JsonToken.END_ARRAY);
        when(jsonToken == JsonToken.VALUE_NULL).thenReturn(false);
        
        stringArrayDeserializer._deserializeCustom(jsonParser, deserializationContext);
        
        verify(jsonParser, atLeast(2)).nextToken();
    }
    
    @Test
    public void testHandleNonArray() throws Exception {
        MockitoAnnotations.initMocks(this);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        
        stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        
        verify(jsonParser, times(1)).isExpectedStartArrayToken();
        verify(jsonParser, times(1)).getCurrentToken();
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        MockitoAnnotations.initMocks(this);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        when(beanProperty.getType()).thenReturn(ctxt.constructType(String.class));
        
        stringArrayDeserializer.createContextual(deserializationContext, beanProperty);
        
        verify(beanProperty, times(1)).getType();
    }
}