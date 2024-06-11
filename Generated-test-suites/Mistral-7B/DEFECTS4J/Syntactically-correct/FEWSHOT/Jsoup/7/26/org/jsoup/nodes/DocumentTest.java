package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
        Element html = doc.head();
        assertSame(doc, html.parent());
        Element head = html.child(0);
        Element body = head.child(0);
        assertEquals("html", html.nodeName());
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testAccessHeadAndBody() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Element body = doc.body();
        assertNull(head);
        assertNull(body);
        doc = Document.createShell("http://example.com");
        head = doc.head();
        body = doc.body();
        assertSame(doc, head.parent());
        assertSame(doc, body.parent());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element div = doc.createElement("div");
        assertEquals("div", div.nodeName());
        assertSame(doc, div.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.head().appendText("Test Title");
        doc.appendText("Test text in document");
        doc.body().appendText("Test text in body");
        doc.normalise();
        List<Node> documentTexts = new ArrayList<>();
        List<Node> headTexts = new ArrayList<>();
        List<Node> bodyTexts = new ArrayList<>();
        for (Node node : doc.childNodes()) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                if (textNode.parentNode() == doc) {
                    documentTexts.add(textNode);
                } else if (textNode.parentNode() == doc.head()) {
                    headTexts.add(textNode);
                } else if (textNode.parentNode() == doc.body()) {
                    bodyTexts.add(textNode);
                }
            }
        }
        assertEquals(3, documentTexts.size());
        assertEquals("Test text in document", documentTexts.get(0).text());
        assertEquals("Test text in body", documentTexts.get(2).text());
        assertEquals(1, headTexts.size());
        assertEquals("Test Title", headTexts.get(0).text());
        assertEquals(0, bodyTexts.size());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.createElement("title");
        html.appendChild(head);
        head.appendChild(title);
        head.appendChild(doc.createElement("meta"));
        head.appendChild(body);
        Element script = doc.createElement("script");
        body.appendChild(script);

        Element foundHtml = doc.findFirstElementByTagName("html", doc);
        Element foundHead = doc.findFirstElementByTagName("head", doc);
        Element foundBody = doc.findFirstElementByTagName("body", doc);
        Element foundTitle = doc.findFirstElementByTagName("title", doc);
        Element foundScript = doc.findFirstElementByTagName("script", doc);

        assertSame(html, foundHtml);
        assertSame(head, foundHead);
        assertSame(body, foundBody);
        assertSame(title, foundTitle);
        assertSame(script, foundScript);

        Element nonExistent = doc.findFirstElementByTagName("non-existent", doc);
        assertNull(nonExistent);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element title = doc.createElement("title");
        title.text("Test Title");
        head.appendChild(title);
        Element body = doc.appendElement("body");
        Element p = doc.createElement("p");
        p.text("Test text");
        body.appendChild(p);

        String documentHtml = doc.outerHtml();
        String expectedDocumentHtml = "<html><head><title>Test Title</title></head><body>" +
                "<p>Test text</p></body></html>";

        assertEquals(expectedDocumentHtml, documentHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.appendElement("body");
        TextNode textNode = doc.createTextNode("Test text");
        body.appendChild(textNode);

        String documentText = doc.text();
        String expectedDocumentText = "Test text";

        assertEquals(expectedDocumentText, documentText);
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}