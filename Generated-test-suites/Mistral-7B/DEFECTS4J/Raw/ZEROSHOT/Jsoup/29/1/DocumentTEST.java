package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 *
 * @author [Your Name]
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNull(document.head());
        assertNull(document.body());
        assertNull(document.title());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertNull(document.title());
        Element html = document.head();
        assertEquals("html", html.tagName());
        Element head = html.getFirstElementChild();
        assertEquals("head", head.tagName());
        Element body = html.getLastElementChild();
        assertEquals("body", body.tagName());
    }

    @Test
    public void testSetAndGetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = new Document("http://example.com");
        document.body().appendText("This is a test");
        document.appendText(" Test text");

        document.normalise();

        assertEquals("html<head><title></title><body>This is a test Test text</body></html>", document.outerHtml());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = new Document("http://example.com");
        Element body = document.body();

        body.appendText("Test 1 ");
        body.appendText("Test 2 ");
        Element title = document.createElement("title");
        title.appendText("Test Title ");
        body.insertBefore(title, body.getFirstChild());

        document.normalise();

        List<Node> nodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            nodes.add(node);
        }

        assertEquals(3, nodes.size());
        assertEquals("Test 1 ", nodes.get(0).text());
        assertEquals("Test 2 ", nodes.get(1).text());
        assertEquals("Test Title ", nodes.get(2).text());
    }

    @Test
    public void testNormaliseStructure() {
        Document document = new Document("http://example.com");
        Element html = document.createElement("html");
        Element head = document.createElement("head");
        Element body = document.createElement("body");
        Element head2 = document.createElement("head");
        Element body2 = document.createElement("body");

        html.appendChild(head);
        html.appendChild(body);
        document.appendChild(html);
        document.appendChild(head2);
        document.appendChild(body2);

        document.normalise();

        assertEquals(1, document.childNodes().size());
        assertEquals(3, document.getElementsByTag("html").size());
        assertEquals(1, document.getElementsByTag("head").size());
        assertEquals(1, document.getElementsByTag("body").size());
    }

    @Test
    public void testGetFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element html = document.createElement("html");
        Element head = document.createElement("head");
        Element body = document.createElement("body");
        Element title = document.createElement("title");
        Element title2 = document.createElement("title");
        Element h1 = document.createElement("h1");

        html.appendChild(head);
        html.appendChild(body);
        head.appendChild(title);
        head.appendChild(title2);
        body.appendChild(h1);
        document.appendChild(html);

        Element found = document.findFirstElementByTagName("title", this);
        assertEquals("title", found.tagName());
        assertEquals(title, found);

        found = document.findFirstElementByTagName("h1", this);
        assertEquals("h1", found.tagName());
        assertEquals(h1, found);

        found = document.findFirstElementByTagName("title", null);
        assertNull(found);

        found = document.findFirstElementByTagName("title", html);
        assertEquals("title", found.tagName());
        assertEquals(title, found);
    }

    @Test
    public void testHtmlOutput() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        document.outputSettings(outputSettings);

        Element title = document.createElement("title");
        title.text("Test Title");
        document.head().appendChild(title);

        Element p = document.createElement("p");
        p.text("This is a test");
        document.body().appendChild(p);

        String htmlOutput = document.outerHtml();

        assertEquals("<html><head><meta charset=\"UTF-8\"><title>Test Title</title></head><body><p>This is a test</p></body></html>",
                htmlOutput);
    }

    @Test
    public void testCloneDocument() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = new OutputSettings();
        document.outputSettings(outputSettings);

        Element title = document.createElement("title");
        title.text("Test Title");
        document.head().appendChild(title);

        Element p = document.createElement("p");
        p.text("This is a test");
        document.body().appendChild(p);

        Document clonedDocument = document.clone();

        OutputSettings clonedOutputSettings = clonedDocument.outputSettings();

        assertEquals(outputSettings, clonedOutputSettings);
        assertEquals(title.text(), clonedDocument.title());
        assertEquals(p.text(), clonedDocument.body().text());
    }
}