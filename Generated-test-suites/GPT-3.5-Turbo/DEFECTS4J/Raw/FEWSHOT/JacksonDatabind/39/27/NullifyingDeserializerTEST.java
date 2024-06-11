package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;

import org.junit.Before;
import org.junit.Test;

public class NullifyingDeserializerTest {
    
    private NullifyingDeserializer nullifyingDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    
    @Before
    public void setUp() {
        nullifyingDeserializer = NullifyingDeserializer.instance;
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        
        reset(jsonParser);
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        
        reset(jsonParser);
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        
        reset(jsonParser);
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_STRING);
        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }
}