package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Nodes.Attribute;
import org.jsoup.Nodes.Document;
import org.jsoup.Nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * XmlDeclarationTest.java
 */
public class XmlDeclarationTest {

 private XmlDeclaration createXmlDeclaration(String name, String baseUri, boolean isProcessingInstruction) {
 String version = "1.0";
 String encoding = "UTF-8";
 Attribute attrVersion = new Attribute("version", version);
 Attribute attrEncoding = new Attribute("encoding", encoding);
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclaration.attributes().set(attrVersion);
 xmlDeclaration.attributes().set(attrEncoding);
 return xmlDeclaration;
 }

 @Test
 public void testXmlDeclarationConstructor() {
 // Basic test case
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());

 // Test with processing instruction
 XmlDeclaration processingInstructionXmlDeclaration = new XmlDeclaration("xml", null, true);
 assertNotNull(processingInstructionXmlDeclaration);
 assertEquals("#declaration", processingInstructionXmlDeclaration.nodeName());
 assertEquals("<?xml", processingInstructionXmlDeclaration.getWholeDeclaration());

 // Test with default name
 XmlDeclaration defaultNameXmlDeclaration = new XmlDeclaration(null, null, false);
 assertNotNull(defaultNameXmlDeclaration);
 assertNull(defaultNameXmlDeclaration.name());
 }

 @Test
 public void testNameMethod() {
 XmlDeclaration xmlDeclaration = createXmlDeclaration("xml", null, false);
 assertEquals("xml", xmlDeclaration.name());

 XmlDeclaration defaultNameXmlDeclaration = new XmlDeclaration(null, null, false);
 assertNull(defaultNameXmlDeclaration.name());
 }

 @Test
 public void testGetWholeDeclarationMethod() {
 // Basic test case
 XmlDeclaration xmlDeclaration = createXmlDeclaration("xml", null, false);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());

 // Test with empty attributes
 XmlDeclaration emptyAttributesXmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<?xml?>", emptyAttributesXmlDeclaration.getWholeDeclaration());

 // Test with only version attribute
 XmlDeclaration versionOnlyXmlDeclaration = new XmlDeclaration("xml", null, false);
 Attribute versionOnlyAttr = new Attribute("version", "1.1");
 versionOnlyXmlDeclaration.attributes().set(versionOnlyAttr);
 assertEquals("<?xml version=\"1.1\"?>", versionOnlyXmlDeclaration.getWholeDeclaration());

 // Test with both version and encoding attributes
 XmlDeclaration versionAndEncodingXmlDeclaration = new XmlDeclaration("xml", null, false);
 Attribute versionAttr = new Attribute("version", "1.2");
 Attribute encodingAttr = new Attribute("encoding", "UTF-16");
 versionAndEncodingXmlDeclaration.attributes().set(versionAttr);
 versionAndEncodingXmlDeclaration.attributes().set(encodingAttr);
 assertEquals("<?xml version=\"1.2\" encoding=\"UTF-16\"?>", versionAndEncodingXmlDeclaration.getWholeDeclaration());

 // Test with processing instruction
 XmlDeclaration processingInstructionXmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml?>", processingInstructionXmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationNameIsNull() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationBaseUriIsNull() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationIsProcessingInstructionNegative() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationIsProcessingInstructionZero() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, 0);
 }

 @Test
 public void testToStringMethod() {
 XmlDeclaration xmlDeclaration = createXmlDeclaration("xml", null, false);
 assertEquals(xmlDeclaration.outerHtml(), xmlDeclaration.toString());
 }
}