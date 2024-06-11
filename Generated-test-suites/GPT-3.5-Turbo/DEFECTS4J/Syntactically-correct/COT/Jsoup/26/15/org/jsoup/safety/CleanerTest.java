
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {
    private final Cleaner cleaner = new Cleaner(Whitelist.basic());

    @Test
    public void testClean() {
        // Test case 1: Empty document
        Document dirtyDocument1 = Document.createShell("");
        Document cleanDocument1 = cleaner.clean(dirtyDocument1);
        Assert.assertNotNull(cleanDocument1);
        
        // Test case 2: Document with valid HTML
        Document dirtyDocument2 = Document.createShell("<html><head></head><body><p>Test</p></body></html>");
        Document cleanDocument2 = cleaner.clean(dirtyDocument2);
        Assert.assertNotNull(cleanDocument2);
        Assert.assertEquals("<html><head></head><body><p>Test</p></body></html>", cleanDocument2.html());
    }

    @Test
    public void testIsValid() {
        // Test case 1: Empty document
        Document dirtyDocument1 = Document.createShell("");
        boolean isValid1 = cleaner.isValid(dirtyDocument1);
        Assert.assertTrue(isValid1);

        // Test case 2: Document with invalid HTML
        Document dirtyDocument2 = Document.createShell("<html><head></head><test><p>Test</p></body></html>");
        boolean isValid2 = cleaner.isValid(dirtyDocument2);
        Assert.assertFalse(isValid2);

        // Test case 3: Document with valid HTML
        Document dirtyDocument3 = Document.createShell("<html><head></head><body><p>Test</p></body></html>");
        boolean isValid3 = cleaner.isValid(dirtyDocument3);
        Assert.assertTrue(isValid3);
    }
}
