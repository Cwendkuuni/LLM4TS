package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = Document.createShell("http://example.com");
 assertTrue(doc.head() != null);
 assertTrue(doc.body() != null);
 }

 @Test
 public void testGetTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 doc.title("Test Title");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Element element = doc.createElement("div");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 doc.body().appendText("Test ");
 doc.body().appendElement("p").text("Normalise test.");
 doc.normalise();
 assertEquals("Test\n<p>Normalise test.</p>", doc.body().html());
 }

 @Test
 public void testOutputSettings() {
 doc.outputSettings().prettyPrint(false);
 assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
 doc.outputSettings().prettyPrint(true);
 doc.title("Pretty Print Title");
 doc.body().appendText("Pretty Print Body");
 doc.outputSettings().charset("UTF-16");
 String expected = "<html>\n<head>\n</head>\n<body>\n<title>Pretty Print Title</title>\nPretty Print Body\n</body>\n</html>";
 assertEquals(expected, doc.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBaseUri() {
 Document doc = new Document(" ");
 fail("Should have thrown IllegalArgumentException");
 }
}