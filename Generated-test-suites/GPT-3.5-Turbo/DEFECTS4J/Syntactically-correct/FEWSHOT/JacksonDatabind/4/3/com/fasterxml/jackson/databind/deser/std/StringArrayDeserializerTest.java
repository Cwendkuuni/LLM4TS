
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;

public class StringArrayDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext context;

    @Mock
    private TypeDeserializer typeDeserializer;

    private StringArrayDeserializer deserializer;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        deserializer = new StringArrayDeserializer();
        mapper = new ObjectMapper();
    }
    
    @Test
    public void testDeserialize_NoElements() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        String[] result = deserializer.deserialize(jsonParser, context);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_WithElements() throws IOException {
        String[] expected = { "foo", "bar" };
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo", "bar");
        String[] result = deserializer.deserialize(jsonParser, context);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testDeserialize_WithNullElement() throws IOException {
        String[] expected = { "foo", null };
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo");
        String[] result = deserializer.deserialize(jsonParser, context);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testDeserialize_WithCustomDeser() throws IOException {
        String[] expected = { "foo", "bar" };
        JsonDeserializer<String> customDeser = mock(JsonDeserializer.class);
        deserializer = new StringArrayDeserializer(customDeser);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo", "bar");
        when(customDeser.deserialize(jsonParser, context)).thenReturn("foo", "bar");
        String[] result = deserializer.deserialize(jsonParser, context);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testDeserialize_WithStringDeserializer() throws IOException {
        String[] expected = { "foo", "bar" };
        JsonDeserializer<String> stringDeser = new StringDeserializer();
        deserializer = new StringArrayDeserializer(stringDeser);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("foo", "bar");
        String[] result = deserializer.deserialize(jsonParser, context);
        assertEquals(Arrays.toString(expected), Arrays.toString(result));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String[] expected = { "foo", "bar" };
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, context)).thenReturn(expected);
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        assertEquals(Arrays.toString(expected), result.toString());
    }

    @Test
    public void testHandleNonArray_EnabledSingleValueAsArray() throws IOException {
        deserializer.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("foo");
        String[] result = deserializer.handleNonArray(jsonParser, context);
        assertEquals("[foo]", Arrays.toString(result));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_DisabledSingleValueAsArray() throws IOException {
        deserializer.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        deserializer.handleNonArray(jsonParser, context);
    }

    @Test
    public void testHandleNonArray_NullValue() throws IOException {
        deserializer.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        String[] result = deserializer.handleNonArray(jsonParser, context);
        assertEquals("[null]", Arrays.toString(result));
    }

    private static class StringDeserializer extends StdDeserializer<String> {

        public StringDeserializer() {
            super(String.class);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText();
        }
    }
}
