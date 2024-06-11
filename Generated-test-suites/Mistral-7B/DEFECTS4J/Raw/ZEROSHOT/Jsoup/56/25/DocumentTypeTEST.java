package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DocumentTypeTest {

 private Document document;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 document = mock(Document.class);
 }

 @Test
 public void testCreateDocumentType() throws IOException {
 String name = "html";
 String publicId = "publicid";
 String systemId = "systemid";
 String baseUri = "baseUri";

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 Attributes attributes = documentType.attributes();
 assertEquals(name, attributes.get(DocumentType.NAME));
 assertEquals(publicId, attributes.get(DocumentType.PUBLIC_ID));
 assertEquals(systemId, attributes.get(DocumentType.SYSTEM_ID));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 OutputSettings out = new OutputSettings().syntax(OutputSettings.Syntax.html);
 DocumentType documentType = new DocumentType("html", null, null, null);

 Appendable appendable = mock(Appendable.class);

 documentType.outerHtmlHead(appendable, 0, out);

 String expected = "<!doctype html>";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicId() throws IOException {
 OutputSettings out = new OutputSettings().syntax(OutputSettings.Syntax.html);
 DocumentType documentType = new DocumentType("html", "publicid", null, null);

 Appendable appendable = mock(Appendable.class);

 documentType.outerHtmlHead(appendable, 0, out);

 String expected = "<!DOCTYPE html PUBLIC \"publicid\" >";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testOuterHtmlHeadWithSystemId() throws IOException {
 OutputSettings out = new OutputSettings().syntax(OutputSettings.Syntax.html);
 DocumentType documentType = new DocumentType("html", null, "systemid", null);

 Appendable appendable = mock(Appendable.class);

 documentType.outerHtmlHead(appendable, 0, out);

 String expected = "<!DOCTYPE html SYSTEM \"systemid\" >";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testOuterHtmlHeadWithNamePublicIdAndSystemId() throws IOException {
 OutputSettings out = new OutputSettings().syntax(OutputSettings.Syntax.html);
 DocumentType documentType = new DocumentType("html", "publicid", "systemid", null);

 Appendable appendable = mock(Appendable.class);

 documentType.outerHtmlHead(appendable, 0, out);

 String expected = "<!DOCTYPE html PUBLIC \"publicid\" \"systemid\" >";
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 DocumentType documentType = new DocumentType("html", null, null, null);

 Appendable appendable = mock(Appendable.class);
 documentType.outerHtmlTail(appendable, 0, null);

 assertEquals("", appendable.toString());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType("html", "publicid", "systemid", null);

 assertEquals(true, documentType.has(DocumentType.NAME));
 assertEquals(true, documentType.has(DocumentType.PUBLIC_ID));
 assertEquals(true, documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasAttributeWithNullAttribute() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals(false, documentType.has(null));
 }

 @Test
 public void testCreateTextNodeWithDocumentTypeAsParent() throws IOException {
 DocumentType documentType = new DocumentType("html", null, null, null);
 Element parentElement = mock(Element.class);
 TextNode textNode = new TextNode("text", documentType);

 when(parentElement.appendChild(Mockito.any(Node.class))).thenReturn(textNode);

 Element parent = (Element) document.createElement("parent");
 parent.appendChild(documentType);
 parent.appendChild(textNode);

 assertEquals(documentType, textNode.parent());
 }

 @Test
 public void testToString() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertEquals("<html>", documentType.toString());
 }
}