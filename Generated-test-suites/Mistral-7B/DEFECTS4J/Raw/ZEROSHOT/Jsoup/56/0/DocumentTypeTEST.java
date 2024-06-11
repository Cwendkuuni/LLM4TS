package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.ElementHelper;
import org.jsoup.helper.OutputSettings;
import org.jsoup.helper.Whitelist;

import org.junit.Test;

public class DocumentTypeTest {

 private Document createDocument(String html) {
 return new Document(html);
 }

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 DocumentType docType = new DocumentType("html", "publicId", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 DocumentType docType = new DocumentType("html", "", "systemId", "");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicAndSystemId() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeNodeName() {
 Document document = createDocument("<!DOCTYPE html>");
 Element documentElement = document.body();
 DocumentType docType = (DocumentType) documentElement;
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 Document document = createDocument("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">");
 DocumentType docType = (DocumentType) document.body();
 assertTrue(docType.has(DocumentType.PUBLIC_ID));
 assertTrue(docType.has(DocumentType.NAME));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeHasNoAttribute() {
 Document document = createDocument("<!DOCTYPE html>");
 DocumentType docType = (DocumentType) document.body();
 assertFalse(docType.has(DocumentType.PUBLIC_ID));
 assertFalse(docType.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = createDocument("<!DOCTYPE html>");
 DocumentType docType = (DocumentType) document.body();
 assertEquals("<!DOCTYPE html>", docType.toString());
 }

 @Test
 public void testDocumentTypeToStringWithPublicId() {
 Document document = createDocument("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">");
 DocumentType docType = (DocumentType) document.body();
 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">", docType.toString());
 }

 @Test
 public void testDocumentTypeToStringWithPublicAndSystemId() {
 Document document = createDocument("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
 DocumentType docType = (DocumentType) document.body();
 assertEquals("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", docType.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document document = createDocument("<!DOCTYPE html>");
 DocumentType docType = (DocumentType) document.body();
 OutputSettings out = new OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);
 StringWriter writer = new StringWriter();
 docType.outerHtmlHead(writer, 0, out);
 assertEquals("<!doctype html>", writer.toString());
 document.close();
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicId() throws IOException {
 Document document = createDocument("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">");
 DocumentType docType = (DocumentType) document.body();
 OutputSettings out = new OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);
 StringWriter writer = new StringWriter();
 docType.outerHtmlHead(writer, 0, out);
 assertEquals("<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">", writer.toString());
 document.close();
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicAndSystemId() throws IOException {
 Document document = createDocument("<!DOCTYPE html SYSTEM \"about:legacy-compat\" PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"DTD/xhtml1-transitional.dtd\">");
 DocumentType docType = (DocumentType) document.body();
 OutputSettings out = new OutputSettings();
 out.syntax(Document.OutputSettings.Syntax.html);
 StringWriter writer = new StringWriter();
 docType.outerHtmlHead(writer, 0, out);
 assertEquals("<!doctype html SYSTEM \"about:legacy-compat\" PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"DTD/xhtml1-transitional.dtd\">", writer.toString());
 document.close();
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = createDocument("<!DOCTYPE html>");
 DocumentType docType = (DocumentType) document.body();
 StringWriter writer = new StringWriter();
 docType.outerHtmlTail(writer, 0, new OutputSettings());
 assertEquals("", writer.toString());
 document.close();
 }

}