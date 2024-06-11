
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><body><div><a href=\"http://example.com\">Link</a></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        boolean valid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(valid);
    }

    @Test
    public void testCopySafeNodes() {
        Document document = Jsoup.parse("<html><body><div><a href=\"http://example.com\">Link</a></div></body></html>");
        Cleaner.ElementMeta meta = cleaner.createSafeElement(document.body());
        Assert.assertNotNull(meta.el);
        Assert.assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Document document = Jsoup.parse("<a href=\"http://example.com\">Link</a>");
        Cleaner.ElementMeta meta = cleaner.createSafeElement(document.getElementsByTag("a").first());
        Assert.assertNotNull(meta.el);
        Assert.assertEquals(0, meta.numAttribsDiscarded);
    }
}
