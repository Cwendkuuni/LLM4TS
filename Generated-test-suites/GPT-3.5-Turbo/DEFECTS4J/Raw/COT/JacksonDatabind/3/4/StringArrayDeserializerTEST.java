import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_StartArrayToken() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(parser, ctxt);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_CustomDeserializer() throws IOException {
        JsonDeserializer<String> elementDeserializer = Mockito.mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(parser, ctxt);

        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_NullValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(parser, ctxt);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testDeserialize_ValueString() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        Mockito.when(parser.getText()).thenReturn("test");

        String[] result = deserializer.deserialize(parser, ctxt);

        assertEquals(1, result.length);
        assertEquals("test", result[0]);
    }

    @Test
    public void testDeserialize_OtherValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY);
        Mockito.when(parser.getText()).thenReturn("123");

        String[] result = deserializer.deserialize(parser, ctxt);

        assertEquals(1, result.length);
        assertEquals("123", result[0]);
    }

    @Test
    public void testDeserialize_NonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(false);
        Mockito.when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getText()).thenReturn("");

        String[] result = deserializer.deserialize(parser, ctxt);

        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_NonArray_WithException() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(false);
        Mockito.when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object result = deserializer.deserializeWithType(parser, ctxt, Mockito.mock(TypeDeserializer.class));

        assertNull(result);
    }

    @Test
    public void testHandleNonArray_WithStringValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getText()).thenReturn("");

        String[] result = deserializer.handleNonArray(parser, ctxt);

        assertEquals(1, result.length);
        assertEquals("", result[0]);
    }

    @Test
    public void testHandleNonArray_WithNullValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        String[] result = deserializer.handleNonArray(parser, ctxt);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArray_WithException() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        deserializer.handleNonArray(parser, ctxt);
    }

    @Test
    public void testCreateContextual_DefaultDeserializer() throws JsonMappingException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertEquals(deserializer, result);
    }

    @Test
    public void testCreateContextual_CustomDeserializer() throws JsonMappingException {
        JsonDeserializer<String> elementDeserializer = Mockito.mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertEquals(deserializer, result);
    }
}

