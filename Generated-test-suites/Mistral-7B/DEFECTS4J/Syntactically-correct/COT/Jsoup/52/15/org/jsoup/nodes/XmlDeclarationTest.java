package org.jsoup.nodes.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class XmlDeclarationTest {

 private static final String XML_DECLARATION_NAME = "xml";
 private static final String XML_DECLARATION_VERSION = "1.0";
 private static final String XML_DECLARATION_ENCODING = "UTF-8";
 private static final String XML_DECLARATION_WITH_VERSION_AND_ENCODING = "<?xml version=\"" + XML_DECLARATION_VERSION + "\" encoding=\"" + XML_DECLARATION_ENCODING + "\">";
 private static final String XML_DECLARATION_EMPTY = "<?xml>";

 @Test
 public void testXmlDeclarationConstructor() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals(XML_DECLARATION_NAME, xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationConstructorWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", XML_DECLARATION_VERSION);
 xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false, attributes);
 String expected = XML_DECLARATION_WITH_VERSION_AND_ENCODING;
 String actual = xmlDeclaration.getWholeDeclaration();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testXmlDeclarationConstructorWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 Attributes attributes = new Attributes();
 attributes.put("encoding", XML_DECLARATION_ENCODING);
 xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false, attributes);
 String expected = XML_DECLARATION_WITH_VERSION_AND_ENCODING;
 String actual = xmlDeclaration.getWholeDeclaration();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testXmlDeclarationConstructorWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", XML_DECLARATION_VERSION);
 attributes.put("encoding", XML_DECLARARION_ENCODING);
 xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false, attributes);
 String expected = XML_DECLARATION_WITH_VERSION_AND_ENCODING;
 String actual = xmlDeclaration.getWholeDeclaration();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testXmlDeclarationConstructorWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, true);
 Assert.assertTrue(xmlDeclaration.isProcessingInstruction);
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationConstructorWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", null, false);
 Assert.assertEquals("", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationEmpty() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 Assert.assertEquals(XML_DECLARATION_EMPTY, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 String expected = xmlDeclaration.getWholeDeclaration();
 String actual = xmlDeclaration.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 Document document = new Document();
 Appendable accumulator = document.createAppender("UTF-8");
 xmlDeclaration.outerHtmlHead(accumulator, 0, document.outputSettings());
 String expected = XML_DECLARATION_WITH_VERSION_AND_ENCODING;
 String actual = accumulator.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testXmlDeclarationOuterHtmlHeadWithNullAccumulator() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 xmlDeclaration.outerHtmlHead(null, 0, null);
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationOuterHtmlHeadWithNullDocSettings() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(XML_DECLARATION_NAME, null, false);
 Document document = new Document();
 xmlDeclaration.outerHtmlHead(document.createAppender("UTF-8"), 0, null);
 }

}