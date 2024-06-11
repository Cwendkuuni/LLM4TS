import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.createObjectNode();
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
        Version version = new Version(1, 0, 0, null, null, null);
        assertEquals(version, parser.version());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException {
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException {
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
        parser.overrideCurrentName("name");
        assertEquals("name", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        assertTrue(parser.getParsingContext() instanceof NodeCursor);
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
        assertEquals(0.0, parser.getDoubleValue(), 0.000001);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals(0.0, parser.getFloatValue(), 0.000001);
    }

    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(0, parser.getLongValue());
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
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), out));
    }

}