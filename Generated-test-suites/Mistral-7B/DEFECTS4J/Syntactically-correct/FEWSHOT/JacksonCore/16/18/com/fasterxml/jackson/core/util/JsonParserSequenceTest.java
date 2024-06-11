package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = createMockParser("{ \"name\": \"John\" }");
        JsonParser parser2 = createMockParser("{ \"age\": 25 }");
        JsonParserSequence seq = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, seq.containedParsersCount());
        assertEquals(JsonToken.START_OBJECT, seq.nextToken());
        assertEquals("name", seq.getCurrentName());
        assertEquals("John", seq.readValueAs(String.class));

        assertTrue(seq.nextToken() == JsonToken.FIELD_NAME);
        assertEquals("age", seq.getCurrentName());

        assertEquals(JsonToken.VALUE_NUMBER_INT, seq.nextToken());
        assertEquals(25, seq.readValueAs(Integer.class));

        assertTrue(seq.nextToken() == JsonToken.END_OBJECT);
        assertFalse(seq.nextToken() == null);

        assertNull(seq.readValueAs(String.class)); // should throw exception
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        JsonParser parser1 = createMockParser("{ \"name\": \"John\" }");
        JsonParser parser2 = createMockParser("{ \"age\": 25 }");
        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        assertEquals(JsonToken.START_OBJECT, seq.nextToken());
        assertTrue(seq.switchToNext());
        assertEquals(JsonToken.START_OBJECT, seq.nextToken());

        assertTrue(seq.switchToNext());
        assertFalse(seq.switchToNext());
    }

    @Test
    public void testCloseAllParsers() throws IOException {
        JsonParser parser1 = createMockParser("{ \"name\": \"John\" }");
        JsonParser parser2 = createMockParser("{ \"age\": 25 }");
        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        seq.close();
        parser1.close();
        parser2.close();
    }

    private JsonParser createMockParser(String json) throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextToken()).thenReturn(parseJsonToken(json));
        return parser;
    }

    private JsonToken parseJsonToken(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new StringReader(json)).iterator().next();
    }
}