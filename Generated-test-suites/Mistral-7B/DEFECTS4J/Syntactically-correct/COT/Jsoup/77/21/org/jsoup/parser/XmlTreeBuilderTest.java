package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

/**
 * Unit tests for the XmlTreeBuilder class
 *
 * @author your_name
 */
public class XmlTreeBuilderTest {

 private Document expectedXmlDocument;

 @Before
 public void setUp() throws IOException, JDOMException {
 // Create an expected XML document using JDOM for comparison
 String xmlInput = "<root><elem attr=\"val\">content</elem></root>";
 SAXBuilder builder = new SAXBuilder();
 expectedXmlDocument = builder.build(new StringReader(xmlInput));
 }

 @After
 public void tearDown() {
 // Clear the static stack for each test
 XmlTreeBuilder.stack.clear();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = XmlTreeBuilder.defaultSettings();
 Document doc = XmlTreeBuilder.parse(new StringReader("<xml/>"), null);
 assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
 }

 @Test
 public void testParse() throws IOException {
 String xmlInput = "<root><elem attr=\"val\">content</elem></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), null);
 assertNotNull(doc);
 assertNotNull(doc.child(0));
 assertNotNull(doc.child(0).child(0));
 assertNotNull(doc.child(0).child(0).attr("attr"));
 assertTrue(doc.child(0).child(0).attr("attr").getValue().equals("val"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 XmlTreeBuilder.parse(null, null);
 }

 @Test
 public void testEmptyInput() {
 Document doc = XmlTreeBuilder.parse(new StringReader(""), null);
 assertNotNull(doc);
 }

 @Test
 public void testParseWithErrors() {
 ParseErrorList errors = new ParseErrorList();
 String xmlInputWithError = "<root><elem attr=\"val>content</elem></root>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.parse(new StringReader(xmlInputWithError), null, errors, XmlTreeBuilder.defaultSettings());
 assertEquals(1, errors.size());
 }

 @Test
 public void testParseFragment() throws IOException {
 String xmlFragment = "<root><elem attr=\"val\">content</elem></root>";
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), XmlTreeBuilder.defaultSettings());
 Element root = (Element) nodes.get(0);
 Element elem = (Element) root.children().get(0);
 assertNotNull(root.getName());
 assertNotNull(elem.getName());
 assertNotNull(elem.getText());
 assertNotNull(elem.attr("attr"));
 assertTrue(elem.attr("attr").getValue().equals("val"));
 }

 @Test
 public void testProcessStartTag() {
 String xmlInput = "<root><elem attr=\"val\">content</elem></root>";
 StringReader input = new StringReader(xmlInput);
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.parse(input, null, ParseErrorList.noTracking(), XmlTreeBuilder.defaultSettings());
 Element root = (Element) builder.doc.child(0);
 Element elem = (Element) root.children().get(0);
 Element tagElement = builder.insert(new Token.StartTag("<elem attr=\"val\">", XmlTreeBuilder.defaultSettings()));
 assertNotNull(tagElement);
 assertTrue(tagElement.getNodeName().equals(elem.getNodeName()));
 assertTrue(tagElement.attributes().size() == elem.attributes().size());
 for (String key : elem.attributes().keySet()) {
 String value = elem.attributes().get(key).getValue();
 String attributeValue = tagElement.attributes().get(key).getValue();
 assertTrue(attributeValue.equals(value));
 }
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<root><elem><elem2/></elem></root>";
 StringReader input = new StringReader(xmlInput);
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.parse(input, null, ParseErrorList.noTracking(), XmlTreeBuilder.defaultSettings());
 Element root = (Element) builder.doc.child(0);
 Element elem = (Element) root.children().get(0);
 Element elem2 = (Element) elem.children().get(0);
 XmlTreeBuilder.popStackToClose(new Token.EndTag("<elem>", XmlTreeBuilder.defaultSettings()));
 Element poppedElem = (Element) builder.stack.get(builder.stack.size() - 1);
 assertEquals(elem.getNodeName(), poppedElem.getNodeName());
 }

 @Test
 public void testProcessComment() {
 String xmlInput = "<!DOCTYPE root [ <!END> ]><!-- comment --><root></root>";
 StringReader input = new StringReader(xmlInput);
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.parse(input, null, ParseErrorList.noTracking(), XmlTreeBuilder.defaultSettings());
 List<Node> nodes = builder.doc.childNodes();
 assertEquals(3, nodes.size());
 Node commentNode = nodes.get(1);
 assertTrue(commentNode instanceof Comment);
 assertEquals(Comment.class, commentNode.getClass());
 String commentText = ((Comment) commentNode).getData();
 assertEquals(commentText, "-- comment --");
 }

 @Test
 public void testProcessCharacter() {
 String xmlInput = "<root>&#x304;content</root>";
 StringReader input = new StringReader(xmlInput);
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.parse(input, null, ParseErrorList.noTracking(), XmlTreeBuilder.defaultSettings());
 List<Node> nodes = builder.doc.childNodes();
 assertEquals(2, nodes.size());
 Node characterNode = nodes.get(1);
 assertTrue(characterNode instanceof TextNode);
 assertEquals(TextNode.class, characterNode.getClass());
 assertEquals(characterNode.getText(), "content\u0304");
 }

 @Test
 public void testProcessDoctype() {
 String xmlInput = "<!DOCTYPE root SYSTEM \"doc.dtd\">";
 StringReader input = new StringReader(xmlInput);
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.parse(input, null, ParseErrorList.noTracking(), XmlTreeBuilder.defaultSettings());
 List<Node> nodes = builder.doc.childNodes();
 assertEquals(1, nodes.size());
 Node doctypeNode = nodes.get(0);
 assertTrue(doctypeNode instanceof DocumentType);
 assertEquals(DocumentType.class, doctypeNode.getClass());
 assertEquals(doctypeNode.getNodeName(), "root");
 assertEquals(doctypeNode.getText(), "doc.dtd");
 }

}