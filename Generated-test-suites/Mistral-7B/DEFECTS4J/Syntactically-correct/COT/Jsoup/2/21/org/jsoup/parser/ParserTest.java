package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser class.
 */
public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test Title</title></head>" +
         "<body><h1>Test Heading</h1>" +
         "<textarea></textarea>" +
         "<img src=\"image.jpg\" alt=\"Test Image\"/>" +
         "<base href=\"base/\"/>" +
         "</body></html>";
 parser = new Parser(html, "http://test.com/", false);
 doc = parser.parse();
 }

 @Test
 public void testParse() {
 assertEquals("Test Title", doc.title());
 assertEquals("Test Heading", doc.select("h1").text());
 assertTrue(doc.select("textarea").isEmpty());
 assertFalse(doc.select("img[src='image.jpg']").isEmpty());
 assertNotNull(doc.selectBaseUri());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<textarea></textarea>";
 Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
 assertEquals("", fragmentDoc.title().text());
 assertTrue(fragmentDoc.body().select("textarea").isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalHtml() {
 String invalidHtml = "<html><body>"; // missing closing tag for head
 Parser invalidParser = new Parser(invalidHtml, "http://test.com/", false);
 invalidParser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidBaseUri() {
 String invalidBaseUri = null;
 String html = "<html><base href=\"" + invalidBaseUri + "\"/></html>";
 Parser invalidParser = new Parser(html, invalidBaseUri, false);
 invalidParser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseStartTag() {
 String startTagHtml = "<h1 class=\"test\" id=\"testId\">Test</h1>";
 Element h1 = new Element(Tag.H1, "http://test.com/");
 Attributes h1Attributes = new Attributes();
 h1Attributes.put(Attribute.createFromEncoded("class", "test"));
 h1Attributes.put(Attribute.createFromEncoded("id", "testId"));
 h1.setAttributes(h1Attributes);

 Parser startTagParser = new Parser(startTagHtml, "http://test.com/", false);
 Document startTagDoc = startTagParser.parse();
 Element parsedH1 = startTagDoc.selectFirst("h1");
 assertEquals(h1.tagName(), parsedH1.tagName());
 assertEquals(h1Attributes.toString(), parsedH1.attributes().toString());
 }

 @Test
 public void testParseEndTag() {
 String endTagHtml = "<html><body><h1>Test</h1></body></html>";
 Parser endTagParser = new Parser(endTagHtml, "http://test.com/", false);
 Document endTagDoc = endTagParser.parse();
 Element h1 = endTagDoc.selectFirst("h1");
 assertNotNull(h1);
 assertEquals(1, stack.size());
 assertEquals("html", stack.getLast().tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTag() {
 String invalidEndTagHtml = "<html><body><h1>Test</h1></h1></body></html>";
 Parser invalidEndTagParser = new Parser(invalidEndTagHtml, "http://test.com/", false);
 invalidEndTagParser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseComment() {
 String commentHtml = "<html><!-- Test Comment --></html>";
 Parser commentParser = new Parser(commentHtml, "http://test.com/", false);
 Document commentDoc = commentParser.parse();
 assertEquals(1, commentDoc.childNodes().size());
 assertEquals("<!-- Test Comment -->", commentDoc.childNodes().get(0).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXmlDecl() {
 String invalidXmlDeclHtml = "<html><xml:declare xmlns:xml=\"http://www.w3.org/2000/xmlns/\"/><body></body></html>";
 Parser invalidXmlDeclParser = new Parser(invalidXmlDeclHtml, "http://test.com/", false);
 invalidXmlDeclParser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseCdata() {
 String cdataHtml = "<html><![CDATA[Test CDATA]]></html>";
 Parser cdataParser = new Parser(cdataHtml, "http://test.com/", false);
 Document cdataDoc = cdataParser.parse();
 assertEquals(1, cdataDoc.childNodes().size());
 assertEquals("<![CDATA[Test CDATA]]>", cdataDoc.childNodes().get(0).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCdata() {
 String invalidCdataHtml = "<html><![CDATA[Test CDATA<html>]]></html>";
 Parser invalidCdataParser = new Parser(invalidCdataHtml, "http://test.com/", false);
 invalidCdataParser.parse();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseTextNode() {
 String textNodeHtml = "<html><body><p>Test Text Node</p></body></html>";
 Parser textNodeParser = new Parser(textNodeHtml, "http://test.com/", false);
 Document textNodeDoc = textNodeParser.parse();
 assertEquals("Test Text Node", textNodeDoc.select("p").text());
 }
}