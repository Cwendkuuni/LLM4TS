import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
        dirtyDocument = new Document("<html><body><div><p>This is a test</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div><p>This is a test</p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create test scenarios to cover edge cases and exceptions
    }

    @Test
    public void testCreateSafeElement() {
        // Create test scenarios to cover edge cases and exceptions
    }

}