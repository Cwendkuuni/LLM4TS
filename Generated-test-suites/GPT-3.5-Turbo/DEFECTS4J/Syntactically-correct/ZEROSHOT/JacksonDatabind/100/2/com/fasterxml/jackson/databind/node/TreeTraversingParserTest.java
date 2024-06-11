package com.fasterxml.jackson.databind.node;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonStreamContext;

public class TreeTraversingParserTest {
    
    private TreeTraversingParser parser;
    
    @Before
    public void setUp() {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node);
    }
    
    @Test
    public void testSetCodec() {
        Assert.assertNull(parser.getCodec());
        
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        
        Assert.assertEquals(codec, parser.getCodec());
    }
    
    @Test
    public void testVersion() {
        Version version = new Version(1, 2, 3, "test", "group", "artifact");
        Assert.assertEquals(version, parser.version());
    }
    
    @Test
    public void testClose() {
        Assert.assertFalse(parser.isClosed());
        
        parser.close();
        
        Assert.assertTrue(parser.isClosed());
    }
    
    @Test
    public void testNextToken() {
        JsonToken token = parser.nextToken();
        
        Assert.assertNull(token);
    }
    
    @Test
    public void testSkipChildren() {
        JsonParser result = parser.skipChildren();
        
        Assert.assertEquals(parser, result);
    }
    
    @Test
    public void testIsClosed() {
        Assert.assertFalse(parser.isClosed());
    }
    
    @Test
    public void testGetCurrentName() {
        String currentName = parser.getCurrentName();
        
        Assert.assertNull(currentName);
    }
    
    @Test
    public void testOverrideCurrentName() {
        String name = "test";
        
        parser.overrideCurrentName(name);
        String currentName = parser.getCurrentName();
        
        Assert.assertEquals(name, currentName);
    }
    
    @Test
    public void testGetParsingContext() {
        JsonStreamContext context = parser.getParsingContext();
        
        Assert.assertNotNull(context);
    }
    
    @Test
    public void testGetTokenLocation() {
        JsonLocation location = parser.getTokenLocation();
        
        Assert.assertEquals(JsonLocation.NA, location);
    }
    
    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = parser.getCurrentLocation();
        
        Assert.assertEquals(JsonLocation.NA, location);
    }
    
    @Test
    public void testGetText() {
        String text = parser.getText();
        
        Assert.assertNull(text);
    }
    
    @Test
    public void testGetTextCharacters() {
        char[] chars = parser.getTextCharacters();
        
        Assert.assertNull(chars);
    }
    
    @Test
    public void testGetTextLength() {
        int length = parser.getTextLength();
        
        Assert.assertEquals(0, length);
    }
    
    @Test
    public void testGetTextOffset() {
        int offset = parser.getTextOffset();
        
        Assert.assertEquals(0, offset);
    }
    
    @Test
    public void testHasTextCharacters() {
        Assert.assertFalse(parser.hasTextCharacters());
    }
    
    @Test
    public void testGetNumberType() {
        NumberType type = parser.getNumberType();
        
        Assert.assertNull(type);
    }
    
    @Test
    public void testGetBigIntegerValue() {
        BigInteger value = parser.getBigIntegerValue();
        
        Assert.assertNull(value);
    }
    
    @Test
    public void testGetDecimalValue() {
        BigDecimal value = parser.getDecimalValue();
        
        Assert.assertNull(value);
    }
    
    @Test
    public void testGetDoubleValue() {
        double value = parser.getDoubleValue();
        
        Assert.assertEquals(0.0, value, 0.0001);
    }
    
    @Test
    public void testGetFloatValue() {
        float value = parser.getFloatValue();
        
        Assert.assertEquals(0.0, value, 0.0001);
    }
    
    @Test
    public void testGetLongValue() {
        long value = parser.getLongValue();
        
        Assert.assertEquals(0, value);
    }
    
    @Test
    public void testGetIntValue() {
        int value = parser.getIntValue();
        
        Assert.assertEquals(0, value);
    }
    
    @Test
    public void testGetNumberValue() {
        Number value = parser.getNumberValue();
        
        Assert.assertNull(value);
    }
    
    @Test
    public void testGetEmbeddedObject() {
        Object object = parser.getEmbeddedObject();
        
        Assert.assertNull(object);
    }
    
    @Test
    public void testIsNaN() {
        boolean nan = parser.isNaN();
        
        Assert.assertFalse(nan);
    }
    
    @Test
    public void testGetBinaryValue() {
        byte[] value = parser.getBinaryValue(Base64Variant.getDefaultVariant());
        
        Assert.assertNull(value);
    }
    
    @Test
    public void testReadBinaryValue() {
        OutputStream outputStream = new OutputStream();
        int length = parser.readBinaryValue(Base64Variant.getDefaultVariant(), outputStream);
        
        Assert.assertEquals(0, length);
    }
    
    @Test
    public void testHandleEOF() {
        try {
            parser._handleEOF();
        } catch (JsonParseException e) {
            Assert.fail("Method should not throw an exception");
        }
    }
}