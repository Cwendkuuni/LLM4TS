import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document createDocument(String baseUri) {
 return new Document(baseUri);
 }

 @Test
 public void testDocumentConstructor() {
 Document document = createDocument("http://example.com");
 assertNotNull(document);
 assertEquals("#root", document.tagName());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testHeadAccessors() {
 Document document = createDocument("http://example.com");
 Element head = document.head();
 assertNotNull(head);
 assertEquals("#head", head.tagName());
 }

 @Test
 public void testBodyAccessors() {
 Document document = createDocument("http://example.com");
 Element body = document.body();
 assertNotNull(body);
 assertEquals("#body", body.tagName());
 }

 @Test
 public void testTitleAccessors() {
 Document document = createDocument("http://example.com");
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testTitleSetterNull() {
 Document document = createDocument("http://example.com");
 try {
 document.title(null);
 fail("Should not allow null title");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateElement() {
 Document document = createDocument("http://example.com");
 Element element = document.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.tagName());
 }

 @Test
 public void testNormalise() {
 Document document = createDocument("http://example.com");
 document.title("Test Title");
 document.text("Test Text");
 document.body().appendElement("Test Element");
 document.appendElement("Test Heading").text("Test Heading Text");
 document.normalise();
 assertEquals("Test Title", document.title());
 assertEquals("Test TextTest Heading TextTest Element", document.outerHtml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTitleSetterEmpty() {
 Document document = createDocument("http://example.com");
 document.title("");
 fail("Should not allow empty title");
 }

 @Test
 public void testTextSetterNull() {
 Document document = createDocument("http://example.com");
 document.text(null);
 assertEquals("", document.body().text().trim());
 }

 @Test
 public void testTextSetterEmpty() {
 Document document = createDocument("http://example.com");
 document.text("");
 assertEquals("", document.body().text().trim());
 }

 @Test
 public void testTextSetterWhitespace() {
 Document document = createDocument("http://example.com");
 document.text(" Test Text ");
 assertEquals(" Test Text ", document.body().text().trim());
 }

 @Test
 public void testNormaliseEmptyDocument() {
 Document document = new Document("");
 document.normalise();
 assertEquals("#document", document.tagName());
 assertEquals(0, document.childNodeSize());
 }

 @Test
 public void testNormaliseWithTextNodes() {
 Document document = new Document("");
 document.title("Test Title");
 document.body().appendChild(new TextNode("Test Text 1"));
 document.body().appendChild(new TextNode("Test Text 2"));
 document.normalise();
 assertEquals("Test Title", document.title());
 assertEquals("Test Text 1Test Text 2", document.body().text().trim());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormaliseNullElement() {
 Document document = new Document("");
 document.normalise(null);
 fail("Should not allow null element");
 }
}