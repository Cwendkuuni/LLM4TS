import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Whitelist whitelist;
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        whitelist.addTags("a");

        cleaner = new Cleaner(whitelist);

        dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element div = new Element(Tag.valueOf("div"), "");
        TextNode textNode = new TextNode("Hello, World!", "");
        div.appendChild(textNode);
        body.appendChild(div);

        dirtyDocument.appendChild(body);
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element cleanBody = cleanDocument.body();

        assertEquals("body", cleanBody.tagName());
        Element div = cleanBody.child(0);
        assertEquals("div", div.tagName());
        TextNode textNode = (TextNode) div.childNode(0);
        assertEquals("Hello, World!", textNode.getWholeText());
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, destination);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element source = new Element(Tag.valueOf("a"), "");
        source.attr("href", "https://www.example.com");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);

        Element dest = elementMeta.el;
        assertEquals("a", dest.tagName());
        Attributes destAttrs = dest.attributes();
        assertEquals(1, destAttrs.size());
        assertTrue(destAttrs.hasKey("href"));
    }

}