package org.jsoup.nodes.test;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private DocumentType createDocumentType(String name, String publicId, String systemId, String baseUri) {
 return new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = createDocumentType("html", "publicId", "systemId", "baseUri");
 assertNotNull(docType);
 }

 @Test
 public void testNodeName() {
 DocumentType docType = createDocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testOuterHtmlHead() {
 DocumentType docType = createDocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicId() {
 DocumentType docType = createDocumentType("html", "", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html>\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
 DocumentType docType = createDocumentType("html", "", "", "baseUri");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadWithNullAccumulator() {
 DocumentType docType = createDocumentType("html", "publicId", "systemId", "baseUri");
 docType.outerHtmlHead(null, 0, null);
 }

 @Test(expected = NullPointerException.class)
 public void testOuterHtmlHeadWithNullDocument() {
 DocumentType docType = createDocumentType("html", "publicId", "systemId", "baseUri");
 docType.outerHtmlHead(new StringBuilder(), -1, null);
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType docType = createDocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }
}