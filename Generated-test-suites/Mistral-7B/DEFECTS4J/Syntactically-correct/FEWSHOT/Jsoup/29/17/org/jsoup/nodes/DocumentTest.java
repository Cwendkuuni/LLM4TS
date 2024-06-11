package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("Test Document");
        Element body = html.appendElement("body");
        Element p = body.appendElement("p");

        Element found = doc.findFirstElementByTagName("html", doc);
        assertSame(html, found);

        found = doc.findFirstElementByTagName("head", doc);
        assertSame(head, found);

        found = doc.findFirstElementByTagName("title", doc);
        assertSame(title, found);

        found = doc.findFirstElementByTagName("body", doc);
        assertSame(body, found);

        found = doc.findFirstElementByTagName("div", doc);
        assertNull(found);
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element title = doc.title("Test Document");
        assertEquals("Test Document", doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element element = doc.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormaliseTextNodes() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element head = doc.appendElement("html").appendElement("head");
        Element textNode1 = head.appendElement(new TextNode("Text 1"));
        Element textNode2 = head.appendElement(new TextNode("Text 2"));
        Element textNode3 = head.appendElement(new TextNode("Text 3"));
        Element body = doc.appendElement("html").appendElement("body");

        doc.normalise();

        List<Node> nodes = new ArrayList<>();
        for (Node node : head.childNodes()) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                if (!textNode.isBlank())
                    nodes.add(textNode);
            }
        }

        assertEquals(3, nodes.size());
        assertEquals("Text 1Text 2Text 3", textNodesToString(nodes));
    }

    @Test
    public void testNormaliseStructure() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head1 = html.appendElement("head");
        Element body1 = html.appendElement("body");
        Element head2 = html.appendElement("head");
        Element body2 = html.appendElement("body");

        doc.normalise();

        assertEquals(1, doc.getElementsByTag("head").size());
        assertEquals(1, doc.getElementsByTag("body").size());
    }

    private String textNodesToString(List<Node> nodes) {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.text());
        }
        return sb.toString();
    }
}