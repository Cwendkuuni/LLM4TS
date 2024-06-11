package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TreeTraversingParserTest {

  @Test
  public void testSetCodec() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    ObjectCodec objectCodec = new ObjectCodec();
    treeTraversingParser.setCodec(objectCodec);
    assertEquals(objectCodec, treeTraversingParser.getCodec());
  }

  @Test
  public void testVersion() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    Version version = new Version();
    assertEquals(version, treeTraversingParser.version());
  }

  @Test
  public void testClose() throws IOException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    treeTraversingParser.close();
    assertTrue(treeTraversingParser.isClosed());
  }

  @Test
  public void testNextToken() throws IOException, JsonParseException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(JsonToken.START_OBJECT, treeTraversingParser.nextToken());
  }

  @Test
  public void testSkipChildren() throws IOException, JsonParseException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(treeTraversingParser, treeTraversingParser.skipChildren());
  }

  @Test
  public void testIsClosed() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertFalse(treeTraversingParser.isClosed());
  }

  @Test
  public void testGetCurrentName() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertNull(treeTraversingParser.getCurrentName());
  }

  @Test
  public void testOverrideCurrentName() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    treeTraversingParser.overrideCurrentName("name");
    assertEquals("name", treeTraversingParser.getCurrentName());
  }

  @Test
  public void testGetParsingContext() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertJsonStreamContextEquals(treeTraversingParser, treeTraversingParser.getParsingContext());
  }

  @Test
  public void testGetTokenLocation() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(JsonLocation.NA, treeTraversingParser.getTokenLocation());
  }

  @Test
  public void testGetCurrentLocation() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(JsonLocation.NA, treeTraversingParser.getCurrentLocation());
  }

  @Test
  public void testGetIntValue() throws IOException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(0, treeTraversingParser.getIntValue());
  }

  @Test
  public void testGetLongValue() throws IOException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(0L, treeTraversingParser.getLongValue());
  }

  @Test
  public void testGetNumberValue() throws IOException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertNull(treeTraversingParser.getNumberValue());
  }

  @Test
  public void testGetEmbeddedObject() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertNull(treeTraversingParser.getEmbeddedObject());
  }

  @Test
  public void testIsNaN() {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertFalse(treeTraversingParser.isNaN());
  }

  @Test
  public void testGetBinaryValue() throws IOException, JsonParseException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertNull(treeTraversingParser.getBinaryValue(new Base64Variant()));
  }

  @Test
  public void testReadBinaryValue() throws IOException, JsonParseException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(0, treeTraversingParser.readBinaryValue(new Base64Variant(), new OutputStream()));
  }

  @Test
  public void testNextValue() throws IOException, JsonParseException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertEquals(treeTraversingParser, treeTraversingParser.nextValue());
  }

  @Test
  public void testNextValueExpectsIOException() throws IOException, JsonParseException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    treeTraversingParser.close();
    assertThrows(IOException.class, () -> treeTraversingParser.nextValue());
  }

  @Test
  public void testHandleEOF() throws JsonParseException {
    TreeTraversingParser treeTraversingParser = new TreeTraversingParser(new JsonNode());
    assertThrows(JsonParseException.class, () -> treeTraversingParser._handleEOF());
  }
  
  private void assertJsonStreamContextEquals(JsonStreamContext expected, JsonStreamContext actual) {
    // assert here
  }
}