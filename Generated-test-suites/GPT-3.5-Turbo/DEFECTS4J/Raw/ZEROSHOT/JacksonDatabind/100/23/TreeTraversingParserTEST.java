package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    @Test
    public void testSetCodec() {
        JsonNode n = new JsonNode();
        ObjectCodec codec = new ObjectCodec();
        TreeTraversingParser parser = new TreeTraversingParser(n);
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        JsonNode n = new JsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(n);
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() {
        JsonNode n = new JsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(n);
        try {
            parser.close();
            assertTrue(parser.isClosed());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    // Add tests for other methods

}