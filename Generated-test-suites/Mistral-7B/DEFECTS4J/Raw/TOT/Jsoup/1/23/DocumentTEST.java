import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testEmptyDocumentCreation() {
 Document emptyDoc = new Document("http://example.com");
 assertNotNull(emptyDoc);
 assertNotNull(emptyDoc.head());
 assertNotNull(emptyDoc.body());
 assertNull(emptyDoc.title());
 }

 @Test
 public void testCreateDocumentWithTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testCreateAndGetElement() {
 Element newElement = document.createElement("div");
 assertNotNull(newElement);
 assertEquals("#document", document.nodeName());
 }

 @Test
 public void testNormaliseDocument() {
 document.appendElement("test").text("This is a test.");
 document.normalise();
 assertEquals(3, document.childNodes().size());
 Element body = document.getElementsByTag("body").first();
 assertEquals("This is a test.", body.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTagName() {
 Document doc = new Document("", "div");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 Document doc = new Document("http://example.com", null);
 }
}