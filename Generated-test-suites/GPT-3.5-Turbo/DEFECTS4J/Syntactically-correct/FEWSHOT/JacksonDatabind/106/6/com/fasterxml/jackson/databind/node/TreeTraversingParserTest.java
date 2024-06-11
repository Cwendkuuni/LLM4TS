import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {
    
    private TreeTraversingParser parser;
    private JsonNode jsonNode;
    private ObjectMapper objectMapper;
    
    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.createObjectNode();
        parser = new TreeTraversingParser(jsonNode);
    }
    
    @Test
    public void testSetCodec() {
        assertNull(parser.getCodec());
        parser.setCodec(objectMapper);
        assertEquals(objectMapper, parser.getCodec());
    }
    
    @Test
    public void testVersion() {
        assertTrue(parser.version() instanceof Version);
    }
    
    @Test
    public void testClose() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        
        parser.close();
        assertNull(parser.nextToken());
    }
    
    @Test
    public void testSkipChildren() throws IOException {
        assertEquals(parser, parser.skipChildren());
    }
    
    @Test
    public void testIsClosed() {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testGetCurrentName() throws IOException {
        assertNull(parser.getCurrentName());
        parser.overrideCurrentName("test");
        assertEquals("test", parser.getCurrentName());
    }
    
    @Test
    public void testOverrideCurrentName() throws IOException {
        assertNull(parser.getCurrentName());
        parser.overrideCurrentName("test");
        assertEquals("test", parser.getCurrentName());
    }
    
    @Test
    public void testGetParsingContext() {
        assertTrue(parser.getParsingContext() instanceof JsonStreamContext);
    }
    
    @Test
    public void testGetTokenLocation() {
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
    }
    
    @Test
    public void testGetCurrentLocation() {
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }
    
    @Test
    public void testGetText() throws IOException {
        assertEquals("", parser.getText());
        
        parser.nextToken();
        
        assertNull(parser.getText());
    }
    
    @Test
    public void testGetTextCharacters() throws IOException {
        char[] textCharacters = parser.getTextCharacters();
        assertNull(textCharacters);
        
        parser.nextToken();
        textCharacters = parser.getTextCharacters();
        assertNull(textCharacters);
    }
    
    @Test
    public void testGetTextLength() throws IOException {
        assertEquals(0, parser.getTextLength());
        
        parser.nextToken();
        
        assertEquals(0, parser.getTextLength());
    }
    
    @Test
    public void testGetTextOffset() throws IOException {
        assertEquals(0, parser.getTextOffset());
        
        parser.nextToken();
        
        assertEquals(0, parser.getTextOffset());
    }
    
    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }
    
    @Test
    public void testGetNumberType() throws IOException {
        assertNull(parser.getNumberType());
        
        parser.nextToken();
        
        assertNull(parser.getNumberType());
    }
    
    @Test
    public void testGetBigIntegerValue() throws IOException {
        assertNull(parser.getBigIntegerValue());
        
        parser.nextToken();
        
        assertNull(parser.getBigIntegerValue());
    }
    
    @Test
    public void testGetDecimalValue() throws IOException {
        assertNull(parser.getDecimalValue());
        
        parser.nextToken();
        
        assertNull(parser.getDecimalValue());
    }
    
    @Test
    public void testGetDoubleValue() throws IOException {
        assertEquals(0.0, parser.getDoubleValue(), 0.0001);
        
        parser.nextToken();
        
        assertEquals(0.0, parser.getDoubleValue(), 0.0001);
    }
    
    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals(0.0f, parser.getFloatValue(), 0.0001);
        
        parser.nextToken();
        
        assertEquals(0.0f, parser.getFloatValue(), 0.0001);
    }
    
    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(0, parser.getIntValue());
        
        parser.nextToken();
        
        assertEquals(0, parser.getIntValue());
    }
    
    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(0L, parser.getLongValue());
        
        parser.nextToken();
        
        assertEquals(0L, parser.getLongValue());
    }
    
    @Test
    public void testGetNumberValue() throws IOException {
        assertNull(parser.getNumberValue());
        
        parser.nextToken();
        
        assertNull(parser.getNumberValue());
    }
    
    @Test
    public void testGetEmbeddedObject() throws IOException {
        assertNull(parser.getEmbeddedObject());
        
        parser.nextToken();
        
        assertNull(parser.getEmbeddedObject());
    }
    
    @Test
    public void testIsNaN() {
        assertFalse(parser.isNaN());
    }
    
    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
        
        parser.nextToken();
        
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }
    
    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), new ByteArrayOutputStream()));
        
        parser.nextToken();
        
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), new ByteArrayOutputStream()));
    }
    
    @Test
    public void testHandleEOF() throws JsonParseException {
        try {
            parser._handleEOF();
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
        	// test passed
        }
    }
}