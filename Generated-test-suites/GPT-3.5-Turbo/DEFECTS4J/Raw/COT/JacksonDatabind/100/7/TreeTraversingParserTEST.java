import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private JsonNode jsonNode;
    private TreeTraversingParser treeTraversingParser;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        jsonNode = objectMapper.createObjectNode();
        treeTraversingParser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testSetCodec() {
        treeTraversingParser.setCodec(null);
        assertNull(treeTraversingParser.getCodec());
    }

    @Test
    public void testVersion() {
        assertNotNull(treeTraversingParser.version());
    }

    @Test
    public void testClose() {
        treeTraversingParser.close();
        assertTrue(treeTraversingParser.isClosed());
    }

    @Test
    public void testNextToken() {
        assertNull(treeTraversingParser.nextToken());
    }

    @Test
    public void testSkipChildren() {
        assertEquals(treeTraversingParser, treeTraversingParser.skipChildren());
    }

    @Test
    public void testIsClosed() {
        assertFalse(treeTraversingParser.isClosed());
        treeTraversingParser.close();
        assertTrue(treeTraversingParser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(treeTraversingParser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() {
        treeTraversingParser.overrideCurrentName("name");
        assertEquals("name", treeTraversingParser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        assertNotNull(treeTraversingParser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        assertNotNull(treeTraversingParser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNotNull(treeTraversingParser.getCurrentLocation());
    }

    @Test
    public void testGetText() {
        assertNull(treeTraversingParser.getText());
    }

    @Test
    public void testGetTextCharacters() {
        assertNull(treeTraversingParser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() {
        assertEquals(0, treeTraversingParser.getTextLength());
    }

    @Test
    public void testGetTextOffset() {
        assertEquals(0, treeTraversingParser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(treeTraversingParser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() {
        assertNull(treeTraversingParser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() {
        assertNull(treeTraversingParser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() {
        assertNull(treeTraversingParser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() {
        assertEquals(0.0, treeTraversingParser.getDoubleValue(), 0);
    }

    @Test
    public void testGetFloatValue() {
        assertEquals(0.0, treeTraversingParser.getFloatValue(), 0);
    }

    @Test
    public void testGetLongValue() {
        assertEquals(0L, treeTraversingParser.getLongValue());
    }

    @Test
    public void testGetIntValue() {
        assertEquals(0, treeTraversingParser.getIntValue());
    }

    @Test
    public void testGetNumberValue() {
        assertNull(treeTraversingParser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        assertNull(treeTraversingParser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        assertFalse(treeTraversingParser.isNaN());
    }

    @Test
    public void testGetBinaryValue() {
        assertNull(treeTraversingParser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() {
        assertEquals(0, treeTraversingParser.readBinaryValue(null, null));
    }
}