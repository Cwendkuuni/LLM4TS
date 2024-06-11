import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setup() {
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
 assertEquals("#root", doc.tagName());
 assertTrue(doc.head() instanceof Element);
 assertTrue(doc.body() instanceof Element);
 }

 @Test
 public void testHeadAccessor() {
 Element head = document.head();
 assertEquals("head", head.tagName());
 }

 @Test
 public void testBodyAccessor() {
 Element body = document.body();
 assertEquals("body", body.tagName());
 }

 @Test
 public void testTitleAccessor() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testTitleSetter() {
 document.title("Test Title");
 document.title("New Title");
 assertEquals("New Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Element p = document.createElement("p");
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 document.title("Test Title");
 document.appendText("Test text");
 Element p = document.createElement("p").text("Test paragraph");
 document.body().appendElement(p);
 document.normalise();

 String expected = String.format("<html><head><title>Test Title</title></head><body>%s%s</body></html>", p.outerHtml(), document.body().text());
 assertEquals(expected, document.outerHtml());
 }

 @Test
 public void testTitleWithEmptyString() {
 document.title("");
 assertEquals("", document.title());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleWithNullArgument() {
 document.title(null);
 }

 @Test
 public void testCreateShellWithNullBaseUri() {
 try {
 Document.createShell(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}