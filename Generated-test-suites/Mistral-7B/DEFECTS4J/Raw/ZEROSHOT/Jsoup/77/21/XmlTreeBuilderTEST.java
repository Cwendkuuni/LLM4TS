package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document expectedDocument;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><child1 attr1=\"val1\"><child2 attr2=\"val2\"/><child3 attr3=\"val3\"/></child1></root>";
 expectedDocument = Jsoup.parse(xmlString, Parser.xmlParser());
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><child1 attr1=\"val1\"><child2 attr2=\"val2\"/><child3 attr3=\"val3\"/></child1></root>";
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child1 attr1=\"val1\"><child2 attr2=\"val2\"/><child3 attr3=\"val3\"/></child1>";
 List<Node> expectedNodes = expectedDocument.childNodes();
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();
 assertEquals(expectedNodes.size(), actualNodes.size());
 for (int i = 0; i < expectedNodes.size(); i++) {
 Node expectedNode = expectedNodes.get(i);
 Node actualNode = actualNodes.get(i);
 assertEquals(expectedNode.nodeName(), actualNode.nodeName());
 assertEquals(expectedNode.tagName(), actualNode.tagName());
 assertEquals(expectedNode.attributes(), actualNode.attributes());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateInputNull() {
 xmlTreeBuilder.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateInputEmpty() {
 xmlTreeBuilder.parse(new StringReader(""), "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateBaseUriNull() {
 xmlTreeBuilder.parse(new StringReader("<root></root>"), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateSettingsNull() {
 xmlTreeBuilder.parse(new StringReader("<root></root>"), "http://example.com", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateSettingsInvalid() {
 ParseSettings invalidSettings = new ParseSettings() {
 @Override
 public ParseSettings defaultSettings() {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public List<String> getErrors() {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void addError(ParseError error) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public boolean isValid() {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public String getErrorMessage() {
 throw new UnsupportedOperationException("Not supported yet.");
 }
 };
 xmlTreeBuilder.parse(new StringReader("<root></root>"), "http://example.com", invalidSettings);
 }

 @Test
 public void testProcessStartTag() {
 String xmlInput = "<root><child1 attr1=\"val1\"><child2 attr2=\"val2\"/><child3 attr3=\"val3\"/></child1></root>";
 Token startTagToken = new Token(Token.START_TAG, "child1", null, null, new Tag("child1"), null, null, null, null, null, null, null, true, null, null);
 Element expectedElement = new Element("child1", "http://example.com", null);
 xmlTreeBuilder.stack = new ArrayList<>();
 xmlTreeBuilder.stack.add(expectedDocument);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 Element actualElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 assertEquals(expectedElement.tagName(), actualElement.tagName());
 assertEquals(expectedElement.baseUri(), actualElement.baseUri());
 assertEquals(expectedElement.attributes(), actualElement.attributes());
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<root><child1 attr1=\"val1\"><child2 attr2=\"val2\"/><child3 attr3=\"val3\"></child1></root>";
 Token endTagToken = new Token(Token.END_TAG, "child1", null, null, null, null, null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.stack = new ArrayList<>();
 xmlTreeBuilder.stack.add(expectedDocument);
 xmlTreeBuilder.stack.add(new Element("child1", "http://example.com", null));
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.size(), 1);
 }

 @Test
 public void testProcessComment() {
 String xmlInput = "<!-- Comment -->";
 Token commentToken = new Token(Token.COMMENT, null, null, null, null, null, null, null, null, null, null, null, null, null, null, " Comment ");
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).nodeName(), Comment.class.getName());
 }

 @Test
 public void testProcessCharacter() {
 String xmlInput = "<root>&#x26;<child1 attr1=\"val1\">&nbsp;</child1></root>";
 Token characterToken1 = new Token(Token.CHAR_DATA, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "&amp;");
 Token characterToken2 = new Token(Token.CHAR_DATA, null, null, null, null, null, null, null, null, null, null, null, null, null, null, " ");
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).nodeName(), TextNode.class.getName());
 }

 @Test
 public void testProcessDoctype() {
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST child1 attr1 CDATA #REQUIRED> ]>";
 Token doctypeToken = new Token(Token.DOCTYPE, null, null, null, null, null, null, null, null, new DocumentType("root", null, null), null, null, null, null, null);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).nodeName(), DocumentType.class.getName());
 }

 @Test
 public void testProcessEOF() {
 String xmlInput = "<root></root>";
 Token eofToken = new Token(Token.EOF, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }
}