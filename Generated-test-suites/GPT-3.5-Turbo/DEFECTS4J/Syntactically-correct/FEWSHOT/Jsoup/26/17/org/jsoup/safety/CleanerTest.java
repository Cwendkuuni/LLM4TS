import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDoc = new Document("");
        Element dirtyElement = new Element(Tag.valueOf("div"), "");
        dirtyElement.append("<script>alert('test')</script>");
        dirtyDoc.body().appendChild(dirtyElement);

        Document cleanDoc = cleaner.clean(dirtyDoc);

        assertFalse(cleanDoc.body().html().contains("<script>"));
    }

    @Test
    public void testIsValid() {
        Document dirtyDoc = new Document("");
        Element dirtyElement = new Element(Tag.valueOf("div"), "");
        dirtyElement.append("<script>alert('test')</script>");
        dirtyDoc.body().appendChild(dirtyElement);

        boolean isValid = cleaner.isValid(dirtyDoc);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        source.append("<p>Hello, world!</p>");
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("<p>Hello, world!</p>", dest.html());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "http://example.com");
        sourceEl.attr("onclick", "alert('test')");
        whitelist.addTags("a");
        whitelist.addAttributes("a", "href");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals(1, elementMeta.el.attributes().size());
        assertEquals("http://example.com", elementMeta.el.attr("href"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}