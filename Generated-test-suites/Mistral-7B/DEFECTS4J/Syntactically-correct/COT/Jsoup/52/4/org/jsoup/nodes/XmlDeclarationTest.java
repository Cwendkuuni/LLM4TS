package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

public class XmlDeclarationTest {

 private String baseUri = "http://example.com";
 private String name = "xml";
 private String version = "1.0";
 private String encoding = "UTF-8";
 private Attributes attributes;

 @Test
 public void testXmlDeclarationConstructor() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals(name, xmlDeclaration.name());
 Assert.assertNull(xmlDeclaration.getWholeDeclaration());

 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration(name, baseUri, true);
 Assert.assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 Assert.assertEquals(name, xmlProcessingInstruction.name());
 Assert.assertTrue(xmlProcessingInstruction.getWholeDeclaration().startsWith("<?xml"));
 }

 @Test
 public void testGetWholeDeclaration() {
 attributes = new Attributes();
 attributes.put("version", version);
 attributes.put("encoding", encoding);

 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
 xmlDeclaration.setAttributes(attributes);
 Assert.assertEquals(name, xmlDeclaration.getWholeDeclaration());

 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration(name, baseUri, false);
 xmlDeclarationWithVersion.setAttributes(attributes);
 Assert.assertEquals("<?xml version=\"" + version + "\" encoding=\"" + encoding + "\"", xmlDeclarationWithVersion.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationConstructorWithNullName() {
 Validate.notNull("name should not be null", name);
 String nullName = null;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(nullName, baseUri, false);
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadThrowsIOException() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
 Appendable accum = new StringBuilder();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
 Assert.assertEquals(xmlDeclaration.toString(), xmlDeclaration.outerHtml());
 }

}