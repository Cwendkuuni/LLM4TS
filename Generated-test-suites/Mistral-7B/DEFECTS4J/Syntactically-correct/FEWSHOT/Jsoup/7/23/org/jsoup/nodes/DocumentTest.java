package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
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

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNull(head);
        doc = Document.createShell("baseUri");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNull(body);
        doc = Document.createShell("baseUri");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        assertEquals("", doc.title());
        doc = Document.createShell("baseUri");
        doc.title("testTitle");
        assertEquals("testTitle", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title("testTitle");
        assertEquals("testTitle", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = Document.createShell("baseUri");
        doc.head().appendText("test1");
        doc.body().appendText("test2");
        doc.appendText("test3");
        doc.normalise();
        Element body = doc.body();
        List<Node> nodes = body.childNodes();
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof TextNode);
        TextNode node1 = (TextNode) nodes.get(0);
        assertEquals("test1", node1.text());
        assertTrue(nodes.get(1) instanceof TextNode);
        TextNode node2 = (TextNode) nodes.get(1);
        assertEquals("test2", node2.text());
        assertTrue(nodes.get(2) instanceof TextNode);
        TextNode node3 = (TextNode) nodes.get(2);
        assertEquals("test3", node3.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = head.appendElement("title");
        title.text("testTitle");
        Element link = body.appendElement("link");
        link.attr("rel", "stylesheet");
        link.attr("href", "test.css");
        Element script = body.appendElement("script");
        script.attr("src", "test.js");

        Element foundTitle = doc.findFirstElementByTagName("title", doc);
        Element foundLink = doc.findFirstElementByTagName("link", doc);
        Element foundScript = doc.findFirstElementByTagName("script", doc);

        assertEquals("title", foundTitle.nodeName());
        assertEquals("link", foundLink.nodeName());
        assertEquals("script", foundScript.nodeName());
        assertEquals("testTitle", foundTitle.text());
        assertEquals("test.css", ((Element) foundLink).attr("href"));
        assertEquals("test.js", ((Element) foundScript).attr("src"));

        Element notFound = doc.findFirstElementByTagName("meta", doc);
        assertNull(notFound);
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");
        title.text("testTitle");
        Element body = html.appendElement("body");
        body.appendText("testText");

        String actual = doc.outerHtml();
        String expected = "<html><head><title>testTitle</title></head><body>testText</body></html>";
        assertEquals(expected, actual);
    }

    @Test
    public void testTextDocument() {
        Document doc = new Document("baseUri");
        doc.text("testText");
        assertEquals("testText", doc.body().text());
    }
}