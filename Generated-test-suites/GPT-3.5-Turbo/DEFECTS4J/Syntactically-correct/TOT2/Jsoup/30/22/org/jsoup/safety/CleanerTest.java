import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        assertEquals(whitelist, cleaner.getWhitelist());
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element rootElement = new Element(Tag.valueOf("div"), "");
        Document dirtyDocument = new Document("");
        dirtyDocument.appendChild(rootElement);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element rootElement = new Element(Tag.valueOf("div"), "");
        Document dirtyDocument = new Document("");
        dirtyDocument.appendChild(rootElement);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodesMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element rootElement = new Element(Tag.valueOf("div"), "");
        Document dirtyDocument = new Document("");
        dirtyDocument.appendChild(rootElement);
        Element destination = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(rootElement, destination);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElementMethod() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(sourceElement.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}