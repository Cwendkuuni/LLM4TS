import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("https://example.com");
        dirtyDocument.body().append("<p>This is a <span>dirty</span> document.</p>");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<body><p>This is a <span>dirty</span> document.</p></body>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("https://example.com");
        dirtyDocument.body().append("<p>This is a <script>alert('XSS')</script> document.</p>");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("body"), "");
        Element destination = new Element(Tag.valueOf("body"), "");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "content");
        sourceEl.attr("style", "color: red");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("p", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals("content", elementMeta.el.attr("class"));
        assertEquals("color: red", elementMeta.el.attr("style"));
    }
}