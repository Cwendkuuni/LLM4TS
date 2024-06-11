
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        // Initialize the cleaner and whitelist
        whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Create a dirty document
        String html = "<html><head></head><body><div>Dirty content</div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Clean the dirty document
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Ensure that the cleaned document contains only safe elements
        Assert.assertEquals(1, cleanedDocument.body().children().size());
        Assert.assertEquals("div", cleanedDocument.body().child(0).tagName());
        Assert.assertEquals("Dirty content", cleanedDocument.body().child(0).text());
    }

    @Test
    public void testIsValid() {
        // Create a dirty document that contains invalid tags and attributes
        String html = "<html><head></head><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Check if the dirty document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Ensure that the dirty document is invalid
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a source element with unsafe tags and attributes
        String html = "<div><script>alert('XSS')</script></div>";
        Document document = Jsoup.parseBodyFragment(html);
        Element sourceElement = document.body().child(0);

        // Create a destination element
        Element destinationElement = new Element("div");

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

        // Ensure that the destination element only contains safe nodes
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals(0, destinationElement.children().size());
        Assert.assertEquals(0, destinationElement.attributes().size());
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source element with unsafe attributes
        String html = "<div style=\"color: red;\" onclick=\"alert('XSS')\">Safe content</div>";
        Document document = Jsoup.parseBodyFragment(html);
        Element sourceElement = document.body().child(0);

        // Create a safe element
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element safeElement = elementMeta.el;

        // Ensure that the safe element only contains safe attributes
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
        Assert.assertEquals(0, safeElement.attributes().size());

        // Ensure that the safe element has the same tag name and content
        Assert.assertEquals("div", safeElement.tagName());
        Assert.assertEquals("Safe content", safeElement.text());
    }
}
