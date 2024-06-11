import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;

public class CleanerTest {

    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertFalse(cleanDocument.body().text().contains("alert('XSS')"));
    }

    // Test cases for other methods here...
}