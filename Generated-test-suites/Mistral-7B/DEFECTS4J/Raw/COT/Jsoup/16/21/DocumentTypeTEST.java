package org.jsoup.nodes.test;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 private DocumentType createDocumentType(String name, String publicId, String systemId, String baseUri) {
 return new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType documentType = createDocumentType("html", "", "", "");
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testDocumentTypePublicId() {
 DocumentType documentType = createDocumentType("html", "publicId", "", "");
 assertEquals("publicId", documentType.attr("publicId"));
 }

 @Test
 public void testDocumentTypeSystemId() {
 DocumentType documentType = createDocumentType("html", "", "systemId", "");
 assertEquals("systemId", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeBaseUri() {
 DocumentType documentType = createDocumentType("html", "", "", "baseUri");
 assertEquals("baseUri", documentType.getBaseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeNullName() {
 DocumentType documentType = createDocumentType(null, "", "", "");
 // an IllegalArgumentException should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeEmptyName() {
 DocumentType documentType = createDocumentType("", "", "", "");
 // an IllegalArgumentException should be thrown
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType documentType = createDocumentType("html", "publicId", "systemId", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() {
 DocumentType documentType = createDocumentType("html", "publicId", "systemId", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithOnlyPublicId() {
 DocumentType documentType = createDocumentType("html", "publicId", "", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \">\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithOnlySystemId() {
 DocumentType documentType = createDocumentType("html", "", "systemId", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html SYSTEM systemId>\n", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType documentType = createDocumentType("html", "", "", "");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);
 assertNull(accum.toString());
 }

 @Test
 public void testDocumentTypeEquals() {
 DocumentType documentType1 = createDocumentType("html", "publicId", "systemId", "");
 DocumentType documentType2 = createDocumentType("html", "publicId", "systemId", "");
 assertEquals(documentType1, documentType2);
 }

 @Test
 public void testDocumentTypeHashCode() {
 DocumentType documentType1 = createDocumentType("html", "publicId", "systemId", "");
 DocumentType documentType2 = createDocumentType("html", "publicId", "systemId", "");
 assertEquals(documentType1.hashCode(), documentType2.hashCode());
 }

 @Test
 public void testDocumentTypeToString() {
 DocumentType documentType = createDocumentType("html", "publicId", "systemId", "");
 assertEquals("#doctype[name=html] [publicId] [systemId]", documentType.toString());
 }

 @Test
 public void testDocumentTypeCloneNode() {
 DocumentType originalDocumentType = createDocumentType("html", "publicId", "systemId", "");
 DocumentType clonedDocumentType = (DocumentType) originalDocumentType.cloneNode(true);
 assertEquals(originalDocumentType.nodeName(), clonedDocumentType.nodeName());
 assertEquals(originalDocumentType.toString(), clonedDocumentType.toString());
 }

 @Test
 public void testDocumentTypeAttributes() {
 DocumentType documentType = createDocumentType("html", "publicId", "systemId", "");
 Attributes attributes = documentType.attributes();
 assertNotNull(attributes);
 assertEquals(3, attributes.size());
 }

 @Test
 public void testDocumentTypeElementChild() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "", "");
 document.appendChild(documentType);
 Element childElement = document.getElementsByTagName("#text").iterator().next();
 assertNotNull(childElement);
 }

}