package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", null );
 doc.appendChild( documentType );

 assertEquals( "#doctype", documentType.nodeName() );
 assertEquals( "html", documentType.attr( DocumentType.NAME ) );
 assertTrue( documentType.has( DocumentType.PUBLIC_ID ) );
 assertEquals( "HTML", documentType.attr( DocumentType.PUBLIC_ID ) );
 assertTrue( documentType.has( DocumentType.SYSTEM_ID ) );
 assertEquals( "http://www.w3.org/TR/html4/strict.dtd", documentType.attr( DocumentType.SYSTEM_ID ) );
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithEmptyName() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "", "HTML", "http://www.w3.org/TR/html4/strict.dtd", null );
 doc.appendChild( documentType );
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithNullName() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( null, "HTML", "http://www.w3.org/TR/html4/strict.dtd", null );
 doc.appendChild( documentType );
 }

 @Test
 public void testDocumentTypeCreationWithEmptyPublicId() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", "", "http://www.w3.org/TR/html4/strict.dtd", null );
 doc.appendChild( documentType );

 assertFalse( documentType.has( DocumentType.PUBLIC_ID ) );
 }

 @Test
 public void testDocumentTypeCreationWithNullPublicId() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", null, "http://www.w3.org/TR/html4/strict.dtd", null );
 doc.appendChild( documentType );

 assertFalse( documentType.has( DocumentType.PUBLIC_ID ) );
 }

 @Test
 public void testDocumentTypeCreationWithEmptySystemId() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", "HTML", "", null );
 doc.appendChild( documentType );

 assertFalse( documentType.has( DocumentType.SYSTEM_ID ) );
 }

 @Test
 public void testDocumentTypeCreationWithNullSystemId() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", "HTML", null, null );
 doc.appendChild( documentType );

 assertFalse( documentType.has( DocumentType.SYSTEM_ID ) );
 }

 @Test
 public void testDocumentTypeCreationWithBaseUri() {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com" );
 doc.appendChild( documentType );

 assertEquals( "http://example.com", documentType.baseUri() );
 }

 @Test
 public void testDocumentTypeOuterHtml() throws Exception {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", null );
 doc.appendChild( documentType );
 Element rootElement = new Element("html");
 doc.appendChild( rootElement );
 Attributes rootAttributes = rootElement.attributes();
 rootAttributes.put("xmlns:html", "http://www.w3.org/1999/xhtml");
 doc.outputSettings( Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html5) );
 String outerHtml = documentType.outerHtml();

 assertEquals( "<!DOCTYPE html PUBLIC \"HTML\" \"http://www.w3.org/TR/html4/strict.dtd\">", outerHtml );
 }

 @Test
 public void testDocumentTypeOuterHtmlWithQuirksMode() throws Exception {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", null );
 doc.appendChild( documentType );
 Element rootElement = new Element("html");
 doc.appendChild( rootElement );
 Attributes rootAttributes = rootElement.attributes();
 rootAttributes.put("xmlns:html", "http://www.w3.org/1999/xhtml");
 Document.OutputSettings out = new Document.OutputSettings();
 out.syntax( Document.OutputSettings.Syntax.quirks );
 String outerHtml = documentType.outerHtml();

 assertEquals( "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", outerHtml );
 }

 @Test
 public void testDocumentTypeOuterHtmlWithoutPublicIdAndSystemId() throws Exception {
 Document doc = new Document();
 DocumentType documentType = new DocumentType( "html5", null, null, null );
 doc.appendChild( documentType );
 Element rootElement = new Element("html");
 doc.appendChild( rootElement );
 String outerHtml = documentType.outerHtml();

 assertEquals( "<!doctype html>", outerHtml );
 }
}