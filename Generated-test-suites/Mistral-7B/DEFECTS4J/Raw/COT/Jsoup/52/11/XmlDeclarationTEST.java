package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import static org.jsoup.helper.Validate.*;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;

 @Test
 public void testXmlDeclarationConstructor() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<xml version=\"\" encoding=\"\" >", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testEmptyXmlDeclarationConstructor() {
 xmlDeclaration = new XmlDeclaration(null, null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertNull(xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNullNameConstructor() {
 xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test
 public void testXmlDeclarationWithAttributes() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\" >", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyAttributes() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().clear();
 assertEquals("<xml version=\"\" encoding=\"\" >", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithNameOtherThanXml() {
 xmlDeclaration = new XmlDeclaration("myxml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("myxml", xmlDeclaration.name());
 assertEquals("<myxml version=\"\" encoding=\"\" >", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 xmlDeclaration = new XmlDeclaration(null, null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertNull(xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationWithInvalidName() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.name = "INVALID_NAME";
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 Document.OutputSettings out = new Document.OutputSettings();
 xmlDeclaration.outerHtmlHead(accum, 0, out);
 assertEquals("<?xml>", accum.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 Document.OutputSettings out = new Document.OutputSettings();
 xmlDeclaration.outerHtmlTail(accum, 0, out);
 assertEquals("", accum.toString());
 }

 @Test
 public void testXmlDeclarationToString() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml version=\"\" encoding=\"\" >", xmlDeclaration.toString());
 }

}