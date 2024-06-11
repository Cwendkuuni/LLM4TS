package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals(Tag.HTML, doc.tagName());
        assertEquals(baseUri, doc.baseUri());

        Document shell = Document.createShell(baseUri);
        assertEquals(Tag.HTML, shell.tagName());
        assertEquals(baseUri, shell.baseUri());
        assertTrue(shell.head() != null);
        assertTrue(shell.body() != null);
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = createTestDocument();
        Element head = doc.head();
        Element body = doc.body();

        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        Document doc = createTestDocument();
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document doc = createTestDocument();
        doc.title("Test Title");

        String originalTitle = doc.title();
        doc.title("New Title");
        String newTitle = doc.title();

        assertNotEquals(originalTitle, newTitle);
        assertEquals("New Title", newTitle);
    }

    @Test
    public void testCreateElement() {
        Document doc = createTestDocument();
        Element element = doc.createElement("p");

        assertEquals(Tag.P, element.tagName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalize() {
        Document doc = createTestDocument();
        doc.head().appendChild(new TextNode("Test"));
        doc.appendChild(new TextNode(" Test "));

        doc.normalize();

        assertTrue(doc.head().childNodes().isEmpty());
        assertEquals(" Test ", doc.getElementsByTag("body").first().text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeWithNullElement() {
        Document doc = createTestDocument();
        doc.normalizeStructure(null, doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeWithInvalidTag() {
        Document doc = createTestDocument();
        doc.normalizeStructure("INVALID", doc);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = createTestDocument();
        Element html = doc.findFirstElementByTagName("html", doc);

        assertNotNull(html);
        assertEquals(Tag.HTML, html.tagName());
    }

    @Test
    public void testOuterHtml() {
        Document doc = createTestDocument();
        doc.head().appendChild(new TextNode("Test"));
        doc.body().appendChild(new TextNode("Test"));

        String html = doc.outerHtml();

        assertTrue(html.startsWith("<html><head>"));
        assertTrue(html.endsWith("</body></html>"));
    }

    @Test
    public void testText() {
        Document doc = createTestDocument();
        doc.text("Test");

        assertEquals("Test", doc.text().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextWithNullText() {
        Document doc = createTestDocument();
        doc.text(null);
    }

    @Test
    public void testNodeName() {
        Document doc = createTestDocument();
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = createTestDocument();
        Document clone = doc.clone();

        assertNotSame(doc, clone);
        assertEquals(doc.outerHtml(), clone.outerHtml());
    }

    private Document createTestDocument() {
        Document doc = new Document("#");
        return doc;
    }

}