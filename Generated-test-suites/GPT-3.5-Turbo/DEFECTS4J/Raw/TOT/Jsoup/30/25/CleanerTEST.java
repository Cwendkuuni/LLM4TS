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
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><body><div>Hello, <a href=\"#\">World!</a></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        // Check if only allowed elements and attributes are present
        Assert.assertEquals("<html><head></head><body><div>Hello, <a href=\"#\">World!</a></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        // Check if the document is valid
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidInvalid() {
        dirtyDocument = Jsoup.parse("<html><body><div><script>alert('Hello!')</script></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        // Check if the document is invalid
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        // Check if the clean document contains only safe nodes
        Assert.assertEquals("<body><div>Hello, <a href=\"#\">World!</a></div></body>", cleanDocument.body().html());
        // Check if the correct number of nodes were discarded
        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        String tagName = "div";
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(tagName);
        sourceEl.attr("id", "test");
        sourceEl.attr("class", "test");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Check if the safe element is created with correct tag name and base URI
        Assert.assertEquals(tagName, elementMeta.el.tagName());
        Assert.assertEquals("", elementMeta.el.baseUri());

        // Check if the safe element has the correct attributes
        Assert.assertEquals("test", elementMeta.el.attr("id"));
        Assert.assertEquals("test", elementMeta.el.attr("class"));

        // Check if the correct number of attributes were discarded
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithUnsafeAttribute() {
        String tagName = "script";
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(tagName);
        sourceEl.attr("src", "http://example.com/malicious.js");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Check if the unsafe element is created with correct tag name and base URI
        Assert.assertEquals(tagName, elementMeta.el.tagName());
        Assert.assertEquals("", elementMeta.el.baseUri());

        // Check if the unsafe attribute was discarded
        Assert.assertNull(elementMeta.el.attr("src"));

        // Check if the correct number of attributes were discarded
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}