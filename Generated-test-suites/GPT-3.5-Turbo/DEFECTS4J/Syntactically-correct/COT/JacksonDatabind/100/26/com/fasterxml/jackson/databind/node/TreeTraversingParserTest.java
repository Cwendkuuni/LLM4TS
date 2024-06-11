
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Test
    public void testNextToken() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        JsonToken token = parser.nextToken();

        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testSkipChildren() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        parser.nextToken();
        parser.nextToken();
        
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNull(parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNotNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNotNull(parser.getTokenLocation());
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNotNull(parser.getCurrentLocation());
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        JsonToken token = parser.nextToken();

        assertEquals("", parser.getText());
    }

    @Test
    public void testGetNumberType() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertEquals(0.0f, parser.getFloatValue(), 0.0f);
    }

    @Test
    public void testGetLongValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node, null);

        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), new ByteArrayOutputStream()));
    }

    private static class JsonNode extends com.fasterxml.jackson.databind.node.JsonNode {
    }
}
