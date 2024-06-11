import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<String> elementDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        elementDeserializer = mock(JsonDeserializer.class);
        stringArrayDeserializer._elementDeserializer = elementDeserializer;
    }

    @Test
    public void testDeserialize_WhenStartArrayToken_ReturnsArrayOfStrings() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        verifyNoInteractions(elementDeserializer);
        assertArrayEquals(new String[]{"value"}, result);
    }

    @Test
    public void testDeserialize_WhenCustomDeserializerPresent_ReturnsArrayOfStrings() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value");

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        verify(elementDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
        assertArrayEquals(new String[]{"value"}, result);
    }

    @Test
    public void testDeserialize_WhenNonArrayTokenAndAcceptSingleValueAsArrayDisabled_ThrowsException() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        org.junit.jupiter.api.Assertions.assertThrows(
                com.fasterxml.jackson.databind.JsonMappingException.class,
                () -> stringArrayDeserializer.deserialize(jsonParser, deserializationContext)
        );
    }

    @Test
    public void testDeserialize_WhenNonArrayTokenAndAcceptSingleValueAsArrayEnabled_ReturnsArrayOfStrings() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(stringArrayDeserializer._parseString(jsonParser, deserializationContext)).thenReturn("value");

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[]{"value"}, result);
    }

    @Test
    public void testDeserialize_WhenNonArrayTokenAndAcceptEmptyStringAsNullObjectEnabledAndValueIsEmpty_ReturnsNull() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test
    public void testDeserialize_WhenNonArrayTokenAndAcceptEmptyStringAsNullObjectEnabledAndValueIsNotEmpty_ReturnsArrayOfStrings() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(stringArrayDeserializer._parseString(jsonParser, deserializationContext)).thenReturn("value");

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[]{"value"}, result);
    }

    @Test
    public void testDeserializeWithType_ReturnsObject() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new Object());

        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        assertNotNull(result);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerIsNull_ReturnsSelf() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        when(elementDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(true);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertEquals(stringArrayDeserializer, result);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerIsNotNullAndDefaultDeserializer_ReturnsSelf() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        when(elementDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(true);
        stringArrayDeserializer._elementDeserializer = elementDeserializer;

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertEquals(stringArrayDeserializer, result);
    }

    @Test
    public void testCreateContextual_WhenElementDeserializerIsNotNullAndNotDefaultDeserializer_ReturnsNewStringArrayDeserializer() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.isDefaultDeserializer(newDeserializer)).thenReturn(false);
        stringArrayDeserializer._elementDeserializer = elementDeserializer;
        when(deserializationContext.handleSecondaryContextualization(elementDeserializer, property)).thenReturn(newDeserializer);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        assertNotEquals(stringArrayDeserializer, result);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(newDeserializer, ((StringArrayDeserializer) result)._elementDeserializer);
    }
}