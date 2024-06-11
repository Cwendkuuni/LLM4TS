import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.head();
 Element head = html.head();
 Element body = html.body();
 assertNotNull(doc);
 assertNotNull(html);
 assertNotNull(head);
 assertNotNull(body);
 }

 @Test
 public void testGetHeadElement() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test
 public void testGetBodyElement() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("baseUri");
 doc.title("");
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("p");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.appendText("Test ");
 doc.appendElement(doc.createElement("p")).text("Text");
 doc.appendText(" Inside body");
 doc.normalise();
 Element body = doc.body();
 assertEquals("Test\nText\n Inside body", body.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("baseUri");
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head").appendElement("title").text("Test Title");
 Element body = doc.appendElement("body").appendElement("p").text("Test");
 Element title = doc.findFirstElementByTagName("title", doc);
 Element bodyElement = doc.findFirstElementByTagName("body", doc);
 Element pElement = doc.findFirstElementByTagName("p", doc);
 assertNotNull(title);
 assertNotNull(bodyElement);
 assertNotNull(pElement);
 assertEquals("Test Title", title.text());
 assertEquals("Test", pElement.text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("baseUri");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 doc.appendElement("body").appendElement("p").text("Test");
 String html = doc.outerHtml();
 assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", html);
 }

 @Test
 public void testText() {
 Document doc = new Document("baseUri");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 doc.appendElement("body").appendElement("p").text("Test");
 doc.text("Test Text");
 String text = doc.text();
 assertEquals("Test TextTest Title\n<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", text);
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testClone() {
 Document doc = new Document("baseUri");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test Title");
 doc.appendElement("body").appendElement("p").text("Test");
 Document clone = doc.clone();
 assertNotNull(clone);
 assertEquals("#document", clone.nodeName());
 Element htmlClone = clone.head();
 Element titleClone = htmlClone.head();
 Element bodyClone = htmlClone.body();
 assertEquals("Test Title", titleClone.text());
 assertEquals("Test", bodyClone.text());
 }
}