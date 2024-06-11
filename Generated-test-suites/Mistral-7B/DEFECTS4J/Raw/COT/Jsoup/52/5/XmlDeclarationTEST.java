package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

 private String baseUri = "http://example.com";

 @Test
 public void testXmlDeclarationConstructor() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", baseUri, true);

 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("xml", xmlProcessingInstruction.name());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", baseUri, false, null, "1.0");
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", baseUri, true);
 XmlDeclaration xmlProcessingInstructionWithVersion = new XmlDeclaration("xml", baseUri, true, null, "1.0");

 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 assertEquals("xml version=\"1.0\"", xmlDeclarationWithVersion.getWholeDeclaration());
 assertEquals("<?xml", xmlProcessingInstruction.getWholeDeclaration());
 assertEquals("<?xml version=\"1.0\"", xmlProcessingInstructionWithVersion.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationNullName() {
 Validate.expectedException(NullPointerException.class, () -> new XmlDeclaration(null, baseUri, false));
 Validate.expectedException(NullPointerException.class, () -> new XmlDeclaration(null, baseUri, true));
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Document doc = new Document();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", baseUri, true);

 Element root = doc.createElement("root");
 root.appendChild(xmlDeclaration);
 root.appendChild(xmlProcessingInstruction);

 StringBuilder sb = new StringBuilder();
 doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlDeclaration.outerHtmlHead(sb, 0, doc.outputSettings());
 xmlProcessingInstruction.outerHtmlHead(sb, 0, doc.outputSettings());
 root.outerHtml(sb, 0, doc.outputSettings());

 assertEquals("<declaration xmlversion='1.0' encoding='UTF-8'?>\n<root>\n<?xml version=\"1.0\"?>\n</root>", sb.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Document doc = new Document();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", baseUri, true);

 Element root = doc.createElement("root");
 root.appendChild(xmlDeclaration);
 root.appendChild(xmlProcessingInstruction);

 StringBuilder sb = new StringBuilder();
 doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlDeclaration.outerHtmlTail(sb, 0, doc.outputSettings());
 xmlProcessingInstruction.outerHtmlTail(sb, 0, doc.outputSettings());
 root.outerHtml(sb, 0, doc.outputSettings());

 assertEquals("<declaration xmlversion='1.0' encoding='UTF-8'?>\n<root>\n</root>", sb.toString());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", baseUri, true);

 assertEquals("<declaration xmlversion='1.0' encoding='UTF-8'?>", xmlDeclaration.toString());
 assertEquals("<?xml", xmlProcessingInstruction.toString());
 }

 @Test(expected = IOException.class)
 public void testXmlDeclarationOuterHtmlHeadIOException() throws IOException {
 Document doc = new Document();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);

 StringBuilder sb = new StringBuilder();
 xmlDeclaration.outerHtmlHead(sb, 0, null);
 }
}