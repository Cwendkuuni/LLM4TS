import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
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
        dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><div><span>Hello World!</span></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNotEquals("Clean document should not be the same as the dirty document", dirtyDocument, cleanDocument);
        Assert.assertEquals("Clean document should have the same base URI as the dirty document", dirtyDocument.baseUri(), cleanDocument.baseUri());
        Assert.assertNotNull("Clean document should have a body", cleanDocument.body());
        Assert.assertEquals("Clean document should only contain elements allowed by the whitelist", 1, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse("Dirty document should not be considered valid", isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = dirtyDocument.body();
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        Assert.assertEquals("Number of discarded nodes should be zero", 0, numDiscarded);
        Assert.assertNotEquals("Destination element should not be empty", 0, dest.children().size());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = dirtyDocument.body().children().first();
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        Assert.assertEquals("Destination element should have the same tag name as the source element", sourceEl.tagName(), dest.tagName());
        Assert.assertEquals("Destination element should have the same base URI as the source element", sourceEl.baseUri(), dest.baseUri());
        Assert.assertEquals("Destination element should have the same number of attributes as the source element", sourceEl.attributes().size(), dest.attributes().size());
    }

    @Test
    public void testCleanerConstructor() {
        Assert.assertNotNull("Cleaner instance should not be null", cleaner);
        Assert.assertNotNull("Whitelist instance in cleaner should not be null", cleaner.whitelist);
    }

}