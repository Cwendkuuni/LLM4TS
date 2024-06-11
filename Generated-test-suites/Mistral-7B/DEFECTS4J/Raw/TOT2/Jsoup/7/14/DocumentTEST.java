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
 Assert.assertEquals("#root", doc.tagName());
 Assert.assertNotNull(doc.head());
 Assert.assertNotNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Assert.assertEquals("html", html.tagName());
 Element head = html.getFirstElementChild();
 Assert.assertEquals("head", head.tagName());
 Element body = html.lastElementChild();
 Assert.assertEquals("body", body.tagName());
 }

 @Test
 public void testTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 Assert.assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 Assert.assertEquals("", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("a");
 Assert.assertEquals("a", element.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.text("Hello World");
 Element p = doc.createElement("p");
 p.text("Test");
 doc.body().appendChild(p);
 doc.normalise();
 Element body = doc.body();
 TextNode textBeforeP = (TextNode) body.getPreviousElementSibling();
 TextNode textAfterP = (TextNode) body.nextElementSibling();
 Assert.assertNull(textBeforeP);
 Assert.assertNull(textAfterP);
 Assert.assertEquals("Hello WorldTest", body.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 Element head = doc.findFirstElementByTagName("head", doc);
 Element body = doc.findFirstElementByTagName("body", doc);
 Element title = doc.findFirstElementByTagName("title", doc);
 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("head", head.tagName());
 Assert.assertEquals("body", body.tagName());
 Assert.assertNull(title);
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 Assert.assertEquals("<html><head></head><body><title>Test Title</title></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("http://example.com");
 doc.text("Hello World");
 Assert.assertEquals("Hello World", doc.text().toString());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("http://example.com");
 Assert.assertEquals("#document", doc.nodeName());
 }
}