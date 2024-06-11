import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDoc = Document.createShell("http://example.com");
        Element dirtyBody = dirtyDoc.body();
        dirtyBody.appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyBody.appendChild(new Element(Tag.valueOf("p"), ""));
        dirtyBody.appendChild(new TextNode("Hello, world!"));

        Document cleanDoc = cleaner.clean(dirtyDoc);

        assertNotNull(cleanDoc);
        assertTrue(cleanDoc instanceof Document);
        assertEquals("http://example.com", cleanDoc.baseUri());
        assertNotNull(cleanDoc.body());

        Element cleanBody = cleanDoc.body();
        List<Node> cleanChildren = cleanBody.childNodes();

        assertEquals(2, cleanChildren.size());
        assertTrue(cleanChildren.get(0) instanceof Element);
        assertTrue(cleanChildren.get(1) instanceof TextNode);
    }

    @Test
    public void testIsValid() {
        Document dirtyDoc = Document.createShell("http://example.com");
        Element dirtyBody = dirtyDoc.body();
        dirtyBody.appendChild(new Element(Tag.valueOf("div"), ""));
        dirtyBody.appendChild(new Element(Tag.valueOf("p"), ""));
        dirtyBody.appendChild(new TextNode("Hello, world!"));

        boolean valid = cleaner.isValid(dirtyDoc);

        assertFalse(valid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        root.appendChild(unsafeElement);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertNotNull(destination);
        assertNotNull(destination.children());
        assertEquals(0, destination.children().size());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element unsafeElement = new Element(Tag.valueOf("script"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(unsafeElement);

        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertNull(elementMeta.el.attributes());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}