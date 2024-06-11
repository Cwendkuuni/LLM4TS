import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().html("<div><p>Hello, <a href=\"http://example.com\">World</a></p></div>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html>\n <head></head>\n <body><div><p>Hello, <a href=\"http://example.com\" rel=\"nofollow\">World</a></p></div></body>\n</html>\n",
                cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().html("<div><p>Hello, <a href=\"http://example.com\">World</a></p></div>");

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().html("<div><p>Hello, <a href=\"http://example.com\">World</a></p></div>");

        Document cleanDocument = Document.createShell("http://example.com");
        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals("<div><p>Hello, <a href=\"http://example.com\" rel=\"nofollow\">World</a></p></div>",
                cleanDocument.body().html());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().html("<div><p>Hello, <a href=\"http://example.com\">World</a></p></div>");

        ElementMeta meta = cleaner.createSafeElement(dirtyDocument.body().children().first());
        assertEquals("<div><p>Hello, <a rel=\"nofollow\" href=\"http://example.com\">World</a></p></div>",
                meta.el.outerHtml());
    }
}