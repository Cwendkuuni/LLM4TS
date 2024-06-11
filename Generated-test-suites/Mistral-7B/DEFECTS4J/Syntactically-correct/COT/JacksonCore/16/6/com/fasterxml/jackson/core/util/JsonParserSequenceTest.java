package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {

    private JsonParser parser1, parser2, parser3;
    private JsonParserSequence sequence;

    @Before
    public void setup() throws IOException {
        parser1 = mock(JsonParser.class);
        parser2 = mock(JsonParser.class);
        parser3 = mock(JsonParser.class);
        sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
    }

    @Test
    public void testConstructor() {
        assertEquals(3, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken_FirstParser_ReturnsExpectedToken() throws IOException, JsonProcessingException {
        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testNextToken_FirstParser_ReturnsNull_ReturnsNextParser() throws IOException, JsonProcessingException {
        when(parser1.nextToken()).thenReturn(null);
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonToken token = sequence.nextToken();
        assertSame(parser2, sequence.delegate);
        assertEquals(JsonToken.VALUE_STRING, token);
    }

    @Test(expected = JsonProcessingException.class)
    public void testNextToken_AllParsersReturnNull() throws IOException, JsonProcessingException {
        when(parser1.nextToken()).thenReturn(null);
        when(parser2.nextToken()).thenReturn(null);
        when(parser3.nextToken()).thenReturn(null);

        sequence.nextToken();
    }

    @Test
    public void testNextToken_LastParser_ReturnsExpectedToken() throws IOException, JsonProcessingException {
        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.END_OBJECT);
        when(parser3.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
    }

    @Test
    public void testClose() throws IOException {
        sequence.close();

        for (JsonParser parser : sequence._parsers) {
            parser.close();
        }
    }

    @Test
    public void testSwitchToNext_NoMoreParsers() {
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNext_SwitchToFirstParser() {
        assertSame(parser1, sequence.delegate);
        assertTrue(sequence.switchToNext());
        assertSame(parser2, sequence.delegate);
    }

    @Test
    public void testSwitchToNext_SwitchToLastParser() {
        for (int i = 0; i < sequence._parsers.length - 1; i++) {
            assertTrue(sequence.switchToNext());
        }
        assertSame(parser3, sequence.delegate);
        assertFalse(sequence.switchToNext());
    }

    @Test(expected = IOException.class)
    public void testSwitchToNext_IOException_ThrowsException() throws IOException {
        when(parser1.nextToken()).thenThrow(new IOException());
        sequence.switchToNext();
    }
}