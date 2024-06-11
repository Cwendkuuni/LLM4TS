import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
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
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
        dirtyDocument = Jsoup.parse("<html><body><div><p>This is a test.</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>This is a test.</p>\n" +
                " </body>\n" +
                "</html>", cleanDocument.html());
    }

    @Test
    public void testIsValidWithValidDocument() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        dirtyDocument.body().append("<script>alert('xss')</script>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div>\n" +
                " <p>This is a test.</p>\n" +
                "</div>", cleanDocument.body().html());
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta elementMeta = cleaner.createSafeElement(new Element(Tag.valueOf("div"), ""));
        Assert.assertEquals("<div></div>", elementMeta.el.outerHtml());
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}