package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 }

 // Test for all public methods in XmlTreeBuilder

 // Test for defaultSettings() method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test for parse() method with valid input
 @Test
 public void testParseValidInput() {
 String xmlInput = "<root><element>Hello</element></root>";
 Document doc = builder.parse(new StringReader(xmlInput), "baseUri");
 assertNotNull(doc);
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("Hello", element.text());
 }

 // Test for parse() method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidInput() {
 String invalidInput = "<root><element>Hello<world</xml>";
 builder.parse(new StringReader(invalidInput), "baseUri");
 }

 // Test for parseFragment() method with valid input
 @Test
 public void testParseFragmentValidInput() {
 String xmlFragment = "<element>Hello</element>";
 List<Node> nodes = builder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("Hello", element.text());
 }

 // Test for parseFragment() method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentInvalidInput() {
 String invalidFragment = "<root><element>Hello<world";
 List<Node> nodes = builder.parseFragment(invalidFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 fail("Expected IllegalArgumentException but got " + nodes);
 }

 // Test for process() method with various token types
 @Test
 public void testProcessTokens() {
 // Start tag
 Token startTag = new Token(Token.Type.StartTag, "<tag>", null);
 builder.process(startTag);
 assertEquals(1, builder.stack.size());

 // End tag
 Token endTag = new Token(Token.Type.EndTag, "</tag>", null);
 builder.process(endTag);
 assertEquals(0, builder.stack.size());

 // Comment
 Token comment = new Token(Token.Type.Comment, "--comment--", true);
 builder.process(comment);
 assertEquals(1, builder.stack.size());

 // Character
 Token charToken = new Token(Token.Type.Character, "abc", false);
 builder.process(charToken);
 assertEquals("abc", builder.doc.child(builder.doc.childNodes().size() - 1).text());

 // Doctype
 Token doctype = new Token(Token.Type.Doctype, "<!DOCTYPE tag [ <!ATTLIST attribute attr \"value\"> ]>", null);
 builder.process(doctype);
 assertEquals(1, builder.stack.size());

 // EOF
 Token eof = new Token(Token.Type.EOF, null);
 builder.process(eof);
 assertEquals(0, builder.stack.size());
 }

 // Test for insertNode() method
 @Test
 public void testInsertNode() {
 Element root = new Element("root");
 builder.insertNode(root);
 assertEquals(1, builder.stack.size());
 assertEquals(root, builder.stack.get(0));
 }

 // Test for insert() method with start tag
 @Test
 public void testInsertStartTag() {
 String xmlInput = "<root><element>Hello</element></root>";
 Token startTag = new Token(Token.Type.StartTag, "<element>", null);
 Element element = builder.insert(startTag);
 assertEquals("element", element.tagName());
 assertEquals("Hello", element.text());
 assertEquals(1, builder.stack.size());
 }

 // Test for insert() method with end tag
 @Test
 public void testInsertEndTag() {
 String xmlInput = "<root><element></element></root>";
 Token endTag = new Token(Token.Type.EndTag, "</element>", null);
 Element element = builder.stack.get(builder.stack.size() - 1);
 builder.popStackToClose(endTag);
 assertEquals(0, builder.stack.size());
 assertEquals("element", element.tagName());
 }

 // Test for insert() method with comment
 @Test
 public void testInsertComment() {
 String xmlInput = "<root><!--comment--></root>";
 Token commentToken = new Token(Token.Type.Comment, "--comment--", false);
 Element comment = builder.insert(commentToken);
 assertEquals(1, builder.stack.size());
 assertEquals(comment, builder.stack.get(0));
 }

 // Test for insert() method with character
 @Test
 public void testInsertCharacter() {
 String xmlInput = "<root>abc</root>";
 Character charToken = new Character('a');
 Element text = builder.insert(charToken);
 assertEquals(1, builder.stack.size());
 assertEquals(text, builder.stack.get(0));
 }

 // Test for insert() method with doctype
 @Test
 public void testInsertDoctype() {
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST attribute attr \"value\"> ]>";
 Doctype doctype = new Doctype("root", null, null);
 Element doctypeNode = builder.insert(doctype);
 assertEquals(1, builder.stack.size());
 assertEquals(doctypeNode, builder.stack.get(0));
 }

 // Test for popStackToClose() method with valid input
 @Test
 public void testPopStackToCloseValidInput() {
 String xmlInput = "<root><element><element></element></element></root>";
 builder.parse(new StringReader(xmlInput), "baseUri");
 builder.popStackToClose(new Token(Token.Type.EndTag, "</element>", null));
 assertEquals(1, builder.stack.size());
 assertEquals("element", builder.stack.get(0).tagName());
 }

 // Test for popStackToClose() method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testPopStackToCloseInvalidInput() {
 String xmlInput = "<root><element></root>";
 builder.parse(new StringReader(xmlInput), "baseUri");
 builder.popStackToClose(new Token(Token.Type.EndTag, "</wrongElement>", null));
 }

}