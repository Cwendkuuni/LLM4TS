package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.NullValueProvider;
import com.fasterxml.jackson.databind.util.ValueInstantiator;
import com.fasterxml.jackson.databind.util.ValueInstantiators;

public class StringCollectionDeserializerTest {
 
    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance().constructCollectionType(Collection.class, String.class),
                null, ValueInstantiators.getDefaultValueInstantiator(null, null));
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(p.isExpectedStartArrayToken()).thenReturn(true);
        when(p.getCurrentToken()).thenReturn(JsonParser.Token.START_ARRAY, JsonParser.Token.VALUE_STRING, JsonParser.Token.VALUE_STRING, JsonParser.Token.END_ARRAY);
        when(p.nextTextValue()).thenReturn("value1", "value2", null);
        
        Collection<String> result = deserializer.deserialize(p, ctxt);
        
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("value1");
        expectedResult.add("value2");
        
        assertEquals(expectedResult, result);
        verify(p, times(3)).nextTextValue();
        verify(p, times(3)).getCurrentToken();
    }
    
    @Test
    public void testWithContextual() throws JsonMappingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(TypeFactory.defaultInstance().constructCollectionType(Collection.class, String.class),
                null, ValueInstantiators.getDefaultValueInstantiator(null, null));
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(property.getContextualType()).thenReturn(TypeFactory.defaultInstance().constructCollectionType(Collection.class, String.class));
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertNull(result);
        verify(ctxt, times(2)).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        verify(property).getContextualType();
    }
    
}