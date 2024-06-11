package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Document.createShell("http://example.com");
 }

 /**
 * Test constructor
 */
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 }

 /**
 * Test createShell method
 */
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 /**
 * Test head method
 */
 @Test
 public void testHeadMethod() {
 Element head = doc.head();
 assertNotNull(head);
 }

 /**
 * Test body method
 */
 @Test
 public void testBodyMethod() {
 Element body = doc.body();
 assertNotNull(body);
 }

 /**
 * Test title method
 */
 @Test
 public void testTitleMethod() {
 String title = doc.title();
 assertEquals("", title);
 doc.title("Test Title");
 title = doc.title();
 assertEquals("Test Title", title);
 }

 /**
 * Test title method with null title
 */
 @Test(expected = NullPointerException.class)
 public void testTitleMethodWithNullTitle() {
 doc.title(null);
 }

 /**
 * Test createElement method
 */
 @Test
 public void testCreateElementMethod() {
 Element element = doc.createElement("div");
 assertNotNull(element);
 }

 /**
 * Test normalise method
 */
 @Test
 public void testNormaliseMethod() {
 doc.title("Test Title");
 doc.text("Test Text");
 doc.body().appendElement("Test Element");
 doc.normalise();
 Element body = doc.body();
 List<Node> bodyChildren = body.childNodes();
 assertEquals(3, bodyChildren.size());
 assertEquals("Test Title", doc.title());
 assertEquals("Test Text", body.text());
 }

 /**
 * Test outerHtml method
 */
 @Test
 public void testOuterHtmlMethod() {
 doc.title("Test Title");
 doc.text("Test Text");
 doc.body().appendElement("Test Element");
 doc.normalise();
 String html = doc.outerHtml();
 assertTrue(html.startsWith("<html><head><title>Test Title</title></head><body>Test Text<div>Test Element</div></body></html>"));
 }

 /**
 * Test text method
 */
 @Test
 public void testTextMethod() {
 doc.text("Test Text");
 assertEquals("Test Text", doc.body().text());
 }

 /**
 * Test nodeName method
 */
 @Test
 public void testNodeNameMethod() {
 assertEquals("#document", doc.nodeName());
 }

 /**
 * Test getElementsByTag method with empty list
 */
 @Test
 public void testGetElementsByTagEmptyList() {
 List<Element> elements = doc.getElementsByTag("div");
 assertEquals(0, elements.size());
 }

 /**
 * Test getElementsByTag method with existing elements
 */
 @Test
 public void testGetElementsByTagExistingElements() {
 doc.body().appendElement("div");
 doc.body().appendElement("div");
 List<Element> elements = doc.getElementsByTag("div");
 assertEquals(2, elements.size());
 }

 /**
 * Test getElementsByTag method with null tag
 */
 @Test(expected = NullPointerException.class)
 public void testGetElementsByTagNullTag() {
 doc.getElementsByTag(null);
 }

 /**
 * Test first method with empty list
 */
 @Test
 public void testFirstEmptyList() {
 List<Element> elements = doc.getElementsByTag("div");
 Element first = elements.isEmpty() ? null : elements.get(0);
 assertNull(first);
 }

 /**
 * Test first method with existing elements
 */
 @Test
 public void testFirstExistingElements() {
 doc.body().appendElement("div");
 doc.body().appendElement("div");
 Element first = doc.getElementsByTag("div").first();
 assertNotNull(first);
 }

 /**
 * Test select method with empty list
 */
 @Test
 public void testSelectEmptyList() {
 List<Element> elements = doc.select("div");
 assertEquals(0, elements.size());
 }

 /**
 * Test select method with existing elements
 */
 @Test
 public void testSelectExistingElements() {
 doc.body().appendElement("div");
 doc.body().appendElement("div");
 List<Element> elements = doc.select("div");
 assertEquals(2, elements.size());
 }

 /**
 * Test select method with null selector
 */
 @Test(expected = NullPointerException.class)
 public void testSelectNullSelector() {
 doc.select(null);
 }

 /**
 * Test appendChild method with null child
 */
 @Test(expected = NullPointerException.class)
 public void testAppendChildNullChild() {
 doc.appendChild(null);
 }

 /**
 * Test appendElement method with null tag
 */
 @Test(expected = NullPointerException.class)
 public void testAppendElementNullTag() {
 doc.appendElement(null);
 }

 /**
 * Test removeChild method with null child
 */
 @Test(expected = NullPointerException.class)
 public void testRemoveChildNullChild() {
 doc.removeChild(null);
 }

 /**
 * Test validate method with null baseUri
 */
 @Test(expected = NullPointerException.class)
 public void testValidateNullBaseUri() {
 Document.validate(null);
 }
}