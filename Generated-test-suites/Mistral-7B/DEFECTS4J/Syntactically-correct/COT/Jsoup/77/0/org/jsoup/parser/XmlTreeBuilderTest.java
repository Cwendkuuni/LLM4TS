package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

/**
 * Test cases for XmlTreeBuilder class
 *
 * @author YourName
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() throws Exception {
 String xmlString = "<root><element>Hello World</element></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 assertEquals(1, xmlDocument.numberOfChildren());
 Element rootElement = xmlDocument.child(0);
 assertEquals("root", rootElement.tagName());
 assertEquals("", rootElement.baseUri());
 List<Node> childNodes = rootElement.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals("element", childNodes.get(0).tagName());
 assertEquals("Hello World", childNodes.get(0).text());
 }

 @Test
 public void testProcessStartTag() throws Exception {
 String xmlString = "<root><element/></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").get();
 Element element = xmlDocument.child(0).child(0);
 assertEquals(0, element.childNodes().size());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() throws Exception {
 String input = "<html><body><h1>Test</h1>"; // incomplete HTML tag
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(input), "").get();
 fail("Should have thrown an AssertionError for unexpected token type: " + XmlTreeBuilder.Token.Type.HTML);
 }

 @Test
 public void testPopStackToClose() throws Exception {
 String xmlString = "<root><a><b><c></c></b></a></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "").get();
 List<Node> stack = xmlDocument.childNodes();
 assertEquals(3, stack.size());
 xmlTreeBuilder.popStackToClose(Token.EndTag.valueOf("c"));
 assertEquals(2, stack.size());
 assertEquals("a", stack.get(stack.size() - 1).tagName());
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlString = "<root><element>Hello World</element></root>";
 List<Node> xmlFragmentNodes = xmlTreeBuilder.parseFragment(xmlString, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Document xmlDocument = Jsoup.parseBodyFragment(xmlFragmentNodes.toString());
 assertEquals(1, xmlDocument.numberOfChildren());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() throws Exception {
 String input = "InvalidInput";
 try {
 xmlTreeBuilder.parse(new StringReader(input), "");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

}