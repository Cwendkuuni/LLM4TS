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
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Hello, <b>World!</b></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertEquals("<html><head></head><body><p>Hello, <b>World!</b></p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid_validDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Hello, <b>World!</b></p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_invalidDocument() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document sourceDocument = Jsoup.parse("<html><head></head><body><p>Hello, <b>World!</b></p></body></html>");
        Document destDocument = Document.createShell(sourceDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(sourceDocument.body(), destDocument.body());

        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<p>Hello, <b>World!</b></p>", destDocument.body().html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = Jsoup.parseBodyFragment("<p class=\"test\" id=\"test\">Hello, <b>World!</b></p>").body();
        ElementMeta meta = cleaner.createSafeElement(sourceElement);

        Assert.assertEquals("p", meta.el.tagName());
        Assert.assertEquals("test", meta.el.attr("class"));
        Assert.assertEquals("test", meta.el.attr("id"));
        Assert.assertEquals(0, meta.numAttribsDiscarded);
    }
}