package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals("baseUri", document.baseUri());
        assertTrue(document.head() == null);
        assertTrue(document.body() == null);
    }

    /**
     * Test createShell method.
     */
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals("baseUri", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    /**
     * Test head method.
     */
    @Test
    public void testHeadMethod() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "baseUri");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        assertEquals("baseUri", head.baseUri());
        assertEquals("Test", head.title());
        assertEquals("Test", document.title());
    }

    /**
     * Test body method.
     */
    @Test
    public void testBodyMethod() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "baseUri");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
        assertEquals("baseUri", body.baseUri());
        assertTrue(body.text().isEmpty());
    }

    /**
     * Test title method.
     */
    @Test
    public void testTitleMethod() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "baseUri");
        String title = document.title();
        assertEquals("Test", title);
    }

    /**
     * Test title method with empty title.
     */
    @Test
    public void testTitleMethodEmpty() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "baseUri");
        String title = document.title();
        assertEquals("", title);
    }

    /**
     * Test title method with no title element.
     */
    @Test
    public void testTitleMethodNoTitle() {
        Document document = Jsoup.parse("<html><body></body></html>", "baseUri");
        String title = document.title();
        assertEquals("", title);
    }

    /**
     * Test title method with set title.
     */
    @Test
    public void testTitleMethodSet() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "baseUri");
        document.title("New Title");
        assertEquals("New Title", document.title());
        assertEquals("New Title", document.head().title().text());
    }

    /**
     * Test createElement method.
     */
    @Test
    public void testCreateElementMethod() {
        Document document = Jsoup.parse("<html><body></body></html>", "baseUri");
        Element newElement = document.createElement("a");
        assertNotNull(newElement);
        assertEquals("a", newElement.nodeName());
        assertEquals("baseUri", newElement.baseUri());
    }

    /**
     * Test normalise method.
     */
    @Test
    public void testNormaliseMethod() {
        Document document = Jsoup.parse("<html><head></head><body><p>Test</p><div>Text</div></body></html>", "baseUri");
        document.normalise();
        assertEquals(2, document.numberOfChildren());
        assertEquals("body", document.getFirstChild().nodeName());
        assertEquals("p", document.getFirstChild().getFirstChild().nodeName());
        assertEquals("Test", document.getFirstChild().getFirstChild().text());
    }

    /**
     * Test normaliseTextNodes method.
     */
    @Test
    public void testNormaliseTextNodesMethod() {
        Document document = Jsoup.parse("<html><head></head><body><p>Test <div>Text</div></p></body></html>", "baseUri");
        document.normalise();
        assertEquals(1, document.numberOfChildren());
        Element bodyElement = document.getFirstChild();
        assertEquals("body", bodyElement.nodeName());
        Element pElement = bodyElement.getFirstChild();
        assertEquals("p", pElement.nodeName());
        assertEquals(2, pElement.numberOfChildren());
        TextNode textNode1 = (TextNode) pElement.getFirstChild();
        assertEquals("Test", textNode1.text());
        TextNode textNode2 = (TextNode) pElement.getChild(1);
        assertEquals("Text", textNode2.text());
    }

    /**
     * Test normaliseStructure method.
     */
    @Test
    public void testNormaliseStructureMethod() {
        Document document = Jsoup.parse("<html><head></head><body><p>Test 1</p><p>Test 2</p></body></html>", "baseUri");
        document.normalise();
        assertEquals(1, document.numberOfChildren());
        Element bodyElement = document.getFirstChild();
        assertEquals("body", bodyElement.nodeName());
        assertEquals(1, bodyElement.numberOfChildren());
        Element pElement1 = (Element) bodyElement.getFirstChild();
        assertEquals("p", pElement1.nodeName());
        assertEquals("Test 1", pElement1.text());
    }

    /**
     * Test findFirstElementByTagName method.
     */
    @Test
    public void testFindFirstElementByTagNameMethod() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></body></html>", "baseUri");
        Element head = document.findFirstElementByTagName("head", document);
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        Element title = document.findFirstElementByTagName("title", document);
        assertNotNull(title);
        assertEquals("title", title.nodeName());
        assertEquals("Test", title.text());
        Element body = document.findFirstElementByTagName("body", document);
        assertNotNull(body);
        assertEquals("body", body.nodeName());
        assertEquals(1, body.numberOfChildren());
        Element p = document.findFirstElementByTagName("p", document);
        assertNotNull(p);
        assertEquals("p", p.nodeName());
        assertEquals("Test", p.text());
        Element nonExistent = document.findFirstElementByTagName("nonExistent", document);
        assertNull(nonExistent);
    }

    /**
     * Test outerHtml method.
     */
    @Test
    public void testOuterHtmlMethod() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></body></html>", "baseUri");
        String html = document.outerHtml();
        assertEquals("<html><head><title>Test</title></head><body><p>Test</p></body></html>", html);
    }

    /**
     * Test text method.
     */
    @Test
    public void testTextMethod() {
        Document document = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>", "baseUri");
        document.text("New Text");
        assertEquals("New Text", document.text());
        assertEquals("New Text", document.head().text());
        assertEquals("New Text", document.body().text());
        assertEquals("New Text", document.getFirstChild().text());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeNameMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "baseUri");
        assertEquals("#document", document.nodeName());
        Element element = document.createElement("a");
        assertEquals("a", element.nodeName());
    }

    /**
     * Test clone method.
     */
    @Test
    public void testCloneMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "baseUri");
        Document clone = document.clone();
        assertEquals(document, clone);
        assertNotSame(document, clone);
        assertEquals("#document", clone.nodeName());
        assertFalse(document.outputSettings().equals(clone.outputSettings()));
    }

    /**
     * Test outputSettings method.
     */
    @Test
    public void testOutputSettingsMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "baseUri");
        OutputSettings outputSettings = document.outputSettings();
        assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        OutputSettings newOutputSettings = new OutputSettings();
        newOutputSettings.escapeMode(Entities.EscapeMode.extended);
        document.outputSettings(newOutputSettings);
        OutputSettings outputSettingsNew = document.outputSettings();
        assertEquals(Entities.EscapeMode.extended, outputSettingsNew.escapeMode());
        assertEquals(outputSettings, document.outputSettings());
    }

    /**
     * Test quirksMode method.
     */
    @Test
    public void testQuirksModeMethod() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "baseUri");
        QuirksMode quirksMode = QuirksMode.noQuirks;
        document.quirksMode(quirksMode);
        QuirksMode quirksModeDocument = document.quirksMode();
        assertEquals(quirksMode, quirksModeDocument);
    }
}