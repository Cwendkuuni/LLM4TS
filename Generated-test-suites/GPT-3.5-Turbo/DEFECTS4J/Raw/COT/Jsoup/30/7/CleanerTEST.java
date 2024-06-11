
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Setup
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><body><p>Hello, World!</p></body></html>");

        // Exercise
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify
        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><p>Hello, World!</p></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        // Setup
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = new Document("<html><body><p>Hello, World!</p></body></html>");

        // Exercise
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Verify
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Setup
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        Element source = new Element(Tag.valueOf("p"), "");
        source.text("Hello, World!");
        root.appendChild(source);

        // Exercise
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Verify
        assertEquals(0, numDiscarded);
        assertEquals("<div><div><p>Hello, World!</p></div></div>", destination.toString());
    }

    @Test
    public void testCreateSafeElement() {
        // Setup
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "red");
        sourceEl.attr("style", "color: red");
        sourceEl.attr("data-id", "123");

        // Exercise
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Verify
        assertNotNull(elementMeta.el);
        assertEquals("p", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals("<p class=\"red\">", elementMeta.el.toString());
    }
}
