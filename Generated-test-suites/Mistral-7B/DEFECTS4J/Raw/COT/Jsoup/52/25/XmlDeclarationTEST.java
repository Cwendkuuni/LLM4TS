package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;

import org.junit.Test;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

 private final String BASE_URI = "baseUri";
 private final String XML_DECLARATION_NAME = "xml";
 private final String XML_DECLARATION_VERSION = "1.0";
 private final String XML_DECLARATION_ENCODING = "UTF-8";

 @Test
 public void testXmlDeclarationConstructor() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, true);

 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(XML_DECLARATION_NAME, xmlDeclaration.name());
 assertEquals(BASE_URI, xmlDeclaration.baseUri());

 assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 assertEquals(XML_DECLARATION_NAME, xmlProcessingInstruction.name());
 assertEquals(BASE_URI, xmlProcessingInstruction.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNullName() {
 assertThrows(NullPointerException.class, () -> new XmlDeclaration(null, BASE_URI, false));
 }

 @Test
 public void testGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false);
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes().put("version", XML_DECLARATION_VERSION));
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes().put("encoding", XML_DECLARATION_ENCODING));
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes()
                 .put("version", XML_DECLARATION_VERSION)
                 .put("encoding", XML_DECLARATION_ENCODING));

 assertEquals(XML_DECLARATION_NAME, xmlDeclaration.name());
 assertEquals(XML_DECLARATION_NAME + " \n", xmlDeclaration.getWholeDeclaration());

 assertEquals(XML_DECLARATION_NAME + " version=\"" + XML_DECLARATION_VERSION + "\" \n", xmlDeclarationWithVersion.getWholeDeclaration());
 assertEquals(XML_DECLARATION_NAME + " encoding=\"" + XML_DECLARATION_ENCODING + "\" \n", xmlDeclarationWithEncoding.getWholeDeclaration());
 assertEquals(XML_DECLARATION_NAME + " version=\"" + XML_DECLARATION_VERSION + "\" encoding=\"" + XML_DECLARATION_ENCODING + "\" \n", xmlDeclarationWithVersionAndEncoding.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWholeDeclarationWithNameNotXml() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("notXml", BASE_URI, false);
 assertThrows(IllegalArgumentException.class, () -> xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithOneAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes().put("version", XML_DECLARATION_VERSION));
 assertEquals(XML_DECLARATION_NAME + " version=\"" + XML_DECLARATION_VERSION + "\" \n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithTwoAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes()
                 .put("version", XML_DECLARATION_VERSION)
                 .put("encoding", XML_DECLARATION_ENCODING));
 assertEquals(XML_DECLARATION_NAME + " version=\"" + XML_DECLARATION_VERSION + "\" encoding=\"" + XML_DECLARATION_ENCODING + "\" \n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false);
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes().put("version", XML_DECLARATION_VERSION));
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes().put("encoding", XML_DECLARATION_ENCODING));
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, false)
         .attributes(new Attributes()
                 .put("version", XML_DECLARATION_VERSION)
                 .put("encoding", XML_DECLARATION_ENCODING));

 assertEquals("<xml>\n", xmlDeclaration.outerHtml());
 assertEquals("<xml version=\"" + XML_DECLARATION_VERSION + "\">\n", xmlDeclarationWithVersion.outerHtml());
 assertEquals("<xml encoding=\"" + XML_DECLARATION_ENCODING + "\">\n", xmlDeclarationWithEncoding.outerHtml());
 assertEquals("<xml version=\"" + XML_DECLARATION_VERSION + "\" encoding=\"" + XML_DECLARATION_ENCODING + "\">\n", xmlDeclarationWithVersionAndEncoding.outerHtml());
 }

 @Test
 public void testToStringProcessingInstruction() {
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration(XML_DECLARATION_NAME, BASE_URI, true);
 assertEquals("<?xml>\n", xmlProcessingInstruction.outerHtml());
 }
}