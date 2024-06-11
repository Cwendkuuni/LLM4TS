import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean_validDirtyDocument_success() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element div = new Element(Tag.valueOf("div"), "");
        div.text("Hello, World!");
        body.appendChild(div);
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div>Hello, World!</div></body></html>", cleanDocument.html());
    }

    @Test
    public void testClean_dirtyDocumentWithUnsafeTags_success() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert(\"XSS!\");");
        body.appendChild(script);
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_validDirtyDocument_success() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element div = new Element(Tag.valueOf("div"), "");
        div.text("Hello, World!");
        body.appendChild(div);
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_dirtyDocumentWithUnsafeTags_failure() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert(\"XSS!\");");
        body.appendChild(script);
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testClean_emptyDirtyDocument_success() {
        Document dirtyDocument = new Document("");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClean_nullDirtyDocument_throwIllegalArgumentException() {
        cleaner.clean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValid_nullDirtyDocument_throwIllegalArgumentException() {
        cleaner.isValid(null);
    }
}