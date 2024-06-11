import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DocumentTest {

 @Test
 public void testDocumentConstructor() {
 Document document = new Document("http://example.com");
 assertNotNull(document);
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 assertNotNull(document);
 Element html = document.select("html").first();
 assertNotNull(html);
 Element head = html.getElementsByTag("head").first();
 assertNotNull(head);
 Element body = html.getElementsByTag("body").first();
 assertNotNull(body);
 }

 @Test
 public void testHeadMethod() {
 Document document = new Document("http://example.com");
 Element head = document.head();
 assertNotNull(head);
 }

 @Test
 public void testBodyMethod() {
 Document document = new Document("http://example.com");
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testTitleMethod() {
 Document document = new Document("http://example.com");
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testCreateElementMethod() {
 Document document = new Document("http://example.com");
 Element element = document.createElement("a");
 assertNotNull(element);
 }

 @Test
 public void testNormaliseMethod() {
 Document document = new Document("http://example.com");
 document.appendElement("html").appendElement("head").appendElement("body").appendText("Test");
 Element textNode = document.elementAt(3);
 document.normalise();
 assertNull(document.select("head > textNode").first());
 assertNotNull(document.body().child(0));
 assertEquals("Test", document.body().child(0).text());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleMethodWithNullTitle() {
 Document document = new Document("http://example.com");
 document.title(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTitleMethodWithEmptyTitle() {
 Document document = new Document("http://example.com");
 document.title("");
 }

 @Test(expected = NullPointerException.class)
 public void testHeadMethodWhenNoHead() {
 Document document = new Document("http://example.com");
 assertThrows(NullPointerException.class, () -> document.head());
 }

 @Test(expected = NullPointerException.class)
 public void testBodyMethodWhenNoBody() {
 Document document = new Document("http://example.com");
 assertThrows(NullPointerException.class, () -> document.body());
 }

 @Test
 public void testOuterHtmlMethod() {
 Document document = new Document("http://example.com");
 assertEquals("<html><head></head><body></body></html>", document.outerHtml());
 }

 @Test
 public void testTextMethod() {
 Document document = new Document("http://example.com");
 document.body().text("Test");
 assertEquals("Test", document.text());
 }

 @Test
 public void testNodeNameMethod() {
 Document document = new Document("http://example.com");
 assertEquals("#document", document.nodeName());
 }
}