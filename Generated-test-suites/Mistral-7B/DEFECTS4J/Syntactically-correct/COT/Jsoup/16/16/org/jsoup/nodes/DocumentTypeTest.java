package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = mock(Document.class);
 }

 // Test constructor method
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals(document.baseUri(), documentType.baseUri());
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 assertEquals("#doctype", documentType.nodeName());
 }

 // Test outerHtmlHead method for normal case
 @Test
 public void testOuterHtmlHeadNormalCase() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\n", accum.toString());
 }

 // Test outerHtmlHead method for publicId is blank case
 @Test
 public void testOuterHtmlHeadPublicIdBlank() {
 DocumentType documentType = new DocumentType("html", "", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE htmlSYSTEM systemId>\n", accum.toString());
 }

 // Test outerHtmlHead method for systemId is blank case
 @Test
 public void testOuterHtmlHeadSystemIdBlank() {
 DocumentType documentType = new DocumentType("html", "publicId", "", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" >\n", accum.toString());
 }

 // Test outerHtmlHead method for both publicId and systemId are blank case
 @Test
 public void testOuterHtmlHeadBothBlank() {
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html>\n", accum.toString());
 }

 // Test outerHtmlTail method for empty implementation
 @Test
 public void testOuterHtmlTailEmptyImplementation() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }
}