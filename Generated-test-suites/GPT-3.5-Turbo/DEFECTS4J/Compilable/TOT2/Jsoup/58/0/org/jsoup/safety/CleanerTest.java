package org.jsoup.safety;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<div><p>Hello</p></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals(1, cleanDocument.getElementsByTag("div").size());
        assertEquals(1, cleanDocument.getElementsByTag("p").size());
        assertEquals("Hello", cleanDocument.getElementsByTag("p").first().text());
    }

    @Test
    public void testIsValid() {
        Document validDocument = Jsoup.parse("<div></div>");
        Document invalidDocument = Jsoup.parse("<script>alert('xss')</script>");

        assertTrue(cleaner.isValid(validDocument));
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCleanEdgeCases() {
        // Test cleaning empty document
        Document emptyDocument = Jsoup.parse("");
        Document cleanEmptyDocument = cleaner.clean(emptyDocument);

        assertNotNull(cleanEmptyDocument);
        assertEquals(0, cleanEmptyDocument.getAllElements().size());

        // Test cleaning document with special characters
        Document specialCharsDocument = Jsoup.parse("<div><p>Hello &amp; World</p></div>");
        Document cleanSpecialCharsDocument = cleaner.clean(specialCharsDocument);

        assertNotNull(cleanSpecialCharsDocument);
        assertEquals("Hello & World", cleanSpecialCharsDocument.getElementsByTag("p").first().text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullDocumentClean() {
        cleaner.clean(null);
    }

}