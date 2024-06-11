package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

public class DocumentTypeTest {

 private static final String VALID_NAME = "html";
 private static final String VALID_PUBLIC_ID = "public.id";
 private static final String VALID_SYSTEM_ID = "system.id";
 private static final String EMPTY_NAME = "";
 private static final String EMPTY_PUBLIC_ID = "";
 private static final String EMPTY_SYSTEM_ID = "";

 @Test
 public void testDocumentTypeConstructor() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(VALID_NAME, VALID_PUBLIC_ID, VALID_SYSTEM_ID, doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertEquals(VALID_NAME, doctype.attr(DocumentType.NAME));
 assertEquals(VALID_PUBLIC_ID, doctype.attr(DocumentType.PUBLIC_ID));
 assertEquals(VALID_SYSTEM_ID, doctype.attr(DocumentType.SYSTEM_ID));
 assertNotNull(doctype.outerHtml());
 }

 @Test
 public void testDocumentTypeConstructorWithEmptyAttributes() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(EMPTY_NAME, EMPTY_PUBLIC_ID, EMPTY_SYSTEM_ID, doc.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(DocumentType.NAME));
 assertNull(doctype.attr(DocumentType.PUBLIC_ID));
 assertNull(doctype.attr(DocumentType.SYSTEM_ID));
 assertNotNull(doctype.outerHtml());
 }

 @Test
 public void testOuterHtmlHeadWithHtmlSyntax() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(VALID_NAME, VALID_PUBLIC_ID, VALID_SYSTEM_ID, doc.baseUri());

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder output = new StringBuilder();
 doctype.outerHtmlHead(output, 0, out);

 assertEquals("<!doctype " + VALID_NAME, output.toString());
 }

 @Test
 public void testOuterHtmlHeadWithXmlSyntax() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(VALID_NAME, VALID_PUBLIC_ID, VALID_SYSTEM_ID, doc.baseUri());

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.xml);

 StringBuilder output = new StringBuilder();
 doctype.outerHtmlHead(output, 0, out);

 assertEquals("<!DOCTYPE " + VALID_NAME, output.toString());
 }

 @Test
 public void testOuterHtmlHeadWithoutPublicIdAndSystemId() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(VALID_NAME, EMPTY_PUBLIC_ID, EMPTY_SYSTEM_ID, doc.baseUri());

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder output = new StringBuilder();
 doctype.outerHtmlHead(output, 0, out);

 assertEquals("<!doctype " + VALID_NAME, output.toString());
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadIOException() throws Exception {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(VALID_NAME, VALID_PUBLIC_ID, VALID_SYSTEM_ID, doc.baseUri());

 OutputSettings out = new OutputSettings();
 out.syntax(OutputSettings.Syntax.html);

 StringBuilder output = new StringBuilder();
 doctype.outerHtmlHead(output, 0, out);

 // This test should throw an IOException since there is no appendable provided
 }

 @Test
 public void testHasPublicId() {
 DocumentType doctype = new DocumentType(VALID_NAME, VALID_PUBLIC_ID, VALID_SYSTEM_ID, null);
 assertTrue(doctype.has(DocumentType.PUBLIC_ID));
 }

 @Test
 public void testHasSystemId() {
 DocumentType doctype = new DocumentType(VALID_NAME, VALID_PUBLIC_ID, VALID_SYSTEM_ID, null);
 assertTrue(doctype.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasEmptyPublicId() {
 DocumentType doctype = new DocumentType(VALID_NAME, EMPTY_PUBLIC_ID, VALID_SYSTEM_ID, null);
 assertFalse(doctype.has(DocumentType.PUBLIC_ID));
 }

 @Test
 public void testHasEmptySystemId() {
 DocumentType doctype = new DocumentType(VALID_NAME, VALID_PUBLIC_ID, EMPTY_SYSTEM_ID, null);
 assertFalse(doctype.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testHasNoPublicIdNorSystemId() {
 DocumentType doctype = new DocumentType(VALID_NAME, EMPTY_PUBLIC_ID, EMPTY_SYSTEM_ID, null);
 assertFalse(doctype.has(DocumentType.PUBLIC_ID));
 assertFalse(doctype.has(DocumentType.SYSTEM_ID));
 }
}