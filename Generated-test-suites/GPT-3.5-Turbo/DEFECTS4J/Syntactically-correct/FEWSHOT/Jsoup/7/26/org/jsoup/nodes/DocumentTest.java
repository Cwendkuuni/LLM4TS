package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("base");
    }

    @Test
    public void testConstructor() {
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("base");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleNull() {
        document.title(null);
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testTitleElement() {
        assertNull(document.getElementsByTag("title").first());

        document.title("Test Title");
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("Test Title", titleElement.text());
    }

    @Test
    public void testNormalise() {
        Element htmlElement = document.appendElement("html");
        Element headElement = htmlElement.appendElement("head");
        Element bodyElement = htmlElement.appendElement("body");

        Element normalisedElement = document.normalise();

        assertNotNull(normalisedElement);
        assertEquals("html", normalisedElement.nodeName());
        assertEquals(1, normalisedElement.children().size());
        assertTrue(normalisedElement.children().contains(headElement));
        assertTrue(normalisedElement.children().contains(bodyElement));
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("base", element.baseUri());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        Element body = document.body();
        Element textElement = body.appendElement("p");
        textElement.text("Test Text");

        Element textElement2 = document.text("Test Text 2");

        assertNotNull(textElement2);
        assertEquals("p", textElement2.nodeName());
        assertEquals("Test Text 2", textElement2.text());
        assertEquals(body, textElement2.parent());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }
}