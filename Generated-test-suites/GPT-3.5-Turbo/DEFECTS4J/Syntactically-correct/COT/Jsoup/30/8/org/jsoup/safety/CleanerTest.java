
import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document dirtyDoc = Jsoup.parseBodyFragment("<p>Hello, <b>world</b>!</p>");
        Document cleanDoc = cleaner.clean(dirtyDoc);

        // Verify that the clean document only contains allowed tags and attributes
        assertEquals("<p>Hello, <b>world</b>!</p>", cleanDoc.body().html());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document dirtyDoc = Jsoup.parseBodyFragment("<p>Hello, <script>alert('World');</script>!</p>");

        // Verify that the dirty document is not valid against the whitelist
        assertFalse(cleaner.isValid(dirtyDoc));
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Element root = Jsoup.parseBodyFragment("<p>Hello, <b>world</b>!</p>").body();
        Element destination = new Element(Tag.valueOf("div"), "");

        // Verify that the safe nodes are copied to the destination element
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        assertEquals("<div><p>Hello, <b>world</b>!</p></div>", destination.html());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Element sourceEl = Jsoup.parseBodyFragment("<b class='important' style='color:red'>Hello, world!</b>").body().select("b").first();

        // Verify that the safe element is created with the correct attributes
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertEquals("b", elementMeta.el.tagName());
        assertEquals("important", elementMeta.el.className());
        assertEquals("color:red", elementMeta.el.attr("style"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}

