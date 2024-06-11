package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws Exception {
        JsonParserSequence jsonParserSequence = mock(JsonParserSequence.class, CALLS_REAL_METHODS);
        jsonParserSequence.close();
        verify(jsonParserSequence.delegate, times(1)).close();
        verify(jsonParserSequence, times(1)).switchToNext();
    }

    @Test
    public void testNextToken() throws Exception {
        JsonParserSequence jsonParserSequence = mock(JsonParserSequence.class, CALLS_REAL_METHODS);
        when(jsonParserSequence.delegate.nextToken()).thenReturn(null);
        when(jsonParserSequence.switchToNext()).thenReturn(true);
        jsonParserSequence.nextToken();
        verify(jsonParserSequence.delegate, times(1)).nextToken();
        verify(jsonParserSequence, times(1)).switchToNext();
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        JsonParserSequence jsonParserSequence = new JsonParserSequence(null);
        jsonParserSequence.containedParsersCount();
    }

    @Test
    public void testSwitchToNextTrue() throws Exception {
        JsonParserSequence jsonParserSequence = new JsonParserSequence(null);
        jsonParserSequence.delegate = mock(JsonParser.class);
        jsonParserSequence._nextParser = 0;
        boolean result = jsonParserSequence.switchToNext();
        assert(result);
    }

    @Test
    public void testSwitchToNextFalse() throws Exception {
        JsonParserSequence jsonParserSequence = new JsonParserSequence(null);
        jsonParserSequence.delegate = mock(JsonParser.class);
        jsonParserSequence._nextParser = 2;
        boolean result = jsonParserSequence.switchToNext();
        assert(!result);
    }

    @Test
    public void testCreateFlattened() throws Exception {
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);
        JsonParserSequence.createFlattened(first, second);
        verify(first, times(1)).addFlattenedActiveParsers(any());
        verify(second, times(1)).addFlattenedActiveParsers(any());
    }

    @Test
    public void testCreateFlattenedNoFlatten() throws Exception {
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);
        when(first instanceof JsonParserSequence).thenReturn(false);
        when(second instanceof JsonParserSequence).thenReturn(false);
        JsonParserSequence.createFlattened(first, second);
        verify(first, never()).addFlattenedActiveParsers(any());
        verify(second, never()).addFlattenedActiveParsers(any());
    }

    @Test
    public void testCreateFlattenedFirstFlatten() throws Exception {
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);
        when(first instanceof JsonParserSequence).thenReturn(true);
        when(second instanceof JsonParserSequence).thenReturn(false);
        JsonParserSequence.createFlattened(first, second);
        verify(first, times(1)).addFlattenedActiveParsers(any());
        verify(second, never()).addFlattenedActiveParsers(any());
    }

    @Test
    public void testCreateFlattenedSecondFlatten() throws Exception {
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);
        when(first instanceof JsonParserSequence).thenReturn(false);
        when(second instanceof JsonParserSequence).thenReturn(true);
        JsonParserSequence.createFlattened(first, second);
        verify(first, never()).addFlattenedActiveParsers(any());
        verify(second, times(1)).addFlattenedActiveParsers(any());
    }

    @Test
    public void testAddFlattenedActiveParsers() throws Exception {
        List<JsonParser> result = mock(List.class);
        JsonParser parser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = mock(JsonParserSequence.class, CALLS_REAL_METHODS);
        jsonParserSequence._parsers = new JsonParser[]{parser};
        jsonParserSequence.addFlattenedActiveParsers(result);
    }
}