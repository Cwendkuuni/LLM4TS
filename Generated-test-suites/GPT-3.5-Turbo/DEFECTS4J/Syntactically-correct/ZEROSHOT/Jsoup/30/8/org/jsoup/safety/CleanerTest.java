import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><head><title>Test Page</title></head><body><p>Test</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that only allowed elements exist in the clean document
        Assert.assertEquals(1, cleanDocument.getElementsByTag("html").size());
        Assert.assertEquals(1, cleanDocument.getElementsByTag("head").size());
        Assert.assertEquals(1, cleanDocument.getElementsByTag("body").size());
        Assert.assertEquals(1, cleanDocument.getElementsByTag("p").size());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><head><title>Test Page</title></head><body><p>Test</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        // Verify that the document is valid
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><head><title>Test Page</title></head><body><p>Test</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Call the private method copySafeNodes to test its functionality
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Verify that the number of discarded nodes is correct
        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><head><title>Test Page</title></head><body><p>Test</p></body></html>");
        Element sourceElement = dirtyDocument.body();

        // Call the private method createSafeElement to test its functionality
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Verify that the created safe element has the correct tag name
        Assert.assertEquals("body", elementMeta.el.tagName());

        // Verify that the number of discarded attributes is correct
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}