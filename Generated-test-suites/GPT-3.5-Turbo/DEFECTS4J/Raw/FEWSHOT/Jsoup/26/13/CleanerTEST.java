package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {
    private static final String BASE_URI = "http://www.example.com";

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parseBodyFragment("<p>This is a <script>script</script> tag.</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();

        assertEquals("This is a tag.", body.text());
        assertFalse(body.getElementsByTag("script").isEmpty());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parseBodyFragment("<p>This is a <script>script</script> tag.</p>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parseBodyFragment("<p>This is a <script>script</script> tag.</p>");
        Document cleanDocument = Jsoup.parseBodyFragment("<p></p>");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("script", BASE_URI);
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Element destElement = elementMeta.el;
        int numDiscarded = elementMeta.numAttribsDiscarded;

        assertEquals("script", destElement.tagName());
        assertEquals(BASE_URI, destElement.baseUri());
        assertEquals(0, numDiscarded);
    }
}