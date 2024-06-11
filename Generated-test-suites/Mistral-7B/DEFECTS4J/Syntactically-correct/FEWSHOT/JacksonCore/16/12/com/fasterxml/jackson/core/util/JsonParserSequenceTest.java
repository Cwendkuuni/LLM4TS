package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParser sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);

        verify(firstParser, times(1)).close();
        verify(secondParser, times(1)).close();

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Test that both parsers were used
        verify(firstParser, times(1)).nextToken();
        verify(secondParser, times(1)).nextToken();

        // Test that both parsers were closed when done
        sequenceParser.close();
        verify(firstParser, times(1)).close();
        verify(secondParser, times(1)).close();
    }

    @Test
    public void testSwitchToNext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser firstParser = mapper.getFactory().createParser("[]");
        JsonParser secondParser = mapper.getFactory().createParser("{}");
        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonToken token = null;
        while (token == null) {
            token = sequenceParser.nextToken();
        }

        assertEquals(JsonToken.START_ARRAY, token.getId());

        sequenceParser.nextToken();
        sequenceParser.nextToken(); // Both parsers should have returned START_OBJECT

        // Switch to the second parser
        sequenceParser.switchToNext();

        // First parser should have been closed
        firstParser.close();
        verify(firstParser, times(1)).close();

        // Second parser should be active now
        JsonParser activeParser = sequenceParser.delegate;
        assertEquals(secondParser, activeParser);

        // Second parser should return START_OBJECT
        token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);
        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        assertEquals(2, sequenceParser.containedParsersCount());

        verifyNoMoreInteractions(firstParser);
        verifyNoMoreInteractions(secondParser);
    }

    @Test
    public void testCreateFlattenedWithNestedSequences() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);
        JsonParser parser4 = mock(JsonParser.class);

        JsonParserSequence parser5 = JsonParserSequence.createFlattened(parser1, JsonParserSequence.createFlattened(parser2, parser3));

        // Verify that parser1 was closed
        parser1.close();
        verify(parser1, times(1)).close();

        // Verify that parser2 and parser3 were closed
        JsonParser activeParser2 = parser2;
        JsonParser activeParser3 = parser3;
        do {
            activeParser2 = parser2.delegate;
            activeParser3 = parser3.delegate;
            activeParser2.close();
            activeParser3.close();
        } while (activeParser2 instanceof JsonParserSequence);
        verify(parser2, times(1)).close();
        verify(parser3, times(1)).close();

        JsonToken token = parser5.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Verify that parser2 and parser3 were used
        inOrder(parser2, parser3).thenReturn(JsonToken.START_OBJECT).thenReturn(JsonToken.END_OBJECT);
        verify(parser2, times(2)).nextToken();
        verify(parser3, times(2)).nextToken();

        token = parser5.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        // Verify that parser4 was not used
        JsonParser parser6 = new JsonParserSequence(new JsonParser[] { parser4 });
        parser5.addParser(parser6);
        token = parser5.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        verify(parser4, never()).nextToken();
    }

    @Test
    public void testAddParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser firstParser = mapper.getFactory().createParser("[]");
        JsonParser secondParser = mapper.getFactory().createParser("{}");
        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        // Add a new parser
        JsonParser newParser = mapper.getFactory().createParser("true");
        sequenceParser.addParser(newParser);

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        sequenceParser.nextToken(); // Both parsers should have returned START_OBJECT

        // Switch to the new parser
        sequenceParser.switchToNext();

        // First two parsers should have been closed
        firstParser.close();
        secondParser.close();
        verify(firstParser, times(1)).close();
        verify(secondParser, times(1)).close();

        // New parser should be active now
        JsonParser activeParser = sequenceParser.delegate;
        assertEquals(newParser, activeParser);

        // New parser should return BOOLEAN
        token = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE_TRUE, token.getId());

        // Close all remaining parsers
        JsonParser activeParser2 = sequenceParser.delegate;
        JsonParser activeParser3 = null;
        do {
            activeParser3 = activeParser2.delegate;
            activeParser2.close();
            activeParser3 = activeParser3;
        } while (activeParser3 != null && activeParser3 instanceof JsonParserSequence);
        verify(newParser, times(1)).close();
    }

    @Test
    public void testAddParserWithNestedSequences() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser1 = mapper.getFactory().createParser("[]");
        JsonParser parser2 = mapper.getFactory().createParser("{}");
        JsonParser parser3 = mapper.getFactory().createParser("[]");
        JsonParser parser4 = mapper.getFactory().createParser("{}");
        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[] { parser1, JsonParserSequence.createFlattened(parser2, parser3) });

        // Add a new parser
        JsonParser newParser = mapper.getFactory().createParser("true");
        sequenceParser.addParser(newParser);

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Verify that parser1 was closed
        parser1.close();
        verify(parser1, times(1)).close();

        // Verify that parser2 and parser3 were closed
        JsonParser activeParser2 = parser2;
        JsonParser activeParser3 = parser3;
        do {
            activeParser2 = parser2.delegate;
            activeParser3 = parser3.delegate;
            activeParser2.close();
            activeParser3.close();
        } while (activeParser2 instanceof JsonParserSequence);
        verify(parser2, times(1)).close();
        verify(parser3, times(1)).close();

        // New parser should be active now
        JsonParser activeParser = sequenceParser.delegate;
        assertEquals(newParser, activeParser);

        // New parser should return BOOLEAN
        token = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE_TRUE, token.getId());

        // Close all remaining parsers
        JsonParser activeParser2_2 = activeParser2;
        JsonParser activeParser3_2 = null;
        JsonParser activeParser4 = null;
        JsonParser activeParser5 = null;
        do {
            activeParser5 = activeParser4;
            activeParser4 = activeParser3_2;
            activeParser3_2 = activeParser3;
            activeParser3 = activeParser2.delegate;
            activeParser2 = activeParser2_2;
            activeParser2.close();
            activeParser3.close();
            activeParser4 = activeParser5;
            activeParser5 = activeParser4.delegate;
        } while (activeParser5 != null && activeParser5 instanceof JsonParserSequence);
        verify(newParser, times(1)).close();
    }
}