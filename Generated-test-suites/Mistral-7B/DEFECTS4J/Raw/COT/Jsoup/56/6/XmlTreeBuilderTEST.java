package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParseWithBaseUri() {
 String input = "<xml version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><root><child/></root></xml>";
 Document document = xmlTreeBuilder.parse(input, "http://example.com");
 assertNotNull(document);
 assertEquals("http://example.com", document.baseUri());
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><root><child/></root></xml>";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child/></root>";
 Tokenizer tokeniser = new Tokenizer(input);
 Token token = tokeniser.next();
 Element root = xmlTreeBuilder.insert(token.asStartTag());
 assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<root><child/></root>";
 Tokenizer tokeniser = new Tokenizer(input);
 Token startTagToken = tokeniser.next();
 Token endTagToken = tokeniser.next();
 xmlTreeBuilder.parse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 assertNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 @Test
 public void testProcessComment() {
 String input = "<!-- Comment -->";
 Tokenizer tokeniser = new Tokenizer(input);
 Token commentToken = tokeniser.next();
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.currentElement().lastChild();
 assertEquals("Comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 String input = "Text";
 Tokenizer tokeniser = new Tokenizer(input);
 Token characterToken = tokeniser.next();
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().lastChild();
 assertEquals("Text", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml [ <!ATTLIST xml root EMPTY> ]>";
 Tokenizer tokeniser = new Tokenizer(input);
 Token doctypeToken = tokeniser.next();
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().lastChild();
 assertEquals("xml", doctypeNode.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTokenType() {
 String input = "<invalid>";
 Tokenizer tokeniser = new Tokenizer(input);
 tokeniser.next();
 xmlTreeBuilder.process(tokeniser.next());
 }

 @Test
 public void testInsertNode() {
 String input = "<root><child/></root>";
 Element child = new Element("child");
 xmlTreeBuilder.insertNode(child);
 Element root = xmlTreeBuilder.currentElement();
 assertEquals(child, root.lastChild());
 }

 @Test
 public void testParseFragmentWithBaseUri() throws IOException {
 String input = "<xml version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><root><child/></root></xml>";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase;
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, "http://example.com", errors, settings);
 assertEquals(3, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 }

 @Test
 public void testParseFragmentWithErrors() throws IOException {
 String input = "<xml version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance><root><child/><broken";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase;
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, "http://example.com", errors, settings);
 assertEquals(2, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 assertEquals(ParseError.MalformedMarkup, errors.get(0).getErrorMessage().get(0));
 }

 @Test
 public void testParseFragmentWithUnknownTag() throws IOException {
 String input = "<xml version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance><root><broken/></root>";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase;
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, "http://example.com", errors, settings);
 assertEquals(2, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 assertNull(nodes.get(1).nodeName());
 }

}