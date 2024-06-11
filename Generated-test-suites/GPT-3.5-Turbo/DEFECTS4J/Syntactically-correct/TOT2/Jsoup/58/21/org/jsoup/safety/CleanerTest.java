import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        whitelist.addTags("a", "b", "p");
        whitelist.addAttributes("a", "href");
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDoc = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.appendChild(new Element(Tag.valueOf("a"), "").attr("href", "http://example.com"));
        dirtyDoc.appendChild(body);

        Document cleanDoc = cleaner.clean(dirtyDoc);

        assertTrue(cleanDoc.body().children().size() == 1);
        assertEquals("a", cleanDoc.body().children().get(0).tagName());
        assertEquals("http://example.com", cleanDoc.body().children().get(0).attr("href"));
    }

    @Test
    public void testIsValid() {
        Document dirtyDoc = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.appendChild(new Element(Tag.valueOf("script"), ""));
        dirtyDoc.appendChild(body);

        boolean valid = cleaner.isValid(dirtyDoc);

        assertFalse(valid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDoc = new Document("");
        Element source = new Element(Tag.valueOf("body"), "");
        Element dest = new Element(Tag.valueOf("body"), "");
        source.appendChild(new Element(Tag.valueOf("a"), "").attr("href", "http://example.com"));
        source.appendChild(new Element(Tag.valueOf("b"), ""));
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(1, dest.children().size());
        assertEquals("a", dest.children().get(0).tagName());
        assertEquals("http://example.com", dest.children().get(0).attr("href"));
        assertEquals(1, numDiscarded);
    }
}