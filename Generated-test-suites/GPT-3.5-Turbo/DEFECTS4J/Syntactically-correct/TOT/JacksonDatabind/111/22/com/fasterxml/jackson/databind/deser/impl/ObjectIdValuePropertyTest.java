
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testDeserializeAndSet_WithNullValue_ShouldReturnNull() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(
            mock(ObjectIdReader.class),
            new PropertyMetadata(null, null, null)
        );
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        // Act
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet_WithNonNullValue_ShouldReturnInstance() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(
            mock(ObjectIdReader.class),
            new PropertyMetadata(null, null, null)
        );
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(objectIdValueProperty._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("id");
        when(deserializationContext.findObjectId(eq("id"), any(), any())).thenReturn(mock(ReadableObjectId.class));
        when(deserializationContext.findObjectId("id", null, null)).thenReturn(mock(ReadableObjectId.class));
        
        // Act
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, null);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_WithIdPropertyReturningNull_ShouldThrowException() throws IOException {
        // Arrange
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(
            mock(ObjectIdReader.class),
            new PropertyMetadata(null, null, null)
        );
        objectIdValueProperty._objectIdReader.idProperty = null;

        // Act & Assert
        objectIdValueProperty.setAndReturn(new Object(), "id");
    }
}
