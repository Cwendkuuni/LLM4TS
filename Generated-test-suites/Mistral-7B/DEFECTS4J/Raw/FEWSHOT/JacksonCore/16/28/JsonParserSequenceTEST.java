package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        when(parser1.isSequentialMode()).thenReturn(true);
        when(parser1.getParserEventManager()).thenReturn(mock(JsonParser.EventManager.class));
        when(parser1.nextValue()).thenReturn(JsonNode.value("parser1Value"));

        when(parser2.isSequentialMode()).thenReturn(true);
        when(parser2.getParserEventManager()).thenReturn(mock(JsonParser.EventManager.class));
        when(parser2.nextValue()).thenReturn(JsonNode.value("parser2Value"));

        when(parser3.isSequentialMode()).thenReturn(false);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);

        assertEquals(3, sequence.containedParsersCount());
        assertEquals(parser1, sequence.delegate);
        verify(parser1).nextToken();
        verify(parser2).nextToken();
        verifyZeroInteractions(parser3);

        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.id);

        JsonNode value = ((JsonParser) sequence.delegate).getValueAsTree();
        assertEquals("parser1Value", value.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.isSequentialMode()).thenReturn(true);
        when(parser1.getParserEventManager()).thenReturn(mock(JsonParser.EventManager.class));
        when(parser1.nextValue()).thenReturn(JsonNode.value("parser1Value"));

        when(parser2.isSequentialMode()).thenReturn(true);
        when(parser2.getParserEventManager()).thenReturn(mock(JsonParser.EventManager.class));
        when(parser2.nextValue()).thenReturn(JsonNode.value("parser2Value"));

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        assertEquals(2, sequence.containedParsersCount());
        assertEquals(parser1, sequence.delegate);

        JsonToken token1 = sequence.nextToken();
        assertNotNull(token1);
        assertEquals(JsonToken.START_OBJECT, token1.id);

        JsonParser newDelegate = sequence.delegate;
        assertTrue(sequence.switchToNext());
        assertNotSame(newDelegate, sequence.delegate);
        assertEquals(parser2, sequence.delegate);

        JsonToken token2 = sequence.nextToken();
        assertNotNull(token2);
        assertEquals(JsonToken.START_ARRAY, token2.id);

        JsonParser originalDelegate = sequence.delegate;
        assertTrue(sequence.switchToNext());
        assertSame(originalDelegate, sequence.delegate);
        assertEquals(parser1, sequence.delegate);

        JsonToken token3 = sequence.nextToken();
        assertNull(token3);
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.isClosed()).thenReturn(false);
        when(parser1.close()).thenReturn(true);
        when(parser1.nextToken()).thenReturn(null);

        when(parser2.isClosed()).thenReturn(false);
        when(parser2.close()).thenReturn(true);
        when(parser2.nextToken()).thenReturn(null);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonParser originalDelegate = sequence.delegate;

        sequence.close();

        verify(parser1).close();
        verify(parser2).close();
        verify(parser1).nextToken();
        verify(parser2).nextToken();

        JsonToken token = sequence.nextToken();
        assertNull(token);

        assertTrue(originalDelegate.isClosed());
        assertTrue(sequence.delegate.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchToNextAfterClose() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.isClosed()).thenReturn(true);
        when(parser1.nextToken()).thenReturn(null);

        when(parser2.isClosed()).thenReturn(false);
        when(parser2.nextToken()).thenReturn(null);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        sequence.close();
        sequence.switchToNext();
    }
}