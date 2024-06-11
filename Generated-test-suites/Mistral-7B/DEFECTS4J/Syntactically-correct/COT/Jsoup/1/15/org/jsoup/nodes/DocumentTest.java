package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Document class.
 */
public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @After
    public void tearDown() {
        document = null;
    }

    //-------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentConstructorNullBaseUri() {
        new Document(null);
    }

    //-------------------------------------------------------------------------
    // createShell
    //-------------------------------------------------------------------------

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.select("html").first();
        Element head = html.select("head").first();
        Element body = html.select("body").first();

        assertNotNull(doc);
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateShellNullBaseUri() {
        Document.createShell(null);
    }

    //-------------------------------------------------------------------------
    // head
    //-------------------------------------------------------------------------

    @Test
    public void testHeadAccessors() {
        Element head = document.head();
        assertNull(head);

        document.title("Test Title");
        head = document.head();
        assertNotNull(head);
        assertEquals("Test Title", head.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testHeadNullBaseUri() {
        Document doc = new Document(null);
        doc.head();
    }

    //-------------------------------------------------------------------------
    // body
    //-------------------------------------------------------------------------

    @Test
    public void testBodyAccessors() {
        Element body = document.body();
        assertNull(body);

        document.appendText("Test Body Text");
        body = document.body();
        assertNotNull(body);
        assertEquals("Test Body Text", body.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testBodyNullBaseUri() {
        Document doc = new Document(null);
        doc.body();
    }

    //-------------------------------------------------------------------------
    // title
    //-------------------------------------------------------------------------

    @Test
    public void testTitleAccessors() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.title(null);
        assertNull(document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleNullBaseUri() {
        Document doc = new Document(null);
        doc.title("Test Title");
    }

    //-------------------------------------------------------------------------
    // createElement
    //-------------------------------------------------------------------------

    @Test
    public void testCreateElement() {
        Element p = document.createElement("p");
        assertNotNull(p);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateElementNullTagName() {
        document.createElement(null);
    }

    //-------------------------------------------------------------------------
    // normalise
    //-------------------------------------------------------------------------

    @Test
    public void testNormaliseEmptyDocument() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        Element html = doc.select("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testNormaliseWithTextNodes() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p");
        TextNode textNode1 = doc.createTextNode("Text Node 1");
        TextNode textNode2 = doc.createTextNode("Text Node 2");

        p.appendChild(textNode1);
        p.appendChild(textNode2);
        doc.body().appendChild(p);

        doc.normalise();

        List<Node> bodyChildNodes = doc.body().childNodes();
        int indexOfTextNode1 = -1;
        int indexOfTextNode2 = -1;

        for (int i = 0; i < bodyChildNodes.size(); i++) {
            if (bodyChildNodes.get(i) instanceof TextNode && textNode1.text().equals(((TextNode) bodyChildNodes.get(i)).text())) {
                indexOfTextNode1 = i;
            }
            if (bodyChildNodes.get(i) instanceof TextNode && textNode2.text().equals(((TextNode) bodyChildNodes.get(i)).text())) {
                indexOfTextNode2 = i;
            }
        }

        assertEquals(0, indexOfTextNode1);
        assertEquals(1, indexOfTextNode2);
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseNullBaseUri() {
        Document doc = new Document(null);
        doc.normalise();
    }
}