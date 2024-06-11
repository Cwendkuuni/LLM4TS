package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.parser.ParserConfigurationException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

 private DocumentBuilderFactory factory;
 private DocumentBuilder builder;
 private W3CDom w3CDom;

 @Before
 public void setUp() {
 factory = DocumentBuilderFactory.newInstance();
 builder = null;
 w3CDom = new W3CDom();
 try {
 factory.setNamespaceAware(true);
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }
 }

 // Test for fromJsoup method
 @Test
 public void testFromJsoup() {
 org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head></head><body><h1>Hello</h1></body></html>");
 Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
 assertNotNull(w3cDoc);
 }

 // Test for fromJsoup method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupNullInput() {
 w3CDom.fromJsoup(null);
 }

 // Test for convert method
 @Test
 public void testConvert() {
 org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head></head><body><h1>Hello</h1></body></html>");
 Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
 w3CDom.convert(jsoupDoc, w3cDoc);
 assertEquals(jsoupDoc.childNodes().item(0).getNodeName(), w3cDoc.getDocumentElement().getTagName());
 }

 // Test for convert method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testConvertNullInput() {
 Document w3cDoc = w3CDom.fromJsoup(new org.jsoup.nodes.Document());
 w3CDom.convert(null, w3cDoc);
 }

 // Test for asString method
 @Test
 public void testAsString() {
 Document w3cDoc = builder.newDocument();
 Element root = w3cDoc.createElement("root");
 root.setTextContent("Hello World");
 w3cDoc.appendChild(root);
 String w3cString = w3CDom.asString(w3cDoc);
 assertEquals("<root>Hello World</root>", w3cString);
 }

 // Test for asString method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testAsStringNullInput() {
 String w3cString = w3CDom.asString(null);
 }

}