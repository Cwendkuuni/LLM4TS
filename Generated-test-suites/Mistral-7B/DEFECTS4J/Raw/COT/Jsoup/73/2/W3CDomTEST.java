package org.jsoup.helper;

import org.junit.jupiter.api.*;
import org.w3c.dom.*;
import org.jsoup.nodes.*;
import static org.junit.jupiter.api.Assertions.*;

class W3CDomTest {

 @BeforeEach
 void setUp() {
 factory = new DocumentBuilderFactory();
 }

 @Test
 void testFromJsoup() {
 // Given a valid jsoup Document
 org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");

 // When converting the jsoup Document to a W3C Document
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

 // Then the W3C Document should not be null
 assertNotNull(w3cDocument);

 // And the W3C Document should have the same structure as the jsoup Document
 assertEquals(jsoupDocument.childNodes().size(), w3cDocument.getChildNodes().getLength());
 }

 @Test
 void testFromJsoup_NullJsoupDocument() {
 // Given a null jsoup Document

 // When converting a null jsoup Document to a W3C Document
 assertThrows(IllegalArgumentException.class, () -> new W3CDom().fromJsoup(null));
 }

 @Test
 void testConvert() {
 // Given a valid jsoup Document
 org.jsoup.nodes.Document jsoupDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
 Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();

 // When converting the jsoup Document to the provided W3C Document
 new W3CDom().convert(jsoupDocument, w3cDocument);

 // Then the W3C Document should not be null
 assertNotNull(w3cDocument);

 // And the W3C Document should have the same structure as the jsoup Document
 assertEquals(jsoupDocument.childNodes().size(), w3cDocument.getChildNodes().getLength());
 }

 @Test
 void testConvert_NullJsoupDocument() {
 // Given a null jsoup Document

 // When converting a null jsoup Document to the provided W3C Document
 Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
 assertDoesNotThrow(() -> new W3CDom().convert(null, w3cDocument));

 // Then the W3C Document should be empty
 assertTrue(w3cDocument.getChildNodes().getLength() == 0);
 }

 @Test
 void testAsString() {
 // Given a valid W3C Document
 Document w3cDocument = DocumentBuilderFactory.newInstance().newDocument();
 w3cDocument.appendChild(w3cDocument.createTextNode("Test"));

 // When converting the W3C Document to a String
 String w3cDocumentAsString = new W3CDom().asString(w3cDocument);

 // Then the String should not be null
 assertNotNull(w3cDocumentAsString);

 // And the String should be equivalent to the W3C Document
 assertEquals("<document><text>Test</text></document>", w3cDocumentAsString);
 }

 @Test
 void testAsString_NullW3CDocument() {
 // Given a null W3C Document

 // When converting a null W3C Document to a String
 assertThrows(NullPointerException.class, () -> new W3CDom().asString(null));
 }
}