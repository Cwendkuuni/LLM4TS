import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Document.OutputSettings.*;
import static org.jsoup.nodes.QuirksMode.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

public class DocumentTest {

 @Test
 public void testConstructor() {
 Document doc = new Document("http://example.com");
 Assert.assertEquals("#document", doc.nodeName());
 Assert.assertNull(doc.getElementById("nonexistent"));
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.getElementsByTag("head").first();
 Element title = head.getElementsByTag("title").first();
 Element body = doc.body();

 Assert.assertEquals("#document", doc.nodeName());
 Assert.assertNotNull(html);
 Assert.assertNotNull(head);
 Assert.assertNotNull(title);
 Assert.assertNotNull(body);
 Assert.assertEquals("", title.text());
 }

 @Test
 public void testHeadAndBodyAccessors() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();

 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 @Test
 public void testTitleAccessors() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 String title = doc.title();

 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");

 Assert.assertEquals("div", element.tagName());
 Assert.assertEquals("http://example.com", element.baseUri());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendText("Text before body");
 Element body = doc.appendElement("body");
 body.appendText("Text inside body");
 doc.normalise();

 Element textNode = doc.getElementById("text-before-body");
 Assert.assertNotNull(textNode);
 Assert.assertEquals("Text before body", textNode.text());
 }

 @Test
 public void testNormaliseTextNodes() {
 Document doc = new Document("http://example.com");
 doc.appendText("Text before head");
 Element head = doc.appendElement("head");
 doc.appendText("Text before body");
 Element body = doc.appendElement("body");
 doc.normalise();

 Element textNode = doc.getElementById("text-before-head");
 Assert.assertNull(textNode);

 textNode = doc.getElementById("text-before-body");
 Assert.assertNotNull(textNode);
 Assert.assertEquals("Text before head", textNode.previousSibling().text());
 Assert.assertEquals("Text before body", textNode.text());
 }

 @Test
 public void testNormaliseStructure() {
 Document doc = new Document("http://example.com");
 Element head1 = doc.appendElement("head");
 Element head2 = doc.appendElement("head");
 doc.normalise();

 Assert.assertEquals(1, doc.getElementsByTag("head").size());
 Assert.assertEquals(head1, doc.getElementsByTag("head").first());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameNullNode() {
 Document doc = new Document("http://example.com");
 Element nullNode = null;
 Element element = doc.findFirstElementByTagName("div", nullNode);
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings settings = doc.outputSettings();
 OutputSettings clonedSettings = settings.clone();
 doc.outputSettings(clonedSettings);

 Assert.assertEquals(settings, doc.outputSettings());
 Assert.assertEquals(clonedSettings, doc.outputSettings());
 }

 @Test
 public void testQuirksMode() {
 Document doc = new Document("http://example.com");
 doc.quirksMode(QuirksMode.quirks);
 Assert.assertEquals(QuirksMode.quirks, doc.quirksMode());
 }
}