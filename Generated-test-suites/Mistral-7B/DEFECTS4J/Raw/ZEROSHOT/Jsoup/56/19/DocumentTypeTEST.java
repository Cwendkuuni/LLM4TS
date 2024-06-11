package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", null, null );
 document.appendChild( documentType );

 assertEquals( "#doctype", documentType.nodeName() );
 assertEquals( "html", documentType.attr( DocumentType.NAME ) );
 assertNull( documentType.attr( DocumentType.PUBLIC_ID ) );
 assertNull( documentType.attr( DocumentType.SYSTEM_ID ) );
 }

 @Test
 public void testDocumentTypeCreationWithAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "public", "public.dtd" );
 document.appendChild( documentType );

 assertEquals( "#doctype", documentType.nodeName() );
 assertEquals( "html", documentType.attr( DocumentType.NAME ) );
 assertEquals( "public", documentType.attr( DocumentType.PUBLIC_ID ) );
 assertEquals( "public.dtd", documentType.attr( DocumentType.SYSTEM_ID ) );
 }

 @Test
 public void testDocumentTypeOuterHtml() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "public", "public.dtd" );
 document.appendChild( documentType );

 String expected = "<!DOCTYPE html PUBLIC \"public.dtd\" SYSTEM>\n";
 assertEquals( expected, documentType.outerHtml() );
 }

 @Test
 public void testDocumentTypeOuterHtmlHtml5() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", null, null, null );
 document.appendChild( documentType );

 String expected = "<!doctype html>\n";
 assertEquals( expected, documentType.outerHtml() );
 }

 @Test
 public void testDocumentTypeOuterHtmlWithNoAttributes() throws Exception {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", null, null );
 document.appendChild( documentType );

 String expected = "<!DOCTYPE html>\n";
 assertEquals( expected, documentType.outerHtml() );
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "public", "public.dtd" );
 document.appendChild( documentType );

 assertTrue( documentType.has( DocumentType.PUBLIC_ID ) );
 assertTrue( documentType.has( DocumentType.SYSTEM_ID ) );
 assertFalse( documentType.has( "not-an-attribute" ) );
 }
}