import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><div><p>Hello, World!</p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertTrue(cleanDocument.body().html().contains("<p>Hello, World!</p>"));
    }

    @Test
    public void testIsValid() {
        Document validDocument = new Document("<html><body><div><p>Hello, World!</p></div></body></html>");
        Document invalidDocument = new Document("<html><head></head><body><div><p>Hello, World!</p></div></body></html>");
        Assert.assertTrue(cleaner.isValid(validDocument));
        Assert.assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element("<div><p>Hello, World!</p></div>");
        Element dest = new Element("");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        Assert.assertEquals(0, numDiscarded);
        Assert.assertTrue(dest.html().contains("<p>Hello, World!</p>"));
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element("<p class=\"paragraph\">Hello, World!</p>");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        Assert.assertEquals("<p>Hello, World!</p>", dest.outerHtml());
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}