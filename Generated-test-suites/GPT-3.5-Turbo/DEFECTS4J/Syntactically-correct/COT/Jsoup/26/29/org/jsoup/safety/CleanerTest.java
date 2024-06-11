
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    @Test
    public void testClean() {
        // Test input
        Document dirtyDocument = Document.createShell("http://example.com");

        // Test clean method
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assertions
        assertEquals(cleanDocument.baseUri(), "http://example.com");
    }

    @Test
    public void testIsValid() {
        // Test input
        Document dirtyDocument = Document.createShell("http://example.com");

        // Test isValid method
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assertions
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Test input
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");

        // Test copySafeNodes method
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Assertions
        assertEquals(numDiscarded, 0);
    }

    @Test
    public void testCreateSafeElement() {
        // Test input
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("class", "safe");

        // Test createSafeElement method
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element dest = meta.el;
        int numDiscarded = meta.numAttribsDiscarded;

        // Assertions
        assertEquals(dest.tagName(), "div");
        assertEquals(dest.attr("class"), "safe");
        assertEquals(numDiscarded, 0);
    }
}
