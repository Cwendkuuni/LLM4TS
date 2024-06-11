import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testCreateEmptyDocument() {
 Document doc = new Document("http://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShellDocument() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Assert.assertNotNull(html);
 Assert.assertEquals("html", html.tagName());
 Element head = html.head();
 Assert.assertNotNull(head);
 Element body = html.body();
 Assert.assertNotNull(body);
 }

 @Test
 public void testGetHeadAndBody() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();
 Assert.assertNotNull(head);
 Assert.assertEquals("head", head.tagName());
 Assert.assertNotNull(body);
 Assert.assertEquals("body", body.tagName());
 }

 @Test
 public void testGetAndSetTitle() {
 Document doc = new Document("http://example.com");
 Assert.assertEquals("", doc.title());
 doc.title("Test Title");
 Assert.assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 Assert.assertEquals("p", p.tagName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 doc.normalise();
 Element html = doc.head();
 Element head = html.head();
 Element body = html.body();
 TextNode title = (TextNode) head.children().get(0);
 TextNode p = (TextNode) body.children().get(0);
 Assert.assertEquals("Test", title.text());
 Assert.assertEquals("Test", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test
 public void testTrimTitle() {
 Document doc = new Document("<html><head><title> Test Title   </title></head><body></body></html>");
 doc.title("Test Title");
 Assert.assertEquals("Test Title", doc.title());
 }

 @Test
 public void testTextSetting() {
 Document doc = new Document();
 doc.text("<p>Test</p>");
 Element body = doc.body();
 TextNode p = (TextNode) body.children().get(0);
 Assert.assertEquals("Test", p.text());
 }

 @Test
 public void testTitleWithExistingTitle() {
 Document doc = new Document("<html><head><title>Test Title</title></head><body></body></html>");
 doc.title("New Title");
 Element title = doc.head().getElementsByTag("title").first();
 Assert.assertEquals("New Title", title.text());
 }

 @Test
 public void testTitleWithEmptyDocument() {
 Document doc = new Document();
 doc.title("Test Title");
 Assert.assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 Document doc = null;
 doc.title("Test Title");
 }

 @Test
 public void testGetOutputSettings() {
 Document doc = new Document();
 OutputSettings settings = doc.outputSettings();
 Assert.assertEquals("UTF-8", settings.charset().name());
 Assert.assertEquals(true, settings.prettyPrint());
 }

 @Test
 public void testSetOutputSettings() {
 Document doc = new Document();
 OutputSettings settings = doc.outputSettings();
 settings.prettyPrint(false);
 doc.outputSettings(settings);
 Assert.assertEquals(false, doc.outputSettings().prettyPrint());
 }
}