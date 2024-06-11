package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
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

 // Test method for defaultSettings()
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test method for parse(Reader)
 @Test
 public void testParseReader() throws Exception {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Reader reader = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(reader, "http://example.com");
 Element tag = document.selectElements("tag").first();
 assertEquals("value", tag.attr("attr"));
 }

 // Test method for parse(String)
 @Test
 public void testParseString() throws Exception {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Document document = xmlTreeBuilder.parse(xmlString, "http://example.com");
 Element tag = document.selectElements("tag").first();
 assertEquals("value", tag.attr("attr"));
 }

 // Test method for initialiseParse(Reader, String, ParseErrorList, ParseSettings)
 @Test
 public void testInitialiseParse() {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Reader reader = new StringReader(xmlString);
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
 assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
 }

 // Test method for process(Token)
 @Test
 public void testProcessStartTag() {
 String xmlString = "<xml><tag attr=\"value\"></tag></xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 Element tag = xmlTreeBuilder.doc.selectElements("tag").first();
 assertEquals("tag", tag.nodeName());
 assertEquals("value", tag.attr("attr"));
 }

 // Test method for processEndTag() - edge case: no matching start tag
 @Test(expected = AssertionError.class)
 public void testProcessEndTagNoStartTag() {
 String xmlString = "<xml></xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.parse(new Token.EndTag("tag").asEndTag(), null);
 }

 // Test method for processEndTag()
 @Test
 public void testProcessEndTag() {
 String xmlString = "<xml><tag><subtag></subtag></tag></xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.parse(new Token.EndTag("tag").asEndTag(), null);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 // Test method for insertNode(Node)
 @Test
 public void testInsertNode() {
 String xmlString = "<xml></xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 Element root = xmlTreeBuilder.doc.selectElements("xml").first();
 Node node = new TextNode("Test");
 xmlTreeBuilder.insertNode(node);
 root.appendChild(node);
 assertEquals("Test", node.text());
 }

 // Test method for insert(Token.StartTag)
 @Test
 public void testInsertStartTag() {
 String xmlString = "<xml></xml>";
 Reader reader = new StringReader(xmlString);xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 Token startTag = new Token.StartTag("tag", new Attribute("attr", "value"));
 Element tag = xmlTreeBuilder.insert(startTag);
 assertEquals("tag", tag.nodeName());
 assertEquals("value", tag.attr("attr"));
 }

 // Test method for insert(Token.Comment)
 @Test
 public void testInsertComment() {
 String xmlString = "<xml><!-- Test --></xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 Token commentToken = new Token.Comment("Test");
 xmlTreeBuilder.insert(commentToken);
 assertEquals("Test", xmlTreeBuilder.doc.select("comment()").text());
 }

 // Test method for insert(Token.Character)
 @Test
 public void testInsertCharacter() {
 String xmlString = "<xml>&#x1F600;</xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 Token characterToken = new Token.Character("&#x1F600;");
 xmlTreeBuilder.insert(characterToken);
 assertEquals("\uD83C\uDF00", xmlTreeBuilder.doc.select("text()").text());
 }

 // Test method for insert(Token.Doctype)
 @Test
 public void testInsertDoctype() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"http://example.com/xml.dtd\">";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 Token doctypeToken = new Token.Doctype("xml", null, null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctypeNode = xmlTreeBuilder.doc.selectElement("!DOCTYPE");
 assertEquals("xml", doctypeNode.nodeName());
 assertEquals("http://example.com/xml.dtd", doctypeNode.systemIdentifier());
 }

 // Test method for popStackToClose(Token.EndTag) - edge case: no matching start tag on stack
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseNoStartTag() {
 String xmlString = "<xml></xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.parse(new Token.EndTag("tag").asEndTag(), null);
 }

 // Test method for popStackToClose(Token.EndTag)
 @Test
 public void testPopStackToClose() {
 String xmlString = "<xml><tag><subtag></subtag></tag></xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, null, null, null);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.parse(new Token.EndTag("tag").asEndTag(), null);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 // Test method for parseFragment(String, String, ParseErrorList, ParseSettings)
 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<tag attr=\"value\"></tag>";
 Document document = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element tag = document.selectElements("tag").first();
 assertEquals("value", tag.attr("attr"));
 }

 // Test method for parseFragment(String, String, ParseErrorList, ParseSettings) - edge case: empty fragment
 @Test
 public void testParseFragmentEmpty() throws Exception {
 Document document = xmlTreeBuilder.parseFragment("", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(0, document.childNodes().size());
 }

}