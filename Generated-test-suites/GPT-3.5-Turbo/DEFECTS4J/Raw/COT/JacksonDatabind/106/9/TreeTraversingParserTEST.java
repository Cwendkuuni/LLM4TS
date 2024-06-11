import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.node.JsonNode;

public class TreeTraversingParserTest {
    
    @Test
    public void testSetCodec() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getCodec());
        parser.setCodec(new ObjectCodec());
        assertNotNull(parser.getCodec());
    }
    
    @Test
    public void testVersion() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNotNull(parser.version());
    }
    
    @Test
    public void testClose() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testNextToken() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
    }
    
    @Test
    public void testSkipChildren() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertSame(parser, parser.skipChildren());
    }
    
    @Test
    public void testIsClosed() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testGetCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getCurrentName());
    }
    
    @Test
    public void testOverrideCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.overrideCurrentName("name");
        assertEquals("name", parser.getCurrentName());
    }
    
    @Test
    public void testGetParsingContext() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNotNull(parser.getParsingContext());
    }
    
    @Test
    public void testGetTokenLocation() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNotNull(parser.getTokenLocation());
    }
    
    @Test
    public void testGetCurrentLocation() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNotNull(parser.getCurrentLocation());
    }
    
    @Test
    public void testGetText() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getText());
    }
    
    @Test
    public void testGetTextCharacters() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getTextCharacters());
    }
    
    @Test
    public void testGetTextLength() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getTextLength());
    }
    
    @Test
    public void testGetTextOffset() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getTextOffset());
    }
    
    @Test
    public void testHasTextCharacters() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.hasTextCharacters());
    }
    
    @Test
    public void testGetNumberType() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getNumberType());
    }
    
    @Test
    public void testGetBigIntegerValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getBigIntegerValue());
    }
    
    @Test
    public void testGetDecimalValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getDecimalValue());
    }
    
    @Test
    public void testGetDoubleValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0.0, parser.getDoubleValue(), 0.01);
    }
    
    @Test
    public void testGetFloatValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0.0f, parser.getFloatValue(), 0.01);
    }
    
    @Test
    public void testGetIntValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getIntValue());
    }
    
    @Test
    public void testGetLongValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0L, parser.getLongValue());
    }
    
    @Test
    public void testGetNumberValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getNumberValue());
    }
    
    @Test
    public void testGetEmbeddedObject() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getEmbeddedObject());
    }
    
    @Test
    public void testIsNaN() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isNaN());
    }
    
    @Test
    public void testGetBinaryValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getBinaryValue());
    }
    
    @Test
    public void testReadBinaryValue() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.readBinaryValue());
    }
}