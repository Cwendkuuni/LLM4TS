import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = Document.createShell("https://example.com");
        Element body = dirtyDocument.body();
        body.append("<p>This is a <img src=\"image.jpg\">test</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element cleanBody = cleanDocument.body();
        Assert.assertEquals("<p>This is a <img src=\"image.jpg\">test</p>", cleanBody.html());

        // Edge case: Empty body
        Document emptyDocument = Document.createShell("https://example.com");
        Document cleanEmptyDocument = cleaner.clean(emptyDocument);
        Element cleanEmptyBody = cleanEmptyDocument.body();
        Assert.assertEquals("", cleanEmptyBody.html());

        // Error scenario: Null input
        try {
            cleaner.clean(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception, test passes
        }
    }

    @Test
    public void testIsValid() {
        // Typical use case: Valid document
        Document validDocument = Document.createShell("https://example.com");
        Element validBody = validDocument.body();
        validBody.append("<p>This is a valid test</p>");
        boolean isValid = cleaner.isValid(validDocument);
        Assert.assertTrue(isValid);

        // Edge case: Empty body
        Document emptyDocument = Document.createShell("https://example.com");
        boolean isEmptyValid = cleaner.isValid(emptyDocument);
        Assert.assertTrue(isEmptyValid);

        // Error scenario: Null input
        try {
            cleaner.isValid(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception, test passes
        }
    }
    
    @Test
    public void testCopySafeNodes() {
        // Typical use case: Valid source and destination element
        Element sourceElement = new Element("div");
        Element destElement = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div></div>", destElement.outerHtml());

        // Edge case: Empty source element
        Element emptySourceElement = new Element("div");
        Element emptyDestElement = new Element("div");
        int emptyNumDiscarded = cleaner.copySafeNodes(emptySourceElement, emptyDestElement);
        Assert.assertEquals(0, emptyNumDiscarded);
        Assert.assertEquals("<div></div>", emptyDestElement.outerHtml());

        // Error scenario: Null input
        try {
            cleaner.copySafeNodes(null, destElement);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception, test passes
        }
    }

    @Test
    public void testCreateSafeElement() {
        // Typical use case: Valid source element and safe attribute
        Element sourceElement = new Element("div");
        sourceElement.attr("data-id", "123");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element safeElement = elementMeta.el;
        int numDiscarded = elementMeta.numAttribsDiscarded;
        Assert.assertEquals("<div data-id=\"123\"></div>", safeElement.outerHtml());
        Assert.assertEquals(0, numDiscarded);

        // Edge case: Empty source element
        Element emptySourceElement = new Element("div");
        ElementMeta emptyElementMeta = cleaner.createSafeElement(emptySourceElement);
        Element emptySafeElement = emptyElementMeta.el;
        int emptyNumDiscarded = emptyElementMeta.numAttribsDiscarded;
        Assert.assertEquals("<div></div>", emptySafeElement.outerHtml());
        Assert.assertEquals(0, emptyNumDiscarded);

        // Error scenario: Null input
        try {
            cleaner.createSafeElement(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception, test passes
        }
    }
}