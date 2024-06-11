import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

 @org.junit.Test
 public void testDocumentCreation() {
 Document document = new Document("http://example.com");
 Assert.assertNotNull(document);
 Assert.assertEquals("#document", document.nodeName());
 }

 @org.junit.Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 Element html = document.head();
 Assert.assertNotNull(html);
 Assert.assertEquals("html", html.tagName());
 Element head = html.head();
 Assert.assertNotNull(head);
 Element body = html.body();
 Assert.assertNotNull(body);
 }

 @org.junit.Test
 public void testGetHeadElement() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
 Element head = document.head();
 Assert.assertNotNull(head);
 Assert.assertEquals("head", head.tagName());
 }

 @org.junit.Test
 public void testGetBodyElement() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
 Element body = document.body();
 Assert.assertNotNull(body);
 Assert.assertEquals("body", body.tagName());
 }

 @org.junit.Test
 public void testGetDocumentTitle() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
 String title = document.title();
 Assert.assertEquals("Test", title);
 }

 @org.junit.Test
 public void testSetDocumentTitle() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
 document.title("New Title");
 String title = document.title();
 Assert.assertEquals("New Title", title);
 }

 @org.junit.Test
 public void testCreateElement() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
 Element element = document.createElement("div");
 Assert.assertNotNull(element);
 Assert.assertEquals("div", element.tagName());
 }

 @org.junit.Test
 public void testNormaliseDocument() {
 Document document = Jsoup.parse("<html><head></head><body><p>Test</p></html>", "http://example.com");
 document.normalise();
 List<Element> elements = document.body().children();
 Assert.assertEquals(1, elements.size());
 Assert.assertEquals("p", elements.get(0).tagName());
 Assert.assertEquals("Test", elements.get(0).text());
 }

 @org.junit.Test
 public void testFindFirstElementByTagName() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></html>", "http://example.com");
 Element element = document.findFirstElementByTagName("head", document);
 Assert.assertNotNull(element);
 Assert.assertEquals("head", element.tagName());
 }

 @org.junit.Test
 public void testOuterHtml() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></body></html>", "http://example.com");
 String outerHtml = document.outerHtml();
 Assert.assertEquals("<html><head><title>Test</title></head><body><p>Test</p></body></html>", outerHtml);
 }

 @org.junit.Test
 public void testTextDocument() {
 Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></body></html>", "http://example.com");
 document.text("New Text");
 String text = document.text();
 Assert.assertEquals("New Text", text);
 }
}