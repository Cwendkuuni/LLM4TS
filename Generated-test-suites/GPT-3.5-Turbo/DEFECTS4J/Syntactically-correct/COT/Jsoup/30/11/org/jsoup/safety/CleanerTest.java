import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        whitelist = Whitelist.relaxed(); // Example whitelist, can be customized for different tests
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Create a dirty document for testing
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("<p>Hello, <b>world</b>!</p>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Test that only tags and attributes allowed by the whitelist are preserved
        assertEquals("<body><p>Hello, <b>world</b>!</p></body>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        // Create a dirty document for testing
        Document dirtyDocument = Document.createShell("");
        dirtyDocument.body().append("<p>Hello, <b>world</b>!</p>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        // Test that the document is considered valid
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a source element and a destination element for testing
        Document document = Document.createShell("");
        Element source = document.createElement("p");
        Element destination = document.createElement("div");

        // Add test attributes to the source element
        source.attr("title", "Test title");
        source.attr("data-src", "test.png");
        source.attr("style", "font-weight: bold;");

        // Copy safe nodes from the source to the destination element
        int numDiscarded = cleaner.copySafeNodes(source, destination);

        // Test that the safe nodes are copied correctly
        assertEquals("<div><p title=\"Test title\" data-src=\"test.png\" style=\"font-weight: bold;\"></p></div>", destination.outerHtml());

        // Test the number of discarded attribtues
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source element for testing
        Document document = Document.createShell("");
        Element source = document.createElement("p");

        // Add test attributes to the source element
        source.attr("title", "Test title");
        source.attr("data-src", "test.png");
        source.attr("style", "font-weight: bold;");

        // Create a safe element from the source element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);

        // Test that the safe element is created correctly
        assertEquals("<p title=\"Test title\" data-src=\"test.png\" style=\"font-weight: bold;\"></p>", elementMeta.el.outerHtml());

        // Test the number of discarded attribtues
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}