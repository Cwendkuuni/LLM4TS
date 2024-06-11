package org.jsoup.nodes.test;

import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class XmlDeclarationTest {

 private String baseUri = "http://example.com";

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals("xml", xmlDeclaration.name());
 Assert.assertFalse(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, true);
 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals("xml", xmlDeclaration.name());
 Assert.assertTrue(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testGetWholeDeclarationForXmlRootDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 Assert.assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationForXmlDeclarationWithNoAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-no-attributes", baseUri, false);
 Assert.assertEquals("xml-no-attributes", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationForXmlDeclarationWithVersionAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 xmlDeclaration.attributes().put("version", "1.1");
 Assert.assertEquals("<xml version=\"1.1\"></xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationForXmlDeclarationWithEncodingAttribute() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 xmlDeclaration.attributes().put("encoding", "UTF-16");
 Assert.assertEquals("<xml encoding=\"UTF-16\"></xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationForXmlDeclarationWithVersionAndEncodingAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 xmlDeclaration.attributes().put("version", "1.1");
 xmlDeclaration.attributes().put("encoding", "UTF-16");
 Assert.assertEquals("<xml version=\"1.1\" encoding=\"UTF-16\"></xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationCreationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, false);
 // should throw a NullPointerException
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 Assert.assertEquals(xmlDeclaration.getWholeDeclaration(), xmlDeclaration.toString());
 }
}