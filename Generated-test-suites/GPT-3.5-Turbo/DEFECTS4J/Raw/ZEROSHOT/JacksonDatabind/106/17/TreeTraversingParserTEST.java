import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private ObjectMapper mapper;
    private JsonNode jsonNode;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        jsonNode = mapper.createObjectNode();
        parser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = new Version();
        assertEquals(version, parser.version());
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        parser.close();
        assertTrue(parser.isClosed());
        parser.nextToken();
    }

    @Test
    public void testNextToken() throws IOException {
        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testSkipChildren() throws IOException {
        assertEquals(parser, parser.skipChildren());
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testNextValue() throws IOException {
        JsonToken token = parser.nextValue();
        assertNull(token);
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() {
        String name = "name";
        parser.overrideCurrentName(name);
        assertEquals(name, parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        assertNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        JsonLocation location = new JsonLocation();
        assertEquals(location, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = new JsonLocation();
        assertEquals(location, parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws IOException {
        assertNull(parser.getText());
        parser.nextValue();
        assertNull(parser.getText());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals(0.0f, parser.getFloatValue(), 0.0f);
    }

    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
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
    public void testGetBinaryValue() throws IOException {
        Base64Variant b64variant = new Base64Variant();
        assertNull(parser.getBinaryValue(b64variant));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        Base64Variant b64variant = new Base64Variant();
        assertEquals(0, parser.readBinaryValue(b64variant, System.out));
    }

    @Test
    public void testHandleEOF() throws IOException {
        parser._handleEOF();
    }
}