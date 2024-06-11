
package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class TreeTraversingParserTest {
	
	private TreeTraversingParser parser;
	
	@Before
	public void setUp() {
		parser = new TreeTraversingParser(null);
	}
	
	@Test
	public void testSetCodec() {
		Assert.assertNull(parser.getCodec());
		
		ObjectCodec codec = new ObjectCodec();
		parser.setCodec(codec);
		
		Assert.assertEquals(codec, parser.getCodec());
	}
	
	@Test
	public void testVersion() {
		Version version = new Version();
		Assert.assertEquals(version, parser.version());
	}
	
	@Test(expected = IOException.class)
	public void testClose() throws IOException {
		parser.close();
		parser.nextToken();
	}
	
	@Test
	public void testNextToken() throws IOException, JsonParseException {
		JsonToken token = parser.nextToken();
		Assert.assertNull(token);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSkipChildren() throws IOException, JsonParseException {
		parser.skipChildren();
	}
	
	@Test
	public void testIsClosed() {
		Assert.assertFalse(parser.isClosed());
	}
	
	@Test
	public void testGetCurrentName() {
		Assert.assertNull(parser.getCurrentName());
	}

	@Test
	public void testGetParsingContext() {
		Assert.assertNull(parser.getParsingContext());
	}

	@Test
	public void testTokenLocation() {
		Assert.assertEquals(JsonLocation.NA, parser.getTokenLocation());
	}

	@Test
	public void testCurrentLocation() {
		Assert.assertEquals(JsonLocation.NA, parser.getCurrentLocation());
	}
	
	@Test
	public void testGetText() throws IOException, JsonParseException {
		Assert.assertNull(parser.getText());
	}

	@Test
	public void testGetTextCharacters() throws IOException, JsonParseException {
		Assert.assertNull(parser.getTextCharacters());
	}

	@Test
	public void testGetTextLength() throws IOException, JsonParseException {
		Assert.assertEquals(0, parser.getTextLength());
	}

	@Test
	public void testGetTextOffset() throws IOException, JsonParseException {
		Assert.assertEquals(0, parser.getTextOffset());
	}

	@Test
	public void testHasTextCharacters() {
		Assert.assertFalse(parser.hasTextCharacters());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetNumberType() throws IOException {
		parser.getNumberType();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetBigIntegerValue() throws IOException {
		parser.getBigIntegerValue();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetDecimalValue() throws IOException {
		parser.getDecimalValue();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetDoubleValue() throws IOException {
		parser.getDoubleValue();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetFloatValue() throws IOException {
		parser.getFloatValue();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetIntValue() throws IOException {
		parser.getIntValue();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetLongValue() throws IOException {
		parser.getLongValue();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetNumberValue() throws IOException {
		parser.getNumberValue();
	}

	@Test
	public void testGetEmbeddedObject() {
		Assert.assertNull(parser.getEmbeddedObject());
	}

	@Test
	public void testIsNaN() {
		Assert.assertFalse(parser.isNaN());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetBinaryValue() throws IOException, JsonParseException {
		parser.getBinaryValue(Base64Variant.getDefaultVariant());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testReadBinaryValue() throws IOException, JsonParseException {
		parser.readBinaryValue(Base64Variant.getDefaultVariant(), null);
	}

	@Test
	public void testHandleEOF() throws JsonParseException {
		parser._handleEOF();
	}
}
