import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        JsonNode node = null;
        try {
            node = new ObjectMapper().readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parser = new TreeTraversingParser(node);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(null, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        JsonParser skippedParser = parser.skipChildren();
        assertNotNull(skippedParser);
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonParser.NumberType.INT, parser.getNumberType());
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonParser.NumberType.STRING, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertEquals(BigInteger.valueOf(30), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertEquals(BigDecimal.valueOf(30), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertEquals(30.0, parser.getDoubleValue(), 0.001);
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertEquals(30.0f, parser.getFloatValue(), 0.001);
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertEquals(30, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertEquals(30L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertEquals(30, parser.getNumberValue().intValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        parser.nextToken();
        parser.nextToken();
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException, JsonParseException {
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        parser.nextToken();
        assertNull(parser.getBinaryValue(Base64Variant.MIME_NO_LINEFEEDS));
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        parser.nextToken();
        assertEquals(0, parser.readBinaryValue(Base64Variant.MIME_NO_LINEFEEDS, new ByteArrayOutputStream()));
    }

    @Test
    public void testGetCurrentName() throws IOException, JsonParseException {
        parser.nextToken();
        assertEquals("name", parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws IOException, JsonParseException {
        parser.nextToken();
        parser.overrideCurrentName("age");
        assertEquals("age", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws IOException, JsonParseException {
        parser.nextToken();
        JsonStreamContext context = parser.getParsingContext();
        assertNotNull(context);
        assertEquals(0, context.getStartLocation().getLineNr());
        assertEquals(-1, context.getStartLocation().getColumnNr());
        assertEquals(0, context.getStartLocation().getCharOffset());
        assertEquals(0, context.getRelativeDepth());
        assertEquals(null, context.getCurrentName());
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
    public void testGetText() throws IOException, JsonParseException {
        parser.nextToken();
        assertEquals(null, parser.getText());
        parser.nextToken();
        assertEquals("John", parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        parser.nextToken();
        assertEquals(null, parser.getTextCharacters());
        parser.nextToken();
        assertArrayEquals("John".toCharArray(), parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        parser.nextToken();
        assertEquals(0, parser.getTextLength());
        parser.nextToken();
        assertEquals(4, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testVersion() {
        assertNotNull(parser.version());
    }
}