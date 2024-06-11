
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(new Whitelist());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><div><p>Test</p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals("<html><head></head><body><div><p>Test</p></div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><head></head><body><div><p>Test</p></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }
}
