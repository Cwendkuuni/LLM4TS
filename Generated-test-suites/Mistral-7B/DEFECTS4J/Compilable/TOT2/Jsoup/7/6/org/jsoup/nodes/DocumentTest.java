package org.jsoup.nodes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

 // Test for createDocument method
 @Test
 public void testCreateDocument() {
 Document document = new Document("baseUri");
 Assert.assertNotNull(document);
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document document = Document.createShell("baseUri");
 Assert.assertNotNull(document);
 Assert.assertNotNull(document.head());
 Assert.assertNotNull(document.body());
 }

 // Test for head method
 @Test
 public void testHead() {
 Document document = new Document("baseUri");
 Element head = document.head();
 Assert.assertNotNull(head);
 }

 // Test for body method
 @Test
 public void testBody() {
 Document document = new Document("baseUri");
 Element body = document.body();
 Assert.assertNotNull(body);
 }

 // Test for title method (normal case)
 @Test
 public void testTitleNormalCase() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 String title = document.title();
 Assert.assertEquals("Test Title", title);
 }

 // Test for title method (null input)
 @Test(expected = NullPointerException.class)
 public void testTitleNullInput() {
 Document document = new Document("baseUri");
 document.title(null);
 }

 // Test for title method (empty string input)
 @Test
 public void testTitleEmptyStringInput() {
 Document document = new Document("baseUri");
 document.title("");
 String title = document.title();
 Assert.assertEquals("", title);
 }

 // Test for createElement method (normal case)
 @Test
 public void testCreateElementNormalCase() {
 Document document = new Document("baseUri");
 Element element = document.createElement("p");
 Assert.assertNotNull(element);
 }

 // Test for createElement method (null input)
 @Test(expected = NullPointerException.class)
 public void testCreateElementNullInput() {
 Document document = new Document("baseUri");
 document.createElement(null);
 }

 // Test for normalise method (no text nodes to move)
 @Test
 public void testNormaliseNoTextNodesToMove() {
 Document document = new Document("baseUri");
 Element body = document.body();
 body.appendText("Hello World");
 String expectedHtml = "<html><head></head><body>Hello World</body></html>";
 String actualHtml = document.outerHtml();
 Assert.assertEquals(expectedHtml, actualHtml);
 }

 // Test for normalise method (text nodes to move)
 @Test
 public void testNormaliseTextNodesToMove() {
 Document document = new Document("baseUri");
 Element html = document.appendElement("html");
 Element head = html.appendElement("head");
 Element body = html.appendElement("body");
 body.appendText("Hello World");
 head.appendText("Test Title");
 String expectedHtml = "<html><head><title>Test Title</title></head><body>Hello World</body></html>";
 String actualHtml = document.normalise().outerHtml();
 Assert.assertEquals(expectedHtml, actualHtml);
 }
}