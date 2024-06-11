
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParserSequence parserSequence;
    private JsonParser[] parsers;

    @Before
    public void setUp() throws Exception {
        parsers = new JsonParser[] { null, null, null };
        parserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() {
        // Arrange

        // Act
        try {
            parserSequence.close();

            // Assert
            for (JsonParser parser : parsers) {
                assertNull(parser);
            }
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testNextToken() {
        // Arrange
        JsonParserDelegate delegate = parserSequence.delegate;
        JsonParserDelegate delegateSpy = Mockito.spy(delegate);
        Mockito.doReturn(JsonToken.START_OBJECT).when(delegateSpy).nextToken();
        parserSequence.delegate = delegateSpy;

        // Act
        try {
            JsonToken token = parserSequence.nextToken();

            // Assert
            assertEquals(JsonToken.START_OBJECT, token);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testContainedParsersCount() {
        // Act
        int count = parserSequence.containedParsersCount();

        // Assert
        assertEquals(parsers.length, count);
    }

}
