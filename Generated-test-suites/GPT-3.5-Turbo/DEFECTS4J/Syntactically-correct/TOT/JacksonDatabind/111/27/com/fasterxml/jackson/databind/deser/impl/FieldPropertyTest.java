
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FieldPropertyTest {

    private ObjectMapper objectMapper;
    private SettableBeanProperty settableBeanProperty;
    private Field field;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        field = mock(Field.class);
        settableBeanProperty = createFieldProperty();
    }

    @Test
    public void testDeserializeAndSet_NullValue_ShouldSkipIfSkipNulls() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(settableBeanProperty._skipNulls).thenReturn(true);

        // Act
        settableBeanProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verifyZeroInteractions(field);
    }

    @Test
    public void testDeserializeAndSet_NullValue_ShouldGetNullValueIfNotSkipNulls() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object nullValue = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(settableBeanProperty._skipNulls).thenReturn(false);
        when(settableBeanProperty._nullProvider.getNullValue(deserializationContext)).thenReturn(nullValue);

        // Act
        settableBeanProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(settableBeanProperty._nullProvider).getNullValue(deserializationContext);
        verify(field).set(instance, nullValue);
    }

    @Test
    public void testDeserializeAndSet_NonNullValueWithNoTypeDeserializer_ShouldDeserializeAndSet() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(settableBeanProperty._valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
        when(settableBeanProperty._skipNulls).thenReturn(false);

        // Act
        settableBeanProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(settableBeanProperty._valueDeserializer).deserialize(jsonParser, deserializationContext);
        verify(field).set(instance, value);
    }

    @Test
    public void testDeserializeAndSet_NullValueWithNoTypeDeserializer_ShouldGetNullValueIfNotSkipNulls() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object nullValue = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(settableBeanProperty._nullProvider.getNullValue(deserializationContext)).thenReturn(nullValue);
        when(settableBeanProperty._skipNulls).thenReturn(false);

        // Act
        settableBeanProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(settableBeanProperty._nullProvider).getNullValue(deserializationContext);
        verify(field).set(instance, nullValue);
    }

    @Test
    public void testDeserializeAndSet_NullValueWithNoTypeDeserializer_ShouldSkipIfSkipNulls() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(settableBeanProperty._skipNulls).thenReturn(true);

        // Act
        settableBeanProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verifyZeroInteractions(field);
    }

    @Test
    public void testDeserializeAndSet_NonNullValueWithTypeDeserializer_ShouldDeserializeAndSet() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(settableBeanProperty._valueDeserializer.deserializeWithType(jsonParser, deserializationContext, settableBeanProperty._valueTypeDeserializer)).thenReturn(value);
        when(settableBeanProperty._skipNulls).thenReturn(false);

        // Act
        settableBeanProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        verify(settableBeanProperty._valueDeserializer).deserializeWithType(jsonParser, deserializationContext, settableBeanProperty._valueTypeDeserializer);
        verify(field).set(instance, value);
    }

    private SettableBeanProperty createFieldProperty() {
        return new FieldProperty(null, null, null, null, new AnnotatedField(field));
    }
}
