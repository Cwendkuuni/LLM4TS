package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element root = doc.getRootElement();
        assertEquals(Tag.HTML, root.tagName());
        assertEquals(baseUri, root.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element root = doc.getRootElement();
        assertEquals(Tag.HTML, root.tagName());
        assertEquals(baseUri, root.baseUri());
        assertEquals(Tag.HEAD, doc.head().tagName());
        assertEquals(Tag.BODY, doc.body().tagName());
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Document");
        Element head = doc.head();
        Element title = head.getElementsByTag("title").first();
        assertEquals("Test Document", title.text());

        doc.title("Updated Title");
        title = doc.head().getElementsByTag("title").first();
        assertEquals("Updated Title", title.text());

        Element body = doc.body();
        body.appendText("Hello World!");
        assertEquals("Hello World!", body.text());
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Document");
        assertEquals("Test Document", doc.title());

        doc.title("Updated Title");
        assertEquals("Updated Title", doc.title());

        doc.title(null);
        assertNull(doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element p = doc.createElement("p");
        assertEquals(Tag.P, p.tagName());
        assertEquals(baseUri, p.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element root = doc.getRootElement();

        root.appendElement(new TextNode("Text outside body"));
        root.appendElement(new TextNode("Text outside body"));
        root.appendElement(new Element("div"));

        Element body = doc.body();
        body.appendElement(new TextNode("Text inside body"));

        doc.normalise();

        List<Node> nodes = new ArrayList<>();
        for (Node node : root.childNodes()) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                if (!textNode.isBlank()) {
                    nodes.add(textNode);
                }
            }
        }

        assertEquals(3, nodes.size());
        assertEquals("Text inside body\nText outside body", body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        html.appendChild(head);
        html.appendChild(body);
        doc.appendChild(html);

        Element title = doc.findFirstElementByTagName("title", html);
        assertNull(title);

        head.appendChild(title = doc.createElement("title"));

        title = doc.findFirstElementByTagName("title", html);
        assertEquals(title, head.getElementsByTag("title").first());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.outputSettings().prettyPrint(false);
        Element p = doc.createElement("p");
        p.text("Hello World!");
        doc.body().appendChild(p);

        Document clonedDoc = doc.clone();
        assertEquals(doc, clonedDoc);
        assertNotEquals(doc.outputSettings(), clonedDoc.outputSettings());
        assertEquals("Hello World!", clonedDoc.body().getElementsByTag("p").first().text());
    }
}