import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testHeadAndBodyAccessors() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 Element body = doc.body();
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testTitleAccessors() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("a");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.appendElement("p").text("Test Text");
 Element p = doc.getElementsByTag("p").first();
 doc.normalise();
 assertEquals("Test Text", p.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("baseUri");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 Element title = doc.findFirstElementByTagName("title", doc.head());
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("baseUri");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 String html = doc.outerHtml();
 assertEquals("<html><head><title>Test Title</title></head></html>", html);
 }

 @Test
 public void testTextMethod() {
 Document doc = new Document("baseUri");
 doc.appendElement("body").text("Test Text");
 String text = doc.text();
 assertEquals("Test Text", text);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTitle() {
 Document doc = new Document("baseUri");
 doc.title("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTitle() {
 Document doc = new Document("baseUri");
 doc.title(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testTitleSetTwice() {
 Document doc = new Document("baseUri");
 doc.title("Test Title 1");
 doc.title("Test Title 2");
 }
}