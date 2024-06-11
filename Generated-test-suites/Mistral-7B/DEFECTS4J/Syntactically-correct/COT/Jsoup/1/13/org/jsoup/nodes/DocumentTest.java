package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    /**
     * Test constructor.
     */
    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
    }

    /**
     * Test createShell method.
     */
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    /**
     * Test head method.
     */
    @Test
    public void testHeadMethod() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        Element head = doc.head();
        assertNotNull(head);
    }

    /**
     * Test body method.
     */
    @Test
    public void testBodyMethod() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        Element body = doc.body();
        assertNotNull(body);
    }

    /**
     * Test title method.
     */
    @Test
    public void testTitleMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
        String title = doc.title();
        assertEquals("Test Title", title);

        doc = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        title = doc.title();
        assertEquals("", title);
    }

    /**
     * Test title method with null title.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTitleMethodWithNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    /**
     * Test createElement method.
     */
    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("a");
        assertNotNull(element);
    }

    /**
     * Test normalise method.
     */
    @Test
    public void testNormaliseMethod() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>", "http://example.com");
        doc.normalise();
        List<Element> bodyElements = doc.body().children();
        assertEquals(1, bodyElements.size());
        Element pElement = bodyElements.get(0);
        assertEquals("p", pElement.tagName());
        assertEquals("Test", pElement.text());
    }

    /**
     * Test normalise method with empty document.
     */
    @Test
    public void testNormaliseMethodWithEmptyDocument() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        List<Element> bodyElements = doc.body().children();
        assertEquals(0, bodyElements.size());
    }

    /**
     * Test normalise method with empty head and body.
     */
    @Test
    public void testNormaliseMethodWithEmptyHeadAndBody() {
        Document doc = Jsoup.parse("<html><head></head></html>", "http://example.com");
        doc.normalise();
        List<Element> bodyElements = doc.body().children();
        assertEquals(0, bodyElements.size());
    }

    /**
     * Test normalise method with empty document and text inside.
     */
    @Test
    public void testNormaliseMethodWithEmptyDocumentAndTextInside() {
        Document doc = Jsoup.parse("<html></html>", "http://example.com");
        doc.text("Test");
        doc.normalise();
        List<Element> bodyElements = doc.body().children();
        assertEquals(1, bodyElements.size());
        TextNode textNode = (TextNode) bodyElements.get(0);
        assertEquals("Test", textNode.text());
    }

    /**
     * Test outerHtml method.
     */
    @Test
    public void testOuterHtmlMethod() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", "http://example.com");
        String html = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", html);
    }

    /**
     * Test text method.
     */
    @Test
    public void testTextMethod() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>", "http://example.com");
        doc.text("New Text");
        assertEquals("New Text", doc.body().text());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeNameMethod() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}