import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private JsonNode rootNode;
    private JsonParser parser;
    private TreeTraversingParser ttParser;

    @Before
    public void setUp() throws Exception {
        String json = "{\"name\":\"John\", \"age\":30, \"car\":null, \"children\":[\"Ann\",\"Bob\",\"Carl\"]}";
        rootNode = MAPPER.readTree(json);
        ttParser = new TreeTraversingParser(rootNode);
        parser = MAPPER.treeAsTokens(rootNode);
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
        ttParser.close();
    }

    @Test
    public void testNextToken() throws Exception {
        JsonToken expectedToken = parser.nextToken();
        JsonToken actualToken = ttParser.nextToken();
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testSkipChildren() throws Exception {
        skipToChildren(parser, ttParser);
        assertTrue(ttParser.skipChildren().isClosed());
    }

    @Test
    public void testIsClosed() throws Exception {
        assertFalse(ttParser.isClosed());
        ttParser.close();
        assertTrue(ttParser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws Exception {
        skipToField(parser, ttParser);
        assertEquals(parser.getCurrentName(), ttParser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        skipToField(parser, ttParser);
        parser.overrideCurrentName("newName");
        ttParser.overrideCurrentName("newName");
        assertEquals(parser.getCurrentName(), ttParser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        assertEquals(parser.getParsingContext().inRoot(), ttParser.getParsingContext().inRoot());
        assertEquals(parser.getParsingContext().inArray(), ttParser.getParsingContext().inArray());
        assertEquals(parser.getParsingContext().inObject(), ttParser.getParsingContext().inObject());
        assertEquals(parser.getParsingContext().getCurrentName(), ttParser.getParsingContext().getCurrentName());
    }

    @Test
    public void testGetTokenLocation() throws Exception {
        assertEquals(parser.getTokenLocation(), ttParser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        assertEquals(parser.getCurrentLocation(), ttParser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws Exception {
        skipToField(parser, ttParser);
        assertEquals(parser.getText(), ttParser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        skipToField(parser, ttParser);
        assertArrayEquals(parser.getTextCharacters(), ttParser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws Exception {
        skipToField(parser, ttParser);
        assertEquals(parser.getTextLength(), ttParser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        skipToField(parser, ttParser);
        assertEquals(parser.getTextOffset(), ttParser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        assertFalse(ttParser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getNumberType(), ttParser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getBigIntegerValue(), ttParser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getDecimalValue(), ttParser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getDoubleValue(), ttParser.getDoubleValue(), 0.0001);
    }

    @Test
    public void testGetFloatValue() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getFloatValue(), ttParser.getFloatValue(), 0.0001);
    }

    @Test
    public void testGetLongValue() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getLongValue(), ttParser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getIntValue(), ttParser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.getNumberValue(), ttParser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        skipToEmbeddedObject(parser, ttParser);
        assertEquals(parser.getEmbeddedObject(), ttParser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        skipToNumberField(parser, ttParser);
        assertEquals(parser.isNaN(), ttParser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        skipToEmbeddedObject(parser, ttParser);
        assertArrayEquals(parser.getBinaryValue(Base64Variants.getDefaultVariant()), ttParser.getBinaryValue(Base64Variants.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        skipToEmbeddedObject(parser, ttParser);
        ByteArrayOutputStream parserOut = new ByteArrayOutputStream();
        ByteArrayOutputStream ttParserOut = new ByteArrayOutputStream();
        parser.readBinaryValue(Base64Variants.getDefaultVariant(), parserOut);
        ttParser.readBinaryValue(Base64Variants.getDefaultVariant(), ttParserOut);
        assertEquals(parserOut.toByteArray(), ttParserOut.toByteArray());
    }

    private void skipToChildren(JsonParser parser, TreeTraversingParser ttParser) throws Exception {
        while (parser.nextToken() != JsonToken.START_OBJECT) {}
        while (ttParser.nextToken() != JsonToken.START_OBJECT) {}
    }

    private void skipToField(JsonParser parser, TreeTraversingParser ttParser) throws Exception {
        while (parser.nextToken() != JsonToken.FIELD_NAME) {}
        while (ttParser.nextToken() != JsonToken.FIELD_NAME) {}
    }

    private void skipToNumberField(JsonParser parser, TreeTraversingParser ttParser) throws Exception {
        while (parser.nextToken() != JsonToken.FIELD_NAME || !parser.getCurrentName().equals("age")) {}
        while (ttParser.nextToken() != JsonToken.FIELD_NAME || !ttParser.getCurrentName().equals("age")) {}
    }

    private void skipToEmbeddedObject(JsonParser parser, TreeTraversingParser ttParser) throws Exception {
        while (parser.nextToken() != JsonToken.VALUE_EMBEDDED_OBJECT) {}
        while (ttParser.nextToken() != JsonToken.VALUE_EMBEDDED_OBJECT) {}
    }

}