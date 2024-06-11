package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Document.createShell("http://example.com");
 }

 @Test
 public void testEmptyDocumentTitle() {
 assertEquals("", doc.title());
 }

 @Test
 public void testSetDocumentTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testGetDocumentHeadElement() {
 assertNotNull(doc.head());
 }

 @Test
 public void testGetDocumentBodyElement() {
 assertNotNull(doc.body());
 }

 @Test
 public void testCreateDocumentElement() {
 Element newElement = doc.createElement("p");
 assertEquals("p", newElement.tagName());
 }

 @Test
 public void testDocumentNormalisation() {
 doc.title("Test Title");
 doc.body().appendText("Test text in body");
 doc.appendElement("p").text("Test text in p tag");
 doc.normalise();
 assertEquals("Test Title", doc.title());
 assertEquals("Test text in bodyTest text in p tag", doc.body().text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUriInDocumentConstructor() {
 Document doc = new Document(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagNameInCreateElement() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }
}