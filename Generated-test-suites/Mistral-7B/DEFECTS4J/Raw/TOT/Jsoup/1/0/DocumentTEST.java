import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = Document.createShell("http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 }

 @Test
 public void testTitleAccessors() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 Element title = doc.head().getElementsByTag("title").first();
 String titleText = title.text();
 Assert.assertEquals("Test Title", titleText);
 Assert.assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("http://example.com");
 Element p = doc.createElement("p");
 doc.body().appendChild(p);
 p.text("Test Text");
 Assert.assertEquals("Test Text", p.text());
 }

 @Test
 public void testNormalise() {
 Document doc = Document.createShell("http://example.com");
 doc.title("Test Title");
 doc.body().appendText("Test Text 1");
 doc.head().appendElement("p").text("Test Text 2");
 Element p = doc.body().appendElement("p");
 p.text("Test Text 3");
 doc.normalise();
 List<Element> bodyElements = doc.body().getElementsByTag("p");
 Assert.assertEquals(3, bodyElements.size());
 String titleText = doc.title();
 Assert.assertEquals("Test Title", titleText);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBaseUri() {
 Document doc = new Document("");
 }

 @Test
 public void testSetTitleWithNullTitle() {
 Document doc = Document.createShell("http://example.com");
 try {
 doc.title(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSetTitleWithEmptyTitle() {
 Document doc = Document.createShell("http://example.com");
 doc.title("");
 Element title = doc.head().getElementsByTag("title").first();
 String titleText = title.text();
 Assert.assertEquals("", titleText);
 Assert.assertEquals("", doc.title());
 }

 @Test
 public void testCreateDocumentWithNullBaseUri() {
 try {
 Document doc = new Document(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }
}