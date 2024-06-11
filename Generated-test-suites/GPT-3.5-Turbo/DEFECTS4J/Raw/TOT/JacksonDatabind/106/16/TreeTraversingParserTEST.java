import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        JsonNode node = JsonNodeFactory.instance.objectNode();
        parser = new TreeTraversingParser(node);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException, JsonParseException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException, JsonParseException {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws IOException, JsonParseException {
        parser.overrideCurrentName("test");
        assertEquals("test", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws IOException, JsonParseException {
        assertNotNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() throws IOException, JsonParseException {
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws IOException, JsonParseException {
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws IOException, JsonParseException {
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        assertEquals(0.0, parser.getDoubleValue(), 0.01);
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        assertEquals(0.0f, parser.getFloatValue(), 0.01);
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException, JsonParseException {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException, JsonParseException {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), new ByteArrayOutputStream()));
    }
}