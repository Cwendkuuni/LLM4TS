import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a Cleaner instance with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><font size=\"5\">This is dirty HTML</font></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that the cleaned document only contains elements allowed by the whitelist
        Assert.assertEquals("<html>\n <head></head>\n <body><font size=\"5\">This is dirty HTML</font></body>\n</html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        // Create a Cleaner instance with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><font size=\"5\">This is dirty HTML</font></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Check if the dirty document is valid against the whitelist
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Verify that the dirty document is valid
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a Cleaner instance with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source element and destination element
        Element source = new Element("div");
        Element destination = new Element("div");

        // Copy safe nodes from the source element to the destination element
        int numDiscarded = cleaner.copySafeNodes(source, destination);

        // Verify that the number of discarded nodes is 0
        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a Cleaner instance with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source element with attributes
        Element sourceElement = new Element("div");
        sourceElement.attr("id", "myDiv");
        sourceElement.attr("class", "myClass");

        // Create a safe element using the createSafeElement method
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceElement);
        Element safeElement = meta.el;

        // Verify that the safe element only contains safe attributes
        Assert.assertEquals("<div id=\"myDiv\" class=\"myClass\"></div>", safeElement.toString());

        // Verify that the number of discarded attributes is 0
        Assert.assertEquals(0, meta.numAttribsDiscarded);
    }

}