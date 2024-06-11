import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testConstructor() {
        Whitelist whitelist = new Whitelist(); // Assuming a valid whitelist implementation
        Cleaner cleaner = new Cleaner(whitelist);
        // Add any necessary assertions here
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element root = cleanDocument.select("html > body").first();
        Assert.assertEquals("Element 'h1' should be present in the cleaned document", "h1", root.tagName());
        // Add any necessary assertions for edge cases and exceptions here
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = new Whitelist();
        Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(invalidDocument);
        Assert.assertFalse("Invalid document should not be considered valid", isValid);
        // Add any necessary assertions for edge cases and exceptions here
    }

    @Test
    public void testCopySafeNodesMethod() {
        Whitelist whitelist = new Whitelist();
        Document sourceDocument = Jsoup.parse("<html><body><h1 id='title'>Title<img src='image.jpg'></h1></body></html>");
        Element dest = new Element(Tag.BODY, null);
        int numDiscarded = Cleaner.copySafeNodes(sourceDocument.body(), dest);
        Element root = dest.select("html > body > h1").first();
        Assert.assertEquals("Element 'h1' should be present in the destination", "h1", root.tagName());
        Assert.assertEquals("Number of discarded elements should be 1 (image)", 1, numDiscarded);
        // Add any necessary assertions for edge cases and exceptions here
    }

    // Add test cases for the createSafeElement and ElementMeta inner class here
}