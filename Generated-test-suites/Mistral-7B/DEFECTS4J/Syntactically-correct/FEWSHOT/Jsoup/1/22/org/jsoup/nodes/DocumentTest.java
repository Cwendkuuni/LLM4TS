package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.TextNode;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateEmptyDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#root", doc.nodeName());
        assertTrue(doc.outerHtml().isEmpty());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("#root", doc.nodeName());
        assertTrue(!doc.outerHtml().isEmpty());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetTitleElement() {
        Document doc = new Document("baseUri");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitleElement() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.nodeName());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        doc.body().appendElement("Test Text");
        doc.body().appendElement(doc.createElement("div"));
        doc.body().appendText("Test Text 2");

        doc.normalise();

        Element titleEl = doc.getElementsByTag("title").first();
        assertEquals("Test Title", titleEl.text());

        Element bodyEl = doc.body();
        List<Node> bodyChildren = new ArrayList<>(bodyEl.childNodes());
        assertEquals(3, bodyChildren.size());
        assertEquals("Test Text", bodyChildren.get(0).text());
        assertTrue(bodyChildren.get(1) instanceof TextNode);
        assertTrue(bodyChildren.get(2) instanceof Document);
    }

    @Test
    public void testCreateDocumentWithInitialContent() {
        Document doc = new Document("baseUri") {
            {
                appendElement("html");
                Element html = appendElement("head");
                appendElement("title").text("Test Title");
                Element body = appendElement("body");
                appendElement("p").text("Test Text");
            }
        };

        assertEquals("Test Title", doc.title());
        assertEquals("Test Text", doc.select("p").first().text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Document(null);
    }
}