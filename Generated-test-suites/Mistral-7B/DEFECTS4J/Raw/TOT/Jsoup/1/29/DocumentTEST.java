import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;
import java.util.List;

public class DocumentTest {

 private Document doc;

 @Before
 public void setup() {
 doc = Document.createShell("http://test.com");
 }

 @Test
 public void testCreateDocument() {
 Document newDoc = new Document("http://test.com");
 assertEquals("#document", newDoc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document shellDoc = Document.createShell("http://test.com");
 assertEquals("#root", shellDoc.tagName());
 assertTrue(shellDoc.head() instanceof Element);
 assertTrue(shellDoc.body() instanceof Element);
 }

 @Test
 public void testGetHeadElement() {
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("#head", head.tagName());
 }

 @Test
 public void testGetBodyElement() {
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("#body", body.tagName());
 }

 @Test
 public void testGetTitle() {
 String title = doc.title();
 assertEquals("", title);
 }

 @Test
 public void testSetTitle() {
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 Element titleEl = doc.getElementsByTag("title").first();
 assertEquals("Test Title", titleEl.text().trim());
 }

 @Test
 public void testCreateElement() {
 Element newEl = doc.createElement("a");
 assertNotNull(newEl);
 assertEquals("a", newEl.tagName());
 }

 @Test
 public void testNormalise() {
 doc.appendElement("p").text("Test");
 doc.title("Test Title");
 doc.head().appendElement("title").text("Title in Head");
 doc.normalise();
 assertEquals("Test Title\n<p>Test</p>", doc.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document newDoc = new Document(null);
 fail("Should have thrown NPE");
 }
}