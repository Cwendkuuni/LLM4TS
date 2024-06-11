
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.JsonStreamContext;
import com.fasterxml.jackson.databind.node.NodeCursor;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;


@RunWith(MockitoJUnitRunner.class)
public class TreeTraversingParserTest {

    private TreeTraversingParser treeTraversingParser;
    private JsonNode n;

    @Mock
    private ObjectCodec objectCodec;

    @Before
    public void setup() {
        n = new BinaryNode(new byte[]{1, 2, 3});
        treeTraversingParser = new TreeTraversingParser(n, objectCodec);
    }

    @Test
    public void testConstructor() {
        assertEquals(JsonToken.START_ARRAY, treeTraversingParser.nextToken());
        NodeCursor cursor = treeTraversingParser.getParsingContext();
        assertTrue(cursor instanceof NodeCursor.ArrayCursor);
        assertEquals(n, treeTraversingParser.currentNode());
    }

    @Test
    public void testSetCodec() {
        treeTraversingParser.setCodec(objectCodec);
        assertEquals(objectCodec, treeTraversingParser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = treeTraversingParser.version();
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, version);
    }

    @Test
    public void testClose() throws Exception {
        assertFalse(treeTraversingParser.isClosed());
        treeTraversingParser.close();
        assertTrue(treeTraversingParser.isClosed());
    }

    @Test
    public void testNextToken() throws Exception {
        assertEquals(JsonToken.START_ARRAY, treeTraversingParser.nextToken());
        assertEquals(JsonToken.END_ARRAY, treeTraversingParser.nextToken());
        assertNull(treeTraversingParser.nextToken());
        assertTrue(treeTraversingParser.isClosed());
    }

    @Test
    public void testSkipChildren() throws Exception {
        treeTraversingParser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, treeTraversingParser.getCurrentToken());
    }

    @Test
    public void testIsClosed() {
        assertFalse(treeTraversingParser.isClosed());
        treeTraversingParser.close();
        assertTrue(treeTraversingParser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(treeTraversingParser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        treeTraversingParser.overrideCurrentName("name");
        assertEquals("name", treeTraversingParser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        NodeCursor cursor = treeTraversingParser.getParsingContext();
        assertTrue(cursor instanceof NodeCursor.ArrayCursor);
    }

    @Test
    public void testGetTokenLocation() {
        assertEquals(JsonLocation.NA, treeTraversingParser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        assertEquals(JsonLocation.NA, treeTraversingParser.getCurrentLocation());
    }

    @Test
    public void testGetText() {
        assertEquals("", treeTraversingParser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        char[] data = treeTraversingParser.getTextCharacters();
        assertEquals("", String.valueOf(data));
    }

    @Test
    public void testGetTextLength() throws Exception {
        assertEquals(0, treeTraversingParser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        assertEquals(0, treeTraversingParser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(treeTraversingParser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        assertEquals(null, treeTraversingParser.getNumberType());
    }

    @Test(expected = JsonParseException.class)
    public void testGetBigIntegerValueWhenNotNumeric() throws Exception {
        treeTraversingParser.getBigIntegerValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetDecimalValueWhenNotNumeric() throws Exception {
        treeTraversingParser.getDecimalValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetDoubleValueWhenNotNumeric() throws Exception {
        treeTraversingParser.getDoubleValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetFloatValueWhenNotNumeric() throws Exception {
        treeTraversingParser.getFloatValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetIntValueWhenNotNumeric() throws Exception {
        treeTraversingParser.getIntValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetLongValueWhenNotNumeric() throws Exception {
        treeTraversingParser.getLongValue();
    }

    @Test
    public void testGetNumberValue() throws Exception {
        assertNull(treeTraversingParser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        assertNull(treeTraversingParser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        assertFalse(treeTraversingParser.isNaN());
    }

    @Test
    public void testGetBinaryValueWhenNull() throws Exception {
        assertNull(treeTraversingParser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testreadBinaryValue() throws Exception {
        assertEquals(0, treeTraversingParser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }

    @Test
    public void testHandleEOF() throws Exception {
        treeTraversingParser._handleEOF();
        assertTrue(treeTraversingParser.isClosed());
    }
}
