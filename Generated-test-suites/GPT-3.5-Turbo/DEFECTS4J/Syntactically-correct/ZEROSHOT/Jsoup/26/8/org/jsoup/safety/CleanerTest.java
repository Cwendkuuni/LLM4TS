import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    private Cleaner cleaner;

    @Test
    public void testClean() {
        // Test case 1: Document with no unsafe elements
        String html1 = "<html><body><p>This is a safe paragraph.</p></body></html>";
        Document dirtyDocument1 = Document.createShell("");
        dirtyDocument1.body().append(html1);
        Document cleanDocument1 = cleaner.clean(dirtyDocument1);
        assertEquals(html1, cleanDocument1.body().html());

        // Test case 2: Document with unsafe elements
        String html2 = "<html><body><script>alert('Unsafe')</script></body></html>";
        Document dirtyDocument2 = Document.createShell("");
        dirtyDocument2.body().append(html2);
        Document cleanDocument2 = cleaner.clean(dirtyDocument2);
        assertEquals("<html><head></head><body></body></html>", cleanDocument2.outerHtml());

        // Add more test cases...
    }

    @Test
    public void testIsValid() {
        // Test case 1: Valid document
        String html1 = "<html><body><p>This is a safe paragraph.</p></body></html>";
        Document dirtyDocument1 = Document.createShell("");
        dirtyDocument1.body().append(html1);
        boolean isValid1 = cleaner.isValid(dirtyDocument1);
        assertTrue(isValid1);

        // Test case 2: Invalid document
        String html2 = "<html><body><script>alert('Unsafe')</script></body></html>";
        Document dirtyDocument2 = Document.createShell("");
        dirtyDocument2.body().append(html2);
        boolean isValid2 = cleaner.isValid(dirtyDocument2);
        assertFalse(isValid2);

        // Add more test cases...
    }

    // Add more test methods...

}