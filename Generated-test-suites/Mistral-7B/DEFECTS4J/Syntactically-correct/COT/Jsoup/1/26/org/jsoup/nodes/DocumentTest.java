package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang. Validate;
import org.apache.commons.lang.notnull.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test for Document class
 */
public class DocumentTest {

 @Test
 public void testDocumentConstructor() {
 Document document = new Document("baseUri");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testCreateShellConstructor() {
 Document document = Document.createShell("baseUri");
 assertNotNull(document);
 assertNotNull(document.head());
 assertNotNull(document.body());
 }

 @Test
 public void testHeadMethod() {
 Document document = new Document("baseUri");
 Element head = document.head();
 assertNotNull(head);
 }

 @Test
 public void testBodyMethod() {
 Document document = new Document("baseUri");
 Element body = document.body();
 assertNotNull(body);
 }

 @Test
 public void testTitleMethod_WhenTitleIsSet_ThenTitleIsSetCorrectly() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testTitleMethod_WhenTitleIsEmpty_ThenTitleIsEmpty() {
 Document document = new Document("baseUri");
 assertEquals("", document.title());
 }

 @Test
 public void testCreateElementMethod() {
 Document document = new Document("baseUri");
 Element element = document.createElement("testTag");
 assertNotNull(element);
 }

 @Test(expected = NullPointerException.class)
 public void testTitleMethod_WhenTitleIsNull() {
 Document document = new Document("baseUri");
 document.title(null);
 }

 @Test
 public void testNormaliseMethod() {
 Document document = new Document("baseUri");
 document.body().appendText("test text");
 document.head().appendText("test head text");
 document.appendText("test root text");
 document.normalise();
 List<Element> bodyElements = document.body().children();
 assertEquals(2, bodyElements.size());
 assertEquals("test text", bodyElements.get(0).text());
 assertEquals("", bodyElements.get(1).text());
 List<Element> headElements = document.head().children();
 assertEquals(1, headElements.size());
 assertEquals("test head text", headElements.get(0).text());
 List<Element> rootElements = document.children();
 assertEquals(3, rootElements.size());
 assertEquals("#document", rootElements.get(0).nodeName());
 assertEquals("html", rootElements.get(1).nodeName());
 assertEquals("head", rootElements.get(2).nodeName());
 }

 @Test
 public void testOuterHtmlMethod() {
 Document document = new Document("baseUri");
 document.body().appendText("test text");
 assertEquals("<html><head></head><body>test text</body></html>", document.outerHtml());
 }

 @Test
 public void testTextMethod() {
 Document document = new Document("baseUri");
 document.body().appendText("test text");
 document.text("new text");
 assertEquals("new text", document.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testTextMethod_WhenTextIsNull() {
 Document document = new Document("baseUri");
 document.text(null);
 }

 @Test
 public void testNodeNameMethod() {
 Document document = new Document("baseUri");
 assertEquals("#document", document.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateShellConstructor_WhenBaseUriIsNull() {
 Document document = Document.createShell(null);
 }

 @Test
 public void testCreateDocumentConstructor_WhenBaseUriIsNull() {
 Document document = new Document(null);
 }

 @Test
 public void testTitleMethod_WhenTitleContainNullCharacters() {
 Document document = new Document("baseUri");
 document.title("Test Title\u0000");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testTitleMethod_WhenTitleIsLongString() {
 Document document = new Document("baseUri");
 String longTitle = "Test Title";
 for (int i = 0; i < 1000; i++) {
 longTitle += "Test Title";
 }
 document.title(longTitle);
 assertEquals(longTitle, document.title());
 }
}