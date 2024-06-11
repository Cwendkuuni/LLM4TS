import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Element body = new Element("body");
        body.appendChild(new Element("div"));
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals(1, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        TextNode textNode = new TextNode("Hello World!");
        dirtyDocument.body().appendChild(textNode);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element("div");
        dirtyElement.appendChild(new TextNode("Hello"));

        Element cleanElement = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);

        assertNotNull(cleanElement);
        assertEquals(1, cleanElement.childNodes().size());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element("div");
        dirtyElement.attr("class", "test");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);

        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}