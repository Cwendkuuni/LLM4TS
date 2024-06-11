package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * JUnit test file for XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test for defaultSettings() method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test for parse(Reader, String) method
 @Test
 public void testParseReader() throws Exception {
 String xmlString = "<xml><tag1 attr1='value1'><tag2 attr2='value2'/><tag3/></tag1></xml>";
 Reader reader = new StringReader(xmlString);
 Document xmlDocument = xmlTreeBuilder.parse(reader, "baseUri");
 assertNotNull(xmlDocument);
 Element rootElement = xmlDocument.rootElement();
 assertEquals("xml", rootElement.tagName());
 List<Node> nodes = xmlDocument.childNodes();
 assertEquals(3, nodes.size());
 }

 // Test for parse(String, String) method
 @Test
 public void testParseString() throws Exception {
 String xmlString = "<xml><tag1 attr1='value1'><tag2 attr2='value2'/><tag3/></tag1></xml>";
 Document xmlDocument = xmlTreeBuilder.parse(xmlString, "baseUri");
 assertNotNull(xmlDocument);
 Element rootElement = xmlDocument.rootElement();
 assertEquals("xml", rootElement.tagName());
 List<Node> nodes = xmlDocument.childNodes();
 assertEquals(3, nodes.size());
 }

 // Test for initialiseParse(Reader, String, ParseErrorList, ParseSettings) method
 @Test
 public void testInitialiseParse() {
 Validate.fail("This method is not intended for testing");
 }

 // Test for process(Token) method
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<tag1 attr1='value1'>", 0);
 xmlTreeBuilder.process(startTag);
 Element tag1 = new Element("tag1", "baseUri", null);
 assertEquals(tag1, xmlTreeBuilder.currentElement());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token(Token.EndTag.class, "</tag1>", 0);
 xmlTreeBuilder.process(endTag);
 assertEquals(null, xmlTreeBuilder.currentElement());
 }

 // Test for insertNode(Node) method
 @Test
 public void testInsertNode() {
 Element tag1 = new Element("tag1", "baseUri", null);
 xmlTreeBuilder.insertNode(tag1);
 Element rootElement = xmlTreeBuilder.currentElement();
 assertEquals(tag1, rootElement.child(0));
 }

 // Test for insert(Token.StartTag) method
 @Test
 public void testInsertStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<tag1 attr1='value1'>", 0);
 xmlTreeBuilder.insert(startTag);
 Element tag1 = new Element("tag1", "baseUri", null);
 assertEquals(tag1, xmlTreeBuilder.currentElement());
 }

 // Test for insert(Token.Comment) method
 @Test
 public void testInsertComment() {
 Token commentToken = new Token(Token.Comment.class, "--comment--", 0);
 xmlTreeBuilder.insert(commentToken);
 Comment comment = new Comment("--comment--");
 assertEquals(comment, xmlTreeBuilder.currentNode());
 }

 // Test for insert(Token.Character) method
 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token(Token.Character.class, "test", 0);
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = new TextNode("test");
 assertEquals(textNode, xmlTreeBuilder.currentNode());
 }

 // Test for insert(Token.Doctype) method
 @Test
 public void testInsertDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE xml SYSTEM \"xml.dtd\">", 0);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentType = new DocumentType("xml", null, null);
 assertEquals(documentType, xmlTreeBuilder.currentNode());
 }

 // Test for popStackToClose(Token.EndTag) method
 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.push("tag1");
 xmlTreeBuilder.push("tag2");
 Token endTag = new Token(Token.EndTag.class, "</tag2>", 0);
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("tag1", xmlTreeBuilder.stack.get(0).toString());
 }

 // Test for parseFragment(String, String, ParseErrorList, ParseSettings) method
 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<tag1 attr1='value1'><tag2 attr2='value2'/><tag3/></tag1>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", null, null);
 assertEquals(3, nodes.size());
 }

}