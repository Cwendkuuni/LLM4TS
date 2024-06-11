import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private static Cleaner cleaner;
    private static String html;

    @Before
    public void setUp() {
        // Set up the cleaner and HTML
        cleaner = new Cleaner(Whitelist.basic());
        html = "<p>This is a <a href='https://example.com'>link</a> with <script>alert('XSS attack')</script> XSS attack.</p>";
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Ensure that the script tag is removed and only allowed tags are present
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>This is a <a href=\"https://example.com\">link</a> with  XSS attack.</p>\n" +
                " </body>\n" +
                "</html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse(html);

        // Assert that the dirty document is not valid
        assertFalse(cleaner.isValid(dirtyDocument));

        // Assert that a clean document is valid
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleaner.isValid(cleanDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        // Assert that the safe nodes are correctly copied to the destination
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(1, numDiscarded);

        // Ensure that the script tag is removed and only allowed tags are present in the destination document
        assertEquals("<body>\n" +
                "  <p>This is a <a href=\"https://example.com\">link</a> with  XSS attack.</p>\n" +
                " </body>", cleanDocument.body().outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Document dirtyDocument = Jsoup.parse(html);
        Element dirtyElement = dirtyDocument.getElementsByTag("p").first();

        // Create a safe element from the dirty element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);

        // Ensure that the script attribute is discarded and enforced attributes are added to the safe element
        assertEquals("<p>This is a <a href=\"https://example.com\">link</a> with  XSS attack.</p>", elementMeta.el.outerHtml());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}