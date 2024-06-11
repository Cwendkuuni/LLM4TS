import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        Whitelist whitelist = Whitelist.basic();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element pTag = new Element(Tag.valueOf("p"), "");
        pTag.text("Hello, World!");
        body.appendChild(pTag);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<body><p>Hello, World!</p></body>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element divTag = new Element(Tag.valueOf("div"), "");
        body.appendChild(divTag);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        Element pTag = new Element(Tag.valueOf("p"), "");
        pTag.text("Hello, World!");
        source.appendChild(pTag);

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("<div><p>Hello, World!</p></div>", dest.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.attr("id", "test");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Element destElement = elementMeta.el;
        assertEquals("div", destElement.tagName());
        assertEquals("test", destElement.attr("id"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}