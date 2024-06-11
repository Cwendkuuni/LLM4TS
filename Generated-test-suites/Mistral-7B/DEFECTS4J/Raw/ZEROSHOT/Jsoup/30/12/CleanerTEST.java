package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test file for the Cleaner class.
 */
public class CleanerTest {

    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        List<Tag> allowedTags = Arrays.asList(Tag.valueOf("p"), Tag.valueOf("a"), Tag.valueOf("img"));
        whitelist = Whitelist.simpleTextOnlyWhitelist().addTags(allowedTags);
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testCreateCleaner() {
        assertNotNull(cleaner);
    }

    @Test
    public void testCreateDocumentShell() {
        Document cleanDocument = cleaner.clean(null);
        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.body().text());
    }

    @Test
    public void testCleanDocument() {
        String html = "<html><body><p>Test</p><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Test", cleanDocument.body().text());
        assertFalse(cleanDocument.body().getElementsByTag("script").isEmpty());
        assertNull(cleanDocument.body().getElementsByTag("script").get(0).text());
    }

    @Test
    public void testValidateDocument() {
        String html = "<html><body><p>Test<script>alert('XSS')</script></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testValidateValidDocument() {
        String html = "<html><body><p>Test</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        String html = "<html><body><p id='test' class='test'><b>Test</b></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals("Test", cleanDocument.body().getElementsByTag("p").get(0).text());
        assertEquals("Test", cleanDocument.body().getElementsByTag("p").get(0).getElementsByTag("b").text());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithDiscardedAttributes() {
        String html = "<html><body><p id='test' class='test' style='color: red'><b style='color: blue'>Test</b></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Element pElement = cleanDocument.body().getElementsByTag("p").get(0);
        assertNotNull(pElement);
        assertNull(pElement.id());
        assertNull(pElement.classes());

        Element bElement = pElement.getElementsByTag("b").get(0);
        assertNotNull(bElement);
        assertNull(bElement.styleAttribute());
        assertEquals(1, numDiscarded);
    }
}