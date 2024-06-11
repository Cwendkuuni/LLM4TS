
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
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
        String dirtyHTML = "<html><body><div><p>Dirty HTML</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertEquals("<html><head></head><body><div><p>Dirty HTML</p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        String dirtyHTML = "<html><body><div><p>Dirty HTML</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }
}
