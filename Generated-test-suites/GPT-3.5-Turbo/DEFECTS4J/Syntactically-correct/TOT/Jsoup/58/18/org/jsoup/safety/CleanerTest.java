import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        String dirtyHtml = "<html><body><h1>Hello, World!</h1></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDoc = cleaner.clean(dirtyDoc);

        Assert.assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", cleanDoc.html());

        // Edge case: Empty input
        dirtyHtml = "";
        dirtyDoc = Jsoup.parse(dirtyHtml);

        cleanDoc = cleaner.clean(dirtyDoc);

        Assert.assertEquals("<html><head></head><body></body></html>", cleanDoc.html());

        // Error scenario: Invalid tag
        dirtyHtml = "<html><body><script>alert('XSS');</script></body></html>";
        dirtyDoc = Jsoup.parse(dirtyHtml);

        cleanDoc = cleaner.clean(dirtyDoc);

        Assert.assertEquals("<html><head></head><body></body></html>", cleanDoc.html());
    }

    @Test
    public void testIsValid() {
        // Typical use case
        String dirtyHtml = "<html><body><h1>Hello, World!</h1></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDoc);

        Assert.assertTrue(isValid);

        // Edge case: Empty input
        dirtyHtml = "";
        dirtyDoc = Jsoup.parse(dirtyHtml);

        isValid = cleaner.isValid(dirtyDoc);

        Assert.assertTrue(isValid);

        // Error scenario: Invalid attribute
        dirtyHtml = "<html><body><p align='center'>Hello, World!</p></body></html>";
        dirtyDoc = Jsoup.parse(dirtyHtml);

        isValid = cleaner.isValid(dirtyDoc);

        Assert.assertFalse(isValid);
    }
}