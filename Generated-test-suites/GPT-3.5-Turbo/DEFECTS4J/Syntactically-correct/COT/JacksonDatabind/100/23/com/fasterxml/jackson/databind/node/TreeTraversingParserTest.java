
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class TreeTraversingParserTest {
	
	private TreeTraversingParser parser;
	private JsonNode node;

	@Before
	public void setUp() {
		node = mock(JsonNode.class);
		parser = new TreeTraversingParser(node);
	}

	@Test
	public void testSetCodec() {
		ObjectCodec codec = mock(ObjectCodec.class);
		parser.setCodec(codec);
		assertSame(codec, parser.getCodec());
	}

	@Test
	public void testGetCodec() {
		ObjectCodec codec = mock(ObjectCodec.class);
		parser.setCodec(codec);
		assertSame(codec, parser.getCodec());
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
	public void testNextToken() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).isArray();
		verify(node).isObject();
		verify(node, atLeastOnce()).isContainerNode();
		verify(node).iterator();
		verify(node).getElements();
		verify(node).fields();
		verify(node).isValueNode();
	}

	@Test
	public void testSkipChildren() throws IOException, JsonParseException {
		parser.nextToken();
		assertEquals(parser.skipChildren(), parser);
	}

	@Test
	public void testIsClosed() throws IOException {
		assertFalse(parser.isClosed());
		parser.close();
		assertTrue(parser.isClosed());
	}

	@Test
	public void testGetCurrentName() {
		parser.getCurrentName();
		verify(node).fieldNames();
	}

	@Test
	public void testOverrideCurrentName() {
		String name = "name";
		parser.overrideCurrentName(name);
		verify(node).fieldNames();
	}

	@Test
	public void testGetParsingContext() {
		assertNotNull(parser.getParsingContext());
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
		parser.nextToken();
		verify(node).isValueNode();
		verify(node).isEmbeddedValue();
		verify(node).isPojo();
		verify(node).isBinary();
		verify(node).textValue();
		verify(node).numberValue();
		verify(node).asText();
		verify(node).asToken();
	}

	@Test
	public void testGetTextCharacters() throws IOException, JsonParseException {
		parser.nextToken();
		assertEquals(parser.getText().toCharArray(), parser.getTextCharacters());
	}

	@Test
	public void testGetTextLength() throws IOException, JsonParseException {
		parser.nextToken();
		assertEquals(parser.getText().length(), parser.getTextLength());
	}

	@Test
	public void testGetTextOffset() throws IOException, JsonParseException {
		assertEquals(0, parser.getTextOffset());
	}

	@Test
	public void testHasTextCharacters() {
		assertFalse(parser.hasTextCharacters());
	}

	@Test
	public void testGetNumberType() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).numberType();
	}

	@Test
	public void testGetBigIntegerValue() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).bigIntegerValue();
	}

	@Test
	public void testGetDecimalValue() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).decimalValue();
	}

	@Test
	public void testGetDoubleValue() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).doubleValue();
	}

	@Test
	public void testGetFloatValue() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).doubleValue();
	}

	@Test
	public void testGetLongValue() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).longValue();
	}

	@Test
	public void testGetIntValue() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).intValue();
	}

	@Test
	public void testGetNumberValue() throws IOException, JsonParseException {
		parser.nextToken();
		verify(node).numberValue();
	}

	@Test
	public void testGetEmbeddedObject() {
		parser.nextToken();
		verify(node, times(2)).isPojo();
		verify(node, times(2)).isBinary();
		verify(node).asText();
		verify(node).binaryValue();
		verify(node).getPojo();
	}

	@Test
	public void testIsNaN() {
		parser.nextToken();
		assertEquals(parser.isNaN(), false);
	}

	@Test
	public void testGetBinaryValue() throws IOException, JsonParseException {
		Base64Variant variant = mock(Base64Variant.class);
		parser.nextToken();
		verify(node).binaryValue();
		verify(node, times(2)).isPojo();
		verify(node).getPojo();
	}

	@Test
	public void testReadBinaryValue() throws IOException, JsonParseException {
		Base64Variant variant = mock(Base64Variant.class);
		parser.nextToken();
		verify(node).binaryValue();
		verify(node, times(2)).isPojo();
		verify(node).getPojo();
	}
}

