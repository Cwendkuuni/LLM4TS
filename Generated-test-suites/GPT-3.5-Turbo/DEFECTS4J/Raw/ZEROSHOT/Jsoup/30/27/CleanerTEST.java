import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><div>Hello World!</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div>Hello World!</div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><div>Hello World!</div></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);

        assertTrue(valid);

        Document invalidDocument = new Document("<html><body><script>alert('Hello World!');</script></body></html>");
        valid = cleaner.isValid(invalidDocument);

        assertFalse(valid);
    }
}