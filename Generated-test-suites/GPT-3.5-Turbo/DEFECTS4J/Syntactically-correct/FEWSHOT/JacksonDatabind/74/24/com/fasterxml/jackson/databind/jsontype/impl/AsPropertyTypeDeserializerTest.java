package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {
    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null, null, "type", true, null, As.PROPERTY);
        
        // Test case 1: JSON object starts with START_OBJECT token
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn("type");
        when(parser.getText()).thenReturn("foo");
        
        Object result1 = typeDeserializer.deserializeTypedFromObject(parser, context);
        assertEquals("foo", result1);
        
        // Test case 2: JSON object starts with FIELD_NAME token
        when(parser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser.getCurrentName()).thenReturn("type");
        when(parser.getText()).thenReturn("bar");
        
        Object result2 = typeDeserializer.deserializeTypedFromObject(parser, context);
        assertEquals("bar", result2);
        
        // Test case 3: JSON object starts with START_ARRAY token
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        Object result3 = typeDeserializer.deserializeTypedFromObject(parser, context);
        assertNull(result3);
    }
    
    @Test
    public void testDeserializeTypedForId() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        
        AsPropertyTypeDeserializer typeDeserializer = new AsPropertyTypeDeserializer(null, null, "type", true, null, As.PROPERTY);
        
        when(parser.getCurrentName()).thenReturn("type");
        when(parser.getText()).thenReturn("foo");
        when(typeDeserializer._findDeserializer(context, "foo")).thenReturn(new FooDeserializer());
        
        Object result = typeDeserializer._deserializeTypedForId(parser, context, tokenBuffer);
        
        assertEquals("foo", result);
        verify(tokenBuffer, times(1)).writeFieldName("type");
        verify(tokenBuffer, times(1)).writeString("foo");
    }
    
    private class FooDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "foo";
        }
    }
}