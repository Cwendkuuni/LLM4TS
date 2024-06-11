package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());

 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false);
 xmlDeclarationWithVersion.attributes().put("version", "1.0");
 assertEquals("xml version=\"1.0\"", xmlDeclarationWithVersion.getWholeDeclaration());

 XmlDeclaration processingInstruction = new XmlDeclaration("--", null, true);
 assertEquals("#processing-instruction", processingInstruction.nodeName());
 assertEquals("--", processingInstruction.name());
 assertNotNull(processingInstruction.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationOuterHtml() throws Exception {
 Document document = Jsoup.parse("<html><head><xml-declaration/></head><body></body></html>");
 Element xmlDeclarationElement = document.head().child(0);
 XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDeclarationElement;

 StringBuilder actual = new StringBuilder();
 xmlDeclaration.outerHtmlHead(actual, 0, null);
 xmlDeclaration.outerHtmlTail(actual, 0, null);
 String expected = xmlDeclaration.getWholeDeclaration();
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testXmlDeclarationToString() throws Exception {
 Document document = Jsoup.parse("<html><head><xml-declaration/></head><body></body></html>");
 Element xmlDeclarationElement = document.head().child(0);
 XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDeclarationElement;
 String actual = xmlDeclaration.toString();
 String expected = xmlDeclaration.getWholeDeclaration();
 assertEquals(expected, actual);
 }
}