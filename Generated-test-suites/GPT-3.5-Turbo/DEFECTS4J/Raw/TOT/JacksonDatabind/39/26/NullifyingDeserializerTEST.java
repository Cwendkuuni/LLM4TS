import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_withValidJson_shouldReturnNull() throws IOException {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        // Act
        Object result = deserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_withStartArrayToken_shouldInvokeTypeDeserializer() throws IOException {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        // Mock the behavior of the jsonParser to return ID_START_ARRAY
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType_withStartObjectToken_shouldInvokeTypeDeserializer() throws IOException {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        // Mock the behavior of the jsonParser to return ID_START_OBJECT
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType_withFieldNameToken_shouldInvokeTypeDeserializer() throws IOException {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        // Mock the behavior of the jsonParser to return ID_FIELD_NAME
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType_withOtherToken_shouldReturnNull() throws IOException {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        // Mock the behavior of the jsonParser to return an unsupported token
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NOT_AVAILABLE);
        
        // Act
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        assertNull(result);
    }
}