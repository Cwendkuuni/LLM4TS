package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

public class CleanerTest {
    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='foo'>Hello, <script>world</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.body().hasText());
        assertFalse(cleanDocument.body().hasMetatags());
        assertFalse(cleanDocument.body().hasScripts());
        assertFalse(cleanDocument.body().hasStylesheets());
        assertFalse(cleanDocument.body().hasImages());
        assertFalse(cleanDocument.body().hasLinks());
        assertFalse(cleanDocument.body().hasForms());
        assertFalse(cleanDocument.body().hasInputElements());

        assertTrue(cleanDocument.body().getAllElements().size() > 0);
        assertTrue(cleanDocument.body().getAllElements().get(0) instanceof Element);
        Element firstElement = cleanDocument.body().getAllElements().get(0);
        assertTrue(whitelist.isSafeTag(firstElement.tagName()));
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='foo'>Hello, <script>world</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);

        String html2 = "<html><body><p id='foo'>Hello, world</p></body></html>";
        Document dirtyDocument2 = Jsoup.parse(html2);

        boolean isValid2 = cleaner.isValid(dirtyDocument2);
        assertTrue(isValid2);
    }

    @Test
    public void testCopySafeNodesWithSafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='foo' class='bar'><p>Hello, world</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();

        Document cleanDocument = Document.createShell(body.baseUri());
        int numDiscarded = cleaner.copySafeNodes(body, cleanDocument.body());

        assertTrue(numDiscarded == 0);
        assertTrue(cleanDocument.body().getAllElements().size() > 0);
        assertTrue(cleanDocument.body().getAllElements().get(0) instanceof Element);
        Element firstElement = cleanDocument.body().getAllElements().get(0);
        assertTrue(whitelist.isSafeTag(firstElement.tagName()));
    }

    @Test
    public void testCopySafeNodesWithUnsafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='foo' class='bar'><script>alert('Hello, world')</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();

        Document cleanDocument = Document.createShell(body.baseUri());
        int numDiscarded = cleaner.copySafeNodes(body, cleanDocument.body());

        assertTrue(numDiscarded > 0);
        assertTrue(cleanDocument.body().getAllElements().size() > 0);
        assertTrue(cleanDocument.body().getAllElements().get(0) instanceof TextNode);
        TextNode firstElement = (TextNode) cleanDocument.body().getAllElements().get(0);
        assertTrue(firstElement.text().isEmpty());
    }

    @Test
    public void testCreateSafeElementWithSafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='foo' class='bar'><p>Hello, world</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element sourceEl = dirtyDocument.body().getElementsByTag("div").get(0);

        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element destChild = elementMeta.el;

        assertTrue(destChild.tagName().equals("div"));
        assertTrue(destChild.attributes().size() > 0);
        assertTrue(destChild.attributes().hasKey("id"));
        assertTrue(destChild.attributes().hasKey("class"));
        assertFalse(destChild.attributes().hasKey("script"));
    }

    @Test
    public void testCreateSafeElementWithUnsafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><div id='foo' class='bar'><script>alert('Hello, world')</script></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element sourceEl = dirtyDocument.body().getElementsByTag("div").get(0);

        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element destChild = elementMeta.el;

        assertTrue(destChild.tagName().equals(""));
        assertTrue(destChild.attributes().size() == 0);
    }
}