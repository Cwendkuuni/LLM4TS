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
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test defaultSettings() method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 // Test parse() method with valid input
 @Test
 public void testParseValidInput() throws Exception {
 String xmlInput = "<root><child>Hello</child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
 Element root = document.getRootElement();
 assertEquals("root", root.tagName());
 Element child = root.getChild(0);
 assertEquals("child", child.tagName());
 assertEquals("Hello", child.text());
 }

 // Test parse() method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidInput() throws Exception {
 String invalidInput = "<root>Hello</root><div>"; // missing '>' for div tag
 Document document = xmlTreeBuilder.parse(new StringReader(invalidInput), "http://example.com");
 fail("Expected an IllegalArgumentException but it did not occur.");
 }

 // Test process() method with valid tokens
 @Test
 public void testProcessValidTokens() {
 Token startTag = new Token.StartTag("root");
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.process(startTag);
 xmlTreeBuilder.process(startTag); // should not throw an exception
 Element root = xmlTreeBuilder.insert(startTag);
 xmlTreeBuilder.process(endTag);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("root", xmlTreeBuilder.stack.get(0).tagName());
 }

 // Test process() method with invalid token
 @Test(expected = Validate.MalformedXmlException.class)
 public void testProcessInvalidToken() {
 Token invalidToken = new Token.StartTag("root>"); // missing '>' for start tag
 xmlTreeBuilder.process(invalidToken);
 fail("Expected a MalformedXmlException but it did not occur.");
 }

 // Test insertNode() method
 @Test
 public void testInsertNode() {
 Node node = new Element("child", "http://example.com");
 xmlTreeBuilder.insertNode(node);
 assertEquals("child", xmlTreeBuilder.currentElement().getChild(0).tagName());
 }

 // Test insert() method with valid comment token
 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("This is a comment.");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(" Comment ", xmlTreeBuilder.stack.get(0).nodeName());
 }

 // Test parseFragment() method with valid input
 @Test
 public void testParseFragmentValidInput() throws Exception {
 String xmlFragment = "<root><child>Hello</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element child = (Element) root.getChild(0);
 assertEquals("child", child.tagName());
 assertEquals("Hello", child.text());
 }

 // Test parseFragment() method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentInvalidInput() throws Exception {
 String invalidInput = "<root>Hello</root><div>"; // missing '>' for div tag
 List<Node> nodes = xmlTreeBuilder.parseFragment(invalidInput, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 fail("Expected an IllegalArgumentException but it did not occur.");
 }

 // Test popStackToClose() method with valid tag name
 @Test
 public void testPopStackToCloseValidTagName() {
 xmlTreeBuilder.stack.add(new Element("root"));
 xmlTreeBuilder.stack.add(new Element("child"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("root", xmlTreeTreeBuilder.stack.get(0).tagName());
 }

 // Test popStackToClose() method with invalid tag name
 @Test
 public void testPopStackToCloseInvalidTagName() {
 xmlTreeBuilder.stack.add(new Element("root"));
 xmlTreeBuilder.stack.add(new Element("child"));
 xmlTreeBuilder.stack.add(new Element("parent"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("parent"));
 assertEquals(2, xmlTreeBuilder.stack.size());
 assertEquals("root", xmlTreeBuilder.stack.get(0).tagName());
 assertEquals("child", xmlTreeBuilder.stack.get(1).tagName());
 }
}