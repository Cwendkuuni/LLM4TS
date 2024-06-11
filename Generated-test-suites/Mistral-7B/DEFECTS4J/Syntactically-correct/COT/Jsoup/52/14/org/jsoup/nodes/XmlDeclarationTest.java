package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;

 @Test
 public void testXmlDeclarationConstructor() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertNull(xmlDeclaration.name());
 Assert.assertEquals("", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertNull(xmlDeclaration.name());
 Assert.assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationConstructorWithName() {
 xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 Assert.assertEquals("<?xml>\n<!DOCTYPE html SYSTEM \"about:blank\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("XML", "http://example.com", true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("XML", xmlDeclaration.name());
 Assert.assertEquals("<?XML version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html SYSTEM \"about:blank\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationConstructorWithAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("version", "1.1");
 attributes.put("encoding", "UTF-16");
 xmlDeclaration = new XmlDeclaration("xml", null, false, attributes);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals("xml", xmlDeclaration.name());
 Assert.assertEquals("<?xml version=\"1.1\" encoding=\"UTF-16\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationNullName() {
 Validate.isNull(xmlDeclaration = new XmlDeclaration(null, null, false));
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationNullBaseUri() {
 Validate.isNull(xmlDeclaration = new XmlDeclaration("xml", null, false));
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationNullIsProcessingInstruction() {
 Validate.isNull(xmlDeclaration = new XmlDeclaration("xml", null, null));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testNodeName() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());

 xmlDeclaration = new XmlDeclaration("XML", null, true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testName() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 Assert.assertNull(xmlDeclaration.name());

 xmlDeclaration = new XmlDeclaration("XML", null, true);
 Assert.assertEquals("XML", xmlDeclaration.name());
 }

 @Test
 public void testGetWholeDeclaration() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 Assert.assertEquals("", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", null, true);
 Assert.assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());

 Attributes attributes = new Attributes();
 attributes.put("version", "1.1");
 attributes.put("encoding", "UTF-16");
 xmlDeclaration = new XmlDeclaration("xml", null, false, attributes);
 Assert.assertEquals("<?xml version=\"1.1\" encoding=\"UTF-16\"?>", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("XML", null, true, attributes);
 Assert.assertEquals("<?XML version=\"1.1\" encoding=\"UTF-16\"?>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document document = new Document();
 Appendable appendable = document.outputStream();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 String expected = "<?xml>\n";
 assertEquals(expected, appendable.toString());

 xmlDeclaration = new XmlDeclaration("xml", null, true);
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 expected = "<?xml>\n";
 assertEquals(expected, appendable.toString());

 xmlDeclaration = new XmlDeclaration("XML", null, false, new Attributes().put("version", "1.1"));
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 expected = "<?xml version=\"1.1\">\n";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Document document = new Document();
 Appendable appendable = document.outputStream();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlTail(appendable, 0, null);
 String expected = "";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testToString() throws IOException {
 Document document = new Document();
 Appendable appendable = document.outputStream();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 xmlDeclaration.outerHtmlTail(appendable, 0, null);
 String expected = "<?xml>";
 assertEquals(expected, xmlDeclaration.toString());

 xmlDeclaration = new XmlDeclaration("xml", null, true);
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 xmlDeclaration.outerHtmlTail(appendable, 0, null);
 expected = "<?xml>";
 assertEquals(expected, xmlDeclaration.toString());

 xmlDeclaration = new XmlDeclaration("XML", null, false, new Attributes().put("version", "1.1"));
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 xmlDeclaration.outerHtmlTail(appendable, 0, null);
 expected = "<?XML version=\"1.1\"?>";
 assertEquals(expected, xmlDeclaration.toString());
 }
}