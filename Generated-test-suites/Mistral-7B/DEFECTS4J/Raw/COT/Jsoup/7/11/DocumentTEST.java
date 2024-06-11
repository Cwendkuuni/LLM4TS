package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.output.OutputSettings;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document createDocument() {
 return new Document("baseUri");
 }

 @Test
 public void testDocumentConstructor() {
 Document doc = createDocument();
 assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testHeadAccessor() {
 Document doc = createDocument();
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test
 public void testBodyAccessor() {
 Document doc = createDocument();
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testTitleAccessor() {
 Document doc = createDocument();
 String title = doc.title();
 assertEquals("", title);
 doc.title("Test Title");
 title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testTitleSetter() {
 Document doc = createDocument();
 doc.title("Test Title");
 String title = doc.title();
 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = createDocument();
 Element element = doc.createElement("a");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document doc = createDocument();
 doc.title("Test Title");
 Element titleElement = doc.getElementsByTag("title").first();
 text(titleElement, "Test Title Modified");
 doc.normalise();
 String title = doc.title();
 assertEquals("Test Title Modified", title);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUriInConstructor() {
 Document doc = new Document(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagNameInCreateElement() {
 Document doc = createDocument();
 try {
 doc.createElement(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = createDocument();
 Element html = doc.appendElement("html");
 Element head = doc.appendElement("head");
 Element body = doc.appendElement("body");
 Element element = doc.findFirstElementByTagName("html", doc);
 assertEquals(html, element);
 element = doc.findFirstElementByTagName("head", body);
 assertEquals(head, element);
 }

 @Test
 public void testOutputSettings() {
 Document doc = createDocument();
 OutputSettings outputSettings = doc.outputSettings();
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 assertEquals("UTF-8", outputSettings.charset().name());
 assertEquals(true, outputSettings.prettyPrint());
 assertEquals(1, outputSettings.indentAmount());
 }

 @Test
 public void testTitleWithEmptyString() {
 Document doc = createDocument();
 String title = doc.title();
 assertEquals("", title);
 }

 @Test
 public void testTitleWithNull() {
 Document doc = createDocument();
 try {
 doc.title(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testTitleWithEmptyStringAndNull() {
 Document doc = createDocument();
 doc.title("");
 try {
 doc.title(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testTextNodesNormalisation() {
 Document doc = createDocument();
 doc.appendText("Test 1 ");
 doc.appendText("Test 2");
 doc.appendText("Test 3");
 doc.normalise();
 assertEquals(3, doc.numberOfChildren());
 }

 @Test
 public void testDocumentsWithSameBaseUriAreEqual() {
 Document doc1 = new Document("baseUri");
 Document doc2 = new Document("baseUri");
 assertTrue(doc1.equals(doc2));
 }

 @Test
 public void testDocumentsWithDifferentBaseUriAreNotEqual() {
 Document doc1 = new Document("baseUri1");
 Document doc2 = new Document("baseUri2");
 assertFalse(doc1.equals(doc2));
 }

 @Test
 public void testDocumentsWithSameChildrenAndOutputSettingsAreEqual() {
 Document doc1 = new Document("baseUri") {{
 appendElement("html").appendElement("head").appendElement("body");
 appendText("Test");
 title("Test Title");
 outputSettings().prettyPrint(false);
 }};
 Document doc2 = new Document("baseUri") {{
 appendElement("html").appendElement("head").appendElement("body");
 appendText("Test");
 title("Test Title");
 outputSettings().prettyPrint(false);
 }};
 assertTrue(doc1.equals(doc2));
 }

 @Test
 public void testDocumentsWithDifferentChildrenAreNotEqual() {
 Document doc1 = new Document("baseUri") {{
 appendElement("html").appendElement("head").appendElement("body");
 appendText("Test 1");
 title("Test Title 1");
 }};
 Document doc2 = new Document("baseUri") {{
 appendElement("html").appendElement("head").appendElement("body");
 appendText("Test 2");
 title("Test Title 2");
 }};
 assertFalse(doc1.equals(doc2));
 }

 @Test
 public void testDocumentsWithDifferentOutputSettingsAreNotEqual() {
 Document doc1 = new Document("baseUri") {{
 appendElement("html").appendElement("head").appendElement("body");
 appendText("Test");
 title("Test Title");
 outputSettings().prettyPrint(false);
 }};
 Document doc2 = new Document("baseUri") {{
 appendElement("html").appendElement("head").appendElement("body");
 appendText("Test");
 title("Test Title");
 }};
 assertFalse(doc1.equals(doc2));
 }
}