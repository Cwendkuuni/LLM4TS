package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * Tests for Document class.
 */
public class DocumentTest {

    /**
     * Test document creation.
     */
    @Test
    public void testDocumentCreation() {
        Document doc = Jsoup.parse("", "base-uri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    /**
     * Test document createShell method.
     */
    @Test
    public void testDocumentCreateShell() {
        Document doc = Document.createShell("base-uri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertEquals(3, doc.childNodeSize());
        Element html = doc.head();
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
     * Test document head method.
     */
    @Test
    public void testDocumentHead() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "base-uri");
        Element head = doc.head();
        assertNotNull(head);
        Element title = head.getElementsByTag("title").first();
        assertNotNull(title);
        assertEquals("Test", title.text());
    }

    /**
     * Test document body method.
     */
    @Test
    public void testDocumentBody() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>", "base-uri");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    /**
     * Test document title method.
     */
    @Test
    public void testDocumentTitle() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "base-uri");
        String title = doc.title();
        assertEquals("Test", title);
    }

    /**
     * Test document title method with empty title.
     */
    @Test
    public void testDocumentTitleEmpty() {
        Document doc = Jsoup.parse("<html><head><title></title></head><body></body></html>", "base-uri");
        String title = doc.title();
        assertEquals("", title);
    }

    /**
     * Test document title method with no title element.
     */
    @Test
    public void testDocumentTitleNone() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>", "base-uri");
        String title = doc.title();
        assertEquals("", title);
    }

    /**
     * Test document title method setting.
     */
    @Test
    public void testDocumentTitleSet() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "base-uri");
        doc.title("New Title");
        String title = doc.title();
        assertEquals("New Title", title);
    }

    /**
     * Test document createElement method.
     */
    @Test
    public void testDocumentCreateElement() {
        Document doc = Jsoup.parse("<html><body></body></html>", "base-uri");
        Element element = doc.createElement("a");
        assertEquals("a", element.nodeName());
    }

    /**
     * Test document normalise method.
     */
    @Test
    public void testDocumentNormalise() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Text in body</p><p>Text in body</p><p>Text in root</p></body></html>", "base-uri");
        doc.normalise();
        assertEquals(3, doc.childNodeSize());
        Element html = doc.head();
        assertEquals(0, html.childNodeSize());
        Element body = doc.body();
        assertEquals(2, body.childNodeSize());
        TextNode firstText = (TextNode) body.child(0);
        assertEquals("Text in body", firstText.text());
        TextNode secondText = (TextNode) body.child(1);
        assertEquals("Text in body", secondText.text());
    }

    /**
     * Test document normaliseTextNodes method.
     */
    @Test
    public void testDocumentNormaliseTextNodes() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Text in body</p><p>Text in body</p><p>Text in root</p></p></body></html>", "base-uri");
        doc.normalise();
        assertEquals(3, doc.childNodeSize());
        Element html = doc.head();
        assertEquals(0, html.childNodeSize());
        Element body = doc.body();
        assertEquals(1, body.childNodeSize());
        Element p = (Element) body.child(0);
        assertEquals(2, p.childNodeSize());
        TextNode firstText = (TextNode) p.child(0);
        assertEquals("Text in body", firstText.text());
        TextNode secondText = (TextNode) p.child(1);
        assertEquals("", secondText.text());
    }

    /**
     * Test document normaliseStructure method.
     */
    @Test
    public void testDocumentNormaliseStructure() {
        Document doc = Jsoup.parse("<html><head></head><body><html><head></head><body><p>Text in body</p></body></html></body></html>", "base-uri");
        doc.normalise();
        assertEquals(1, doc.childNodeSize());
        Element html = doc.head();
        assertEquals(0, html.childNodeSize());
        Element body = doc.body();
        assertEquals(1, body.childNodeSize());
        Element htmlInBody = body.child(0);
        assertEquals(1, htmlInBody.childNodeSize());
        Element headInBody = htmlInBody.child(0);
        assertEquals(0, headInBody.childNodeSize());
        Element bodyInBody = htmlInBody.child(1);
        assertEquals(1, bodyInBody.childNodeSize());
        TextNode textNode = (TextNode) bodyInBody.child(0);
        assertEquals("Text in body", textNode.text());
    }

    /**
     * Test document outerHtml method.
     */
    @Test
    public void testDocumentOuterHtml() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Text in body</p></body></html>", "base-uri");
        doc.title("Test");
        doc.head().appendElement("meta").attr("charset", "UTF-8");
        String html = doc.outerHtml();
        assertEquals("<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body><p>Text in body</p></body></html>", html);
    }

    /**
     * Test document text method.
     */
    @Test
    public void testDocumentText() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Text in body</p></body></html>", "base-uri");
        doc.body().text("New Text");
        String text = doc.text();
        assertEquals("New Text", text);
    }

    /**
     * Test document nodeName method.
     */
    @Test
    public void testDocumentNodeName() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>", "base-uri");
        assertEquals("#document", doc.nodeName());
    }

    /**
     * Test document clone method.
     */
    @Test
    public void testDocumentClone() {
        Document doc = Jsoup.parse("<html><head></head><body><p>Text in body</p></body></html>", "base-uri");
        OutputSettings outputSettings = doc.outputSettings().clone();
        Document cloned = doc.clone();
        assertEquals(doc.outerHtml(), cloned.outerHtml());
        assertEquals(doc.outputSettings(), cloned.outputSettings());
        assertEquals(doc.outputSettings().charset().name(), cloned.outputSettings().charset().name());
        assertEquals(outputSettings.prettyPrint(), cloned.outputSettings().prettyPrint());
    }
}