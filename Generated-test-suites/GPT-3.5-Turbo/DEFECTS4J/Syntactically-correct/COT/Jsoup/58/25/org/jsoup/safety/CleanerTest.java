import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test case 1: Cleaning a document with no unsafe elements
        Document dirtyDocument1 = new Document("<html><body><div><p>Test</p></div></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument1 = cleaner.clean(dirtyDocument1);
        assertEquals("<html><head></head><body><div><p>Test</p></div></body></html>", cleanDocument1.html());

        // Test case 2: Cleaning a document with unsafe elements
        Document dirtyDocument2 = new Document("<html><body><div><script>alert('XSS')</script><p>Test</p></div></body></html>");
        Document cleanDocument2 = cleaner.clean(dirtyDocument2);
        assertEquals("<html><head></head><body><div><p>Test</p></div></body></html>", cleanDocument2.html());
    }

    @Test
    public void testIsValid() {
        // Test case 1: Valid document with no unsafe elements
        Document validDocument1 = new Document("<html><body><div><p>Test</p></div></body></html>");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        assertTrue(cleaner.isValid(validDocument1));

        // Test case 2: Invalid document with unsafe elements
        Document invalidDocument1 = new Document("<html><head><script>alert('XSS')</script></head><body><div><p>Test</p></div></body></html>");
        assertFalse(cleaner.isValid(invalidDocument1));
    }
}