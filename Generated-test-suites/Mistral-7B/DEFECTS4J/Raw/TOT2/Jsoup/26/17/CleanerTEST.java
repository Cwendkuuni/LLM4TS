import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist(); // Assuming a valid whitelist is set up
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromString("<html><body><h1>Test</h1><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertNotNull(cleanDocument.body().select("h1"));
        assertNull(cleanDocument.body().select("script"));
    }
}