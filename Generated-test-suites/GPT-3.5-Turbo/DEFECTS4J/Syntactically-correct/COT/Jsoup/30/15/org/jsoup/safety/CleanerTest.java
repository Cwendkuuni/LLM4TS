
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test case 1: Test clean method with a valid document
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><p>Test</p></body></html>", cleanDocument.outerHtml());

        // Test case 2: Test clean method with an empty document
        Document emptyDocument = new Document("");
        Document cleanEmptyDocument = cleaner.clean(emptyDocument);
        assertEquals("<html><head></head><body></body></html>", cleanEmptyDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Test case 1: Test isValid method with a valid document
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document validDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        boolean isValid = cleaner.isValid(validDocument);
        assertTrue(isValid);

        // Test case 2: Test isValid method with an invalid document
        Document invalidDocument = Jsoup.parseBodyFragment("<p onclick=\"alert('XSS')\">Test</p>");
        boolean isInvalid = cleaner.isValid(invalidDocument);
        assertFalse(isInvalid);
    }

    @Test
    public void testCopySafeNodes() {
        // Test case 1: Test copySafeNodes method with a valid element
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Element root = cleaner.clean(Jsoup.parseBodyFragment("<p>Test</p>")).body();
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertTrue(numDiscarded == 0);
        assertEquals("<div><p>Test</p></div>", destination.outerHtml());

        // Test case 2: Test copySafeNodes method with an element containing unsafe tag
        Element unsafeRoot = Jsoup.parseBodyFragment("<p onclick=\"alert('XSS')\">Test</p>").body();
        Element unsafeDestination = new Element(Tag.valueOf("div"), "");
        int numUnsafeDiscarded = cleaner.copySafeNodes(unsafeRoot, unsafeDestination);
        assertTrue(numUnsafeDiscarded == 1);
        assertEquals("<div></div>", unsafeDestination.outerHtml());
    }
}
