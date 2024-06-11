package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.Test;

/**
 * Test class for Document.
 */
public class DocumentTest {

    /**
     * Test constructor.
     */
    @Test
    public void testDocumentConstructor() {
        final String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
        assertNull(document.title());
    }

    /**
     * Test createShell method.
     */
    @Test
    public void testCreateShell() {
        final String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertNull(document.title());
    }

    /**
     * Test head method.
     */
    @Test
    public void testHeadMethod() {
        final String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    /**
     * Test body method.
     */
    @Test
    public void testBodyMethod() {
        final String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    /**
     * Test title method.
     */
    @Test
    public void testTitleMethod() {
        final String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    /**
     * Test setTitle method.
     */
    @Test
    public void testSetTitleMethod() {
        final String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        document.title("New Test Title");
        assertEquals("New Test Title", document.title());
    }

    /**
     * Test createElement method.
     */
    @Test
    public void testCreateElementMethod() {
        final String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    /**
     * Test normalise method.
     */
    @Test
    public void testNormaliseMethod() {
        final String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        document.head().appendElement("p").text("Head Title");
        document.body().appendElement("p").text("Body Title");
        document.appendElement("p").text("Root Title");

        document.normalise();

        List<Element> titles = document.select("title, p");
        assertEquals(1, titles.size());
        assertEquals("Test Title", titles.get(0).text().trim());

        List<Element> heads = document.select("head p");
        assertEquals(1, heads.size());
        assertEquals("Head Title", heads.get(0).text().trim());

        List<Element> bodies = document.select("body p");
        assertEquals(2, bodies.size());
        assertEquals("Body Title", bodies.get(0).text().trim());
        assertEquals("", bodies.get(1).text().trim());

        List<TextNode> rootTitles = document.select("p:has(text()) ~ text()");
        assertEquals(1, rootTitles.size());
        assertEquals("Root Title", rootTitles.get(0).text().trim());
    }
}