package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 }

 // Test defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 // Test parse method with String input and String baseUri
 @Test
 public void testParseWithStringInputAndBaseUri() {
 String xmlString = "<xml><tag>Hello World</tag></xml>";
 String baseUri = "http://example.com";
 Document xmlDoc = builder.parse(xmlString, baseUri);
 Element root = xmlDoc.rootElement();
 assertEquals("tag", root.tagName());
 assertEquals("Hello World", root.text());
 }

 // Test parse method with Reader input and String baseUri
 @Test
 public void testParseWithReaderInputAndBaseUri() {
 String xmlString = "<xml><tag>Hello World</tag></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "http://example.com";
 Document xmlDoc = builder.parse(input, baseUri);
 Element root = xmlDoc.rootElement();
 assertEquals("tag", root.tagName());
 assertEquals("Hello World", root.text());
 }

 // Test process method with StartTag token
 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<tag>", 1, 1, 1);
 builder.process(startTagToken);
 Element el = new Element("tag");
 assertEquals(el, builder.currentElement());
 }

 // Test process method with EndTag token
 @Test (expected = AssertionError.class)
 public void testProcessEndTagWithNoMatchingElement() {
 Token endTagToken = new Token(Token.EndTag.class, "</tag>", 1, 1, 1);
 builder.process(endTagToken);
 Validate.fail("Unexpected error: no matching element on stack to close.");
 }

 // Test process method with EndTag token and matching element on stack
 @Test
 public void testProcessEndTagWithMatchingElement() {
 Token startTagToken = new Token(Token.StartTag.class, "<tag>", 1, 1, 1);
 Token endTagToken = new Token(Token.EndTag.class, "</tag>", 1, 1, 1);
 builder.process(startTagToken);
 Element el = new Element("tag");
 builder.currentElement().appendChild(el);
 builder.stack.add(el);
 builder.process(endTagToken);
 assertEquals(0, builder.stack.size());
 }

 // Test process method with Comment token
 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Comment.class, "--comment--", 1, 1, 1);
 builder.process(commentToken);
 Comment comment = new Comment("--comment--");
 assertEquals(comment, builder.currentNode());
 }

 // Test process method with Character token
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Character.class, "\\u00A9", 1, 1, 1);
 builder.process(characterToken);
 TextNode textNode = new TextNode("©");
 assertEquals(textNode, builder.currentNode());
 }

 // Test process method with Doctype token
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE xml SYSTEM \"xml.dtd\">", 1, 1, 1);
 DocumentType documentType = new DocumentType("xml", null, null);
 builder.process(doctypeToken);
 assertEquals(documentType, builder.currentNode());
 }

 // Test insertNode method
 @Test
 public void testInsertNode() {
 Element el = new Element("tag");
 Node node = new TextNode("Hello World");
 builder.insertNode(el);
 builder.currentElement().appendChild(node);
 assertEquals(node, builder.currentNode());
 }

 // Test parseFragment method with String input, String baseUri, ParseErrorList, and ParseSettings
 @Test
 public void testParseFragmentWithStringInputBaseUriParseErrorListParseSettings() {
 String xmlFragment = "<xml><tag><subtag>Hello World</subtag></tag></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = builder.defaultSettings();
 List<Node> nodes = builder.parseFragment(xmlFragment, baseUri, errors, settings);
 Element root = (Element) nodes.get(0);
 assertEquals("tag", root.tagName());
 List<Node> children = root.children();
 assertEquals(1, children.size());
 assertEquals("subtag", children.get(0).tagName());
 }

 // Test popStackToClose method with non-matching end tag
 @Test (expected = AssertionError.class)
 public void testPopStackToCloseWithNonMatchingEndTag() {
 Token endTagToken1 = new Token(Token.EndTag.class, "</tag1>", 1, 1, 1);
 Token endTagToken2 = new Token(Token.EndTag.class, "</tag2>", 1, 1, 1);
 builder.process(new Token(Token.StartTag.class, "<tag1>", 1, 1, 1));
 builder.process(endTagToken1);
 builder.popStackToClose(endTagToken2);
 Validate.fail("Unexpected error: EndTag tag2 does not match current stack top tag tag1.");
 }

 // Test popStackToClose method with matching end tag
 @Test
 public void testPopStackToCloseWithMatchingEndTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<tag>", 1, 1, 1);
 Token endTagToken = new Token(Token.EndTag.class, "</tag>", 1, 1, 1);
 builder.process(startTagToken);
 builder.currentElement().appendChild(new Element("tag"));
 builder.stack.add(builder.currentElement());
 builder.process(endTagToken);
 assertEquals(0, builder.stack.size());
 }
}