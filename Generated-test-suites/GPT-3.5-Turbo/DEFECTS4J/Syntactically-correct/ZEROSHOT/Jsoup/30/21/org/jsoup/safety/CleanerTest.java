import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void clean_shouldRemoveUnallowedTagsAndAttributes() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element unallowedTag = new Element(Tag.valueOf("script"), "");
        unallowedTag.text("alert('hello');");
        body.appendChild(unallowedTag);
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element cleanBody = cleanDocument.body();
        assertEquals("", cleanBody.html());
    }

    @Test
    public void clean_shouldNotModifyAllowedTagsAndAttributes() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element allowedTag = new Element(Tag.valueOf("p"), "");
        allowedTag.attr("align", "center");
        allowedTag.text("Hello World");
        body.appendChild(allowedTag);
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element cleanBody = cleanDocument.body();
        Element cleanTag = cleanBody.child(0);
        assertEquals("p", cleanTag.tagName());
        assertEquals("center", cleanTag.attr("align"));
        assertEquals("Hello World", cleanTag.text());
    }

    @Test
    public void isValid_shouldReturnTrueIfDocumentIsValid() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element allowedTag = new Element(Tag.valueOf("p"), "");
        allowedTag.attr("align", "center");
        allowedTag.text("Hello World");
        body.appendChild(allowedTag);
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void isValid_shouldReturnFalseIfDocumentIsInvalid() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element unallowedTag = new Element(Tag.valueOf("script"), "");
        unallowedTag.text("alert('hello');");
        body.appendChild(unallowedTag);
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(!isValid);
    }
}