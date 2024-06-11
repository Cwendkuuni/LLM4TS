package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

 private Document createDocument() {
 return new Document();
 }

 @Test
 public void testDocumentTypeConstruction() {
 DocumentType documentType = new DocumentType("html", "HTML", "HTML", null);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeConstructionWithAttributes() {
 Document document = createDocument();
 DocumentType documentType = new DocumentType("html", "HTML", "HTML", null, document);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("HTML", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", "HTML", "HTML", null);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType("html", "HTML", "HTML", null);
 documentType.attr(DocumentType.NAME, "html");
 documentType.attr(DocumentType.PUBLIC_ID, "PUBLIC");
 documentType.attr(DocumentType.SYSTEM_ID, "HTML");

 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithNullName() {
 Document document = createDocument();
 try {
 new DocumentType(null, "HTML", "HTML", null, document);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithEmptyName() {
 Document document = createDocument();
 try {
 new DocumentType("", "HTML", "HTML", null, document);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithNullPublicId() {
 Document document = createDocument();
 try {
 new DocumentType("html", null, "HTML", null, document);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithEmptyPublicId() {
 Document document = createDocument();
 try {
 new DocumentType("html", "", "HTML", null, document);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithNullSystemId() {
 Document document = createDocument();
 try {
 new DocumentType("html", "HTML", null, null, document);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentTypeWithEmptySystemId() {
 Document document = createDocument();
 try {
 new DocumentType("html", "HTML", "", null, document);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}