import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.getElementsByTag("head").first();
 Element body = doc.body();

 Assert.assertNotNull(doc);
 Assert.assertNotNull(html);
 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("head", head.tagName());
 Assert.assertEquals("body", body.tagName());
 }

 @Test
 public void testGetHeadAndBody() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();

 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();

 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 doc.title("Initial Title");
 doc.title("Test Title");
 String title = doc.title();

 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");

 Assert.assertEquals("p", p.tagName());
 Assert.assertNotNull(p.baseUri());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("http://example.com");
 doc.head().text("Test Head");
 doc.body().text("Test Body");
 doc.appendElement("div").text("Test Div");

 doc.normalise();

 Element head = doc.head();
 Element body = doc.body();
 List<Element> divs = doc.select("div");

 Assert.assertEquals("Test Head", head.text().trim());
 Assert.assertEquals("Test Body", body.text().trim());
 Assert.assertEquals(0, divs.size());
 }

 @Test
 public void testTitleAndText() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.text("Test Body");
 String title = doc.title();
 String bodyText = doc.body().text();

 Assert.assertEquals("Test Title", title);
 Assert.assertEquals("Test Body", bodyText);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBaseUri() {
 Document doc = new Document(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement(null);
 }
}