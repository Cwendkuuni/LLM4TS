package org.jsoup.safety;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerWithEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().childNodeSize() == 0);
    }

    @Test
    public void testCleanerWithAllowedTags() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><div id='test'>Test</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element cleanDiv = cleanDocument.body().getElementsByTag("div").first();
        assertEquals("Test", cleanDiv.text());
        assertEquals("div", cleanDiv.tagName());
    }

    @Test
    public void testCleanerWithDisallowedTags() {
        Whitelist whitelist = Whitelist.simpleDefault().addTag("script");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><div id='test'><script>alert('Hello World!');</script></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanerWithAllowedAttributes() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><div id='test' class='test' title='Test Title'></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element cleanDiv = cleanDocument.body().getElementsByTag("div").first();
        assertEquals("test", cleanDiv.classes());
        assertEquals("Test Title", cleanDiv.attr("title"));
    }

    @Test
    public void testCleanerWithDisallowedAttributes() {
        Whitelist whitelist = Whitelist.simpleDefault().addAttribute("div", "onclick", ".*");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><div id='test' onclick='alert(\"Hello World!\");'></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals(0, cleanDocument.body().childNodeSize());
    }

    @Test
    public void testCleanerWithAllowedText() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><p>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Text cleanText = cleanDocument.body().getElementsByTag("p").first().text();
        assertEquals("Hello World!", cleanText.getWholeText());
    }

    @Test
    public void testCleanerWithRemovedText() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><p>Hello World! <script>alert('Hello World!');</script></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Text cleanText = cleanDocument.body().getElementsByTag("p").first().text();
        assertEquals("", cleanText.getWholeText());
    }

    @Test
    public void testIsValidWithAllowedDocument() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><div id='test'>Test</div></body></html>");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithDisallowedDocument() {
        Whitelist whitelist = Whitelist.simpleDefault().addTag("script");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html><body><div id='test'><script>alert('Hello World!');</script></div></body></html>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithEmptyDocument() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyElements("<html></html>");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

}