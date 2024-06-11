
package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {
    
    private JsonParserSequence parserSequence;
    private JsonParser[] parsers;
    
    @Before
    public void setup() {
        parsers = new JsonParser[3];
        for (int i = 0; i < parsers.length; i++) {
            parsers[i] = mock(JsonParser.class);
        }
        parserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void close_ShouldCloseAllParsers_WhenCalled() throws Exception {
        // Act
        parserSequence.close();

        // Assert
        for (JsonParser parser : parsers) {
            verify(parser).close();
        }
    }
    
    @Test
    public void nextToken_ShouldReturnNextTokenFromParsers() throws Exception {
        // Arrange
        JsonToken token1 = JsonToken.START_ARRAY;
        JsonToken token2 = JsonToken.VALUE_STRING;
        JsonToken token3 = JsonToken.END_ARRAY;

        when(parsers[0].nextToken()).thenReturn(token1);
        when(parsers[1].nextToken()).thenReturn(token2);
        when(parsers[2].nextToken()).thenReturn(token3);

        // Act & Assert
        assertSame(token1, parserSequence.nextToken());
        assertSame(token2, parserSequence.nextToken());
        assertSame(token3, parserSequence.nextToken());

        // Verify that parsers were called in sequence
        verify(parsers[0]).nextToken();
        verify(parsers[1]).nextToken();
        verify(parsers[2]).nextToken();
    }
    
    @Test
    public void nextToken_ShouldReturnNull_WhenAllParsersReturnNull() throws Exception {
        // Arrange
        when(parsers[0].nextToken()).thenReturn(null);
        when(parsers[1].nextToken()).thenReturn(null);
        when(parsers[2].nextToken()).thenReturn(null);

        // Act & Assert
        assertNull(parserSequence.nextToken());

        // Verify that parsers were called in sequence
        verify(parsers[0]).nextToken();
        verify(parsers[1]).nextToken();
        verify(parsers[2]).nextToken();
    }
    
    @Test
    public void containedParsersCount_ShouldReturnCorrectCount() {
        // Assert
        assertEquals(3, parserSequence.containedParsersCount());
    }
    
    @Test
    public void createFlattened_ShouldCreateFlattenedSequence_WhenGivenSequences() {
        // Arrange
        JsonParserSequence sequence1 = mock(JsonParserSequence.class);
        JsonParserSequence sequence2 = mock(JsonParserSequence.class);
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        when(sequence1.containedParsersCount()).thenReturn(2);
        when(sequence1._parsers).thenReturn(new JsonParser[] { parser1, parser2 });
        when(sequence2.containedParsersCount()).thenReturn(1);
        when(sequence2._parsers).thenReturn(new JsonParser[] { parser3 });

        // Act
        JsonParserSequence flattenedSequence = JsonParserSequence.createFlattened(sequence1, sequence2);

        // Assert
        assertArrayEquals(new JsonParser[] { parser1, parser2, parser3 }, flattenedSequence._parsers);
    }
}

