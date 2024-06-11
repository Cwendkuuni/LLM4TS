package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectCodec;

public class TreeTraversingParserTest {
    private TreeTraversingParser parser;
    
    @Before
    public void setup() {
        JsonNode node = new ObjectMapper().createObjectNode();
        parser = new TreeTraversingParser(node, null);
    }
    
    @Test
    public void testSetCodec()
    {
        assertNull(parser.getCodec());
        
        ObjectCodec codec = new ObjectMapper();
        parser.setCodec(codec);
        
        assertEquals(codec, parser.getCodec());
    }
    
    @Test
    public void testVersion()
    {
        assertNotNull(parser.version());
    }
    
    @Test
    public void testClose() throws Exception
    {
        assertFalse(parser.isClosed());
        
        parser.close();
        
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testNextToken() throws Exception
    {
        assertNull(parser.nextToken());
        
        JsonNode node = new ObjectMapper().createObjectNode();
        parser = new TreeTraversingParser(node, null);
        
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }
    
    @Test
    public void testSkipChildren() throws Exception
    {
        assertEquals(parser, parser.skipChildren());
        
        JsonNode node = new ObjectMapper().createArrayNode();
        parser = new TreeTraversingParser(node, null);
        
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken());
    }
    
    @Test
    public void testIsClosed() throws Exception
    {
        assertFalse(parser.isClosed());
        
        parser.close();
        
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testGetCurrentName() throws Exception
    {
        assertNull(parser.getCurrentName());
    }
    
    @Test
    public void testOverrideCurrentName() throws Exception
    {
        parser.overrideCurrentName("test");
        assertEquals("test", parser.getCurrentName());
    }
    
    @Test
    public void testGetParsingContext() throws Exception
    {
        assertNotNull(parser.getParsingContext());
    }
    
    @Test
    public void testGetTokenLocation() throws Exception
    {
        assertNotNull(parser.getTokenLocation());
    }
    
    @Test
    public void testGetCurrentLocation() throws Exception
    {
        assertNotNull(parser.getCurrentLocation());
    }
    
    @Test
    public void testGetText() throws Exception
    {
        assertNull(parser.getText());
    }
    
    @Test
    public void testGetTextCharacters() throws Exception
    {
        assertNull(parser.getTextCharacters());
    }
    
    @Test
    public void testGetTextLength() throws Exception
    {
        assertEquals(0, parser.getTextLength());
    }
    
    @Test
    public void testGetTextOffset() throws Exception
    {
        assertEquals(0, parser.getTextOffset());
    }
    
    @Test
    public void testHasTextCharacters() throws Exception
    {
        assertFalse(parser.hasTextCharacters());
    }
    
    @Test
    public void testGetNumberType() throws Exception
    {
        assertNull(parser.getNumberType());
    }
    
    @Test
    public void testGetBigIntegerValue() throws Exception
    {
        assertNull(parser.getBigIntegerValue());
    }
    
    @Test
    public void testGetDecimalValue() throws Exception
    {
        assertNull(parser.getDecimalValue());
    }
    
    @Test
    public void testGetDoubleValue() throws Exception
    {
        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }
    
    @Test
    public void testGetFloatValue() throws Exception
    {
        assertEquals(0.0, parser.getFloatValue(), 0.0);
    }
    
    @Test
    public void testGetLongValue() throws Exception
    {
        assertEquals(0, parser.getLongValue());
    }
    
    @Test
    public void testGetIntValue() throws Exception
    {
        assertEquals(0, parser.getIntValue());
    }
    
    @Test
    public void testGetNumberValue() throws Exception
    {
        assertNull(parser.getNumberValue());
    }
    
    @Test
    public void testGetEmbeddedObject() throws Exception
    {
        assertNull(parser.getEmbeddedObject());
    }
    
    @Test
    public void testIsNaN() throws Exception
    {
        assertFalse(parser.isNaN());
    }
    
    @Test
    public void testGetBinaryValue() throws Exception
    {
        assertNull(parser.getBinaryValue(null));
    }
    
    @Test
    public void testReadBinaryValue() throws Exception
    {
        assertEquals(0, parser.readBinaryValue(null, null));
    }
    
    @Test
    public void testHandleEOF() throws Exception
    {
        try {
            parser._handleEOF();
            fail("JsonParseException should have been thrown");
        } catch (JsonParseException e) {
            //expected
        }
    }
}