import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist wl = Whitelist.basic();
        Cleaner cleaner = new Cleaner(wl);
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement("div").text("Hello, World!");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Cleaned document should only contain allowed elements", "<html>\n <head></head>\n <body><div>Hello, World!</div></body>\n</html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        Whitelist wl = Whitelist.basic();
        Cleaner cleaner = new Cleaner(wl);
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement("script").text("alert('XSS Attack!')");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertEquals("Invalid document with script tag should return false", false, isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist wl = Whitelist.basic();
        Cleaner cleaner = new Cleaner(wl);
        Document dirtyDocument = Document.createShell("http://example.com");
        Element root = dirtyDocument.body();
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals("Number of discarded nodes should be 1", 1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist wl = Whitelist.basic();
        Cleaner cleaner = new Cleaner(wl);
        Element sourceEl = new Element(Tag.valueOf("img"), "");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals("Created safe element should have the same tag name", "img", meta.el.tagName());
        assertEquals("Number of discarded attributes should be 0", 0, meta.numAttribsDiscarded);
    }
}