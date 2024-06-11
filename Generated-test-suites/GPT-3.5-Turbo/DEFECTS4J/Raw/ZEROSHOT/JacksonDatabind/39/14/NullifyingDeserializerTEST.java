package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setUp() {
        nullifyingDeserializer = NullifyingDeserializer.instance;
        deserializationContext = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);
        typeDeserializer = mock(TypeDeserializer.class);
    }

    @Test
    public void testDeserialize_SkipChildren_ReturnsNull() throws Exception {
        // Arrange
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        
        // Act
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        verify(jsonParser).skipChildren();
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_ReturnsNull() throws Exception {
        // Arrange
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(null);
        
        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNull(result);
    }
}