import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;
    private JsonParser jsonParserMock;
    private DeserializationContext deserializationContextMock;
    private ObjectBuffer objectBufferMock;

    @Before
    public void setup() {
        stringArrayDeserializer = new StringArrayDeserializer();
        jsonParserMock = mock(JsonParser.class);
        deserializationContextMock = mock(DeserializationContext.class);
        objectBufferMock = mock(ObjectBuffer.class);
    }

    @Test
    public void testDeserialize_handlesEmptyArray() throws IOException {
        when(jsonParserMock.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParserMock.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(jsonParserMock, deserializationContextMock);

        assertNull(result);
    }

    @Test
    public void testDeserialize_handlesArrayOfStrings() throws IOException {
        when(jsonParserMock.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParserMock.getText()).thenReturn("string1");

        String[] result = stringArrayDeserializer.deserialize(jsonParserMock, deserializationContextMock);

        assertEquals(1, result.length);
        assertEquals("string1", result[0]);
    }

    @Test
    public void testDeserialize_handlesArrayOfNulls() throws IOException {
        when(jsonParserMock.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParserMock.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(jsonParserMock, deserializationContextMock);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_handlesInvalidToken() throws IOException {
        when(jsonParserMock.isExpectedStartArrayToken()).thenReturn(false);

        stringArrayDeserializer.deserialize(jsonParserMock, deserializationContextMock);
    }

    @Test(expected = MismatchedInputException.class)
    public void testDeserialize_handlesNonArrayValue() throws IOException {
        when(jsonParserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParserMock.getText()).thenReturn("string");
        when(deserializationContextMock.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        stringArrayDeserializer.deserialize(jsonParserMock, deserializationContextMock);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonDeserializer<?> typeDeserializerMock = mock(JsonDeserializer.class);
        when(typeDeserializerMock.deserializeTypedFromArray(jsonParserMock, deserializationContextMock)).thenReturn(new Object());

        Object result = stringArrayDeserializer.deserializeWithType(jsonParserMock, deserializationContextMock, typeDeserializerMock);

        assertNotNull(result);
    }

    @Test
    public void testHandleNonArrayAcceptsSingleValueAsArray() throws IOException {
        when(jsonParserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParserMock.getText()).thenReturn("string");
        when(deserializationContextMock.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);

        String[] result = stringArrayDeserializer.handleNonArray(jsonParserMock, deserializationContextMock);

        assertEquals(1, result.length);
        assertEquals("string", result[0]);
    }

    @Test(expected = MismatchedInputException.class)
    public void testHandleNonArrayThrowsMismatchedInputException() throws IOException {
        stringArrayDeserializer.handleNonArray(jsonParserMock, deserializationContextMock);
    }

    @Test
    public void testCreateContextualReturnsThisForDefaultDeserializer() throws JsonMappingException {
        BeanProperty propertyMock = mock(BeanProperty.class);
        JsonDeserializer<String> elementDeserializerMock = mock(JsonDeserializer.class);
        when(elementDeserializerMock.isDefaultDeserializer()).thenReturn(true);
        stringArrayDeserializer._elementDeserializer = elementDeserializerMock;

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContextMock, propertyMock);

        assertEquals(stringArrayDeserializer, result);
    }

    @Test
    public void testCreateContextualReturnsNewStringArrayDeserializerWithProvidedDeserializer() throws JsonMappingException {
        BeanProperty propertyMock = mock(BeanProperty.class);
        JsonDeserializer<String> elementDeserializerMock = mock(JsonDeserializer.class);
        when(elementDeserializerMock.isDefaultDeserializer()).thenReturn(false);
        when(deserializationContextMock.handleSecondaryContextualization(eq(elementDeserializerMock), eq(propertyMock))).thenReturn(elementDeserializerMock);

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContextMock, propertyMock);

        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertEquals(elementDeserializerMock, ((StringArrayDeserializer) result)._elementDeserializer);
    }

    @Test
    public void testCreateContextualSetsElementDeserializerToNullIfProvidedDeserializerEqualsCurrent() throws JsonMappingException {
        BeanProperty propertyMock = mock(BeanProperty.class);
        JsonDeserializer<String> elementDeserializerMock = mock(JsonDeserializer.class);
        when(elementDeserializerMock.isDefaultDeserializer()).thenReturn(false);
        stringArrayDeserializer._elementDeserializer = elementDeserializerMock;

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContextMock, propertyMock);

        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertNull(((StringArrayDeserializer) result)._elementDeserializer);
    }

}