
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;


class CleanerTest {
    private Cleaner cleaner;

    // Constructor tests

    @Test
    public void testConstructor_NullWhitelist() {
        assertThrows(IllegalArgumentException.class, () -> new Cleaner(null));
    }

    @Test
    public void testConstructor_ValidWhitelist() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.getWhitelist());
    }

    // clean() tests

    @Test
    public void testClean_NullDocument() {
        cleaner = new Cleaner(new Whitelist());
        assertThrows(IllegalArgumentException.class, () -> cleaner.clean(null));
    }

    @Test
    public void testClean_ValidDocument() {
        cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = createValidDocument();
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        // assert cleanedDocument is valid - contains only elements allowed by the whitelist
        assertTrue(cleaner.isValid(cleanedDocument));
    }

    // isValid() tests

    @Test
    public void testIsValid_NullDocument() {
        cleaner = new Cleaner(new Whitelist());
        assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(null));
    }

    @Test
    public void testIsValid_ValidDocument() {
        cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = createValidDocument();
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    // Private methods tests

    @Test
    public void testCopySafeNodes() {
        cleaner = new Cleaner(new Whitelist());
        Element root = createValidDocument().body();
        Document clean = Document.createShell(root.baseUri());
        int numDiscarded = cleaner.copySafeNodes(root, clean.body());
        // assert clean is a valid document - contains only elements allowed by the whitelist
        assertTrue(cleaner.isValid(clean));
        // assert numDiscarded is correct
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        cleaner = new Cleaner(new Whitelist());
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        // assert meta.el is safe element
        Element dest = meta.el;
        assertTrue(cleaner.getWhitelist().isSafeTag(dest.tagName()));
        // assert meta.numAttribsDiscarded is correct
        assertEquals(0, meta.numAttribsDiscarded);
    }

    // Helper methods

    private Document createValidDocument() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();
        body.append("<div>Valid Element</div>");
        return dirtyDocument;
    }

}
