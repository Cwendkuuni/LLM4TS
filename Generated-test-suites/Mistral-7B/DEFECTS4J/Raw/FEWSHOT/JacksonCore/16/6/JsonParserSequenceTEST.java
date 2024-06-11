package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonParserSequenceTest {

    private JsonParser firstParserMock;
    private JsonParser secondParserMock;
    private JsonParser thirdParserMock;
    private JsonParser sequenceParser;

    @Before
    public void setUp() throws Exception {
        firstParserMock = mock(JsonParser.class);
        secondParserMock = mock(JsonParser.class);
        thirdParserMock = mock(JsonParser.class);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();

        doReturn(JsonToken.START_OBJECT).when(firstParserMock).nextToken();
        doReturn(JsonToken.FIELD_NAME).when(firstParserMock).nextToken();
        doReturn("name1").when(firstParserMock).getCurrentName();
        doReturn("value1").when(firstParserMock).getText();
        doReturn(JsonToken.END_OBJECT).when(firstParserMock).nextToken();

        doReturn(JsonToken.START_OBJECT).when(secondParserMock).nextToken();
        doReturn("name2").when(secondParserMock).getCurrentName();
        doReturn("value2").when(secondParserMock).getText();
        doReturn(JsonToken.END_OBJECT).when(secondParserMock).nextToken();

        doReturn(JsonToken.START_OBJECT).when(thirdParserMock).nextToken();
        doReturn("name3").when(thirdParserMock).getCurrentName();
        doReturn("value3").when(thirdParserMock).getText();
        doReturn(JsonToken.END_OBJECT).when(thirdParserMock).nextToken();

        sequenceParser = JsonParserSequence.createFlattened(firstParserMock, secondParserMock, thirdParserMock);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(3, sequenceParser.containedParsersCount());
    }

    @Test
    public void testNextToken_ReturnsTokenFromFirstParser() throws IOException {
        JsonToken jsonToken = sequenceParser.nextToken();
        assertEquals(JsonToken.START_OBJECT, jsonToken);
    }

    @Test
    public void testNextToken_ReturnsTokenFromSecondParser() throws IOException {
        JsonToken jsonToken = sequenceParser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, jsonToken);
    }

    @Test
    public void testNextToken_ReturnsTokenFromThirdParser() throws IOException {
        JsonToken jsonToken = sequenceParser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, jsonToken);

        jsonToken = sequenceParser.nextToken();
        assertEquals("name3", jsonToken.getCurrentName());
        assertEquals("value3", jsonToken.getText());
        assertEquals(JsonToken.END_OBJECT, jsonToken);
    }

    @Test(expected = IOException.class)
    public void testNextToken_ThrowsIOException_WhenAllParsersHaveEnded() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();
        sequenceParser.nextToken();
    }

    @Test
    public void testClose() throws IOException {
        sequenceParser.close();
        verify(firstParserMock, times(1)).close();
        verify(secondParserMock, times(1)).close();
        verify(thirdParserMock, times(1)).close();
    }
}