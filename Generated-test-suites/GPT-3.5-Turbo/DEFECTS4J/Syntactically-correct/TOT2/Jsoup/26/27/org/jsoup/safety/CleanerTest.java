import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        Element dirtyBody = dirtyDocument.body();
        Element div = new Element(Tag.valueOf("div"), "");
        dirtyBody.appendChild(div);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element cleanBody = cleanDocument.body();

        assertEquals(0, cleanBody.children().size());
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        Element dirtyBody = dirtyDocument.body();
        Element paragraph = new Element(Tag.valueOf("p"), "");
        paragraph.text("Hello, World!");
        dirtyBody.appendChild(paragraph);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodesMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        Element p = new Element(Tag.valueOf("p"), "");
        p.text("Hello, World!");
        source.appendChild(p);

        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
        assertEquals(1, dest.children().size());
    }

    @Test
    public void testCreateSafeElementMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "http://example.com");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
        Attributes destAttrs = elementMeta.el.attributes();
        assertEquals(1, destAttrs.size());
        Attribute hrefAttr = destAttrs.get("href");
        assertNotNull(hrefAttr);
    }
}