import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private String json = "{ \"key\":\"value\" }";
    private ObjectMapper objectMapper;
    private TreeTraversingParser parser;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        parser = new TreeTraversingParser(jsonNode, objectMapper);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = mock(ObjectCodec.class);
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("value", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.skipChildren().getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        parser.overrideCurrentName("newKey");
        assertEquals("newKey", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(0, parser.getParsingContext().getParent().getCurrentIndex());
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
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("value", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());

        char[] expected = new char[]{'v', 'a', 'l', 'u', 'e'};
        char[] actual = parser.getTextCharacters();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());

        String text = parser.getText();
        int length = parser.getTextLength();

        assertEquals(text.length(), length);

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());

        int offset = parser.getTextOffset();

        assertEquals(0, offset);

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());        
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }

}