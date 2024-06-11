import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        // Test case 1: Empty document
        Document dirtyDocument1 = Document.createShell("");
        Document clean1 = cleaner.clean(dirtyDocument1);
        assertEquals("<html><head></head><body></body></html>", clean1.outerHtml());

        // Test case 2: Document with safe tags
        Document dirtyDocument2 = Document.createShell("");
        dirtyDocument2.body().append("<div><p>Hello</p></div><a href='#'>Link</a>");
        Document clean2 = cleaner.clean(dirtyDocument2);
        assertEquals("<html><head></head><body><div><p>Hello</p></div><a href=\"#\">Link</a></body></html>", clean2.outerHtml());

        // Test case 3: Document with unsafe tags
        Document dirtyDocument3 = Document.createShell("");
        dirtyDocument3.body().append("<script>alert('XSS')</script>");
        Document clean3 = cleaner.clean(dirtyDocument3);
        assertEquals("<html><head></head><body></body></html>", clean3.outerHtml());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Test case 1: Valid document
        Document dirtyDocument1 = Document.createShell("");
        dirtyDocument1.body().append("<p>Hello</p><a href='#'>Link</a>");
        boolean valid1 = cleaner.isValid(dirtyDocument1);
        assertTrue(valid1);

        // Test case 2: Invalid document
        Document dirtyDocument2 = Document.createShell("");
        dirtyDocument2.body().append("<p onmouseover='alert(\"XSS\")'>Hello</p>");
        boolean valid2 = cleaner.isValid(dirtyDocument2);
        assertFalse(valid2);
    }
}