package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Document.createShell("http://example.com");
 }

 @Test
 public void testDocumentCreation() {
 Document doc1 = new Document("http://example.com");
 assertEquals("#document", doc1.nodeName());
 assertEquals("http://example.com", doc1.baseUri());

 Document doc2 = Document.createShell("http://example.com");
 assertEquals("#document", doc2.nodeName());
 assertEquals("http://example.com", doc2.baseUri());
 }

 @Test
 public void testEmptyDocument() {
 assertEquals(3, doc.childNodes().size());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertEquals("", doc.title());
 }

 @Test
 public void testSetTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullTitle() {
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Element p = doc.createElement("p");
 assertEquals("p", p.nodeName());
 assertEquals("http://example.com", p.baseUri());
 }

 @Test
 public void testNormalise() {
 doc.title("Test Title");
 doc.body().text("Test Text");
 doc.appendChild(doc.createElement("span"));
 doc.appendChild(doc.createElement("span"));
 doc.normalise();

 assertEquals(6, doc.childNodes().size());
 assertNotNull(doc.getElementsByTag("title").first());
 assertNotNull(doc.getElementsByTag("body").first());
 assertEquals("Test Title", doc.title());
 assertEquals("Test Text", doc.body().text().trim());
 }

 @Test
 public void testEncoder() {
 Charset utf8 = Charset.forName("UTF-8");
 CharsetEncoder encoder = utf8.newEncoder();
 String text = "Tést text";
 assertEquals("T%C3%A9st text", encoder.encode(CharBuffer.wrap(text)).toString());
 }
}