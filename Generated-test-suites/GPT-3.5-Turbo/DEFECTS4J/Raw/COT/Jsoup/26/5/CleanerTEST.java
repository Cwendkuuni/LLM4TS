
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><h1>Dirty HTML</h1><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertEquals("<html><head></head><body><h1>Dirty HTML</h1></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid_ValidDocument() {
        String validHtml = "<html><body><h1>Valid HTML</h1></body></html>";
        Document validDocument = Jsoup.parse(validHtml);

        boolean isValid = cleaner.isValid(validDocument);

        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        String invalidHtml = "<html><body><h1>Invalid HTML</h1><a href='javascript:alert(1)'>Click me</a></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHtml);

        boolean isValid = cleaner.isValid(invalidDocument);

        Assert.assertFalse(isValid);
    }
}
