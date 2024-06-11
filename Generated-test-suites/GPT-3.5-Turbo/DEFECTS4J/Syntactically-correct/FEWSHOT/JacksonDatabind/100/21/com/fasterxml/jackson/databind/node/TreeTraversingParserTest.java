import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        JsonNode node = new ObjectNode(null, null);
        parser = new TreeTraversingParser(node);
    }

    @Test
    public void testGetCodec() {
        assertNull(parser.getCodec());
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() {
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() {
        assertNull(parser.skipChildren());
    }

    @Test
    public void testIsClosed() {
        assertFalse(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() {
        parser.overrideCurrentName("test");
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        assertNull(parser.getParsingContext());
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
    public void testGetText() {
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() {
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() {
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() {
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() {
        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() {
        assertEquals(0.0f, parser.getFloatValue(), 0.0f);
    }

    @Test
    public void testGetLongValue() {
        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() {
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() {
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() {
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }

    @Test
    public void testHandleEOF() {
        try {
            parser._handleEOF();
            fail("Expected JsonParseException was not thrown");
        } catch (JsonParseException e) {
            assertEquals("Cannot skipChildren: no current token", e.getMessage());
        }
    }
}