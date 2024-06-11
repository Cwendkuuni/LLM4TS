import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        Whitelist whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><h1>Hello World!</h1></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        Document cleanDoc = cleaner.clean(dirtyDoc);

        assertTrue(cleanDoc.body().hasText());
        assertEquals("Hello World!", cleanDoc.body().text());
    }

    @Test
    public void testIsValid() {
        String dirtyHtml = "<html><body><h1>Hello World!</h1></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        boolean isValid = cleaner.isValid(dirtyDoc);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        String dirtyHtml = "<html><body><h1>Hello World!</h1></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        Document cleanDoc = Document.createShell(dirtyDoc.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDoc.body(), cleanDoc.body());

        assertEquals(0, numDiscarded);
        assertTrue(cleanDoc.body().hasText());
        assertEquals("Hello World!", cleanDoc.body().text());
    }

    @Test
    public void testCreateSafeElement() {
        String tag = "h1";
        Element sourceEl = new Element(Tag.valueOf(tag), "");
        Attributes sourceAttrs = new Attributes();
        sourceAttrs.put("style", "color: red;");
        sourceEl.attributes().addAll(sourceAttrs);

        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals(tag, elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertTrue(elementMeta.el.attributes().hasKey("style"));
        assertEquals("color: red;", elementMeta.el.attributes().get("style"));
    }
}