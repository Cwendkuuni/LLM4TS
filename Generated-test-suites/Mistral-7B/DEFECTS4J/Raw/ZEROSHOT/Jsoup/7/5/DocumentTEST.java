package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.TextNode;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 4 test file for Document class.
 */
public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any test data or setup required for all tests.
    }

    @org.junit.After
    public void tearDown() {
        // Clean up any test data or resources used.
    }

    /**
     * Test constructor with valid baseUri.
     */
    @org.junit.Test
    public void testDocumentConstructorValidBaseUri() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("#document", document.nodeName());
    }

    /**
     * Test constructor with null baseUri.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testDocumentConstructorNullBaseUri() {
        String baseUri = null;
        new Document(baseUri);
    }

    /**
     * Test createShell method with valid baseUri.
     */
    @org.junit.Test
    public void testDocumentCreateShellValidBaseUri() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("#document", document.nodeName());
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element body = html.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    /**
     * Test createShell method with null baseUri.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testDocumentCreateShellNullBaseUri() {
        String baseUri = null;
        Document.createShell(baseUri);
    }

    /**
     * Test head method with document having no head element.
     */
    @org.junit.Test
    public void testDocumentHeadEmpty() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element head = document.head();
        assertNull(head);
    }

    /**
     * Test head method with document having head element.
     */
    @org.junit.Test
    public void testDocumentHeadPresent() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    /**
     * Test body method with document having no body element.
     */
    @org.junit.Test
    public void testDocumentBodyEmpty() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        assertNull(body);
    }

    /**
     * Test body method with document having body element.
     */
    @org.junit.Test
    public void testDocumentBodyPresent() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    /**
     * Test title method with document having no title element.
     */
    @org.junit.Test
    public void testDocumentTitleEmpty() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        String title = document.title();
        assertEquals("", title);
    }

    /**
     * Test title method with document having title element.
     */
    @org.junit.Test
    public void testDocumentTitlePresent() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    /**
     * Test title method with document having multiple title elements.
     */
    @org.junit.Test
    public void testDocumentTitleMultiple() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element title1 = document.head().appendElement("title").text("Title 1");
        Element title2 = document.head().appendElement("title").text("Title 2");
        String title = document.title();
        assertEquals("Title 1", title);
    }

    /**
     * Test createElement method with valid tagName.
     */
    @org.junit.Test
    public void testDocumentCreateElementValidTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element newElement = document.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.nodeName());
    }

    /**
     * Test createElement method with invalid tagName.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDocumentCreateElementInvalidTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.createElement("invalidTag");
    }

    /**
     * Test normalise method with document having no text nodes outside body.
     */
    @org.junit.Test
    public void testDocumentNormaliseEmpty() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element textNode = document.body().appendElement("p").appendText("Test Text");
        assertEquals(1, document.numberOfChildNodes());
        document.normalise();
        assertEquals(1, document.numberOfChildNodes());
    }

    /**
     * Test normalise method with document having text nodes outside body.
     */
    @org.junit.Test
    public void testDocumentNormaliseOutsideBody() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element textNode = document.appendElement("p").appendText("Test Text 1");
        document.text("Test Text 2");

        assertEquals(3, document.numberOfChildNodes());
        document.normalise();
        assertEquals(2, document.numberOfChildNodes());
        assertEquals("Test Text 2", document.text().trim());
        assertEquals("Test Text 1", textNode.text().trim());
    }

    /**
     * Test normaliseTextNodes method with empty Element.
     */
    @org.junit.Test
    public void testDocumentNormaliseTextNodesEmpty() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals(0, document.numberOfChildNodes());
        document.normaliseTextNodes(document);
        assertEquals(0, document.numberOfChildNodes());
    }

    /**
     * Test normaliseTextNodes method with Element having no TextNodes.
     */
    @org.junit.Test
    public void testDocumentNormaliseTextNodesNoTextNodes() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.appendElement("p");
        assertEquals(1, document.numberOfChildNodes());
        document.normaliseTextNodes(element);
        assertEquals(1, document.numberOfChildNodes());
    }

    /**
     * Test normaliseTextNodes method with Element having TextNodes.
     */
    @org.junit.Test
    public void testDocumentNormaliseTextNodesWithTextNodes() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element element = document.appendElement("p");
        TextNode textNode1 = element.appendText("Test Text 1 ");
        TextNode textNode2 = element.appendText("Test Text 2");
        assertEquals(4, document.numberOfChildNodes());
        document.normaliseTextNodes(element);
        assertEquals(3, document.numberOfChildNodes());
        assertEquals("Test Text 1 Test Text 2", element.text().trim());
    }
}