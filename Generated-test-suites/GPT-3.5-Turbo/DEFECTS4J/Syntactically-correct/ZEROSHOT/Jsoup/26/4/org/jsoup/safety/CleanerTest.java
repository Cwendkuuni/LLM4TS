import org.junit.*;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test case 1
        Document dirtyDoc = new Document("<html><body><div><p>Unsafe text</p></div></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleanDoc = cleaner.clean(dirtyDoc);
        assertEquals("cleaned document", "<html><head></head><body><div><p>Unsafe text</p></div></body></html>", cleanDoc.toString());

        // Test case 2
        dirtyDoc = new Document("<html><body><script>alert('XSS attack')</script></body></html>");
        cleanDoc = cleaner.clean(dirtyDoc);
        assertEquals("cleaned document", "<html><head></head><body></body></html>", cleanDoc.toString());
    }

    @Test
    public void testIsValid() {
        // Test case 1
        Document dirtyDoc = new Document("<html><body><div><p>Unsafe text</p></div></body></html>");
        Cleaner cleaner = new Cleaner(new Whitelist());
        boolean valid = cleaner.isValid(dirtyDoc);
        assertTrue("valid document", valid);

        // Test case 2
        dirtyDoc = new Document("<html><body><script>alert('XSS attack')</script></body></html>");
        valid = cleaner.isValid(dirtyDoc);
        assertFalse("invalid document", valid);
    }

    @Test
    public void testCopySafeNodes() {
        // Test case 1
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        Cleaner cleaner = new Cleaner(new Whitelist());
        int discarded = cleaner.copySafeNodes(source, dest);
        assertEquals("number of discarded elements", 0, discarded);

        // Test case 2
        source = new Element(Tag.valueOf("script"), "");
        discarded = cleaner.copySafeNodes(source, dest);
        assertEquals("number of discarded elements", 1, discarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Test case 1
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        Cleaner cleaner = new Cleaner(new Whitelist());
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals("number of discarded attributes", 0, meta.numAttribsDiscarded);

        // Test case 2
        sourceEl.attr("onclick", "alert('XSS attack')");
        meta = cleaner.createSafeElement(sourceEl);
        assertEquals("number of discarded attributes", 1, meta.numAttribsDiscarded);
    }
}