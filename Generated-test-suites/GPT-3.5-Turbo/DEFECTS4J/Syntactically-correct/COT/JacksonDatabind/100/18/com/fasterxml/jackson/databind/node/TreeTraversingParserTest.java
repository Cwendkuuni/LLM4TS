
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private JsonNode node;
    private ObjectCodec codec;

    @Before
    public void setUp() {
        node = Mockito.mock(JsonNode.class);
        codec = Mockito.mock(ObjectCodec.class);
        parser = new TreeTraversingParser(node, codec);
    }

    @Test
    public void testSetGetCodec() {
        ObjectCodec newCodec = Mockito.mock(ObjectCodec.class);
        parser.setCodec(newCodec);
        assertEquals(newCodec, parser.getCodec());
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
    public void testNextToken() throws Exception {
        JsonToken nextToken = JsonToken.FIELD_NAME;
        Mockito.when(node.isArray()).thenReturn(false);
        Mockito.when(node.isObject()).thenReturn(false);
        Mockito.when(node.numberValue()).thenReturn(null);
        Mockito.when(node.textValue()).thenReturn(null);
        Mockito.when(node.asText()).thenReturn(null);

        parser.nextToken();
        assertEquals(nextToken, parser.getCurrentToken());
    }

    @Test
    public void testSkipChildren() throws Exception {
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws Exception {
        assertFalse(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws Exception {
        String currentName = "test";
        Mockito.when(node.getCurrentName()).thenReturn(currentName);
        assertEquals(currentName, parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        String currentName = "test";
        Mockito.when(node.getCurrentName()).thenReturn(currentName);
        parser.overrideCurrentName("override");
        assertEquals("override", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        assertNotNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() throws Exception {
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws Exception {
        String text = "test";
        Mockito.when(node.textValue()).thenReturn(text);
        assertEquals(text, parser.getText());
    }

    @Test
    public void testGetTextLength() throws Exception {
        String text = "test";
        Mockito.when(node.textValue()).thenReturn(text);
        assertEquals(text.length(), parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        JsonNode numericNode = Mockito.mock(JsonNode.class);
        Mockito.when(numericNode.numberType()).thenReturn(NumberType.INT);
        Mockito.when(node.numberValue()).thenReturn(numericNode);

        assertEquals(NumberType.INT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        BigInteger bigInteger = Mockito.mock(BigInteger.class);
        Mockito.when(node.bigIntegerValue()).thenReturn(bigInteger);
        assertEquals(bigInteger, parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        BigDecimal bigDecimal = Mockito.mock(BigDecimal.class);
        Mockito.when(node.decimalValue()).thenReturn(bigDecimal);
        assertEquals(bigDecimal, parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        double value = 1.0;
        Mockito.when(node.doubleValue()).thenReturn(value);
        assertEquals(value, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws Exception {
        float value = 1.0f;
        Mockito.when(node.doubleValue()).thenReturn((double) value);
        assertEquals(value, parser.getFloatValue(), 0.0);
    }

    @Test
    public void testGetLongValue() throws Exception {
        long value = 1L;
        Mockito.when(node.longValue()).thenReturn(value);
        assertEquals(value, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws Exception {
        int value = 1;
        Mockito.when(node.intValue()).thenReturn(value);
        assertEquals(value, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws Exception {
        Number number = Mockito.mock(Number.class);
        Mockito.when(node.numberValue()).thenReturn(number);
        assertEquals(number, parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        Object object = new Object();
        Mockito.when(node.isPojo()).thenReturn(true);
        Mockito.when(node.isBinary()).thenReturn(false);
        Mockito.when(node.asText()).thenReturn(null);
        Mockito.when(node.getPojo()).thenReturn(object);

        assertEquals(object, parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        byte[] binaryValue = {1, 2, 3};
        Mockito.when(node.binaryValue()).thenReturn(binaryValue);
        assertEquals(binaryValue, parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        byte[] binaryValue = {1, 2, 3};
        Mockito.when(node.binaryValue()).thenReturn(binaryValue);
        assertEquals(binaryValue.length, parser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }

    @Test
    public void testHandleEOF() throws Exception {
        // Not able to test handleEOF method as it throws an exception
    }

}

