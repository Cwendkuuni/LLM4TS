package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

    @Mock
    private JsonParser parser;
    
    @Mock
    private DeserializationContext context;
    
    @Mock
    private TypeDeserializer typeDeserializer;
    
    @Test
    public void deserialize_shouldSkipChildrenAndReturnNull() throws IOException {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        
        // Act
        Object result = deserializer.deserialize(parser, context);
        
        // Assert
        verify(parser).skipChildren();
        assert result == null;
    }
    
    @Test
    public void deserializeWithType_shouldDeserializeTypedFromAny() throws IOException {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        
        // Act
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(parser, context);
        assert result == null;
    }
}