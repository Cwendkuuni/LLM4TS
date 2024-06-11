package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class XmlDeclarationTest {

 private String baseUri = "http://example.com";
 private XmlDeclaration xmlDeclaration;

 @Test
 public void testXmlDeclarationConstructor() {
 xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals(baseUri, xmlDeclaration.baseUri());
 assertNull(xmlDeclaration.previousSibling());
 assertNull(xmlDeclaration.nextSibling());
 }

 @Test
 public void testXmlDeclarationConstructorWithProcessingInstruction() {
 xmlDeclaration = new XmlDeclaration("<?xml", baseUri, true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("<?xml", xmlDeclaration.name());
 assertEquals(baseUri, xmlDeclaration.baseUri());
 assertNull(xmlDeclaration.previousSibling());
 assertNull(xmlDeclaration.nextSibling());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNullName() {
 assertThrows(NullPointerException.class, () -> new XmlDeclaration(null, baseUri, false));
 }

 @Test
 public void testGetWholeDeclarationEmptyDeclaration() {
 xmlDeclaration = new XmlDeclaration("", baseUri, false);
 assertEquals("", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithVersion() {
 xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("<declaration version=\"1.0\"></declaration>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithEncoding() {
 xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<declaration encoding=\"UTF-8\"></declaration>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithVersionAndEncoding() {
 xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"></declaration>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationForProcessingInstruction() {
 xmlDeclaration = new XmlDeclaration("<?xml", baseUri, true);
 assertEquals("<?xml>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 Document.OutputSettings out = new Document.OutputSettings();
 xmlDeclaration.outerHtmlHead(accum, 0, out);
 assertEquals("<declaration xml:base=\"" + baseUri + "\"></declaration>", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 Document.OutputSettings out = new Document.OutputSettings();
 xmlDeclaration.outerHtmlTail(accum, 0, out);
 assertNull(accum.toString());
 }

 @Test
 public void testToString() throws IOException {
 Appendable accum = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 Document.OutputSettings out = new Document.OutputSettings();
 xmlDeclaration.outerHtml(accum, 0, out);
 assertEquals("<declaration xml:base=\"" + baseUri + "\"></declaration>", accum.toString());
 }
}