
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
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);

        dirtyDocument = new Document("");
        dirtyDocument.body().append("<div><p>This is a test</p></div>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);

        String cleanedHtml = cleanDocument.body().html();
        Assert.assertEquals("<div><p>This is a test</p></div>", cleanedHtml);
    }

    @Test
    public void testIsValid() {
        boolean valid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(valid);
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body());

        Element destinationElement = elementMeta.el;
        int numDiscardedAttributes = elementMeta.numAttribsDiscarded;

        Assert.assertEquals("div", destinationElement.tagName());
        Assert.assertEquals(0, numDiscardedAttributes);
    }

}
