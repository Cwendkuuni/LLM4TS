package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Document class.
 */
public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document("https://example.com");
 }

 /**
 * Test constructor.
 */
 @Test
 public void testDocumentConstruction() {
 Document doc = new Document("https://example.com");
 assertSame(Tag.valueOf("#root"), doc.tagName());
 assertSame("https://example.com", doc.baseUri());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 /**
 * Test createShell method.
 */
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("https://example.com");
 assertSame(Tag.HTML, doc.head().tagName());
 assertSame(Tag.HEAD, doc.head().getElementsByTag("head").first().tagName());
 assertSame(Tag.BODY, doc.body().tagName());
 }

 /**
 * Test head method.
 */
 @Test
 public void testHeadMethod() {
 Element headEl = doc.head();
 assertSame(Tag.HEAD, headEl.tagName());
 assertNull(headEl.text());
 }

 /**
 * Test body method.
 */
 @Test
 public void testBodyMethod() {
 Element bodyEl = doc.body();
 assertSame(Tag.BODY, bodyEl.tagName());
 assertNull(bodyEl.text());
 }

 /**
 * Test title method.
 */
 @Test
 public void testTitleMethod() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 /**
 * Test createElement method.
 */
 @Test
 public void testCreateElementMethod() {
 Element newElement = doc.createElement("a");
 assertSame(Tag.A, newElement.tagName());
 assertSame("https://example.com", newElement.baseUri());
 }

 /**
 * Test normalise method.
 */
 @Test
 public void testNormaliseMethod() {
 doc.appendElement("html").appendElement("head").appendText("Test Heading");
 doc.appendElement("body").appendText("Test Body");
 doc.appendText("Test Text");
 doc.normalise();
 List<Node> bodyNodes = doc.body().childNodes();
 assertEquals(3, bodyNodes.size());
 assertEquals("Test Body", bodyNodes.get(0).text());
 assertEquals("Test Heading", doc.head().text());
 }

 /**
 * Test title method with null title.
 */
 @Test(expected = NullPointerException.class)
 public void testTitleMethodWithNullTitle() {
 doc.title(null);
 }

 /**
 * Test createDocumentWithNullBaseUri method.
 */
 @Test(expected = NullPointerException.class)
 public void testCreateDocumentWithNullBaseUri() {
 new Document(null);
 }

 /**
 * Test createDocumentWithEmptyBaseUri method.
 */
 @Test
 public void testCreateDocumentWithEmptyBaseUri() {
 Document doc = new Document("");
 assertEquals("", doc.baseUri());
 }

 /**
 * Test createDocumentWithInvalidBaseUri method.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCreateDocumentWithInvalidBaseUri() {
 new Document("javascript:someFunction()");
 }

}