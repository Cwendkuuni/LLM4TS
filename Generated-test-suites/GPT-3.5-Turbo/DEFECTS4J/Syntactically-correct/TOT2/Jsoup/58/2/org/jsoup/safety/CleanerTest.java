import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element div = new Element(Tag.valueOf("div"), "");
        body.appendChild(div);
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals(1, cleanDocument.body().childNodeSize());
        assertEquals("div", cleanDocument.body().child(0).tagName());
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element p = new Element(Tag.valueOf("p"), "");
        body.appendChild(p);
        dirtyDocument.appendChild(body);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());

        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element div = new Element(Tag.valueOf("div"), "");
        body.appendChild(div);
        dirtyDocument.appendChild(body);

        Element cleanBody = new Element(Tag.valueOf("body"), "");
        int discardedNodes = cleaner.copySafeNodes(dirtyDocument.body(), cleanBody);

        assertEquals(0, discardedNodes);
        assertEquals(1, cleanBody.childNodeSize());
        assertEquals("div", cleanBody.child(0).tagName());
    }

}