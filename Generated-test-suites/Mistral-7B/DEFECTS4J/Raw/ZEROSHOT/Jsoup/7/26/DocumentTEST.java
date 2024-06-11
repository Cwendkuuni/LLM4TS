import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.head();
 Element body = html.body();
 assertNotNull(doc);
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testAccessors() {
 Document doc = new Document("http://example.com");
 Element head = doc.head();
 Element body = doc.body();
 String title = doc.title();

 assertEquals(doc, head.parent());
 assertEquals(doc, body.parent());
 assertEquals("", title);
 }

 @Test
 public void testTitleAccessorAndSetter() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element a = doc.createElement("a");
 assertNotNull(a);
 assertEquals("a", a.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.text("Test 1\nTest 2\nTest 3");
 Element p = doc.createElement("p");
 p.text("Test 4\nTest 5");
 doc.body().appendChild(p);
 doc.normalise();
 String expected = "Test 1Test 2Test 3Test 4Test 5";
 assertEquals(expected, doc.body().text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><div><p>Test</p></div></body></html>", "http://example.com");
 Element head = doc.findFirstElementByTagName("head");
 Element title = doc.findFirstElementByTagName("title", head);
 Element body = doc.findFirstElementByTagName("body", doc);
 Element div = doc.findFirstElementByTagName("div", body);
 Element p = doc.findFirstElementByTagName("p", div);

 assertEquals(head, title.parent());
 assertEquals(doc, body.parent());
 assertEquals(body, div.parent());
 assertEquals(div, p.parent());
 assertEquals("Test", p.text());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 outputSettings.charset("UTF-16");
 outputSettings.prettyPrint(false);
 outputSettings.indentAmount(4);
 assertNotNull(outputSettings.charset());
 assertFalse(outputSettings.prettyPrint());
 assertEquals(4, outputSettings.indentAmount());
 }
}