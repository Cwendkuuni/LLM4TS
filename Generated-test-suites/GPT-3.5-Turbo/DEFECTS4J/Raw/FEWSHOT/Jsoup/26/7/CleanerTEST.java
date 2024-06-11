import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        // Create a new cleaner with default whitelist
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        // Create a dirty document with unsafe elements
        Document dirtyDocument = new Document("");
        Element dirtyBody = new Element(Tag.valueOf("body"), "");
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        dirtyBody.appendChild(unsafeElement);
        dirtyDocument.appendChild(dirtyBody);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if unsafe elements are removed
        assertFalse(cleanDocument.getElementsByTag("script").size() > 0);
    }

    @Test
    public void testIsValid() {
        // Create a new cleaner with default whitelist
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        // Create a dirty document with only safe elements
        Document dirtyDocument = new Document("");
        Element dirtyBody = new Element(Tag.valueOf("body"), "");
        Element safeElement = new Element(Tag.valueOf("div"), "");
        dirtyBody.appendChild(safeElement);
        dirtyDocument.appendChild(dirtyBody);

        // Check if the dirty document is considered valid
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        // Create a new cleaner with default whitelist
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        // Create a source element with safe and unsafe child elements
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Element unsafeChild = new Element(Tag.valueOf("script"), "");
        Element safeChild = new Element(Tag.valueOf("p"), "");
        sourceElement.appendChild(unsafeChild);
        sourceElement.appendChild(safeChild);

        // Create a destination element
        Element destElement = new Element(Tag.valueOf("div"), "");

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        // Check if unsafe child is discarded and safe child is copied
        assertEquals(1, numDiscarded);
        assertFalse(destElement.getElementsByTag("script").size() > 0);
        assertTrue(destElement.getElementsByTag("p").size() > 0);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a new cleaner with default whitelist
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        // Create a source element with safe and unsafe attributes
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.attr("unsafeAttr", "value");
        sourceElement.attr("safeAttr", "value");

        // Create a safe element using the cleaner's whitelist
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element safeElement = elementMeta.el;
        int numDiscarded = elementMeta.numAttribsDiscarded;

        // Check if unsafe attribute is discarded and safe attribute is copied
        assertEquals(1, numDiscarded);
        assertFalse(safeElement.hasAttr("unsafeAttr"));
        assertTrue(safeElement.hasAttr("safeAttr"));
    }
}