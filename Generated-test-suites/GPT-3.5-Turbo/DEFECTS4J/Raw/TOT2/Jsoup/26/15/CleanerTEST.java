import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        // Creating a new cleaner with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating a dirty document
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyDocument.body().appendChild(new TextNode("Hello World", ""));

        // Cleaning the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        // Typical use case
        // Creating a new cleaner with a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating a dirty document
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyDocument.body().appendChild(new TextNode("Hello World", ""));

        // Checking if the document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Typical use case
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating source and destination elements
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");

        // Copying safe nodes
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Edge case: Whitelist denies an attribute
        Whitelist whitelist = new Whitelist();
        whitelist.addEnforcedAttribute("div", "id", "xyz");
        Cleaner cleaner = new Cleaner(whitelist);

        // Creating a source element with an unsafe attribute
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("id", "unsafe");

        // Creating a safe element and checking discarded attributes
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}