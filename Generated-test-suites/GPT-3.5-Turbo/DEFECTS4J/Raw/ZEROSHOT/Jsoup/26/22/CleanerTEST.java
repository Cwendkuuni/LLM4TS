import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element div = new Element(Tag.valueOf("div"), "");
        Element p = new Element(Tag.valueOf("p"), "");
        div.appendChild(p);
        body.appendChild(div);
        dirtyDocument.appendChild(body);

        Document cleanedDocument = cleaner.clean(dirtyDocument);

        assertEquals(1, cleanedDocument.body().children().size());
        assertEquals("div", cleanedDocument.body().children().get(0).tagName());
        assertEquals("p", cleanedDocument.body().children().get(0).children().get(0).tagName());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element p = new Element(Tag.valueOf("p"), "");
        body.appendChild(p);
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("class", "test");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", elementMeta.el.tagName());
        assertEquals("test", elementMeta.el.attr("class"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}