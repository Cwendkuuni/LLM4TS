
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><body><div><h1>Title</h1><p>Paragraph</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        // Check if cleanDocument only contains expected elements
        assertTrue(cleanDocument.getAllElements().size() == 3);
        assertEquals("html", cleanDocument.tagName());
        assertEquals("body", cleanDocument.body().tagName());
        assertEquals("div", cleanDocument.body().child(0).tagName());
    }

    @Test
    public void testIsValid() {
        // dirtyDocument contains only whitelisted tags, so it should be valid
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element source = dirtyDocument.body().child(0);
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        // Check if all elements are successfully copied to the destination
        assertEquals(0, numDiscarded);
        assertEquals("h1", dest.child(0).tagName());
        assertEquals("Title", dest.child(0).text());
        assertEquals("p", dest.child(1).tagName());
        assertEquals("Paragraph", dest.child(1).text());
    }

    @Test
    public void testCreateSafeElement() {
        Element source = dirtyDocument.body().child(0);
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);
        Element dest = elementMeta.el;
        // Check if the created element contains only whitelisted attributes
        assertEquals("div", dest.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
        Attributes destAttrs = dest.attributes();
        assertEquals(0, destAttrs.size());
    }
}
