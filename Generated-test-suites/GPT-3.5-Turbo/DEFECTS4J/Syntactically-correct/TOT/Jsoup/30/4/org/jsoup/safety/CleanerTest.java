
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Document doc;

    @Before
    public void setUp() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
        doc = Jsoup.parse("<html><head></head><body><p>This is a paragraph</p></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDoc = cleaner.clean(doc);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>This is a paragraph</p>\n" +
                " </body>\n" +
                "</html>", cleanDoc.html());
    }

    @Test
    public void testIsValidWithValidDocument() {
        boolean isValid = cleaner.isValid(doc);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Document invalidDoc = Jsoup.parse("<html><script>alert('XSS');</script></html>");
        boolean isValid = cleaner.isValid(invalidDoc);
        Assert.assertFalse(isValid);
    }

}
