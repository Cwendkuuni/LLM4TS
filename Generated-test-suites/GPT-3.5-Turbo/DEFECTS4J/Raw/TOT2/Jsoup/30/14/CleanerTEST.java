import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
        dirtyDocument = Document.createShell("http://example.com");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testIsValidValidDocument() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidInvalidDocument() {
        dirtyDocument.body().appendElement("script").appendText("alert('XSS Attack!')");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(!isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Tag tag = Tag.valueOf("div");
        Document document = Document.createShell("http://example.com");
        document.body().appendElement(tag).attr("id", "testId");
        
        int numDiscarded = cleaner.copySafeNodes(document.body(), document.body());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Tag tag = Tag.valueOf("a");
        Document document = Document.createShell("http://example.com");
        document.body().appendElement(tag).attr("href", "javascript:alert('XSS Attack!')");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(document.body().child(0));
        assertEquals("a", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}