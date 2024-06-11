import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertTrue(html instanceof Element);
 assertTrue(html.tagName().equals("#html"));
 assertTrue(doc.head() != null);
 assertTrue(doc.body() != null);
 }

 @Test
 public void testHeadAccessor() {
 document.title("Test Title");
 Element head = document.head();
 Element title = head.getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 }

 @Test
 public void testBodyAccessor() {
 Element body = document.body();
 assertTrue(body instanceof Element);
 assertTrue(body.tagName().equals("body"));
 }

 @Test
 public void testTitleAccessor() {
 document.title("Test Title");
 String title = document.title();
 assertEquals("Test Title", title.trim());
 }

 @Test
 public void testTitleSetter() {
 document.title("Test Title");
 document.title("New Title");
 String newTitle = document.title();
 assertEquals("New Title", newTitle.trim());
 }

 @Test
 public void testCreateElement() {
 Element newElement = document.createElement("a");
 assertTrue(newElement instanceof Element);
 assertEquals("a", newElement.tagName().toString());
 }

 @Test
 public void testNormalise() {
 document.body().appendText("Test text");
 assertEquals("Test text", document.body().text().trim());
 document.normalise();
 assertTrue(document.body().childNodes().isEmpty());
 Element textNode = document.createElement("p").textNodeSibling();
 document.body().appendChild(textNode);
 textNode.text("Test text 2");
 document.normalise();
 assertEquals("Test text 2", document.body().text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleNull() {
 document.title(null);
 }

 @Test
 public void testTitleEmpty() {
 document.title("");
 assertEquals("", document.title());
 }

 @Test
 public void testHeadAndTitle() {
 document.title("Test Title");
 Element head = document.head();
 Element title = head.getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 }
}