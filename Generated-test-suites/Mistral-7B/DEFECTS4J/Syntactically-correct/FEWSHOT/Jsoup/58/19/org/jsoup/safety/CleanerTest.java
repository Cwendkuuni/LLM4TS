package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;
    private Parser parser;

    @Before
    public void setup() {
        whitelist = mock(Whitelist.class);
        parser = mock(Parser.class);
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testCreateCleaner() {
        assertTrue(cleaner instanceof Cleaner);
    }

    @Test
    public void testCleanDocument() {
        Document dirtyDocument = createDocument();
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(cleanDocument.body(), cleanDocument.getElementsByTag("body").get(0));
        assertNotSame(dirtyDocument.body(), cleanDocument.body());
        assertNull(cleanDocument.head());
    }

    @Test
    public void testIsValidDocument() {
        Document validDocument = createValidDocument();
        Document invalidDocument = createInvalidDocument();

        assertTrue(cleaner.isValid(validDocument));
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testIsValidDocumentWithCleanedDocument() {
        Document validDocument = createValidDocument();
        Document cleanedDocument = cleaner.clean(validDocument);

        assertTrue(cleaner.isValid(cleanedDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element sourceElement = createElement("div", "id", "source");
        Element destinationElement = createElement("div", null, "dest");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

        assertEquals(numDiscarded, 0);
        assertEquals(destinationElement.getElementsByTag("div").size(), 1);
        assertEquals(sourceElement, destinationElement.getElementsByTag("div").get(0));
    }

    @Test
    public void testCopySafeNodesWithDiscardedAttributes() {
        Element sourceElement = createElement("div", "id", "source", "class", "discarded", "data-test", "test");
        Element destinationElement = createElement("div", null, "dest");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

        assertEquals(numDiscarded, 2);
        assertEquals(destinationElement.getElementsByTag("div").size(), 1);
        assertNull(destinationElement.getElementsByClass("discarded").get(0));
        assertNull(destinationElement.data().get("test"));
        assertEquals(destinationElement.getElementsByTag("div").get(0), sourceElement);
    }

    @Test
    public void testCopySafeNodesWithDiscardedTags() {
        Element sourceElement = createElement("script", null, null);
        Element destinationElement = createElement("div", null, "dest");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

        assertEquals(numDiscarded, 1);
        assertEquals(destinationElement.getElementsByTag("script").size(), 0);
        assertEquals(destinationElement.getElementsByTag("div").size(), 1);
    }

    private Document createDocument() {
        Document document = parser.parseBodyFragment("<html><body><div id='test' class='test' data-test='test'><script>alert('XSS')</script></div></body></html>", null);
        return document;
    }

    private Document createValidDocument() {
        Document document = parser.parseBodyFragment("<html><body><div id='test' class='test'><p>Test</p></div></body></html>", null);
        return document;
    }

    private Document createInvalidDocument() {
        Document document = parser.parseBodyFragment("<html><body><script>alert('XSS')</script></body></html>", null);
        return document;
    }

    private Element createElement(String tagName, String... attributes) {
        Element element = new Element(Tag.valueOf(tagName), null);
        if (attributes != null) {
            for (String attribute : attributes) {
                String[] keyValue = attribute.split("=");
                element.attr(keyValue[0], keyValue[1]);
            }
        }
        return element;
    }

}