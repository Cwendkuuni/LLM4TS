
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

        // Act
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        verify(jsonParser).skipChildren();
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_WhenStartArray() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_WhenStartObject() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_WhenFieldName() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_WhenOtherToken() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NOT_AVAILABLE);

        // Act
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertNull(result);
    }
}
