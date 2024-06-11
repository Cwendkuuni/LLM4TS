import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseUri");
 assertTrue(doc instanceof Document);
 assertEquals("#root", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertTrue(doc instanceof Document);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testHeadAccess() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertTrue(head instanceof Element);
 assertEquals("head", head.nodeName());
 }

 @Test
 public void testBodyAccess() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertTrue(body instanceof Element);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testTitleAccess() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testTitleSetNull() {
 Document doc = new Document("baseUri");
 doc.title(null);
 assertEquals("", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("a");
 assertTrue(element instanceof Element);
 assertEquals("a", element.nodeName());
 }

 @Test
 public void testNormaliseDocument() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 doc.appendText("Test Text 1");
 Element pElement = doc.createElement("p");
 pElement.text("Test Text 2");
 doc.body().appendChild(pElement);
 doc.normalise();
 List<Node> bodyChildren = doc.body().childNodes();
 assertEquals(3, bodyChildren.size());
 assertEquals("Test Text 1", bodyChildren.get(0).text());
 assertEquals("Test Text 2", bodyChildren.get(1).text());
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testFindFirstElementByTagNameNullNode() {
 Document doc = new Document("baseUri");
 assertNull(Document.findFirstElementByTagName("html", null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindFirstElementByTagNameInvalidTag() {
 Document doc = new Document("baseUri");
 assertNull(Document.findFirstElementByTagName("invalidTag", doc));
 }

 @Test
 public void testFindFirstElementByTagNameValidTag() {
 Document doc = new Document("baseUri");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertTrue(html instanceof Element);
 assertEquals("html", html.nodeName());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 doc.appendText("Test Text 1");
 Element pElement = doc.createElement("p");
 pElement.text("Test Text 2");
 doc.body().appendChild(pElement);
 assertEquals("<html><head><title>Test Title</title></head><body>" +
 "Test Text 1<p>Test Text 2</p></body></html>", doc.outerHtml());
 }

 @Test
 public void testTextSetting() {
 Document doc = new Document("baseUri");
 doc.body().text("Test Text 1");
 assertEquals("Test Text 1", doc.text().trim());
 }

 @Test
 public void testTextSettingWithElements() {
 Document doc = new Document("baseUri");
 doc.body().text("Test Text 1");
 Element pElement = doc.createElement("p");
 pElement.text("Test Text 2");
 doc.body().appendChild(pElement);
 doc.text("Test Text 3");
 assertEquals("Test Text 3", doc.text().trim());
 }

 @Test
 public void testTextSettingNull() {
 Document doc = new Document("baseUri");
 doc.text(null);
 assertEquals("", doc.text().trim());
 }
}