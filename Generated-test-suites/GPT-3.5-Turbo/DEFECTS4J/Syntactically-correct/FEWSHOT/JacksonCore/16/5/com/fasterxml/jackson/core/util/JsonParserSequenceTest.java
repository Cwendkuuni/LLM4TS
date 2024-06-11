package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;

public class JsonParserSequenceTest {

    private JsonParserSequence parserSequence;
    private ArrayList<JsonParser> parsers;

    @Before
    public void setUp() throws Exception {
        this.parsers = new ArrayList<JsonParser>();
        this.parserSequence = new JsonParserSequence(this.parsers.toArray(new JsonParser[this.parsers.size()]));
    }

    @Test
    public void testClose() throws IOException {
        // Test when the sequence is empty
        this.parserSequence.close();

        assertEquals(0, this.parsers.size());

        // Add a parser to the sequence
        JsonParser parser = new JsonParser();
        this.parsers.add(parser);

        // Test when the sequence has only one parser
        this.parserSequence.close();

        assertEquals(parser.isClosed(), true);

        // Add two more parsers to the sequence
        JsonParser secondParser = new JsonParser();
        this.parsers.add(secondParser);
        JsonParser thirdParser = new JsonParser();
        this.parsers.add(thirdParser);

        // Test when the sequence has three parsers
        this.parserSequence.close();

        assertEquals(parser.isClosed(), true);
        assertEquals(secondParser.isClosed(), true);
        assertEquals(thirdParser.isClosed(), true);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Add a parser to the sequence
        JsonParser parser = new JsonParser();
        this.parsers.add(parser);

        assertEquals(this.parserSequence.nextToken(), parser.nextToken());

        // Add two more parsers to the sequence
        JsonParser secondParser = new JsonParser();
        this.parsers.add(secondParser);
        JsonParser thirdParser = new JsonParser();
        this.parsers.add(thirdParser);

        assertEquals(this.parserSequence.nextToken(), parser.nextToken());
        assertEquals(this.parserSequence.nextToken(), secondParser.nextToken());
        assertEquals(this.parserSequence.nextToken(), thirdParser.nextToken());
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(this.parserSequence.containedParsersCount(), this.parsers.size());

        // Add two parsers to the sequence
        this.parsers.add(new JsonParser());
        this.parsers.add(new JsonParser());

        assertEquals(this.parserSequence.containedParsersCount(), this.parsers.size());
    }
}