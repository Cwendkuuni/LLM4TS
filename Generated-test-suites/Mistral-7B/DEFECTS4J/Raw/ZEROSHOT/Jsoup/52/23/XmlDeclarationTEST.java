package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;

 @org.junit.Before
 public void setUp() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 }

 @Test
 public void testXmlDeclarationName() {
 assertEquals(xmlDeclaration.name(), "xml");
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationEmpty() throws IOException {
 assertEquals(xmlDeclaration.getWholeDeclaration(), "xml");
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersion() throws IOException {
 StringBuilder mockAttributes = Mockito.mock(StringBuilder.class);
 Mockito.when(mockAttributes.toString()).thenReturn("version=\"1.0\"");
 xmlDeclaration = new XmlDeclaration(xmlDeclaration.name(), xmlDeclaration.baseUri(), false);
 xmlDeclaration.attributes = new org.jsoup.nodes.Attributes();
 xmlDeclaration.attributes.put("version", mockAttributes);
 assertEquals(xmlDeclaration.getWholeDeclaration(), "xml version=\"1.0\"");
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithEncoding() throws IOException {
 StringBuilder mockAttributes = Mockito.mock(StringBuilder.class);
 Mockito.when(mockAttributes.toString()).thenReturn("encoding=\"UTF-8\"");
 xmlDeclaration = new XmlDeclaration(xmlDeclaration.name(), xmlDeclaration.baseUri(), false);
 xmlDeclaration.attributes = new org.jsoup.nodes.Attributes();
 xmlDeclaration.attributes.put("encoding", mockAttributes);
 assertEquals(xmlDeclaration.getWholeDeclaration(), "xml encoding=\"UTF-8\"");
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersionAndEncoding() throws IOException {
 StringBuilder mockAttributesVersion = Mockito.mock(StringBuilder.class);
 Mockito.when(mockAttributesVersion.toString()).thenReturn("version=\"1.0\"");
 StringBuilder mockAttributesEncoding = Mockito.mock(StringBuilder.class);
 Mockito.when(mockAttributesEncoding.toString()).thenReturn("encoding=\"UTF-8\"");
 xmlDeclaration = new XmlDeclaration(xmlDeclaration.name(), xmlDeclaration.baseUri(), false);
 xmlDeclaration.attributes = new org.jsoup.nodes.Attributes();
 xmlDeclaration.attributes.put("version", mockAttributesVersion);
 xmlDeclaration.attributes.put("encoding", mockAttributesEncoding);
 assertEquals(xmlDeclaration.getWholeDeclaration(), "xml version=\"1.0\" encoding=\"UTF-8\"");
 }

 @Test
 public void testXmlDeclarationToString() {
 assertEquals(xmlDeclaration.toString(), xmlDeclaration.outerHtml());
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() throws IOException {
 xmlDeclaration = new XmlDeclaration(xmlDeclaration.name(), xmlDeclaration.baseUri(), true);
 StringBuilder mockAccum = Mockito.mock(StringBuilder.class);
 Document.OutputSettings mockOut = Mockito.mock(Document.OutputSettings.class);
 xmlDeclaration.outerHtmlHead(mockAccum, 0, mockOut);
 assertEquals(mockAccum.toString(), "<?xml>");
 }
}