package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class TreeTraversingParserTest {
	
	private TreeTraversingParser parser;
	private JsonNode root;
	
	@Before
	public void setUp() {
		ObjectMapper mapper = new ObjectMapper();
		root = mapper.createObjectNode();
		parser = new TreeTraversingParser(root);
	}
	
	@After
	public void tearDown() {
		parser = null;
		root = null;
	}
	
	@Test
	public void testConstructor() throws Exception {
		ObjectCodec codec = mock(ObjectCodec.class);
		new TreeTraversingParser(root, codec);
	}
	
	@Test
	public void testSetCodec() throws Exception {
		ObjectCodec codec = mock(ObjectCodec.class);
		parser.setCodec(codec);
	}
	
	@Test
	public void testGetCodec() throws Exception {
		ObjectCodec codec = mock(ObjectCodec.class);
		parser.setCodec(codec);
		assertEquals(codec, parser.getCodec());
	}
	
	@Test
	public void testVersion() throws Exception {
		assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
	}
	
	@Test
	public void testClose() throws Exception {
		parser.close();
	}
	
	@Test
	public void testNextToken() throws Exception {
		parser.nextToken();
		parser.close();
		assertNull(parser.nextToken());
	}
	
	@Test
	public void testSkipChildren() throws Exception {
		parser.nextToken();
		parser.skipChildren();
	}
	
	@Test
	public void testIsClosed() throws Exception {
		parser.isClosed();
	}
	
	@Test
	public void testGetCurrentName() throws Exception {
		parser.getCurrentName();
	}
	
	@Test
	public void testOverrideCurrentName() throws Exception {
		parser.overrideCurrentName("name");
	}
	
	@Test
	public void testGetParsingContext() throws Exception {
		parser.getParsingContext();
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
		parser.nextToken();
		parser.getText();
	}
	
	@Test
	public void testGetTextCharacters() throws Exception {
		parser.nextToken();
		parser.getTextCharacters();
	}
	
	@Test
	public void testGetTextLength() throws Exception {
		parser.nextToken();
		parser.getTextLength();
	}
	
	@Test
	public void testGetTextOffset() throws Exception {
		parser.nextToken();
		parser.getTextOffset();
	}
	
	@Test
	public void testHasTextCharacters() throws Exception {
		parser.nextToken();
		parser.hasTextCharacters();
	}
	
	@Test
	public void testGetNumberType() throws Exception {
		parser.nextToken();
		parser.getNumberType();
	}
	
	@Test
	public void testGetBigIntegerValue() throws Exception {
		parser.nextToken();
		parser.getBigIntegerValue();
	}
	
	@Test
	public void testGetDecimalValue() throws Exception {
		parser.nextToken();
		parser.getDecimalValue();
	}
	
	@Test
	public void testGetDoubleValue() throws Exception {
		parser.nextToken();
		parser.getDoubleValue();
	}
	
	@Test
	public void testGetFloatValue() throws Exception {
		parser.nextToken();
		parser.getFloatValue();
	}
	
	@Test
	public void testGetIntValue() throws Exception {
		parser.nextToken();
		parser.getIntValue();
	}
	
	@Test
	public void testGetLongValue() throws Exception {
		parser.nextToken();
		parser.getLongValue();
	}
	
	@Test
	public void testGetNumberValue() throws Exception {
		parser.nextToken();
		parser.getNumberValue();
	}
	
	@Test
	public void testGetEmbeddedObject() throws Exception {
		parser.nextToken();
		parser.getEmbeddedObject();
	}
	
	@Test
	public void testIsNaN() throws Exception {
		parser.nextToken();
		parser.isNaN();
	}
	
	@Test
	public void testGetBinaryValue() throws Exception {
		parser.nextToken();
		Base64Variant variant = mock(Base64Variant.class);
		parser.getBinaryValue(variant);
	}
	
	@Test
	public void testReadBinaryValue() throws Exception {
		parser.nextToken();
		Base64Variant variant = mock(Base64Variant.class);
		OutputStream out = mock(OutputStream.class);
		parser.readBinaryValue(variant, out);
	}
	
	@Test
	public void testCurrentNode() throws Exception {
		parser.nextToken();
		parser.currentNode();
	}
	
	@Test
	public void testCurrentNumericNode() throws Exception {
		parser.nextToken();
		parser.currentNumericNode();
	}
	
	@Test
	public void test_handleEOF() throws Exception {
		parser._handleEOF();
	}
}