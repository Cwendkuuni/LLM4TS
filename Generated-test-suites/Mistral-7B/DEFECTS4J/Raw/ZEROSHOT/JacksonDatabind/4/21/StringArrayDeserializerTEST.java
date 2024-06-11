package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test class for StringArrayDeserializer.
 */
@JacksonStdImpl
public class StringArrayDeserializerTest extends TestBase {

    private ObjectBuffer mockObjectBuffer;
    private StandardDeserializationContext mockContext;
    private JsonParser mockJsonParser;
    private StringArrayDeserializer deserializer;

    @Before
    public void setup() {
        mockObjectBuffer = mock(ObjectBuffer.class);
        mockContext = mock(StandardDeserializationContext.class);
        mockJsonParser = mock(JsonParser.class);
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize_NullElementDeserializer() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserialize_EmptyJsonArray() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserialize_SingleValueString() throws IOException {
        String testString = "test";
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn(testString);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[] { testString }, result);
    }

    @Test
    public void testDeserialize_SingleValueNull() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[] { null }, result);
    }

    @Test
    public void testDeserialize_MultipleValues() throws IOException {
        String value1 = "test1";
        String value2 = "test2";

        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn(value1);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn(value2);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[] { value1, value2 }, result);
    }

    @Test
    public void testDeserializeWithContextualDeserializer() throws IOException {
        JsonDeserializer<String> mockElementDeserializer = mock(JsonDeserializer.class);

        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(mockContext.findContextualValueDeserializer(mock(Class.class), anyObject())).thenReturn(mockElementDeserializer);

        StringArrayDeserializer deserializerWithElementDeserializer = new StringArrayDeserializer(mockElementDeserializer);

        String[] result = deserializerWithElementDeserializer.deserialize(mockJsonParser, mockContext);

        assertEquals("test", result[0]);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_InvalidJson() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");

        deserializer.deserialize(mockJsonParser, mockContext);

        // This shouldn't be reached as the test should throw an IOException
    }

    @Test
    public void testDeserializeCustom_NullElementDeserializer() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        String[] result = deserializer._deserializeCustom(mockJsonParser, mockContext);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeCustom_EmptyJsonArray() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer._deserializeCustom(mockJsonParser, mockContext);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeCustom_SingleValueString() throws IOException {
        String testString = "test";
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn(testString);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer._deserializeCustom(mockJsonParser, mockContext);

        assertArrayEquals(new String[] { testString }, result);
    }

    @Test
    public void testDeserializeCustom_SingleValueNull() throws IOException {
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer._deserializeCustom(mockJsonParser, mockContext);

        assertArrayEquals(new String[] { null }, result);
    }

    @Test
    public void testDeserializeCustom_MultipleValues() throws IOException {
        String value1 = "test1";
        String value2 = "test2";

        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn(value1);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn(value2);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer._deserializeCustom(mockJsonParser, mockContext);

        assertArrayEquals(new String[] { value1, value2 }, result);
    }
}