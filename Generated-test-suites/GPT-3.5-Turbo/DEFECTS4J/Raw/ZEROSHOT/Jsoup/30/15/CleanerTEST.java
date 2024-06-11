import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.relaxed();

        // Create a Cleaner object
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><div><p>Test paragraph</p></div></body></html>");

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that the clean document only contains allowed elements and attributes
        assertTrue(cleanDocument.getElementsByTag("html").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("head").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("title").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("body").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("div").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("p").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("div").get(0).getElementsByTag("p").size() == 1);
    }

    @Test
    public void testIsValid() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.relaxed();

        // Create a Cleaner object
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><div><p>Test paragraph</p></div></body></html>");

        // Check if the dirty document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Verify that the dirty document is valid
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.relaxed();

        // Create a Cleaner object
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source element
        Element sourceElement = new Element(Tag.valueOf("div"), "");

        // Create a destination element
        Element destinationElement = new Element(Tag.valueOf("div"), "");

        // Copy safe nodes
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

        // Verify that the number of discarded nodes is 0
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a whitelist
        Whitelist whitelist = Whitelist.relaxed();

        // Create a Cleaner object
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source element
        Element sourceElement = new Element(Tag.valueOf("div"), "");

        // Create a safe element
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Verify that the safe element is not null
        assertNotNull(elementMeta.el);

        // Verify that the number of discarded attributes is 0
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}