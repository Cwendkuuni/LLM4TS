
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Test clean method with a dirty document
        Document dirtyDocument = Jsoup.parse("<html><body>Hello <a href=\"http://example.com\">example</a></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        Assert.assertEquals("Hello <a href=\"http://example.com\">example</a>", body.html());
    }

    @Test
    public void testIsValid() {
        // Test isValid method with a valid document
        Document validDocument = Jsoup.parse("<html><body><p>Valid</p></body></html>");
        boolean isValid = cleaner.isValid(validDocument);
        Assert.assertTrue(isValid);

        // Test isValid method with an invalid document
        Document invalidDocument = Jsoup.parse("<html><head></head><body><p>Invalid</p></body></html>");
        isValid = cleaner.isValid(invalidDocument);
        Assert.assertFalse(isValid);
    }
}
