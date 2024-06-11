package com.fasterxml.jackson.core.util; // make sure the test class is in the same package as the JsonParserSequence class

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceConstructor() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, jsonParserSequence.containedParsersCount());
        assertEquals(parser1, jsonParserSequence.delegate);
    }

    @Test
    public void testNextToken_delegateHasNextToken() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });
        JsonToken nextToken = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, nextToken);
        verify(parser).nextToken();
    }

    @Test
    public void testNextToken_delegateDoesNotHaveNextToken() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextToken()).thenReturn(null);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });
        JsonToken nextToken = jsonParserSequence.nextToken();
        assertNull(nextToken);
        verify(parser, times(2)).nextToken();
    }

    @Test
    public void testNextToken_switchToNextParser() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        when(parser1.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser2.nextToken()).thenReturn(JsonToken.START_OBJECT);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken nextToken = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, nextToken);
        nextToken = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, nextToken);
        verify(parser1).nextToken();
        verify(parser2).nextToken();
    }

    @Test(expected = IOException.class)
    public void testClose_delegateThrowsIOException() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.close()).thenThrow(new IOException("test"));
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });
        jsonParserSequence.close();
    }

    @Test
    public void testContainsOnlyOneParser() {
        JsonParser parser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });
        assertEquals(1, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_allParsersHaveNullNextToken() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        when(parser1.nextToken()).thenReturn(null);
        when(parser2.nextToken()).thenReturn(null);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken nextToken = jsonParserSequence.nextToken();
        assertNull(nextToken);
        verify(parser1, times(2)).nextToken();
        verify(parser2, times(2)).nextToken();
    }
}

