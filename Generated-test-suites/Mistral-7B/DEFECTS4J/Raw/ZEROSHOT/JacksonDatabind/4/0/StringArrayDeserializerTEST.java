package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.serializer.StandardSerializer;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    private JsonParser jsonParser;
    private DeserializationContext context;
    private ObjectBuffer buffer;

    @Before
    public void setup() throws IOException {
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        buffer = mock(ObjectBuffer.class);
    }

    @Test
    public void testDeserializeStandard() throws JsonProcessingException, IOException {
        // Given: Standard deserialization
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("Hello");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("World");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, context);
        assertArrayEquals(new String[] {"Hello", "World"}, result);

        // Then: No custom deserializer was used
        when(deserializer._elementDeserializer).thenReturn(null);
        assertNull(deserializer._elementDeserializer);
    }

    @Test
    public void testDeserializeCustom() throws JsonProcessingException, IOException {
        // Given: Custom deserialization
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("Hello");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("World");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(context.findContextualValueDeserializer(String.class, null)).thenReturn(customDeserializer);
        when(deserializer.isDefaultDeserializer(customDeserializer)).thenReturn(false);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(jsonParser, context);
        assertArrayEquals(new String[] {"Hello", "World"}, result);

        // Then: Custom deserializer was used
        assertNotNull(deserializer._elementDeserializer);
        assertEquals(customDeserializer, deserializer._elementDeserializer);
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException, IOException {
        // Given: Empty Array
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, context);
        assertArrayEquals(new String[0], result);

        // Then: No custom deserializer was used
        assertNull(deserializer._elementDeserializer);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws JsonProcessingException, IOException {
        // Given: Invalid JSON
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testCreateContextual() throws JsonProcessingException, IOException {
        // Given: Contextual deserialization
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("Hello");

        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(String.class, null)).thenReturn(stringDeserializer);
        when(deserializer.isDefaultDeserializer(stringDeserializer)).thenReturn(false);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        JsonDeserializer<String[]> result = deserializer.createContextual(context, null);

        // Then: A new instance of StringArrayDeserializer was returned with the custom deserializer set
        assertSame(deserializer, result);
        assertNotNull(deserializer._elementDeserializer);
        assertEquals(stringDeserializer, deserializer._elementDeserializer);
    }

    @Test
    public void testCreateContextualNoCustomDeserializer() throws JsonProcessingException, IOException {
        // Given: No custom deserializer
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("Hello");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<String[]> result = deserializer.createContextual(context, null);

        // Then: A new instance of StringArrayDeserializer was returned with no custom deserializer
        assertSame(deserializer, result);
        assertNull(deserializer._elementDeserializer);
    }

    @Test
    public void testDeserializeWithType() throws JsonProcessingException, IOException {
        // Given: Deserialize with type
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("Hello");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("World");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(context.findValueDeserializer(String[].class)).thenReturn(typeDeserializer);

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);

        // Then: The deserializer returned the deserialized array
        assertArrayEquals(new String[] {"Hello", "World"}, result);

        // Then: No custom deserializer was used
        assertNull(deserializer._elementDeserializer);
    }

    @Test
    public void testHandleNonArraySingleValue() throws JsonProcessingException, IOException {
        // Given: Single value is treated as an array if 'acceptSingleValueAsArray' is enabled
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("Hello");

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jsonParser, context);

        // Then: A single value is treated as an array if 'acceptSingleValueAsArray' is enabled
        assertArrayEquals(new String[] {"Hello"}, result);

        // Then: No custom deserializer was used
        assertNull(deserializer._elementDeserializer);
    }

    @Test
    public void testHandleNonArrayEmptyString() throws JsonProcessingException, IOException {
        // Given: An empty string is treated as null if 'acceptEmptyStringAsNullObject' is enabled
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("");

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(jsonParser, context);

        // Then: An empty string is treated as null if 'acceptEmptyStringAsNullObject' is enabled
        assertArrayEquals(new String[] {null}, result);

        // Then: No custom deserializer was used
        assertNull(deserializer._elementDeserializer);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArrayInvalidJson() throws JsonProcessingException, IOException {
        // Given: Invalid JSON
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("Hello");

        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.handleNonArray(jsonParser, context);
    }
}