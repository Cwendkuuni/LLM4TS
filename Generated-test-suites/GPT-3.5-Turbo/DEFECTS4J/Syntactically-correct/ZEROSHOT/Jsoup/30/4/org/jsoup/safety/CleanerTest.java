import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Create a dirty document
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<h1>Test</h1><p>This is a test</p>");

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that only the allowed elements are present
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Test</h1>\n" +
                "  <p>This is a test</p>\n" +
                " </body>\n" +
                "</html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        // Create a valid document
        Document validDocument = new Document("");
        validDocument.body().append("<p>This is a valid document</p>");

        // Verify that the valid document is considered valid
        assertTrue(cleaner.isValid(validDocument));

        // Create an invalid document
        Document invalidDocument = new Document("");
        invalidDocument.body().append("<script>alert('Invalid document')</script>");

        // Verify that the invalid document is considered invalid
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCopySafeNodes() {
        // Create a source element
        Element source = new Element("div");
        source.append("<span>Safe</span><script>alert('Unsafe')</script>");

        // Create a destination element
        Element destination = new Element("div");

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source, destination);

        // Verify that the safe nodes are copied correctly
        assertEquals("<div>\n" +
                " <span>Safe</span>\n" +
                "</div>", destination.html());

        // Verify the number of discarded nodes
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source element
        Element source = new Element("div");
        source.attr("id", "test");
        source.attr("onclick", "alert('Unsafe')");

        // Create a safe element using the createSafeElement method
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);
        Element safeElement = elementMeta.el;

        // Verify that only the safe attributes are present in the safe element
        assertEquals("<div id=\"test\"></div>", safeElement.outerHtml());

        // Verify the number of discarded attributes
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}