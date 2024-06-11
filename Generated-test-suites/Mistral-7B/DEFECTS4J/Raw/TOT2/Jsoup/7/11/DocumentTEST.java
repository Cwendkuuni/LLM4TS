import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document("").createShell("");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testHeadMethod() {
 Element head = document.head();
 assertNotNull(head);
 }

 @Test
 public void testBodyMethod() {
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testTitleMethod() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testTitleMethodWithNullInput() {
 document.title(null);
 assertEquals("", document.title());
 }

 @Test
 public void testCreateElementMethod() {
 Element element = document.createElement("a");
 assertNotNull(element);
 }

 @Test
 public void testNormaliseMethod() {
 String initialText = "Initial text in root";
 document.text(initialText);
 document.normalise();
 Element body = document.body();
 String expectedText = "Initial text in root\n<body>\n</body>";
 assertEquals(expectedText, document.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFirstElementByTagNameWithNullNode() {
 document.findFirstElementByTagName("tag", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFirstElementByTagNameWithInvalidTag() {
 document.findFirstElementByTagName("invalidTag", this);
 }

 @Test
 public void testOuterHtmlMethod() {
 document.head().appendElement("title").text("Test Title");
 String expectedHtml = "<html><head><title>Test Title</title></head><body></body></html>";
 assertEquals(expectedHtml, document.outerHtml());
 }

 @Test
 public void testTextMethod() {
 document.body().text("Test text");
 String expectedText = "Test text";
 assertEquals(expectedText, document.body().text());
 }

 @Test
 public void testTextMethodWithNullInput() {
 document.body().text(null);
 assertEquals("", document.body().text());
 }

 @After
 public void tearDown() {
 document = null;
 }

}