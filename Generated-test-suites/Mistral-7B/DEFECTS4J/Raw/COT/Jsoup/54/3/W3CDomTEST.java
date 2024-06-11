package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentException;
import org.w3c.dom.Text;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setUp() {
 jsoupDocument = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
 w3cDocument = W3CDom.fromJsoup(jsoupDocument);
 }

 // Basic test case for fromJsoup method
 @Test
 public void testFromJsoup() {
 assertNotNull(w3cDocument);
 assertEquals(jsoupDocument.getDocumentElement().getTagName(), w3cDocument.getDocumentElement().getTagName());
 }

 // Test for null input
 @Test(expected = IllegalStateException.class)
 public void testFromJsoupNullInput() {
 W3CDom w3cdom = new W3CDom();
 assertNull(w3cdom.fromJsoup(null));
 }

 // Test for convert method
 @Test
 public void testConvert() {
 Element rootEl = w3cDocument.getDocumentElement();
 NodeTraversor traversor = new NodeTraversor(new W3CBuilder(w3cDocument));
 traversor.traverse(rootEl);
 assertEquals(jsoupDocument.body().text(), w3cDocument.getElementsByTagName("p").item(0).getTextContent());
 }

 // Test for null input in convert method
 @Test(expected = IllegalArgumentException.class)
 public void testConvertNullInput() {
 W3CDom w3cdom = new W3CDom();
 assertNull(w3cdom.convert(null, w3cDocument));
 }

 // Test for asString method
 @Test
 public void testAsString() {
 String jsoupString = jsoupDocument.outerHtml();
 String w3cString = W3CDom.asString(w3cDocument);
 assertEquals(jsoupString, w3cString);
 }

 // Test for null input in asString method
 @Test(expected = IllegalStateException.class)
 public void testAsStringNullInput() {
 W3CDom w3cdom = new W3CDom();
 assertNull(w3cdom.asString(null));
 }

 // Test for edge case: empty Document
 @Test
 public void testEmptyDocument() {
 Document emptyJsoupDocument = org.jsoup.Jsoup.parse("");
 Document emptyW3cDocument = W3CDom.fromJsoup(emptyJsoupDocument);
 String emptyW3cString = W3CDom.asString(emptyW3cDocument);
 assertEquals("", emptyW3cString);
 }

 // Test for exception handling: ParserConfigurationException in fromJsoup method
 @Test(expected = IllegalStateException.class)
 public void testFromJsoupParserConfigurationException() {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 factory.setNamespaceAware(true);
 DocumentBuilder builder;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 throw new IllegalStateException(e);
 }
 W3CDom w3cdom = new W3CDom();
 assertNull(w3cdom.fromJsoup(jsoupDocument));
 }

 // Test for exception handling: TransformerException in asString method
 @Test(expected = IllegalStateException.class)
 public void testAsStringTransformerException() {
 TransformerFactory tf = TransformerFactory.newInstance();
 Transformer transformer;
 try {
 transformer = tf.newTransformer();
 } catch (TransformerConfigurationException e) {
 throw new IllegalStateException(e);
 }
 DOMSource domSource = new DOMSource(w3cDocument);
 StreamResult result = new StreamResult(new StringWriter());
 try {
 transformer.transform(domSource, result);
 } catch (TransformerException e) {
 throw new IllegalStateException(e);
 }
 assertNull(W3CDom.asString(null));
 }

}