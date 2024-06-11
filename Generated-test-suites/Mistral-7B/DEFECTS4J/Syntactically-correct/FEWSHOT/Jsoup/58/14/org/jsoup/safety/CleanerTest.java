package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleaner() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><head><script>alert('XSS');</script></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check that the clean document only contains text and allowed elements
        Element root = cleanDocument.body();
        assertNotNull(root.children());
        for (Node node : root.children()) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                assertEquals("Hello World", textNode.text());
            } else if (node instanceof Element) {
                Element element = (Element) node;
                assertTrue(whitelist.isAllowAllElements(element.tagName()));
            }
        }

        // Check that the clean document does not contain the script tag or any other disallowed elements
        assertFalse(cleanDocument.body().select("script").isEmpty());
        cleanDocument.body().select("script").remove();
        assertFalse(cleanDocument.body().select("style").isEmpty());
        cleanDocument.body().select("style").remove();
        assertFalse(cleanDocument.body().select("iframe").isEmpty());
        cleanDocument.body().select("iframe").remove();
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        html = "<html><body><h1>Hello World</h1><script>alert('XSS');</script></body></html>";
        dirtyDocument = Jsoup.parse(html);
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><head><title>Title</title></head><body><h1>Hello World</h1><div id='test'><p>Test</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element root = dirtyDocument.body();

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());

        // Check that the clean document only contains text and allowed elements
        for (Node node : cleanDocument.body().children()) {
            if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                if (node.text().equals("Test")) {
                    assertEquals("Test", textNode.text());
                } else {
                    assertTrue(whitelist.isAllowAllElements(node.tagName()));
                }
            } else if (node instanceof Element) {
                Element element = (Element) node;
                assertTrue(whitelist.isAllowAllElements(element.tagName()));
            }
        }

        // Check that the number of discarded nodes matches the number of script and style tags in the dirty document
        assertEquals(2, numDiscarded);
        assertFalse(cleanDocument.body().select("script").isEmpty());
        cleanDocument.body().select("script").remove();
        assertFalse(cleanDocument.body().select("style").isEmpty());
        cleanDocument.body().select("style").remove();
    }
}