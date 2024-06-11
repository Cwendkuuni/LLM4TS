import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String[] expected = new String[]{"value1", "value2", "value3"};
        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value1", "value2", "value3");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testDeserializeWithNullValues() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String[] expected = new String[]{"value1", null, "value3"};
        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value1", "value3");
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.getNullValue()).thenReturn(null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializer.deserialize(jp, ctxt);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testDeserializeWithNonStringValues() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String[] expected = new String[]{"value1", "value2", "value3"};
        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("1", "2", "3");
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.deserialize(jp, ctxt)).thenReturn("value1", "value2", "value3");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializer.deserialize(jp, ctxt);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        String[] expected = new String[]{"value1", "value2", "value3"};
        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value1", "value2", "value3");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertEquals(expected.getClass(), result.getClass());
        String[] arrayResult = (String[]) result;
        assertEquals(expected.length, arrayResult.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], arrayResult[i]);
        }
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String[] expected = new String[]{"value"};
        when(jp.getText()).thenReturn("value");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jp, ctxt);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testHandleNonArrayWithNullValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jp, ctxt);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayWithSingleValueNotAllowed() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_BOOLEAN);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.handleNonArray(jp, ctxt);
    }

    @Test
    public void testCreateContextualWithElementDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(elementDeserializer);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        StringArrayDeserializer result = (StringArrayDeserializer) deserializer.createContextual(ctxt, property);
        assertEquals(elementDeserializer, result._elementDeserializer);
    }

    @Test
    public void testCreateContextualWithoutElementDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        StringArrayDeserializer result = (StringArrayDeserializer) deserializer.createContextual(ctxt, property);
        assertNull(result._elementDeserializer);
    }
}