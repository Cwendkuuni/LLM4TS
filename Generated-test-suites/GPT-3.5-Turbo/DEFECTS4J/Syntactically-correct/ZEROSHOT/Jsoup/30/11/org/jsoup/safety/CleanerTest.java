import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Dirty <a href=\"http://example.com\">link</a></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element body = cleanDocument.body();
        assertTrue(body.children().size() > 0);
        assertEquals("p", body.child(0).tagName());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Dirty <a href=\"http://example.com\">link</a></p></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);
        
        assertTrue(valid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        
        Element p = new Element(Tag.valueOf("p"), "");
        p.text("Paragraph");
        root.appendChild(p);
        
        Element a = new Element(Tag.valueOf("a"), "");
        a.attr("href", "http://example.com");
        a.text("Link");
        root.appendChild(a);
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(0, numDiscarded);
        assertEquals(2, destination.children().size());
        assertEquals("p", destination.child(0).tagName());
        assertEquals("a", destination.child(1).tagName());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "http://example.com");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        
        assertEquals("a", dest.tagName());
        assertEquals("http://example.com", dest.attr("href"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}