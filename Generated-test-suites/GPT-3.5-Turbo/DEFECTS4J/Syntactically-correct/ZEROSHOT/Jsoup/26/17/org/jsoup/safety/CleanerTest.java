import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = new Document("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Document clean should be a new Document", true, dirtyDocument != cleanDocument);
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = new Document("");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertEquals("Dirty document should be valid", true, isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Element source = new Element("div");
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals("Number of discarded elements should be 0", 0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Element sourceEl = new Element("div");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertEquals("Element should have the same tag", "div", elementMeta.el.tagName());
        assertEquals("Number of discarded attributes should be 0", 0, elementMeta.numAttribsDiscarded);
    }
}