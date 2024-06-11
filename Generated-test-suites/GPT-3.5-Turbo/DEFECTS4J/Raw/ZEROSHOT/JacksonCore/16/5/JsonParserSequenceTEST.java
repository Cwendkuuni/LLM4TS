package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {

    private JsonParser mockParser1;
    private JsonParser mockParser2;
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() {
        mockParser1 = mock(JsonParser.class);
        mockParser2 = mock(JsonParser.class);
        jsonParserSequence = new JsonParserSequence(new JsonParser[]{mockParser1, mockParser2});
    }

    @Test
    public void testClose() throws Exception {
        jsonParserSequence.close();
        verify(mockParser1, times(1)).close();
        verify(mockParser2, times(1)).close();
    }

    @Test
    public void testNextToken() throws Exception {
        JsonToken mockToken = mock(JsonToken.class);
        when(mockParser1.nextToken()).thenReturn(null);
        when(mockParser2.nextToken()).thenReturn(mockToken);

        JsonToken result = jsonParserSequence.nextToken();
        Assert.assertEquals(mockToken, result);

        verify(mockParser1, times(1)).nextToken();
        verify(mockParser2, times(1)).nextToken();
    }

    @Test
    public void testContainedParsersCount() {
        int result = jsonParserSequence.containedParsersCount();
        Assert.assertEquals(2, result);
    }
}