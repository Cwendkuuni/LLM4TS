package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 @Mock
 private Parser parser;

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child>Hello</child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");
 assertEquals("<root><child>Hello</child></root>", document.html());
 }

 @Test
 public void testParseWithBaseUri() {
 String xmlString = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><root xmlns:mynamespace=\"http://mynamespace.com\"><child>Hello</child></root></html>";
 String baseUri = "http://test.com";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 assertEquals("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><root xmlns:mynamespace=\"http://mynamespace.com\"><child>Hello</child></root></html>", document.html());
 assertEquals("http://test.com/root", document.child(0).attributes().get("id").toString());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child>Hello</child>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, "baseUrl", parser);
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertEquals("Hello", node.text());
 }

 @Test
 public void testParseFragmentWithContext() {
 String xmlFragment = "<child>Hello</child>";
 Element context = new Element("root");
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, "baseUrl", parser);
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertEquals("Hello", node.text());
 context.appendChild(node);
 assertEquals("<root><child>Hello</child></root>", context.html());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidTokenType() {
 Token token = new Token(1, "invalid", "invalid");
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<root><child>Hello</child></root>";
 Token token = new Token(Token.Type.StartTag, "<root>", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(token);
 assertEquals("<root>", xmlTreeBuilder.doc.html());
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<root><child><grandchild/></child></root>";
 Token token = new Token(Token.Type.EndTag, "</grandchild>", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(token);
 assertEquals("<root><child></child></root>", xmlTreeBuilder.doc.html());
 }

 @Test
 public void testProcessComment() {
 String xmlString = "<!-- This is a comment -->";
 Token token = new Token(Token.Type.Comment, "-->This is a comment <--", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(token);
 assertEquals("<!-- This is a comment -->", xmlTreeBuilder.doc.html());
 }

 @Test
 public void testProcessCharacter() {
 String xmlString = "<root>Hello World</root>";
 Token token = new Token(Token.Type.Character, 'H', null, null, null, null, null, null, null);
 xmlTreeBuilder.process(token);
 assertEquals("<root>Hel</root>", xmlTreeBuilder.doc.html());
 }

 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE root [ <!ATTLIST root id ID #REQUIRED > ]><root></root>";
 Token token = new Token(Token.Type.Doctype, null, null, null, "root", null, null, null, asList(new Token.Attribute("id", "ID", null, null, null, null, null, null, null)));
 xmlTreeBuilder.process(token);
 assertEquals("<!DOCTYPE root [ <!ATTLIST root id ID #REQUIRED > ]><root></root>", xmlTreeBuilder.doc.html());
 }

 @Test
 public void testProcessEOF() {
 String xmlString = "<root><child>Hello</child></root>";
 xmlTreeBuilder.process(new Token(Token.Type.EOF, null, null, null, null, null, null, null, null));
 assertEquals("<root><child>Hello</child></root>", xmlTreeBuilder.doc.html());
 }
}