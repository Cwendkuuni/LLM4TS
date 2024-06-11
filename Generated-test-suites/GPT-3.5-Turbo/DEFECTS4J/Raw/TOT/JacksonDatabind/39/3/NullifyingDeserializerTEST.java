package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() throws Exception {
        nullifyingDeserializer = NullifyingDeserializer.instance;
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserialize_WithFieldTokens_ReturnsNull() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_WithOtherTokens_ReturnsNull() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        when(jsonParser.skipChildren()).thenReturn(mock(TokenBuffer.class));
        
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNull(result);
        verify(jsonParser, times(1)).skipChildren();
    }
    
    @Test
    public void testDeserializeWithType_WithArrayToken_ReturnsTypedDeserializationResult() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
        
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertNotNull(result);
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeWithType_WithObjectToken_ReturnsTypedDeserializationResult() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
        
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertNotNull(result);
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeWithType_WithFieldNameToken_ReturnsTypedDeserializationResult() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
        
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertNotNull(result);
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeWithType_WithOtherTokens_ReturnsNull() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_END_OBJECT);
        
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, null);
        
        assertNull(result);
    }
}