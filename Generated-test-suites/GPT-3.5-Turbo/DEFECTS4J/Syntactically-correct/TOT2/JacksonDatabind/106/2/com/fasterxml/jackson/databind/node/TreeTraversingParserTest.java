package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

public class TreeTraversingParserTest extends TestCase {

    @Test
    public void testConstructor() {
        JsonNode node = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(0, parser.getTextOffset());
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testSetCodec() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        ObjectMapper codec = new ObjectMapper();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        assertFalse(parser.isClosed());
        try {
            parser.close();
        } catch (IOException e) {
            fail("Exception not expected here");
        }
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        try {
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        } catch (IOException | JsonParseException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testSkipChildren() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        try {
            parser.nextToken();
            parser.skipChildren();
            assertEquals(JsonToken.END_OBJECT, parser.currentToken());
        } catch (IOException | JsonParseException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testGetCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testGetNumberType() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        try {
            assertNull(parser.getNumberType());
        } catch (IOException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testGetEmbeddedObject() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testGetBinaryValue() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().createObjectNode());
        try {
            assertNull(parser.getBinaryValue(null));
        } catch (IOException | JsonParseException e) {
            fail("Exception not expected here");
        }
    }

}