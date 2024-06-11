package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        JsonParserSequence parserSequence1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonParserSequence parserSequence2 = new JsonParserSequence(new JsonParser[] { parser2, parser3 });

        JsonParser expectedParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });

        JsonParser actualParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(expectedParserSequence, actualParserSequence);

        verify(parser1, times(1)).close();
        verify(parser2, times(1)).close();

        JsonToken token = new JsonParser() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.START_OBJECT;
            }
        }.nextToken();

        when(parser1.nextToken()).thenReturn(token);
        when(parser2.nextToken()).thenReturn(JsonToken.END_OBJECT);

        JsonParser actualParser = actualParserSequence.delegate;
        JsonParser expectedParser = parser1;

        JsonToken actualToken = actualParserSequence.nextToken();
        JsonToken expectedToken = token;

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedParser, actualParser);

        actualParser = expectedParserSequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, actualToken);
        assertNotSame(expectedParser, actualParser);
        assertSame(parser2, actualParser);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        verify(parser1, times(0)).close();
        verify(parser2, times(0)).close();

        JsonToken token1 = new JsonToken(JsonTokenId.START_OBJECT);
        JsonToken token2 = new JsonToken(JsonTokenId.END_OBJECT);

        when(parser1.nextToken()).thenReturn(token1);
        when(parser2.nextToken()).thenReturn(token2);

        parserSequence.nextToken();
        parserSequence.nextToken();

        verify(parser1, times(1)).close();
        verify(parser2, times(1)).close();

        verify(parser1, times(1)).nextToken();
        verify(parser2, times(0)).nextToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchToNextWhenEndOfParsers() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        assertEquals(2, parserSequence.containedParsersCount());
    }
}