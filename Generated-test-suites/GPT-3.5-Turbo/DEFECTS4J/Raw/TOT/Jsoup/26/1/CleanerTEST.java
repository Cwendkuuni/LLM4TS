import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("");
        Element bodyElement = new Element(Tag.valueOf("body"), "");
        Element divElement = new Element(Tag.valueOf("div"), "");
        bodyElement.appendChild(divElement);
        dirtyDocument.appendChild(bodyElement);
        
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanedDocument);
        assertEquals(0, cleanedDocument.head().children().size());
        assertEquals(1, cleanedDocument.body().children().size());
        
        // Edge case: empty document
        Document emptyDocument = Document.createShell("");
        Document cleanedEmptyDocument = cleaner.clean(emptyDocument);
        assertNotNull(cleanedEmptyDocument);
        assertEquals(0, cleanedEmptyDocument.head().children().size());
        assertEquals(0, cleanedEmptyDocument.body().children().size());
    }

    @Test
    public void testIsValid() {
        // Typical use case: valid document
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("");
        Element bodyElement = new Element(Tag.valueOf("body"), "");
        Element divElement = new Element(Tag.valueOf("div"), "");
        bodyElement.appendChild(divElement);
        dirtyDocument.appendChild(bodyElement);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
        
        // Edge case: invalid document
        Whitelist emptyWhitelist = Whitelist.none();
        Cleaner emptyCleaner = new Cleaner(emptyWhitelist);
        boolean isEmptyValid = emptyCleaner.isValid(dirtyDocument);
        assertFalse(isEmptyValid);
    }
    
    @Test
    public void testCopySafeNodes() {
        // Typical use case: copy safe nodes from source to destination
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        cleaner.copySafeNodes(source, dest);
        
        assertEquals(0, dest.children().size());
        
        // Edge case: source contains unsafe elements
        Element sourceUnsafe = new Element(Tag.valueOf("script"), "");
        Element destUnsafe = new Element(Tag.valueOf("div"), "");
        cleaner.copySafeNodes(sourceUnsafe, destUnsafe);
        
        assertEquals(0, destUnsafe.children().size());
        assertEquals(1, destUnsafe.attributes().size());
    }

    @Test
    public void testCreateSafeElement() {
        // Typical use case: create safe element with safe attributes
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        assertNotNull(meta.el);
        assertEquals(0, meta.numAttribsDiscarded);
        
        // Edge case: create safe element with unsafe attributes
        Element unsafeSourceEl = new Element(Tag.valueOf("a"), "");
        unsafeSourceEl.attr("href", "javascript:alert('XSS');");
        ElementMeta unsafeMeta = cleaner.createSafeElement(unsafeSourceEl);
        
        assertNotNull(unsafeMeta.el);
        assertEquals(1, unsafeMeta.numAttribsDiscarded);
    }

}