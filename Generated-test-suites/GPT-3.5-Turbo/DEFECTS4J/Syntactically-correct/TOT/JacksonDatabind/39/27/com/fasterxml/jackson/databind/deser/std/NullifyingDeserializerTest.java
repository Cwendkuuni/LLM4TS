
package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_WhenTokenIsStartArray_ShouldReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        // Act
        Object actual = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser, times(0)).skipChildren();
        assertNull(actual);
    }

    @Test
    public void testDeserialize_WhenTokenIsStartObject_ShouldReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        // Act
        Object actual = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser, times(0)).skipChildren();
        assertNull(actual);
    }

    @Test
    public void testDeserialize_WhenTokenIsFieldName_ShouldReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        // Act
        Object actual = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser, times(0)).skipChildren();
        assertNull(actual);
    }

    @Test
    public void testDeserializeWithType_WhenTokenIsStartArray_ShouldReturnDeserializedValueFromTypeDeserializer() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object expected = new Object();
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(expected);
        
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        // Act
        Object actual = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        verify(jsonParser, times(1)).getCurrentTokenId();
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithType_WhenTokenIsStartObject_ShouldReturnDeserializedValueFromTypeDeserializer() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object expected = new Object();
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(expected);
        
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        // Act
        Object actual = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        verify(jsonParser, times(1)).getCurrentTokenId();
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithType_WhenTokenIsFieldName_ShouldReturnDeserializedValueFromTypeDeserializer() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Object expected = new Object();
        
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(expected);
        
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        // Act
        Object actual = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        // Assert
        verify(jsonParser, times(1)).getCurrentTokenId();
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertEquals(expected, actual);
    }
}
