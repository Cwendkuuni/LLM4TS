import static org.junit.Assert.*;
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
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendElement("div");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element cleanBody = cleanDocument.body();
        assertTrue("Cleaned document should not contain any unsafe tags", cleanBody.children().isEmpty());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendElement("p").attr("id", "test");
        assertTrue("Dirty document should be considered valid by whitelist", cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.appendElement("p").attr("id", "test");
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals("Number of discarded elements should be 0", 0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "test");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        Element dest = elementMeta.el;
        Attributes destAttrs = dest.attributes();
        
        assertFalse("Class attribute should be discarded", destAttrs.hasKey("class"));
        assertTrue("Enforced attributes should be added", destAttrs.hasKey("style"));
    }
}