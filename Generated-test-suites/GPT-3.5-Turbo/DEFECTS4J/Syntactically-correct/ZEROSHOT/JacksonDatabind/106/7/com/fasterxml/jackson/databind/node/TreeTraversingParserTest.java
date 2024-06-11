package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    @Test
    public void testSetCodec() {
        // Create a TreeTraversingParser object
        JsonNode n = new JsonNode(); // replace with actual JSON node
        TreeTraversingParser parser = new TreeTraversingParser(n);

        // Set the ObjectCodec
        ObjectCodec codec = new ObjectCodec(); // replace with actual ObjectCodec
        parser.setCodec(codec);

        // Verify that the ObjectCodec was set correctly
        assertEquals(codec, parser.getCodec());
    }

    // Implement tests for other methods in TreeTraversingParser class

}