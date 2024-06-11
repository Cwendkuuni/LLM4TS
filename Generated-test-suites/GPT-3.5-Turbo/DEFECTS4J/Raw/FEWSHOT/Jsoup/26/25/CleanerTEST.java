import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean() {
        // Create a dirty document
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        // Add unsafe elements
        Element script = new Element(Tag.valueOf("script"), "");
        body.appendChild(script);

        Element img = new Element(Tag.valueOf("img"), "");
        body.appendChild(img);

        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check if unsafe elements are removed
        assertFalse(cleanDocument.body().children().contains(script));
        assertFalse(cleanDocument.body().children().contains(img));
    }

    @Test
    public void testIsValid() {
        // Create a dirty document
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        // Add unsafe elements
        Element script = new Element(Tag.valueOf("script"), "");
        body.appendChild(script);

        Element img = new Element(Tag.valueOf("img"), "");
        body.appendChild(img);

        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if document is considered valid
        assertFalse(cleaner.isValid(dirtyDocument));

        // Remove unsafe elements
        cleanDocument.body().children().remove(script);
        cleanDocument.body().children().remove(img);

        // Check if document is now considered valid
        assertTrue(cleaner.isValid(cleanDocument));
    }

    @Test
    public void testCopySafeNodes() {
        // Create a source element
        Element source = new Element(Tag.valueOf("div"), "");
        Element child1 = new Element(Tag.valueOf("p"), "");
        Element child2 = new Element(Tag.valueOf("span"), "");
        source.appendChild(child1);
        source.appendChild(child2);

        // Create a destination element
        Element dest = new Element(Tag.valueOf("div"), "");

        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Check if safe nodes are copied
        assertEquals(2, dest.children().size());
        assertTrue(dest.children().contains(child1));
        assertTrue(dest.children().contains(child2));

        // Check if discarded nodes count is correct
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source element
        Element sourceEl = new Element(Tag.valueOf("a"), "");

        // Create a whitelist
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a safe element
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        // Check if safe element is created
        Element dest = meta.el;
        assertEquals(sourceEl.tagName(), dest.tagName());

        // Check if attributes are copied correctly
        Attributes sourceAttrs = sourceEl.attributes();
        Attributes destAttrs = dest.attributes();
        assertEquals(sourceAttrs.size(), destAttrs.size());
        for (int i = 0; i < sourceAttrs.size(); i++) {
            assertEquals(sourceAttrs.get(i), destAttrs.get(i));
        }

        // Check if discarded attributes count is 0 since all attributes are safe
        assertEquals(0, meta.numAttribsDiscarded);
    }
}