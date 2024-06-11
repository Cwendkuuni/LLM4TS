import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_SkipChildren_ReturnsNull() throws IOException {
        // Arrange
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        // Act
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        verify(jsonParser).skipChildren();
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_CurrentTokenStartArray_ReturnsDeserializedObject() throws IOException {
        // Arrange
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithType_CurrentTokenStartObject_ReturnsDeserializedObject() throws IOException {
        // Arrange
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeWithType_CurrentTokenFieldName_ReturnsDeserializedObject() throws IOException {
        // Arrange
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithType_CurrentTokenNotHandled_ReturnsNull() throws IOException {
        // Arrange
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_UNKNOWN);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertNull(result);
    }

}