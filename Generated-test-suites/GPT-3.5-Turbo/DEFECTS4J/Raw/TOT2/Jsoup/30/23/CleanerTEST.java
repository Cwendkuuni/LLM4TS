import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><p>This is a test</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if the clean document only contains allowed elements
        assertEquals("<html><head></head><body><p>This is a test</p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValidValidDocument() {
        Document document = new Document("<p>This is a valid document</p>");
        assertTrue(cleaner.isValid(document));
    }

    @Test
    public void testIsValidInvalidDocument() {
        Document document = new Document("<script>Unsafe script</script>");
        assertFalse(cleaner.isValid(document));
    }

    @Test
    public void testCopySafeNodes() {
        // Test copySafeNodes method with different types of nodes
        Document dirtyDocument = new Document("<div><p>This is a paragraph</p><span>Span text</span></div>");
        Document cleanDocument = new Document("<div></div>");

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Check if the safe nodes are copied correctly
        assertEquals("Expected HTML: '<div><p>This is a paragraph</p><span>Span text</span></div>'", cleanDocument.body().html());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Test createSafeElement method with safe attribute
        Document document = new Document("<p class='safe' id='unique'>Safe paragraph</p>");
        cleaner.clean(document);

        // Check if the safe attributes are copied correctly
        assertEquals("<p class=\"safe\" id=\"unique\">Safe paragraph</p>", document.body().html());
    }

    @Test
    public void testCreateSafeElementWithUnsafeAttribute() {
        // Test createSafeElement method with unsafe attribute
        Document document = new Document("<p class='unsafe' onclick='alert()'>Unsafe paragraph</p>");
        cleaner.clean(document);

        // Check if the unsafe attribute is discarded
        assertEquals("<p class=\"unsafe\">Unsafe paragraph</p>", document.body().html());
    }
}