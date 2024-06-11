
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Mock
    private JsonDeserializer<String> elementDeserializer;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private TypeDeserializer typeDeserializer;
    @Mock
    private JsonStreamContext jsonStreamContext;
    @Mock
    private ObjectBuffer objectBuffer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        String[] expected = new String[] { "test1", "test2" };
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.END_ARRAY);
        Mockito.when(jsonParser.getText()).thenReturn("test1", "test2");
        Mockito.when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        Mockito.when(objectBuffer.resetAndStart()).thenReturn(new Object[2]);
        Mockito.when(objectBuffer.appendCompletedChunk(Mockito.any())).thenReturn(new Object[2]);
        Mockito.when(objectBuffer.completeAndClearBuffer(Mockito.any(), Mockito.anyInt(), Mockito.eq(String.class)))
                .thenReturn(expected);
        Mockito.doNothing().when(deserializationContext).returnObjectBuffer(Mockito.any());
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
        Mockito.verify(jsonParser, Mockito.times(3)).nextToken();
    }

    @Test
    public void testDeserialize_Custom() throws IOException {
        stringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] expected = new String[] { "test1", "test2" };
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.END_ARRAY);
        Mockito.when(jsonParser.getText()).thenReturn("test1", "test2");
        Mockito.when(elementDeserializer.getNullValue()).thenReturn(null);
        Mockito.when(elementDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test1", "test2");
        Mockito.when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        Mockito.when(objectBuffer.resetAndStart()).thenReturn(new Object[2]);
        Mockito.when(objectBuffer.appendCompletedChunk(Mockito.any())).thenReturn(new Object[2]);
        Mockito.when(objectBuffer.completeAndClearBuffer(Mockito.any(), Mockito.anyInt(), Mockito.eq(String.class)))
                .thenReturn(expected);
        Mockito.doNothing().when(deserializationContext).returnObjectBuffer(Mockito.any());
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
        Mockito.verify(jsonParser, Mockito.times(3)).nextToken();
    }

    @Test
    public void testDeserialize_ReturnNull() throws IOException {
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        Mockito.when(deserializationContext.isEnabled(Mockito.any())).thenReturn(false);
        try {
            stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
            fail("IOException is expected");
        } catch (IOException e) {
            assertEquals("com.fasterxml.jackson.core.JsonParseException: Current token (VALUE_STRING) not START_ARRAY, trying to skip... (through reference chain: com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer[\"_elementDeserializer\"])", e.getMessage());
        }
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
    }

    @Test
    public void testDeserialize_NullStringArray() throws IOException {
        String[] expected = null;
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        Mockito.when(deserializationContext.isEnabled(Mockito.any())).thenReturn(true);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
    }

    @Test
    public void testDeserialize_EmptyStringArray() throws IOException {
        String[] expected = new String[] { "" };
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getText()).thenReturn("");
        Mockito.when(deserializationContext.isEnabled(Mockito.any())).thenReturn(true);
        Mockito.when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT))
                .thenReturn(true);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
    }

    @Test
    public void testDeserialize_NonExpectedEmptyStringArray() throws IOException {
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getText()).thenReturn("");
        Mockito.when(deserializationContext.isEnabled(Mockito.any())).thenReturn(true);
        Mockito.when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT))
                .thenReturn(false);
        try {
            stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
            fail("IOException is expected");
        } catch (IOException e) {
            assertEquals("com.fasterxml.jackson.databind.JsonMappingException: Wrong token (VALUE_STRING), expected START_ARRAY: need JSON Array to contain As.POJO type information for class [Ljava.lang.String; (through reference chain: com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer[\"_elementDeserializer\"])", e.getMessage());
        }
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
    }

    @Test
    public void testDeserialize_NullObjectBuffer() throws IOException {
        String[] expected = new String[] { "test1" };
        Mockito.when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        Mockito.when(jsonParser.getText()).thenReturn("test1");
        Mockito.when(deserializationContext.leaseObjectBuffer()).thenReturn(null);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
        Mockito.verify(jsonParser, Mockito.times(2)).nextToken();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Mockito.when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn("type");
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals("type", result);
    }

    @Test
    public void testCreateContextual_DifferentElementDeserializer() throws JsonMappingException {
        Mockito.when(stringArrayDeserializer.isDefaultDeserializer(elementDeserializer)).thenReturn(false);
        Mockito.when(deserializationContext.handleSecondaryContextualization(elementDeserializer, null))
                .thenReturn(elementDeserializer);
        Mockito.when(deserializationContext.constructType(String.class)).thenReturn(null);
        Mockito.when(deserializationContext.findContextualValueDeserializer(null, null))
                .thenReturn(elementDeserializer);
        StringArrayDeserializer result = (StringArrayDeserializer) stringArrayDeserializer.createContextual(
                deserializationContext, null);
        assertNotNull(result);
        assertEquals(elementDeserializer, result._elementDeserializer);
    }

    @Test
    public void testCreateContextual_DefaultDeserializer() throws JsonMappingException {
        Mockito.when(stringArrayDeserializer.isDefaultDeserializer(null)).thenReturn(true);
        Mockito.when(deserializationContext.constructType(String.class)).thenReturn(null);
        Mockito.when(deserializationContext.findContextualValueDeserializer(null, null)).thenReturn(null);
        StringArrayDeserializer result = (StringArrayDeserializer) stringArrayDeserializer.createContextual(
                deserializationContext, null);
        assertNotNull(result);
        assertEquals(null, result._elementDeserializer);
    }

}
