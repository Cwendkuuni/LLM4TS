package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test defaultSettings()
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test parse() with valid XML input
 @Test
 public void testParseValidXml() {
 String xmlInput = "<hello><world>Hello World</world></hello>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "baseUri");
 Element root = doc.rootElement();
 assertEquals("hello", root.tagName());
 assertEquals("baseUri", root.baseUri());
 }

 // Test parse() with invalid XML input (missing root element)
 @Test(expected = org.jsoup.parser.Parser.MalformedXmlException.class)
 public void testParseInvalidXmlMissingRoot() {
 String xmlInput = "<world>Hello World</world>";
 xmlTreeBuilder.parse(xmlInput, "baseUri");
 }

 // Test parse() with invalid XML input (missing closing tag)
 @Test(expected = org.jsoup.parser.Parser.MalformedXmlException.class)
 public void testParseInvalidXmlMissingClosingTag() {
 String xmlInput = "<hello><world>Hello World";
 xmlTreeBuilder.parse(xmlInput, "baseUri");
 }

 // Test parseFragment() with valid XML fragment
 @Test
 public void testParseFragmentValidXml() {
 String xmlFragment = "<tag attribute=\"value\"><subtag/></tag>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(3, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("tag", root.tagName());
 assertNull(root.baseUri());
 Element subtag = (Element) nodes.get(1);
 assertEquals("subtag", subtag.tagName());
 assertNull(subtag.baseUri());
 }

 // Test parseFragment() with invalid XML fragment (missing closing tag)
 @Test(expected = org.jsoup.parser.Parser.MalformedXmlException.class)
 public void testParseFragmentInvalidXmlMissingClosingTag() {
 String xmlFragment = "<tag><subtag/><subtag>";
 xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 // Test initialiseParse() with null input
 @Test(expected = NullPointerException.class)
 public void testInitialiseParseNullInput() {
 xmlTreeBuilder.initialiseParse(null, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 // Test initialiseParse() with empty input
 @Test
 public void testInitialiseParseEmptyInput() {
 String emptyInput = "";
 xmlTreeBuilder.initialiseParse(emptyInput, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(0, doc.childNodes().size());
 }

 // Test process() with null token
 @Test(expected = NullPointerException.class)
 public void testProcessNullToken() {
 xmlTreeBuilder.process(null);
 }

 // Test process() with unknown token
 @Test
 public void testProcessUnknownToken() {
 Token unknownToken = new Token(1, "unknown", null);
 xmlTreeBuilder.process(unknownToken);
 fail("Process unknown token should have failed");
 }

 // Test popStackToClose() with non-existent tag
 @Test
 public void testPopStackToCloseNonExistentTag() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("nonExistentTag"));
 assertEquals(0, stack.size());
 }

 // Test popStackToClose() with existing tag
 @Test
 public void testPopStackToCloseExistingTag() {
 String xmlInput = "<outer><inner></inner></outer>";
 Document doc = xmlTreeBuilder.parse(xmlInput, "baseUri");
 xmlTreeBuilder.popStackToClose(new Token.EndTag("inner"));
 assertEquals(1, stack.size());
 }
}