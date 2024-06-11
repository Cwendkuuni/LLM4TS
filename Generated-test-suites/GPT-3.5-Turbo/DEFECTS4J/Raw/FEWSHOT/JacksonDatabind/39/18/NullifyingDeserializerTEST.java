
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class NullifyingDeserializerTest {
    
    private NullifyingDeserializer deserializer;
    private JsonParser mockJsonParser;
    private DeserializationContext mockDeserializationContext;
    private TypeDeserializer mockTypeDeserializer;
    
    @Before
    public void setUp() {
        deserializer = NullifyingDeserializer.instance;
        mockJsonParser = mock(JsonParser.class);
        mockDeserializationContext = mock(DeserializationContext.class);
        mockTypeDeserializer = mock(TypeDeserializer.class);
    }
    
    @Test
    public void testDeserialize_skipChildren() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        assertNull(deserializer.deserialize(mockJsonParser, mockDeserializationContext));
        
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        assertNull(deserializer.deserialize(mockJsonParser, mockDeserializationContext));
        
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        assertNull(deserializer.deserialize(mockJsonParser, mockDeserializationContext));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        when(mockTypeDeserializer.deserializeTypedFromAny(mockJsonParser, mockDeserializationContext)).thenReturn(null);
        assertNull(deserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
        
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        when(mockTypeDeserializer.deserializeTypedFromAny(mockJsonParser, mockDeserializationContext)).thenReturn(null);
        assertNull(deserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
        
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        when(mockTypeDeserializer.deserializeTypedFromAny(mockJsonParser, mockDeserializationContext)).thenReturn(null);
        assertNull(deserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
        
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NOT_AVAILABLE);
        assertNull(deserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer));
    }
}
