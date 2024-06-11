import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello World!</p></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div>\n" +
                "   <p>\n" +
                "    Hello World!\n" +
                "   </p>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>\n", cleanDocument.toString());

        // Edge case: Empty document
        dirtyDocument = Jsoup.parseBodyFragment("");
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                " </body>\n" +
                "</html>\n", cleanDocument.toString());

        // Error scenario: Null document
        assertThrows(IllegalArgumentException.class, () -> {
            cleaner.clean(null);
        });
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello World!</p></div>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        // Edge case: Empty document
        dirtyDocument = Jsoup.parseBodyFragment("");
        isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        // Error scenario: Null document
        assertThrows(IllegalArgumentException.class, () -> {
            cleaner.isValid(null);
        });
    }

    @Test
    public void testCopySafeNodes() {
        // Typical use case
        Element source = Jsoup.parseBodyFragment("<div><p>Hello World!</p></div>").body();
        Element dest = new Element(Tag.valueOf("html"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals("<html>\n" +
                " <body>\n" +
                "  <div>\n" +
                "   <p>\n" +
                "    Hello World!\n" +
                "   </p>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>\n", dest.toString());
        assertEquals(0, numDiscarded);

        // Edge case: No safe nodes
        source = Jsoup.parseBodyFragment("<script>alert('XSS!');</script>").body();
        dest = new Element(Tag.valueOf("html"), "");
        numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals("<html>\n" +
                " <body>\n" +
                " </body>\n" +
                "</html>\n", dest.toString());
        assertEquals(1, numDiscarded);

        // Error scenario: Null source element
        assertThrows(NullPointerException.class, () -> {
            cleaner.copySafeNodes(null, dest);
        });

        // Error scenario: Null destination element
        assertThrows(NullPointerException.class, () -> {
            cleaner.copySafeNodes(source, null);
        });
    }

    @Test
    public void testCreateSafeElement() {
        // Typical use case
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("class", "container");
        sourceEl.attr("style", "color: red;");
        int numDiscarded = 0;
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);

        // Edge case: No safe attributes
        sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("onmouseover", "alert('XSS!');");
        numDiscarded = 0;
        elementMeta = cleaner.createSafeElement(sourceEl);
        assertEquals("div", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);

        // Error scenario: Null source element
        assertThrows(NullPointerException.class, () -> {
            cleaner.createSafeElement(null);
        });
    }

}