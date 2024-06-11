import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<p>This is a <script>alert('XSS attack');</script> test.</p>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<p>This is a  test.</p>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<p>This is a <script>alert('XSS attack');</script> test.</p>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<p>This is a <script>alert('XSS attack');</script> test.</p>");

        Document cleanDocument = new Document("");
        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals("<p>This is a  test.</p>", cleanDocument.body().html());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<div style='color:red;'>Valid content</div>");

        cleaner.clean(dirtyDocument);

        assertTrue(whitelist.isSafeTag("div"));
    }
}