package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

    @SuppressWarnings("unchecked")
    private static JsonParser createMockParser(Class<?> expectedType) {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.nextValue()).thenReturn(createJsonNode(expectedType));
        return parserMock;
    }

    @SuppressWarnings("unchecked")
    private static JsonNode createJsonNode(Class<?> expectedType) {
        if (expectedType.isAssignableFrom(JsonNode.class)) {
            return mock(JsonNode.class);
        } else {
            return new ObjectMapper().readTree(String.format("{\"value\": %s}", expectedType.getName()));
        }
    }

    @Test
    public void testCreateFlattenedWithTwoParsers() {
        JsonParser firstParser = createMockParser(Integer.class);
        JsonParser secondParser = createMockParser(String.class);

        JsonParser sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);

        assertEquals(2, sequenceParser.containedParsersCount());
        assertEquals(firstParser, sequenceParser.delegate);

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, token.getId());
        assertEquals(1, ((JsonNode) token.getValue()).asInt());

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("value", token.getName());

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, token.getId());
        assertEquals("string", token.getValue().asText());
    }

    @Test
    public void testCreateFlattenedWithNestedParsers() {
        JsonParser firstParser = createMockParser(JsonParserSequence.class);
        JsonParser secondParser = createMockParser(Integer.class);

        JsonParser sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);

        assertEquals(2, sequenceParser.containedParsersCount());
        assertEquals(secondParser, sequenceParser.delegate);

        JsonToken token = sequenceParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        JsonParser nestedSequenceParser = (JsonParserSequence) firstParser;
        JsonParser nestedParser = createMockParser(String.class);
        JsonParser nestedSecondParser = createMockParser(Boolean.class);
        nestedSequenceParser.addFlattenedActiveParsers(Arrays.asList(nestedParser, nestedSecondParser));

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, token.getId());
        assertEquals(1, ((JsonNode) token.getValue()).asInt());

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("parserSequence", token.getName());

        JsonParser nestedParserDelegate = nestedParser;
        while (nestedParserDelegate instanceof JsonParserSequence) {
            nestedParserDelegate = ((JsonParserSequence) nestedParserDelegate).delegate;
        }
        assertEquals(nestedSecondParser, nestedParserDelegate);

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, token.getId());
        assertEquals("string", token.getValue().asText());

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("booleanParser", token.getName());

        token = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, token.getId());
        assertEquals(true, token.getValue().asBoolean());
    }

    @Test
    public void testSwitchToNextParserWhenFirstParserHasNoMoreTokens() {
        JsonParser firstParser = createMockParser(JsonToken.VALUE);
        JsonParser secondParser = createMockitoNiceMock(JsonParser.class);

        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[]{firstParser, secondParser});

        JsonToken firstToken = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, firstToken.getId());
        assertEquals("value1", firstToken.getValue().asText());

        firstToken = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, firstToken.getId());
        assertEquals("value2", firstToken.getValue().asText());

        firstToken = sequenceParser.nextToken();
        assertEquals(JsonToken.END_ARRAY, firstToken.getId());

        verify(firstParser).close();
        verify(secondParser).nextToken();
        verifyNoMoreInteractions(firstParser);

        JsonToken secondToken = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, secondToken.getId());
    }

    @Test
    public void testSwitchToNextParserWhenFirstParserHasNoMoreTokensButThrowsException() {
        JsonParser firstParser = createMockParser(JsonToken.VALUE);
        doThrow(new IOException("Exception from first parser")).when(firstParser).nextToken();
        JsonParser secondParser = createMockitoNiceMock(JsonParser.class);

        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[]{firstParser, secondParser});

        JsonToken firstToken = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, firstToken.getId());
        assertEquals("value1", firstToken.getValue().asText());

        firstToken = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE, firstToken.getId());
        assertEquals("value2", firstToken.getValue().asText());

        firstToken = sequenceParser.nextToken();
        assertEquals(JsonToken.END_ARRAY, firstToken.getId());

        verify(firstParser).close();
        verify(secondParser).nextToken();
        verifyNoMoreInteractions(firstParser);

        JsonToken secondToken = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, secondToken.getId());
    }

    @Test
    public void testSwitchToNextParserWhenFirstParserThrowsException() {
        JsonParser firstParser = createMockParser(IOException.class);
        JsonParser secondParser = createMockitoNiceMock(JsonParser.class);

        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[]{firstParser, secondParser});

        verify(firstParser).nextToken();
        verify(firstParser).close();
        verify(secondParser).nextToken();

        JsonToken secondToken = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, secondToken.getId());
    }

    @Test
    public void testCloseParsers() throws IOException {
        JsonParser firstParser = mock(JsonParser.class);
        JsonParser secondParser = mock(JsonParser.class);

        JsonParser sequenceParser = new JsonParserSequence(new JsonParser[]{firstParser, secondParser});

        sequenceParser.close();

        verify(firstParser).close();
        verify(secondParser).close();
    }
}