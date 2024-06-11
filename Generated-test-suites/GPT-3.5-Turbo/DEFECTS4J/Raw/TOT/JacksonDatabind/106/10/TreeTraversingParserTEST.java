import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private JsonNode jsonNode;

    @Mock
    private ObjectCodec objectCodec;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Mock the JsonNode object
        jsonNode = new ObjectMapper().readTree(" {\"name\":\"John\", \"age\":30, \"car\":null} ");

        // Create a TreeTraversingParser object
        parser = new TreeTraversingParser(jsonNode, objectCodec);
    }

    @Test
    public void testSetCodec() throws IOException {
        assertNull(parser.getCodec());

        parser.setCodec(objectCodec);

        assertEquals(objectCodec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        assertNotNull(parser.version());
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
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        parser.skipChildren();

        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(parser.isClosed());

        parser.close();

        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        parser.nextToken();
        assertEquals("name", parser.getCurrentName());

        parser.nextToken();
        assertEquals("age", parser.getCurrentName());

        parser.nextToken();
        assertEquals("car", parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws IOException {
        parser.nextToken();
        parser.overrideCurrentName("newName");

        assertEquals("newName", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws IOException {
        assertNotNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        assertNotNull(parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNotNull(parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws IOException {
        parser.nextToken();
        assertEquals("John", parser.getText());

        parser.nextToken();
        assertEquals("30", parser.getText());

        parser.nextToken();
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        parser.nextToken();
        char[] result = parser.getTextCharacters();

        assertNotNull(result);
        assertEquals("John".toCharArray(), result);
    }

    @Test
    public void testGetTextLength() throws IOException {
        parser.nextToken();
        assertEquals(4, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException {
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws IOException {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        parser.nextToken();
        assertNull(parser.getNumberType());

        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        parser.nextToken();
        assertNull(parser.getBigIntegerValue());

        parser.nextToken();
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        parser.nextToken();
        assertNull(parser.getDecimalValue());

        parser.nextToken();
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        parser.nextToken();
        assertNull(parser.getDoubleValue());

        parser.nextToken();
        assertEquals(30.0, parser.getDoubleValue(), 0.001);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        parser.nextToken();
        assertEquals(0.0f, parser.getFloatValue(), 0.001);

        parser.nextToken();
        assertEquals(30.0f, parser.getFloatValue(), 0.001);
    }

    @Test
    public void testGetIntValue() throws IOException {
        parser.nextToken();
        assertEquals(0, parser.getIntValue());

        parser.nextToken();
        assertEquals(30, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        parser.nextToken();
        assertEquals(0L, parser.getLongValue());

        parser.nextToken();
        assertEquals(30L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        parser.nextToken();
        assertNull(parser.getNumberValue());

        parser.nextToken();
        assertEquals(new BigInteger("30"), parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        parser.nextToken();
        assertNull(parser.getEmbeddedObject());

        parser.nextToken();
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException {
        parser.nextToken();
        assertFalse(parser.isNaN());

        parser.nextToken();
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        parser.nextToken();
        assertNull(parser.getBinaryValue(null));

        parser.nextToken();
        assertNull(parser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        parser.nextToken();

        assertEquals(0, parser.readBinaryValue(null, null));
    }

    @Test
    public void testHandleEOF() throws IOException {
        parser._handleEOF();
    }
}