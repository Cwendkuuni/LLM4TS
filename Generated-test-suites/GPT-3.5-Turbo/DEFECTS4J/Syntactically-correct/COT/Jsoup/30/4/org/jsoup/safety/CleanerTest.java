
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<div><script>alert('XSS')</script></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        String expected = "<html><head></head><body><div></div></body></html>";
        Assert.assertEquals(expected, cleanDocument.toString());
    }

    @Test
    public void testIsValid_validDocument() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = Jsoup.parse("<html><body><div></div></body></html>");
        boolean isValid = cleaner.isValid(validDocument);

        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(invalidDocument);

        Assert.assertFalse(isValid);
    }
}
