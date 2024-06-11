package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
    
    @Test
    public void testClose() throws IOException {
        JsonParser[] parsers = { mock(JsonParser.class), mock(JsonParser.class) };
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        sequence.close();
        
        verify(parsers[0]).close();
        verify(parsers[1]).close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken token1 = JsonToken.START_OBJECT;
        JsonToken token2 = JsonToken.FIELD_NAME;
        JsonToken token3 = JsonToken.VALUE_STRING;
        
        JsonParser[] parsers = { mock(JsonParser.class), mock(JsonParser.class) };
        when(parsers[0].nextToken()).thenReturn(null); // First parser returns null
        when(parsers[1].nextToken()).thenReturn(token1, token2, token3); // Second parser returns tokens
        
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        JsonToken result1 = sequence.nextToken();
        JsonToken result2 = sequence.nextToken();
        JsonToken result3 = sequence.nextToken();
        JsonToken result4 = sequence.nextToken();
        
        assertEquals(token1, result1);
        assertEquals(token2, result2);
        assertEquals(token3, result3);
        assertEquals(null, result4); // Second parser returned non-null token, so no more parser-switching required
        
        verify(parsers[0]).nextToken();
        verify(parsers[1]).nextToken();
        verifyNoMoreInteractions(parsers[0]);
        verifyNoMoreInteractions(parsers[1]);
    }

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence1 = mock(JsonParserSequence.class);
        JsonParserSequence sequence2 = mock(JsonParserSequence.class);
        
        JsonParserSequence result1 = JsonParserSequence.createFlattened(parser1, parser2);
        JsonParserSequence result2 = JsonParserSequence.createFlattened(sequence1, parser2);
        JsonParserSequence result3 = JsonParserSequence.createFlattened(parser1, sequence2);
        JsonParserSequence result4 = JsonParserSequence.createFlattened(sequence1, sequence2);
        
        verifyNoInteractions(parser1, parser2, sequence1, sequence2);
        assertEquals(2, result1.containedParsersCount());
        assertEquals(1, result2.containedParsersCount());
        assertEquals(1, result3.containedParsersCount());
        assertEquals(2, result4.containedParsersCount());
    }
}