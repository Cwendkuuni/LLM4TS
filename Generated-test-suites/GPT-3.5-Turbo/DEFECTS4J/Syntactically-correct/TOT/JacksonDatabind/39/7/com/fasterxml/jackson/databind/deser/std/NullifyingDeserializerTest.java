
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer;

    @Before
    public void setUp() {
        nullifyingDeserializer = NullifyingDeserializer.instance;
    }

    @Test
    public void testDeserialize_SkipsChildren_ReturnsNull() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        // Act
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        assertNull(result);
        verify(jsonParser).skipChildren();
    }

    @Test
    public void testDeserializeWithType_FieldNameToken_ReturnsNull() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertNull(result);
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType_ArrayStartToken_ReturnsResultFromTypeDeserializer() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        Object expectedResult = new Object();
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(expectedResult);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertEquals(expectedResult, result);
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType_ObjectStartToken_ReturnsResultFromTypeDeserializer() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        Object expectedResult = new Object();
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(expectedResult);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertEquals(expectedResult, result);
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType_InvalidToken_ReturnsNull() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NOT_AVAILABLE);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertNull(result);
    }
    
}

