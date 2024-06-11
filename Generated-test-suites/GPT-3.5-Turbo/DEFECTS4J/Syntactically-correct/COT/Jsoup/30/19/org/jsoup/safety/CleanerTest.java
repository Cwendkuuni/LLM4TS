import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Test", cleanDocument.body().text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument1 = Jsoup.parse("<html><head></head><body><p>Test</p></body></html>");
        Document dirtyDocument2 = Jsoup.parse("<html><head></head><body><script>alert('XSS')</script></body></html>");

        assertTrue(cleaner.isValid(dirtyDocument1));
        assertFalse(cleaner.isValid(dirtyDocument2));
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        Element p = new Element(Tag.valueOf("p"), "");
        p.text("Test");

        root.appendChild(p);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertEquals("Test", destination.text());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("href", "/test");
        sourceAttrs.put("target", "_blank");
        sourceEl.attributes().addAll(sourceAttrs);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        int numDiscarded = elementMeta.numAttribsDiscarded;

        assertEquals("a", dest.tagName());
        assertEquals("/test", dest.attr("href"));
        assertEquals(1, numDiscarded);
    }
}