package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

 private DocumentBuilderFactory factory;
 private W3CDom w3CDom;
 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setup() {
 factory = DocumentBuilderFactory.newInstance();
 w3CDom = new W3CDom();

 try {
 factory.setNamespaceAware(true);
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }

 DocumentBuilder builder = null;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }

 jsoupDocument = builder.newDocument();
 jsoupDocument.appendChild(jsoupDocument.createComment("This is a comment"));
 Element root = jsoupDocument.createElement("root");
 root.setAttribute("id", "1");
 root.setTextContent("This is the root element");
 jsoupDocument.appendChild(root);
 }

 @Test
 public void testFromJsoup() {
 w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 assertEquals(jsoupDocument.getDocumentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupNullDocument() {
 w3CDom.fromJsoup(null);
 }

 @Test
 public void testConvert() {
 w3CDom.convert(jsoupDocument, w3cDocument);

 String expectedXml = "<!--This is a comment-->\n" +
 "<root id=\"1\">This is the root element</root>";
 String actualXml = w3CDom.asString(w3cDocument);
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvertNullJsoupDocument() {
 w3CDom.convert(null, w3cDocument);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvertNullW3CDocument() {
 w3CDom.convert(jsoupDocument, null);
 }

 @Test
 public void testAsString() {
 String expectedXml = "<!--This is a comment-->\n" +
 "<root id=\"1\">This is the root element</root>";
 String actualXml = w3CDom.asString(w3cDocument);
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsStringNullDocument() {
 w3CDom.asString(null);
 }

}