package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.xml.Xml;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Whitespace;
import org.jsoup.nodes.Document.OutputSettings;

import org.junit.Test;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );

 assertEquals( "#doctype", documentType.nodeName() );
 assertEquals( "html", documentType.attr( DocumentType.NAME ) );
 assertEquals( "DTD HTML", documentType.attr( DocumentType.SYSTEM_ID ) );
 assertEquals( "html.dtd", documentType.attr( DocumentType.PUBLIC_ID ) );
 assertEquals( document, documentType.parent() );
 }

 @Test
 public void testDocumentTypeCreationWithEmptyValues() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "", "", document );

 assertEquals( "#doctype", documentType.nodeName() );
 assertNull( documentType.attr( DocumentType.NAME ) );
 assertNull( documentType.attr( DocumentType.PUBLIC_ID ) );
 assertNull( documentType.attr( DocumentType.SYSTEM_ID ) );
 assertEquals( document, documentType.parent() );
 }

 @Test
 public void testDocumentTypeCreationWithNullBaseUri() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", null, null, null );

 assertNull( documentType.parent() );
 }

 @Test
 public void testDocumentTypeCreationWithAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document, "foo", "bar" );

 assertEquals( "#doctype", documentType.nodeName() );
 assertEquals( "html", documentType.attr( DocumentType.NAME ) );
 assertEquals( "DTD HTML", documentType.attr( DocumentType.SYSTEM_ID ) );
 assertEquals( "html.dtd", documentType.attr( DocumentType.PUBLIC_ID ) );
 assertEquals( "foo", documentType.attr( DocumentType.NAME ) );
 assertEquals( "bar", documentType.attr( DocumentType.SYSTEM_ID ) );
 assertEquals( document, documentType.parent() );
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document, "", "" );

 assertEquals( "#doctype", documentType.nodeName() );
 assertNull( documentType.attr( DocumentType.NAME ) );
 assertNull( documentType.attr( DocumentType.PUBLIC_ID ) );
 assertNull( documentType.attr( DocumentType.SYSTEM_ID ) );
 assertEquals( document, documentType.parent() );
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );

 assertEquals( "<!DOCTYPE html SYSTEM \"html.dtd\">", documentType.toString() );
 }

 @Test
 public void testDocumentTypeToStringWithPublicID() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document, "PUBLIC", "publicID" );

 assertEquals( "<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"html.dtd\">", documentType.toString() );
 }

 @Test
 public void testDocumentTypeToStringWithEmptyPublicID() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document, "PUBLIC", "" );

 assertEquals( "<!DOCTYPE html PUBLIC \"\" SYSTEM \"html.dtd\">", documentType.toString() );
 }

 @Test
 public void testDocumentTypeToStringWithEmptyName() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( null, "DTD HTML", "html.dtd", document );

 assertEquals( "<!DOCTYPE html SYSTEM \"html.dtd\">", documentType.toString() );
 }

 @Test
 public void testDocumentTypeToStringWithEmptySystemID() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", null, document );

 assertEquals( "<!DOCTYPE html PUBLIC \"DTD HTML\">", documentType.toString() );
 }

 @Test
 public void testDocumentTypeToStringWithEmptyNameAndSystemID() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( null, null, null, document );

 assertEquals( "<!DOCTYPE>", documentType.toString() );
 }

 @Test
 public void testDocumentTypeToStringWithQuirksMode() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 documentType.baseURI = "http://example.com";
 documentType.setQuirksMode( true );

 assertEquals( "<!DOCTYPE html SYSTEM \"html.dtd\">\n<![ATTLIST html xml:id ID #IMPLIED]\n<![IGNORE]\n<![CDATA[\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=emulated\"/>\n<body>\n<!]]>\n", documentType.outerHtml() );
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );

 assertTrue( documentType.has( DocumentType.NAME ) );
 assertTrue( documentType.has( DocumentType.PUBLIC_ID ) );
 assertTrue( documentType.has( DocumentType.SYSTEM_ID ) );
 }

 @Test
 public void testDocumentTypeHasNoAttribute() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "", "", document );

 assertFalse( documentType.has( DocumentType.NAME ) );
 assertFalse( documentType.has( DocumentType.PUBLIC_ID ) );
 assertFalse( documentType.has( DocumentType.SYSTEM_ID ) );
 }

 @Test
 public void testDocumentTypeWithSameAttributesAreEqual() {
 Document document = new Document();
 DocumentType documentType1 = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 DocumentType documentType2 = new DocumentType( "html", "DTD HTML", "html.dtd", document );

 assertEquals( documentType1, documentType2 );
 }

 @Test
 public void testDocumentTypeWithDifferentAttributesAreNotEqual() {
 Document document = new Document();
 DocumentType documentType1 = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 DocumentType documentType2 = new DocumentType( "html", "DTD HTML", "html.dtd", document, "foo", "bar" );

 assertNotSame( documentType1, documentType2 );
 }

 @Test
 public void testCreateDocumentTypeWithNullName() {
 Document document = new Document();
 try {
 new DocumentType( null, "DTD HTML", "html.dtd", document );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateDocumentTypeWithNullPublicID() {
 Document document = new Document();
 try {
 new DocumentType( "html", null, "html.dtd", document );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateDocumentTypeWithNullSystemID() {
 Document document = new Document();
 try {
 new DocumentType( "html", "DTD HTML", null, document );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateDocumentTypeWithNullBaseUri() {
 Document document = new Document();
 try {
 new DocumentType( "html", "DTD HTML", "html.dtd", null );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateDocumentTypeWithBlankName() {
 Document document = new Document();
 try {
 new DocumentType( "", "DTD HTML", "html.dtd", document );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateDocumentTypeWithBlankPublicID() {
 Document document = new Document();
 try {
 new DocumentType( "html", "", "html.dtd", document );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateDocumentTypeWithBlankSystemID() {
 Document document = new Document();
 try {
 new DocumentType( "html", "DTD HTML", "", document );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDocumentTypeIsChildOfDocument() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );

 assertSame( document, documentType.parent() );
 }

 @Test
 public void testDocumentTypeIsNotChildOfElement() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 Element element = new Element( "html" );

 assertFalse( element.getParentNode() instanceof DocumentType );
 }

 @Test
 public void testDocumentTypeIsNotChildOfTextNode() {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 TextNode textNode = new TextNode( "test" );

 assertFalse( textNode.getParentNode() instanceof DocumentType );
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 Appendable accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, OutputSettings.html4());

 assertEquals( "<!DOCTYPE html SYSTEM \"html.dtd\">", accum.toString() );
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 Appendable accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, OutputSettings.html4());

 assertEquals( "", accum.toString() );
 }

 @Test
 public void testDocumentTypeToStringWithCustomOutputSettings() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "html.dtd", document );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Xml );

 assertEquals( "<!DOCTYPE html SYSTEM \"html.dtd\">", documentType.toString( outputSettings ) );
 }

 @Test
 public void testDocumentTypeToStringWithEmptyPublicIdAndSystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "", "", document );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Html5 );

 assertEquals( "<!DOCTYPE html>", documentType.toString( outputSettings ) );
 }

 @Test
 public void testDocumentTypeToStringWithCustomPublicId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "PUBLIC \"My DTD\"", "html.dtd", document );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Html5 );

 assertEquals( "<!DOCTYPE html PUBLIC \"My DTD\">", documentType.toString( outputSettings ) );
 }

 @Test
 public void testDocumentTypeToStringWithCustomSystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "html", "DTD HTML", "custom.dtd", document );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Xml );

 assertEquals( "<!DOCTYPE html SYSTEM \"custom.dtd\">", documentType.toString( outputSettings ) );
 }

 @Test
 public void testDocumentTypeToStringWithCustomName() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "customName", "DTD HTML", "html.dtd", document );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Html5 );

 assertEquals( "<!DOCTYPE customName [ <!ATTLIST html xml:id ID #IMPLIED > ]>\n<!DOCTYPE html>", documentType.toString( outputSettings ) );
 }

 @Test
 public void testDocumentTypeToStringWithCustomNameAndPublicId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "customName", "PUBLIC \"My DTD\"", "html.dtd", document );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Html5 );

 assertEquals( "<!DOCTYPE customName PUBLIC \"My DTD\" [ <!ATTLIST html xml:id ID #IMPLIED > ]>\n<!DOCTYPE html>", documentType.toString( outputSettings ) );
 }

 @Test
 public void testDocumentTypeToStringWithCustomNameAndSystemId() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "customName", "DTD HTML", "custom.dtd", document );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Html5 );

 assertEquals( "<!DOCTYPE customName SYSTEM \"custom.dtd\">\n<!DOCTYPE html [ <!ATTLIST html xml:id ID #IMPLIED > ]>", documentType.toString( outputSettings ) );
 }

 @Test
 public void testDocumentTypeToStringWithCustomNameAndQuirksMode() throws IOException {
 Document document = new Document();
 DocumentType documentType = new DocumentType( "customName", "DTD HTML", "html.dtd", document );
 documentType.baseURI = "http://example.com";
 documentType.setQuirksMode( true );
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.syntax( OutputSettings.Html5 );

 assertEquals( "<!DOCTYPE customName SYSTEM \"html.dtd\">\n<![ATTLIST html xml:id ID #IMPLIED]\n<![IGNORE]\n<![CDATA[\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=emulated\"/>\n<body>\n<!]]>", documentType.toString( outputSettings ) );
 }
}