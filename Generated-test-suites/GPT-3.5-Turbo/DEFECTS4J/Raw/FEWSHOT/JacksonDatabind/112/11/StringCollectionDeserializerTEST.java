package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

public class StringCollectionDeserializerTest {
    
    private final String value1 = "value1";
    private final String value2 = "value2";
    
    private final JsonParser parser = mock(JsonParser.class);
    private final DeserializationContext context = mock(DeserializationContext.class);
    private final Collection<String> result = new ArrayList<>();
    private final JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
    private final ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
    
    @Test
    public void testDeserialize() throws IOException {
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value1, value2);
        
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiator);
        Collection<String> deserializedResult = deserializer.deserialize(parser, context, result);
        
        assertEquals(2, deserializedResult.size());
        assertEquals(value1, deserializedResult.iterator().next());
        
        verify(valueDeserializer, times(2)).deserialize(parser, context);
        verify(parser, times(2)).nextTextValue();
        verify(parser, times(2)).getCurrentToken();
    }
    
    @Test
    public void testDeserialize_WhenNotStartArray() throws IOException {
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiator);
        Collection<String> deserializedResult = deserializer.deserialize(parser, context, result);
        
        assertNull(deserializedResult);
        verify(parser).isExpectedStartArrayToken();
    }
    
    @Test
    public void testDeserialize_WhenUnwrapSingle() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value1);
        
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiator);
        deserializer.unwrapSingle = Boolean.TRUE;
        Collection<String> deserializedResult = deserializer.deserialize(parser, context, result);
        
        assertEquals(1, deserializedResult.size());
        assertEquals(value1, deserializedResult.iterator().next());
        
        verify(valueDeserializer).deserialize(parser, context);
        verify(parser).getCurrentToken();
    }
    
    @Test
    public void testDeserialize_WhenUnwrapSingleAndNotStartArray() throws IOException {
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiator);
        deserializer.unwrapSingle = Boolean.TRUE;
        Collection<String> deserializedResult = deserializer.deserialize(parser, context, result);
        
        assertNull(deserializedResult);
        verify(parser).isExpectedStartArrayToken();
    }
}