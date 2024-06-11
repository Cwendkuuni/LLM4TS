package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

 private String baseUri = "http://example.com";

 @Test
 public void testDocumentConstructor() {
 Document doc = new Document(baseUri);
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell(baseUri);
 assertEquals("#document", doc.nodeName());
 Element html = doc.select("html").first();
 assertTrue(html != null);
 Element head = html.select("head").first();
 assertTrue(head != null);
 Element body = html.select("body").first();
 assertTrue(body != null);
 }

 @Test
 public void testHeadMethod() {
 Document doc = new Document(baseUri);
 Element head = doc.head();
 assertTrue(head != null);
 }

 @Test
 public void testBodyMethod() {
 Document doc = new Document(baseUri);
 Element body = doc.body();
 assertTrue(body != null);
 }

 @Test
 public void testTitleMethod() {
 Document doc = new Document(baseUri);
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testTitleMethodWithExistingTitle() {
 Document doc = new Document(baseUri);
 doc.title("Original Title");
 doc.title("New Title");
 String title = doc.title();
 assertEquals("New Title", title);
 }

 @Test(expected = NullPointerException.class)
 public void testTitleMethodWithNullTitle() {
 Document doc = new Document(baseUri);
 doc.title(null);
 }

 @Test
 public void testCreateElementMethod() {
 Document doc = new Document(baseUri);
 Element element = doc.createElement("a");
 assertEquals("a", element.tagName());
 }

 @Test
 public void testNormaliseMethod() {
 Document doc = new Document(baseUri);
 doc.head().appendElement("test1").appendText("Test Text 1");
 doc.appendElement("test2").appendText("Test Text 2");
 doc.select("html").first().appendElement("test3").appendText("Test Text 3");
 doc.normalise();
 List<Element> elements = doc.select("body > *");
 assertEquals(2, elements.size());
 Element body = doc.select("body").first();
 List<Node> textNodes = body.childNodes();
 assertEquals(2, textNodes.size());
 String text1 = ((TextNode) textNodes.get(0)).text();
 assertEquals("Test Text 1", text1);
 String text2 = ((TextNode) textNodes.get(1)).text();
 assertEquals("Test Text 2", text2);
 }

 @Test
 public void testNormaliseMethodWithEmptyDocument() {
 Document doc = new Document(baseUri);
 doc.normalise();
 List<Element> elements = doc.select("body > *");
 assertTrue(elements.isEmpty());
 }

 @Test
 public void testOuterHtmlMethod() {
 Document doc = new Document(baseUri);
 doc.appendElement("html").appendElement("head").appendElement("body").text("Test Text");
 String html = doc.outerHtml();
 assertEquals("<html><head></head><body>Test Text</body></html>", html);
 }

 @Test
 public void testTextMethod() {
 Document doc = new Document(baseUri);
 doc.body().text("Test Text");
 String text = doc.text();
 assertEquals("Test Text\n", text);
 }

 @Test
 public void testTextMethodWithEmptyDocument() {
 Document doc = new Document(baseUri);
 doc.text("Test Text");
 assertTrue(doc.body().childNodes().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testTextMethodWithNullText() {
 Document doc = new Document(baseUri);
 doc.text(null);
 }

 @Test
 public void testNodeNameMethod() {
 Document doc = new Document(baseUri);
 assertEquals("#document", doc.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentWithNullBaseUri() {
 Validate.testIllegalArgument(()-> new Document(null
));

 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateShellWithNullBaseUri() {
 Validate.testIllegalArgument(()-> Document.createShell(null));
 }

}