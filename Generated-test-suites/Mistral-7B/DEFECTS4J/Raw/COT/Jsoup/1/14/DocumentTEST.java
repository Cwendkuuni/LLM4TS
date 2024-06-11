package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertNotNull(document);
        Element html = document.select("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        assertNotNull(head);
    }

    @Test(expected = NullPointerException.class)
    public void testHeadMethodWithNullBaseUri() {
        new Document(null);
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        assertNotNull(body);
    }

    @Test(expected = NullPointerException.class)
    public void testBodyMethodWithNullBaseUri() {
        new Document(null);
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test(expected = NullPointerException.class)
    public void testTitleMethodWithNullTitle() {
        Document document = new Document("baseUri");
        document.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("div");
        assertNotNull(element);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateElementMethodWithNullTagName() {
        Document document = new Document("baseUri");
        document.createElement(null);
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        Element title = document.appendElement("title").text("Test Title");
        Element body = document.createElement("body").text("Test Body");
        document.appendChild(title);
        document.appendChild(body);

        document.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals(1, bodyChildren.size());
        Node bodyText = bodyChildren.get(0);
        assertTrue(bodyText instanceof TextNode);
        TextNode titleText = document.getElementsByTag("title").first().text();
        assertEquals("Test Title", titleText.trim());
    }

    @Test
    public void testNormaliseMethodWithEmptyDocument() {
        Document document = new Document("baseUri");
        document.normalise();

        List<Node> documentChildren = document.childNodes();
        assertEquals(3, documentChildren.size());
        Node html = documentChildren.get(0);
        Node head = html.getElementsByTag("head").first();
        Node body = html.getElementsByTag("body").first();

        assertEquals("#document", html.nodeName());
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }
}