package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test parse method with valid XML input
 @Test
 public void testParseValidXml() {
 String xmlInput = "<root><element1 attr1='value1' attr2='value2'><element2/><element3/></element1></root>";
 Document document = xmlTreeBuilder.parse(xmlInput, "baseUrl");
 assertNotNull(document);
 Element root = document.getRootElement();
 assertEquals("root", root.tagName());
 Element element1 = root.child(0);
 assertEquals("element1", element1.tagName());
 assertNotNull(element1.attr("attr1"));
 assertEquals("value1", element1.attr("attr1").getValue());
 assertNotNull(element1.attr("attr2"));
 assertEquals("value2", element1.attr("attr2").getValue());
 Element element2 = element1.child(0);
 assertNull(element2.attr("attr1"));
 assertNull(element2.attr("attr2"));
 assertEquals("element2", element2.tagName());
 }

 // Test parse method with invalid XML input
 @Test(expected = ParseError.class)
 public void testParseInvalidXml() {
 String xmlInput = "<root><element1 attr1='value1'><element2/><element3> <-- this tag is not closed";
 xmlTreeBuilder.parse(xmlInput, "baseUrl");
 }

 // Test parseFragment method with valid XML fragment input
 @Test
 public void testParseFragmentValidXml() {
 String xmlFragmentInput = "<element1 attr1='value1'><element2/><element3/></element1>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragmentInput, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element fragmentRoot = (Element) fragmentNodes.get(0);
 assertEquals("element1", fragmentRoot.tagName());
 Element element2 = (Element) fragmentNodes.get(1);
 assertEquals("element2", element2.tagName());
 assertNull(element2.attr("attr1"));
 }

 // Test parseFragment method with invalid XML fragment input
 @Test(expected = ParseError.class)
 public void testParseFragmentInvalidXml() {
 String xmlFragmentInput = "<element1 attr1='value1'><element2/><element3> <-- this tag is not closed";
 xmlTreeBuilder.parseFragment(xmlFragmentInput, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 // Test process method with start tag
 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root");
 xmlTreeBuilder.process(startTagToken);
 Element rootElement = new Element("root", "baseUrl", null);
 assertEquals(rootElement, xmlTreeBuilder.currentElement());
 }

 // Test process method with end tag
 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.process(endTagToken);
 assertNull(xmlTreeBuilder.currentElement());
 }

 // Test process method with character
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = new TextNode("A", "baseUrl");
 assertEquals(textNode, xmlTreeBuilder.currentElement());
 }

 // Test process method with comment
 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.process(commentToken);
 Comment comment = new Comment("This is a comment", "baseUrl");
 assertEquals(comment, xmlTreeBuilder.currentElement());
 }

 // Test process method with doctype
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = new DocumentType("html", null, null, "baseUrl");
 assertEquals(doctypeNode, xmlTreeBuilder.currentElement());
 }

 // Test popStackToClose method with element on stack
 @Test
 public void testPopStackToCloseWithElementOnStack() {
 xmlTreeBuilder.insert(new Token.StartTag("root"));
 xmlTreeBuilder.insert(new Token.StartTag("element1"));
 Token endTagToken = new Token.EndTag("element1");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeBuilder.currentElement());
 }

 // Test popStackToClose method with no element on stack
 @Test
 public void testPopStackToCloseWithNoElementOnStack() {
 xmlTreeBuilder.insert(new Token.StartTag("root"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("element1"));
 assertNull(xmlTreeBuilder.currentElement());
 }
}