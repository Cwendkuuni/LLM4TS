package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
  ParseSettings settings = xmlTreeBuilder.defaultSettings();
  assertTrue(settings.isPreserveCase());
 }

 @Test
 public void testParseWithBaseUri() {
  String xmlInput = "<xml version='1.0'><root><child xmlns:x='http://example.com'></child></root></xml>";
  String baseUri = "http://example.com";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

  Element root = xmlDoc.rootElement();
  assertEquals("root", root.tagName());
  Element child = root.getFirstElement();
  assertEquals("child", child.tagName());
  assertNull(child.namespaceInfo().namespacePrefix());
  assertEquals("http://example.com", child.namespaceInfo().namespaceUri());
 }

 @Test
 public void testParseWithoutBaseUri() {
  String xmlInput = "<xml version='1.0'><root><child xmlns:x='http://example.com'></child></root></xml>";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput));

  Element root = xmlDoc.rootElement();
  assertEquals("root", root.tagName());
  Element child = root.getFirstElement();
  assertNull(child.namespaceInfo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
  xmlTreeBuilder.parse(null, "http://example.com");
  fail("Expected IllegalArgumentException but was not thrown.");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
  String xmlInput = "<xml version='1.0'><root><child xmlns:x='http://example.com'></child></root></xml>";
  xmlTreeBuilder.parse(new StringReader(xmlInput), null);
  fail("Expected NullPointerException but was not thrown.");
 }

 @Test
 public void testProcessStartTag() {
  String xmlInput = "<root><child xmlns:x='http://example.com'></child></root>";
  String baseUri = "http://example.com";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);
  Element root = xmlDoc.rootElement();
  Element child = root.getFirstElement();

  Token startTagToken = new Token.StartTag("root");
  Element rootFromToken = xmlTreeBuilder.insert(startTagToken);
  assertEquals(rootFromToken, root);
 }

 @Test
 public void testProcessEndTag() {
  String xmlInput = "<root><child xmlns:x='http://example.com'><tag></tag></child></root>";
  String baseUri = "http://example.com";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);
  Element root = xmlDoc.rootElement();
  Element child = root.getFirstElement();
  Element tag = child.getFirstElement();

  Token endTagToken = new Token.EndTag("tag");
  xmlTreeBuilder.popStackToClose(endTagToken);
  assertEquals(1, stack.size());
  assertNull(tag.parentNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithUnknownTagName() {
  String xmlInput = "<root><child xmlns:x='http://example.com'><tag></tag></child></root>";
  String baseUri = "http://example.com";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);
  Element root = xmlDoc.rootElement();
  Element child = root.getFirstElement();

  Token endTagToken = new Token.EndTag("unknownTag");
  xmlTreeBuilder.popStackToClose(endTagToken);
  fail("Expected IllegalArgumentException but was not thrown.");
 }

 @Test
 public void testProcessComment() {
  String xmlInput = "<!--This is a comment-->";
  String baseUri = "http://example.com";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

  Comment comment = xmlDoc.getFirstComment();
  assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
  String xmlInput = "<xml version='1.0'><root><child>&lt;Test&gt;</child></root></xml>";
  String baseUri = "http://example.com";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

  TextNode textNode = (TextNode) xmlDoc.childNodes().get(1);
  assertEquals("&lt;Test&gt;", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
  String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">";
  String baseUri = "http://example.com";
  Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

  DocumentType docTypeNode = (DocumentType) xmlDoc.childNodes().get(0);
  assertEquals("root", docTypeNode.nodeName());
  assertEquals("root.dtd", docTypeNode.systemIdentifier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidTokenType() {
  String xmlInput = "<xml version='1.0'>";
  String baseUri = "http://example.com";
  Token token = new Token(1);
  xmlTreeBuilder.process(token);
  fail("Expected IllegalArgumentException but was not thrown.");
 }

 @Test
 public void testParseFragment() {
  String xmlFragment = "<root><child xmlns:x='http://example.com'><tag></tag></child></root>";
  String baseUri = "http://example.com";
  List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
  Document xmlDoc = new Document();
  xmlDoc.appendChild(nodes.get(0));

  Element root = xmlDoc.rootElement();
  Element child = root.getFirstElement();
  Element tag = child.getFirstElement();

  assertEquals("root", root.tagName());
  assertEquals("child", child.tagName());
  assertEquals("tag", tag.tagName());
 }

}