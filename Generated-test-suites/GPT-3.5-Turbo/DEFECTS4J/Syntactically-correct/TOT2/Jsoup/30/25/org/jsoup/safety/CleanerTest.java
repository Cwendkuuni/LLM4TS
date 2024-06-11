import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("https://www.example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().isEmpty());
        assertEquals("https://www.example.com", cleanDocument.baseUri());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("https://www.example.com");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("https://www.example.com");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), doc.body());

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("https://www.example.com");
        ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body());

        assertEquals(Tag.valueOf("body"), elementMeta.el.tag());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}