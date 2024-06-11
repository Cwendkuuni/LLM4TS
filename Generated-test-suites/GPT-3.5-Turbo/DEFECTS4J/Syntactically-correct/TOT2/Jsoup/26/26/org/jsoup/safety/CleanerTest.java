import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.safety.Cleaner;
import org.jsoup.nodes.Document;
import org.jsoup.select.Whitelist;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<div><p>Some text</p><script>alert('XSS attack')</script></div>");
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertTrue(cleanDocument.body().html().contains("<div><p>Some text</p></div>"));
        assertFalse(cleanDocument.body().html().contains("<script>"));
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<div><p>Some text</p><script>alert('XSS attack')</script></div>");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<div><p>Some text</p><script>alert('XSS attack')</script></div>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, numDiscarded);
        assertTrue(cleanDocument.body().html().contains("<div><p>Some text</p></div>"));
        assertFalse(cleanDocument.body().html().contains("<script>"));
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("");
        dirtyDocument.body().html("<div style='background-color:red'></div>");
        Element sourceElement = dirtyDocument.body().children().first();

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("div", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertTrue(elementMeta.el.attributes().hasKey("style"));
        assertFalse(elementMeta.el.attributes().get("style").contains("background-color:red"));
    }
}