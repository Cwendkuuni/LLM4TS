import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test basic functionality

        // Create input document
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.appendElement("body");
        body.append("<p>This is a paragraph</p>");
        body.append("<h1>This is a heading</h1>");
        body.append("<div>This is a div</div>");

        // Create expected output document
        Document expectedClean = Document.createShell("");
        Element expectedBody = expectedClean.appendElement("body");
        expectedBody.append("<p>This is a paragraph</p>");
        expectedBody.append("<h1>This is a heading</h1>");
        expectedBody.append("<div>This is a div</div>");

        // Create Cleaner object
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Clean the document
        Document clean = cleaner.clean(dirtyDocument);

        // Compare cleaned document with expected output
        assertEquals(expectedClean, clean);
    }

    @Test
    public void testIsValid() {
        // Test basic functionality

        // Create input document
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.appendElement("body");
        body.append("<p>This is a paragraph</p>");
        body.append("<h1>This is a heading</h1>");
        body.append("<div>This is a div</div>");

        // Create Cleaner object
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Check if document is valid
        boolean valid = cleaner.isValid(dirtyDocument);

        // Document should be valid since all tags and attributes are allowed by the whitelist
        assertTrue(valid);
    }

    @Test
    public void testCopySafeNodes() {
        // Test basic functionality

        // Create source element
        Element source = new Element(Tag.valueOf("div"), "");

        // Create destination element
        Element dest = new Element(Tag.valueOf("div"), "");

        // Create Cleaner object
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Number of discarded elements should be 0 since all elements are safe
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Test basic functionality

        // Create source element
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("class", "container");
        sourceEl.attr("id", "main");

        // Create expected destination element
        Element expectedEl = new Element(Tag.valueOf("div"), "");
        expectedEl.attr("class", "container");
        expectedEl.attr("id", "main");

        // Create Cleaner object
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        // Create safe element
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element destEl = meta.el;

        // Compare destination element with expected element
        assertEquals(expectedEl, destEl);

        // Number of discarded attributes should be 0 since all attributes are safe
        assertEquals(0, meta.numAttribsDiscarded);
    }
}