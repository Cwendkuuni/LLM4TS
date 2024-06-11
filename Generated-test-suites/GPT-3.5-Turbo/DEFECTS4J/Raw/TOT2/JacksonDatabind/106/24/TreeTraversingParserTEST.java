import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Mock
    private JsonNode mockNode;

    @Mock
    private ObjectCodec mockCodec;
    
    @Before
    public void setUp() {
        mockNode = mock(JsonNode.class);
        mockCodec = mock(ObjectCodec.class);
        parser = new TreeTraversingParser(mockNode, mockCodec);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec newCodec = mock(ObjectCodec.class);
        parser.setCodec(newCodec);
        assertEquals(newCodec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Version expectedVersion = PackageVersion.VERSION;
        assertEquals(expectedVersion, parser.version());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken expectedToken = JsonToken.START_OBJECT;
        assertEquals(expectedToken, parser.nextToken());
        verify(mockNode).isObject();
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        JsonToken expectedToken = JsonToken.END_OBJECT;
        assertEquals(parser, parser.skipChildren());
        assertEquals(expectedToken, parser.getCurrentToken());
    }

    @Test
    public void testGetBigIntValue() throws IOException {
        BigInteger expectedValue = BigInteger.TEN;
        when(mockNode.bigIntegerValue()).thenReturn(expectedValue);
        assertEquals(expectedValue, parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        BigDecimal expectedValue = BigDecimal.TEN;
        when(mockNode.decimalValue()).thenReturn(expectedValue);
        assertEquals(expectedValue, parser.getDecimalValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        POJONode mockPojoNode = mock(POJONode.class);
        when(mockNode.isPojo()).thenReturn(true);
        when(mockNode.getPojo()).thenReturn(mockPojoNode);
        assertEquals(mockPojoNode, parser.getEmbeddedObject());
        verify(mockNode, times(1)).isPojo();
        verify(mockNode, times(1)).getPojo();
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        byte[] expectedBytes = "test".getBytes();
        Base64Variant mockB64Variant = Base64Variants.MIME_NO_LINEFEEDS;
        when(mockNode.binaryValue()).thenReturn(expectedBytes);
        assertEquals(expectedBytes, parser.getBinaryValue(mockB64Variant));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        byte[] expectedBytes = "test".getBytes();
        Base64Variant mockB64Variant = Base64Variants.MIME_NO_LINEFEEDS;
        when(mockNode.binaryValue()).thenReturn(expectedBytes);
        assertEquals(expectedBytes.length, parser.readBinaryValue(mockB64Variant, null));
    }

}